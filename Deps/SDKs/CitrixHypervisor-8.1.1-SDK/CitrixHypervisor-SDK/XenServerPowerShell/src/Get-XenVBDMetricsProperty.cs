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
    [Cmdlet(VerbsCommon.Get, "XenVBDMetricsProperty", SupportsShouldProcess = false)]
    public class GetXenVBDMetricsProperty : XenServerCmdlet
    {
        #region Cmdlet Parameters

        [Parameter(ParameterSetName = "XenObject", Mandatory = true, ValueFromPipeline = true, Position = 0)]
        public XenAPI.VBD_metrics VBDMetrics { get; set; }

        [Parameter(ParameterSetName = "Ref", Mandatory = true, ValueFromPipelineByPropertyName = true, Position = 0)]
        [Alias("opaque_ref")]
        public XenRef<XenAPI.VBD_metrics> Ref { get; set; }


        [Parameter(Mandatory = true)]
        public XenVBDMetricsProperty XenProperty { get; set; }

        #endregion

        #region Cmdlet Methods

        protected override void ProcessRecord()
        {
            GetSession();

            string vbd_metrics = ParseVBDMetrics();

            switch (XenProperty)
            {
                case XenVBDMetricsProperty.Uuid:
                    ProcessRecordUuid(vbd_metrics);
                    break;
                case XenVBDMetricsProperty.IoReadKbs:
                    ProcessRecordIoReadKbs(vbd_metrics);
                    break;
                case XenVBDMetricsProperty.IoWriteKbs:
                    ProcessRecordIoWriteKbs(vbd_metrics);
                    break;
                case XenVBDMetricsProperty.LastUpdated:
                    ProcessRecordLastUpdated(vbd_metrics);
                    break;
                case XenVBDMetricsProperty.OtherConfig:
                    ProcessRecordOtherConfig(vbd_metrics);
                    break;
            }

            UpdateSessions();
        }

        #endregion

        #region Private Methods

        private string ParseVBDMetrics()
        {
            string vbd_metrics = null;

            if (VBDMetrics != null)
                vbd_metrics = (new XenRef<XenAPI.VBD_metrics>(VBDMetrics)).opaque_ref;
            else if (Ref != null)
                vbd_metrics = Ref.opaque_ref;
            else
            {
                ThrowTerminatingError(new ErrorRecord(
                    new ArgumentException("At least one of the parameters 'VBDMetrics', 'Ref', 'Uuid' must be set"),
                    string.Empty,
                    ErrorCategory.InvalidArgument,
                    VBDMetrics));
            }

            return vbd_metrics;
        }

        private void ProcessRecordUuid(string vbd_metrics)
        {
            RunApiCall(()=>
            {
                    string obj = XenAPI.VBD_metrics.get_uuid(session, vbd_metrics);

                        WriteObject(obj, true);
            });
        }

        private void ProcessRecordIoReadKbs(string vbd_metrics)
        {
            RunApiCall(()=>
            {
                    double obj = XenAPI.VBD_metrics.get_io_read_kbs(session, vbd_metrics);

                        WriteObject(obj, true);
            });
        }

        private void ProcessRecordIoWriteKbs(string vbd_metrics)
        {
            RunApiCall(()=>
            {
                    double obj = XenAPI.VBD_metrics.get_io_write_kbs(session, vbd_metrics);

                        WriteObject(obj, true);
            });
        }

        private void ProcessRecordLastUpdated(string vbd_metrics)
        {
            RunApiCall(()=>
            {
                    DateTime obj = XenAPI.VBD_metrics.get_last_updated(session, vbd_metrics);

                        WriteObject(obj, true);
            });
        }

        private void ProcessRecordOtherConfig(string vbd_metrics)
        {
            RunApiCall(()=>
            {
                    var dict = XenAPI.VBD_metrics.get_other_config(session, vbd_metrics);

                        Hashtable ht = CommonCmdletFunctions.ConvertDictionaryToHashtable(dict);
                        WriteObject(ht, true);
            });
        }

        #endregion
    }

    public enum XenVBDMetricsProperty
    {
        Uuid,
        IoReadKbs,
        IoWriteKbs,
        LastUpdated,
        OtherConfig
    }

}
