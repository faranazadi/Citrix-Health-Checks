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
    [Cmdlet(VerbsLifecycle.Invoke, "XenPVSSite", SupportsShouldProcess = true)]
    public class InvokeXenPVSSite : XenServerCmdlet
    {
        #region Cmdlet Parameters

        [Parameter]
        public SwitchParameter PassThru { get; set; }

        [Parameter(ParameterSetName = "XenObject", Mandatory = true, ValueFromPipeline = true, Position = 0)]
        public XenAPI.PVS_site PVSSite { get; set; }

        [Parameter(ParameterSetName = "Ref", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("opaque_ref")]
        public XenRef<XenAPI.PVS_site> Ref { get; set; }

        [Parameter(ParameterSetName = "Uuid", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        public Guid Uuid { get; set; }

        [Parameter(ParameterSetName = "Name", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("name_label")]
        public string Name { get; set; }


        [Parameter(Mandatory = true)]
        public XenPVSSiteAction XenAction { get; set; }

        #endregion

        public override object GetDynamicParameters()
        {
            switch (XenAction)
            {
                case XenPVSSiteAction.Introduce:
                    _context = new XenPVSSiteActionIntroduceDynamicParameters();
                    return _context;
                case XenPVSSiteAction.Forget:
                    _context = new XenPVSSiteActionForgetDynamicParameters();
                    return _context;
                default:
                    return null;
            }
        }

        #region Cmdlet Methods

        protected override void ProcessRecord()
        {
            GetSession();

            string pvs_site = ParsePVSSite();

            switch (XenAction)
            {
                case XenPVSSiteAction.Introduce:
                    ProcessRecordIntroduce(pvs_site);
                    break;
                case XenPVSSiteAction.Forget:
                    ProcessRecordForget(pvs_site);
                    break;
            }

            UpdateSessions();
        }

        #endregion

        #region Private Methods

        private string ParsePVSSite()
        {
            string pvs_site = null;

            if (PVSSite != null)
                pvs_site = (new XenRef<XenAPI.PVS_site>(PVSSite)).opaque_ref;
            else if (Uuid != Guid.Empty)
            {
                var xenRef = XenAPI.PVS_site.get_by_uuid(session, Uuid.ToString());
                if (xenRef != null)
                    pvs_site = xenRef.opaque_ref;
            }
            else if (Name != null)
            {
                var xenRefs = XenAPI.PVS_site.get_by_name_label(session, Name);
                if (xenRefs.Count == 1)
                    pvs_site = xenRefs[0].opaque_ref;
                else if (xenRefs.Count > 1)
                    ThrowTerminatingError(new ErrorRecord(
                        new ArgumentException(string.Format("More than one XenAPI.PVS_site with name label {0} exist", Name)),
                        string.Empty,
                        ErrorCategory.InvalidArgument,
                        Name));
            }
            else if (Ref != null)
                pvs_site = Ref.opaque_ref;
            else
            {
                ThrowTerminatingError(new ErrorRecord(
                    new ArgumentException("At least one of the parameters 'PVSSite', 'Ref', 'Uuid' must be set"),
                    string.Empty,
                    ErrorCategory.InvalidArgument,
                    PVSSite));
            }

            return pvs_site;
        }

        private void ProcessRecordIntroduce(string pvs_site)
        {
            if (!ShouldProcess(pvs_site, "PVS_site.introduce"))
                return;

            RunApiCall(()=>
            {
                var contxt = _context as XenPVSSiteActionIntroduceDynamicParameters;

                if (contxt != null && contxt.Async)
                {
                    taskRef = XenAPI.PVS_site.async_introduce(session, contxt.NameLabel, contxt.NameDescription, contxt.PVSUuid);

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
                    string objRef = XenAPI.PVS_site.introduce(session, contxt.NameLabel, contxt.NameDescription, contxt.PVSUuid);

                    if (PassThru)
                    {
                        XenAPI.PVS_site obj = null;

                        if (objRef != "OpaqueRef:NULL")
                        {
                            obj = XenAPI.PVS_site.get_record(session, objRef);
                            obj.opaque_ref = objRef;
                        }

                        WriteObject(obj, true);
                    }
                }

            });
        }

        private void ProcessRecordForget(string pvs_site)
        {
            if (!ShouldProcess(pvs_site, "PVS_site.forget"))
                return;

            RunApiCall(()=>
            {
                var contxt = _context as XenPVSSiteActionForgetDynamicParameters;

                if (contxt != null && contxt.Async)
                {
                    taskRef = XenAPI.PVS_site.async_forget(session, pvs_site);

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
                    XenAPI.PVS_site.forget(session, pvs_site);

                    if (PassThru)
                    {
                        var obj = XenAPI.PVS_site.get_record(session, pvs_site);
                        if (obj != null)
                            obj.opaque_ref = pvs_site;
                        WriteObject(obj, true);
                    }
                }

            });
        }

        #endregion
    }

    public enum XenPVSSiteAction
    {
        Introduce,
        Forget
    }

    public class XenPVSSiteActionIntroduceDynamicParameters : IXenServerDynamicParameter
    {
        [Parameter]
        public SwitchParameter Async { get; set; }

        [Parameter]
        public string NameLabel { get; set; }

        [Parameter]
        public string NameDescription { get; set; }

        [Parameter]
        public string PVSUuid { get; set; }
   
    }

    public class XenPVSSiteActionForgetDynamicParameters : IXenServerDynamicParameter
    {
        [Parameter]
        public SwitchParameter Async { get; set; }

    }

}
