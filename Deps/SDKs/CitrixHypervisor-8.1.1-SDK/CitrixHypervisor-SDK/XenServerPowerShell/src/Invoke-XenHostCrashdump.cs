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
    [Cmdlet(VerbsLifecycle.Invoke, "XenHostCrashdump", SupportsShouldProcess = true)]
    public class InvokeXenHostCrashdump : XenServerCmdlet
    {
        #region Cmdlet Parameters

        [Parameter]
        public SwitchParameter PassThru { get; set; }

        [Parameter(ParameterSetName = "XenObject", Mandatory = true, ValueFromPipeline = true, Position = 0)]
        public XenAPI.Host_crashdump HostCrashdump { get; set; }

        [Parameter(ParameterSetName = "Ref", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("opaque_ref")]
        public XenRef<XenAPI.Host_crashdump> Ref { get; set; }

        [Parameter(ParameterSetName = "Uuid", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        public Guid Uuid { get; set; }


        [Parameter(Mandatory = true)]
        public XenHostCrashdumpAction XenAction { get; set; }

        #endregion

        public override object GetDynamicParameters()
        {
            switch (XenAction)
            {
                case XenHostCrashdumpAction.Upload:
                    _context = new XenHostCrashdumpActionUploadDynamicParameters();
                    return _context;
                default:
                    return null;
            }
        }

        #region Cmdlet Methods

        protected override void ProcessRecord()
        {
            GetSession();

            string host_crashdump = ParseHostCrashdump();

            switch (XenAction)
            {
                case XenHostCrashdumpAction.Upload:
                    ProcessRecordUpload(host_crashdump);
                    break;
            }

            UpdateSessions();
        }

        #endregion

        #region Private Methods

        private string ParseHostCrashdump()
        {
            string host_crashdump = null;

            if (HostCrashdump != null)
                host_crashdump = (new XenRef<XenAPI.Host_crashdump>(HostCrashdump)).opaque_ref;
            else if (Uuid != Guid.Empty)
            {
                var xenRef = XenAPI.Host_crashdump.get_by_uuid(session, Uuid.ToString());
                if (xenRef != null)
                    host_crashdump = xenRef.opaque_ref;
            }
            else if (Ref != null)
                host_crashdump = Ref.opaque_ref;
            else
            {
                ThrowTerminatingError(new ErrorRecord(
                    new ArgumentException("At least one of the parameters 'HostCrashdump', 'Ref', 'Uuid' must be set"),
                    string.Empty,
                    ErrorCategory.InvalidArgument,
                    HostCrashdump));
            }

            return host_crashdump;
        }

        private void ProcessRecordUpload(string host_crashdump)
        {
            if (!ShouldProcess(host_crashdump, "Host_crashdump.upload"))
                return;

            RunApiCall(()=>
            {
                var contxt = _context as XenHostCrashdumpActionUploadDynamicParameters;

                if (contxt != null && contxt.Async)
                {
                    taskRef = XenAPI.Host_crashdump.async_upload(session, host_crashdump, contxt.Url_, CommonCmdletFunctions.ConvertHashTableToDictionary<string, string>(contxt.Options));

                    if (PassThru)
                    {
                        XenAPI.Task taskObj = null;
                        if (taskRef != "OpaqueRef:NULL")
                        {
                            taskObj = XenAPI.Task.get_record(session, taskRef.opaque_ref);
                            taskObj.opaque_ref = taskRef.opaque_ref;
                        }

                        WriteObject(taskObj, true);
                    }
                }
                else
                {
                    XenAPI.Host_crashdump.upload(session, host_crashdump, contxt.Url_, CommonCmdletFunctions.ConvertHashTableToDictionary<string, string>(contxt.Options));

                    if (PassThru)
                    {
                        var obj = XenAPI.Host_crashdump.get_record(session, host_crashdump);
                        if (obj != null)
                            obj.opaque_ref = host_crashdump;
                        WriteObject(obj, true);
                    }
                }

            });
        }

        #endregion
    }

    public enum XenHostCrashdumpAction
    {
        Upload
    }

    public class XenHostCrashdumpActionUploadDynamicParameters : IXenServerDynamicParameter
    {
        [Parameter]
        public SwitchParameter Async { get; set; }

        [Parameter]
        public string Url_ { get; set; }

        [Parameter]
        public Hashtable Options { get; set; }
  
    }

}
