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
    [Cmdlet(VerbsCommon.Add, "XenVM", SupportsShouldProcess = true)]
    [OutputType(typeof(XenAPI.VM))]
    [OutputType(typeof(XenAPI.Task))]
    [OutputType(typeof(void))]
    public class AddXenVM : XenServerCmdlet
    {
        #region Cmdlet Parameters

        [Parameter]
        public SwitchParameter PassThru { get; set; }

        [Parameter(ParameterSetName = "XenObject", Mandatory = true, ValueFromPipeline = true, Position = 0)]
        public XenAPI.VM VM { get; set; }

        [Parameter(ParameterSetName = "Ref", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("opaque_ref")]
        public XenRef<XenAPI.VM> Ref { get; set; }

        [Parameter(ParameterSetName = "Uuid", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        public Guid Uuid { get; set; }

        [Parameter(ParameterSetName = "Name", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("name_label")]
        public string Name { get; set; }


        protected override bool GenerateAsyncParam
        {
            get
            {
                return _vCPUsParamsLiveIsSpecified;
            }
        }

        [Parameter]
        public KeyValuePair<string, string> VCPUsParams
        {
            get { return _vCPUsParams; }
            set
            {
                _vCPUsParams = value;
                _vCPUsParamsIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _vCPUsParams;
        private bool _vCPUsParamsIsSpecified;

        [Parameter]
        public KeyValuePair<string, string> HVMBootParams
        {
            get { return _hVMBootParams; }
            set
            {
                _hVMBootParams = value;
                _hVMBootParamsIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _hVMBootParams;
        private bool _hVMBootParamsIsSpecified;

        [Parameter]
        public KeyValuePair<string, string> Platform
        {
            get { return _platform; }
            set
            {
                _platform = value;
                _platformIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _platform;
        private bool _platformIsSpecified;

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
        public KeyValuePair<string, string> XenstoreData
        {
            get { return _xenstoreData; }
            set
            {
                _xenstoreData = value;
                _xenstoreDataIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _xenstoreData;
        private bool _xenstoreDataIsSpecified;

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
        public KeyValuePair<vm_operations, string> BlockedOperations
        {
            get { return _blockedOperations; }
            set
            {
                _blockedOperations = value;
                _blockedOperationsIsSpecified = true;
            }
        }
        private KeyValuePair<vm_operations, string> _blockedOperations;
        private bool _blockedOperationsIsSpecified;

        [Parameter]
        public KeyValuePair<string, string> VCPUsParamsLive
        {
            get { return _vCPUsParamsLive; }
            set
            {
                _vCPUsParamsLive = value;
                _vCPUsParamsLiveIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _vCPUsParamsLive;
        private bool _vCPUsParamsLiveIsSpecified;

        [Parameter]
        public KeyValuePair<string, string> NVRAM
        {
            get { return _nVRAM; }
            set
            {
                _nVRAM = value;
                _nVRAMIsSpecified = true;
            }
        }
        private KeyValuePair<string, string> _nVRAM;
        private bool _nVRAMIsSpecified;

        #endregion

        #region Cmdlet Methods

        protected override void ProcessRecord()
        {
            GetSession();

            string vm = ParseVM();

            if (_vCPUsParamsIsSpecified)
                ProcessRecordVCPUsParams(vm);
            if (_hVMBootParamsIsSpecified)
                ProcessRecordHVMBootParams(vm);
            if (_platformIsSpecified)
                ProcessRecordPlatform(vm);
            if (_otherConfigIsSpecified)
                ProcessRecordOtherConfig(vm);
            if (_xenstoreDataIsSpecified)
                ProcessRecordXenstoreData(vm);
            if (_tagsIsSpecified)
                ProcessRecordTags(vm);
            if (_blockedOperationsIsSpecified)
                ProcessRecordBlockedOperations(vm);
            if (_vCPUsParamsLiveIsSpecified)
                ProcessRecordVCPUsParamsLive(vm);
            if (_nVRAMIsSpecified)
                ProcessRecordNVRAM(vm);

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

                        var obj = XenAPI.VM.get_record(session, vm);
                        if (obj != null)
                            obj.opaque_ref = vm;
                        WriteObject(obj, true);

                    }
                });

            UpdateSessions();
        }

        #endregion

        #region Private Methods

        private string ParseVM()
        {
            string vm = null;

            if (VM != null)
                vm = (new XenRef<XenAPI.VM>(VM)).opaque_ref;
            else if (Uuid != Guid.Empty)
            {
                var xenRef = XenAPI.VM.get_by_uuid(session, Uuid.ToString());
                if (xenRef != null)
                    vm = xenRef.opaque_ref;
            }
            else if (Name != null)
            {
                var xenRefs = XenAPI.VM.get_by_name_label(session, Name);
                if (xenRefs.Count == 1)
                    vm = xenRefs[0].opaque_ref;
                else if (xenRefs.Count > 1)
                    ThrowTerminatingError(new ErrorRecord(
                        new ArgumentException(string.Format("More than one XenAPI.VM with name label {0} exist", Name)),
                        string.Empty,
                        ErrorCategory.InvalidArgument,
                        Name));
            }
            else if (Ref != null)
                vm = Ref.opaque_ref;
            else
            {
                ThrowTerminatingError(new ErrorRecord(
                    new ArgumentException("At least one of the parameters 'VM', 'Ref', 'Uuid' must be set"),
                    string.Empty,
                    ErrorCategory.InvalidArgument,
                    VM));
            }

            return vm;
        }

        private void ProcessRecordVCPUsParams(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_VCPUs_params"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_to_VCPUs_params(session, vm, VCPUsParams.Key, VCPUsParams.Value);

            });
        }

        private void ProcessRecordHVMBootParams(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_HVM_boot_params"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_to_HVM_boot_params(session, vm, HVMBootParams.Key, HVMBootParams.Value);

            });
        }

        private void ProcessRecordPlatform(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_platform"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_to_platform(session, vm, Platform.Key, Platform.Value);

            });
        }

        private void ProcessRecordOtherConfig(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_other_config"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_to_other_config(session, vm, OtherConfig.Key, OtherConfig.Value);

            });
        }

        private void ProcessRecordXenstoreData(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_xenstore_data"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_to_xenstore_data(session, vm, XenstoreData.Key, XenstoreData.Value);

            });
        }

        private void ProcessRecordTags(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_tags"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_tags(session, vm, Tags);

            });
        }

        private void ProcessRecordBlockedOperations(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_blocked_operations"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_to_blocked_operations(session, vm, BlockedOperations.Key, BlockedOperations.Value);

            });
        }

        private void ProcessRecordVCPUsParamsLive(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_VCPUs_params_live"))
                return;

            RunApiCall(()=>
            {
                var contxt = _context as XenServerCmdletDynamicParameters;

                if (contxt != null && contxt.Async)
                {
                    taskRef = XenAPI.VM.async_add_to_VCPUs_params_live(session, vm, VCPUsParamsLive.Key, VCPUsParamsLive.Value);

                }
                else
                {
                    XenAPI.VM.add_to_VCPUs_params_live(session, vm, VCPUsParamsLive.Key, VCPUsParamsLive.Value);

                }

            });
        }

        private void ProcessRecordNVRAM(string vm)
        {
            if (!ShouldProcess(vm, "VM.add_to_NVRAM"))
                return;

            RunApiCall(()=>
            {
                    XenAPI.VM.add_to_NVRAM(session, vm, NVRAM.Key, NVRAM.Value);

            });
        }

        #endregion
    }
}
