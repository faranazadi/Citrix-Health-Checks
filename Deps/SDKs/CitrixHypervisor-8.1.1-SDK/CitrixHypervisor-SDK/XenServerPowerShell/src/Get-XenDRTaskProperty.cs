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
    [Cmdlet(VerbsCommon.Get, "XenDRTaskProperty", SupportsShouldProcess = false)]
    public class GetXenDRTaskProperty : XenServerCmdlet
    {
        #region Cmdlet Parameters

        [Parameter(ParameterSetName = "XenObject", Mandatory = true, ValueFromPipeline = true, Position = 0)]
        public XenAPI.DR_task DRTask { get; set; }

        [Parameter(ParameterSetName = "Ref", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("opaque_ref")]
        public XenRef<XenAPI.DR_task> Ref { get; set; }


        [Parameter(Mandatory = true)]
        public XenDRTaskProperty XenProperty { get; set; }

        #endregion

        #region Cmdlet Methods

        protected override void ProcessRecord()
        {
            GetSession();

            string dr_task = ParseDRTask();

            switch (XenProperty)
            {
                case XenDRTaskProperty.Uuid:
                    ProcessRecordUuid(dr_task);
                    break;
                case XenDRTaskProperty.IntroducedSRs:
                    ProcessRecordIntroducedSRs(dr_task);
                    break;
            }

            UpdateSessions();
        }

        #endregion

        #region Private Methods

        private string ParseDRTask()
        {
            string dr_task = null;

            if (DRTask != null)
                dr_task = (new XenRef<XenAPI.DR_task>(DRTask)).opaque_ref;
            else if (Ref != null)
                dr_task = Ref.opaque_ref;
            else
            {
                ThrowTerminatingError(new ErrorRecord(
                    new ArgumentException("At least one of the parameters 'DRTask', 'Ref', 'Uuid' must be set"),
                    string.Empty,
                    ErrorCategory.InvalidArgument,
                    DRTask));
            }

            return dr_task;
        }

        private void ProcessRecordUuid(string dr_task)
        {
            RunApiCall(()=>
            {
                    string obj = XenAPI.DR_task.get_uuid(session, dr_task);

                        WriteObject(obj, true);
            });
        }

        private void ProcessRecordIntroducedSRs(string dr_task)
        {
            RunApiCall(()=>
            {
                    var refs = XenAPI.DR_task.get_introduced_SRs(session, dr_task);

                        var records = new List<XenAPI.SR>();

                        foreach (var _ref in refs)
                        {
                            if (_ref.opaque_ref == "OpaqueRef:NULL")
                                continue;

                            var record = XenAPI.SR.get_record(session, _ref);
                            record.opaque_ref = _ref.opaque_ref;
                            records.Add(record);
                        }

                        WriteObject(records, true);
            });
        }

        #endregion
    }

    public enum XenDRTaskProperty
    {
        Uuid,
        IntroducedSRs
    }

}
