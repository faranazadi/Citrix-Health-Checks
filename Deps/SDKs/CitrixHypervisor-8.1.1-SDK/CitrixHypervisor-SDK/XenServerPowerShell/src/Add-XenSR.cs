/*
 * Copyright (c) Citrix Systems, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1) Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *
 *   2) Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials
 *      provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */


using System;
using System.Collections;
using System.Collections.Generic;
using System.Management.Automation;
using System.Text;

using XenAPI;

namespace Citrix.XenServer.Commands
{
    [Cmdlet(VerbsCommon.Add, "XenSR", SupportsShouldProcess = true)]
    [OutputType(typeof(XenAPI.SR))]
    [OutputType(typeof(void))]
    public class AddXenSR : XenServerCmdlet
    {
        #region Cmdlet Parameters

        [Parameter]
        public SwitchParameter PassThru { get; set; }

        [Parameter(ParameterSetName = "XenObject", Mandatory = true, ValueFromPipeline = true, Position = 0)]
        public XenAPI.SR SR { get; set; }

        [Parameter(ParameterSetName = "Ref", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("opaque_ref")]
        public XenRef<XenAPI.SR> Ref { get; set; }

        [Parameter(ParameterSetName = "Uuid", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        public Guid Uuid { get; set; }

        [Parameter(ParameterSetName = "Name", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("name_label")]
        public string Name { get; set; }


        [Parameter]
        public KeyValuePair<string, string> OtherConfig
        {
            get { return _otherConfig; }
            set
            {
                _otherConfig = value;
                _otherConfigIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _otherConfig;
        private bool _otherConfigIsSpecified;

        [Parameter]
        public string Tags
        {
            get { return _tags; }
            set
            {
                _tags = value;
                _tagsIsSpecified = true;
            }
        }
        private string _tags;
        private bool _tagsIsSpecified;

        [Parameter]
        public KeyValuePair<string, string> SmConfig
        {
            get { return _smConfig; }
            set
            {
                _smConfig = value;
                _smConfigIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _smConfig;
        private bool _smConfigIsSpecified;

        #endregion

        #region Cmdlet Methods

        protected override void ProcessRecord()
        {
            GetSession();

            string sr = ParseSR();

            if (_otherConfigIsSpecified)
                ProcessRecordOtherConfig(sr);
            if (_tagsIsSpecified)
                ProcessRecordTags(sr);
            if (_smConfigIsSpecified)
                ProcessRecordSmConfig(sr);

            if (!PassThru)
                return;

            RunApiCall(() =>
                {
                    var contxt = _context as XenServerCmdletDynamicParameters;

                    if (contxt != null && contxt.Async)
                    {
                        XenAPI.Task taskObj = null;
                        if (taskRef != null && taskRef != "OpaqueRef:NULL")
                        {
                            taskObj = XenAPI.Task.get_record(session, taskRef.opaque_ref);
                            taskObj.opaque_ref = taskRef.opaque_ref;
                        }

                        WriteObject(taskObj, true);
                    }
                    else
                    {

                        var obj = XenAPI.SR.get_record(session, sr);
                        if (obj != null)
                            obj.opaque_ref = sr;
                        WriteObject(obj, true);

                    }
                });

            UpdateSessions();
        }

        #endregion

        #region Private Methods

        private string ParseSR()
        {
            string sr = null;

            if (SR != null)
                sr = (new XenRef<XenAPI.SR>(SR)).opaque_ref;
            else if (Uuid != Guid.Empty)
            {
                var xenRef = XenAPI.SR.get_by_uuid(session, Uuid.ToString());
                if (xenRef != null)
                    sr = xenRef.opaque_ref;
            }
            else if (Name != null)
            {
                var xenRefs = XenAPI.SR.get_by_name_label(session, Name);
                if (xenRefs.Count == 1)
                    sr = xenRefs[0].opaque_ref;
                else if (xenRefs.Count > 1)
                    ThrowTerminatingError(new ErrorRecord(
                        new ArgumentException(string.Format("More than one XenAPI.SR with name label {0} exist", Name)),
                        string.Empty,
                        ErrorCategory.InvalidArgument,
                        Name));
            }
            else if (Ref != null)
                sr = Ref.opaque_ref;
            else
            {
                ThrowTerminatingError(new ErrorRecord(
                    new ArgumentException("At least one of the parameters 'SR', 'Ref', 'Uuid' must be set"),
                    string.Empty,
                    ErrorCategory.InvalidArgument,
                    SR));
            }

            return sr;
        }

        private void ProcessRecordOtherConfig(string sr)
        {
            if (!ShouldProcess(sr, "SR.add_to_other_config"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.SR.add_to_other_config(session, sr, OtherConfig.Key, OtherConfig.Value);

            });
        }

        private void ProcessRecordTags(string sr)
        {
            if (!ShouldProcess(sr, "SR.add_tags"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.SR.add_tags(session, sr, Tags);

            });
        }

        private void ProcessRecordSmConfig(string sr)
        {
            if (!ShouldProcess(sr, "SR.add_to_sm_config"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.SR.add_to_sm_config(session, sr, SmConfig.Key, SmConfig.Value);

            });
        }

        #endregion
    }
}
