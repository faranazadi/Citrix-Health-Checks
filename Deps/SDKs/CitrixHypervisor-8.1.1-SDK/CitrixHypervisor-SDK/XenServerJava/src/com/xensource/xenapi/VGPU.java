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


package com.xensource.xenapi;

import com.xensource.xenapi.Types.BadServerResponse;
import com.xensource.xenapi.Types.VersionException;
import com.xensource.xenapi.Types.XenAPIException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;

/**
 * A virtual GPU (vGPU)
 * First published in XenServer 6.0.
 *
 * @author Citrix Systems, Inc.
 */
public class VGPU extends XenAPIObject {

    /**
     * The XenAPI reference (OpaqueRef) to this object.
     */
    protected final String ref;

    /**
     * For internal use only.
     */
    VGPU(String ref) {
       this.ref = ref;
    }

    /**
     * @return The XenAPI reference (OpaqueRef) to this object.
     */
    public String toWireString() {
       return this.ref;
    }

    /**
     * If obj is a VGPU, compares XenAPI references for equality.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj instanceof VGPU)
        {
            VGPU other = (VGPU) obj;
            return other.ref.equals(this.ref);
        } else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return ref.hashCode();
    }

    /**
     * Represents all the fields in a VGPU
     */
    public static class Record implements Types.Record {
        public String toString() {
            StringWriter writer = new StringWriter();
            PrintWriter print = new PrintWriter(writer);
            print.printf("%1$20s: %2$s\n", "uuid", this.uuid);
            print.printf("%1$20s: %2$s\n", "VM", this.VM);
            print.printf("%1$20s: %2$s\n", "GPUGroup", this.GPUGroup);
            print.printf("%1$20s: %2$s\n", "device", this.device);
            print.printf("%1$20s: %2$s\n", "currentlyAttached", this.currentlyAttached);
            print.printf("%1$20s: %2$s\n", "otherConfig", this.otherConfig);
            print.printf("%1$20s: %2$s\n", "type", this.type);
            print.printf("%1$20s: %2$s\n", "residentOn", this.residentOn);
            print.printf("%1$20s: %2$s\n", "scheduledToBeResidentOn", this.scheduledToBeResidentOn);
            print.printf("%1$20s: %2$s\n", "compatibilityMetadata", this.compatibilityMetadata);
            print.printf("%1$20s: %2$s\n", "extraArgs", this.extraArgs);
            print.printf("%1$20s: %2$s\n", "PCI", this.PCI);
            return writer.toString();
        }

        /**
         * Convert a VGPU.Record to a Map
         */
        public Map<String,Object> toMap() {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("uuid", this.uuid == null ? "" : this.uuid);
            map.put("VM", this.VM == null ? new VM("OpaqueRef:NULL") : this.VM);
            map.put("GPU_group", this.GPUGroup == null ? new GPUGroup("OpaqueRef:NULL") : this.GPUGroup);
            map.put("device", this.device == null ? "" : this.device);
            map.put("currently_attached", this.currentlyAttached == null ? false : this.currentlyAttached);
            map.put("other_config", this.otherConfig == null ? new HashMap<String, String>() : this.otherConfig);
            map.put("type", this.type == null ? new VGPUType("OpaqueRef:NULL") : this.type);
            map.put("resident_on", this.residentOn == null ? new PGPU("OpaqueRef:NULL") : this.residentOn);
            map.put("scheduled_to_be_resident_on", this.scheduledToBeResidentOn == null ? new PGPU("OpaqueRef:NULL") : this.scheduledToBeResidentOn);
            map.put("compatibility_metadata", this.compatibilityMetadata == null ? new HashMap<String, String>() : this.compatibilityMetadata);
            map.put("extra_args", this.extraArgs == null ? "" : this.extraArgs);
            map.put("PCI", this.PCI == null ? new PCI("OpaqueRef:NULL") : this.PCI);
            return map;
        }

        /**
         * Unique identifier/object reference
         */
        public String uuid;
        /**
         * VM that owns the vGPU
         */
        public VM VM;
        /**
         * GPU group used by the vGPU
         */
        public GPUGroup GPUGroup;
        /**
         * Order in which the devices are plugged into the VM
         */
        public String device;
        /**
         * Reflects whether the virtual device is currently connected to a physical device
         */
        public Boolean currentlyAttached;
        /**
         * Additional configuration
         */
        public Map<String, String> otherConfig;
        /**
         * Preset type for this VGPU
         * First published in XenServer 6.2 SP1 Tech-Preview.
         */
        public VGPUType type;
        /**
         * The PGPU on which this VGPU is running
         * First published in XenServer 6.2 SP1 Tech-Preview.
         */
        public PGPU residentOn;
        /**
         * The PGPU on which this VGPU is scheduled to run
         * First published in XenServer 7.0.
         */
        public PGPU scheduledToBeResidentOn;
        /**
         * VGPU metadata to determine whether a VGPU can migrate between two PGPUs
         * First published in XenServer 7.3.
         */
        public Map<String, String> compatibilityMetadata;
        /**
         * Extra arguments for vGPU and passed to demu
         * First published in Citrix Hypervisor 8.1.
         */
        public String extraArgs;
        /**
         * Device passed trough to VM, either as full device or SR-IOV virtual function
         * First published in Citrix Hypervisor 8.1.
         */
        public PCI PCI;
    }

    /**
     * Get a record containing the current state of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @return all fields from the object
     */
    public VGPU.Record getRecord(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_record";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toVGPURecord(result);
    }

    /**
     * Get a reference to the VGPU instance with the specified UUID.
     * First published in XenServer 6.0.
     *
     * @param uuid UUID of object to return
     * @return reference to the object
     */
    public static VGPU getByUuid(Connection c, String uuid) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_by_uuid";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(uuid)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toVGPU(result);
    }

    /**
     * Get the uuid field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public String getUuid(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_uuid";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the VM field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public VM getVM(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_VM";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toVM(result);
    }

    /**
     * Get the GPU_group field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public GPUGroup getGPUGroup(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_GPU_group";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toGPUGroup(result);
    }

    /**
     * Get the device field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public String getDevice(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_device";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the currently_attached field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Boolean getCurrentlyAttached(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_currently_attached";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toBoolean(result);
    }

    /**
     * Get the other_config field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Map<String, String> getOtherConfig(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the type field of the given VGPU.
     * First published in XenServer 6.2 SP1 Tech-Preview.
     *
     * @return value of the field
     */
    public VGPUType getType(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_type";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toVGPUType(result);
    }

    /**
     * Get the resident_on field of the given VGPU.
     * First published in XenServer 6.2 SP1 Tech-Preview.
     *
     * @return value of the field
     */
    public PGPU getResidentOn(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_resident_on";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toPGPU(result);
    }

    /**
     * Get the scheduled_to_be_resident_on field of the given VGPU.
     * First published in XenServer 7.0.
     *
     * @return value of the field
     */
    public PGPU getScheduledToBeResidentOn(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_scheduled_to_be_resident_on";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toPGPU(result);
    }

    /**
     * Get the compatibility_metadata field of the given VGPU.
     * First published in XenServer 7.3.
     *
     * @return value of the field
     */
    public Map<String, String> getCompatibilityMetadata(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_compatibility_metadata";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the extra_args field of the given VGPU.
     * First published in Citrix Hypervisor 8.1.
     *
     * @return value of the field
     */
    public String getExtraArgs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_extra_args";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the PCI field of the given VGPU.
     * First published in Citrix Hypervisor 8.1.
     *
     * @return value of the field
     */
    public PCI getPCI(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_PCI";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toPCI(result);
    }

    /**
     * Set the other_config field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @param otherConfig New value to set
     */
    public void setOtherConfig(Connection c, Map<String, String> otherConfig) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.set_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(otherConfig)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Add the given key-value pair to the other_config field of the given VGPU.
     * First published in XenServer 6.0.
     *
     * @param key Key to add
     * @param value Value to add
     */
    public void addToOtherConfig(Connection c, String key, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.add_to_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the given key and its corresponding value from the other_config field of the given VGPU.  If the key is not in that Map, then do nothing.
     * First published in XenServer 6.0.
     *
     * @param key Key to remove
     */
    public void removeFromOtherConfig(Connection c, String key) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.remove_from_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the extra_args field of the given VGPU.
     * First published in Citrix Hypervisor 8.1.
     *
     * @param extraArgs New value to set
     */
    public void setExtraArgs(Connection c, String extraArgs) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.set_extra_args";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(extraArgs)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * 
     * First published in XenServer 6.0.
     *
     * @param VM 
     * @param GPUGroup 
     * @param device 
     * @param otherConfig 
     * @return Task
     */
    public static Task createAsync(Connection c, VM VM, GPUGroup GPUGroup, String device, Map<String, String> otherConfig) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.VGPU.create";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(VM), Marshalling.toXMLRPC(GPUGroup), Marshalling.toXMLRPC(device), Marshalling.toXMLRPC(otherConfig)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * 
     * First published in XenServer 6.0.
     *
     * @param VM 
     * @param GPUGroup 
     * @param device 
     * @param otherConfig 
     * @return reference to the newly created object
     */
    public static VGPU create(Connection c, VM VM, GPUGroup GPUGroup, String device, Map<String, String> otherConfig) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.create";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(VM), Marshalling.toXMLRPC(GPUGroup), Marshalling.toXMLRPC(device), Marshalling.toXMLRPC(otherConfig)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toVGPU(result);
    }

    /**
     * 
     * First published in XenServer 6.0.
     *
     * @param VM 
     * @param GPUGroup 
     * @param device 
     * @param otherConfig 
     * @param type  First published in XenServer 6.2 SP1 Tech-Preview.
     * @return Task
     */
    public static Task createAsync(Connection c, VM VM, GPUGroup GPUGroup, String device, Map<String, String> otherConfig, VGPUType type) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.VGPU.create";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(VM), Marshalling.toXMLRPC(GPUGroup), Marshalling.toXMLRPC(device), Marshalling.toXMLRPC(otherConfig), Marshalling.toXMLRPC(type)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * 
     * First published in XenServer 6.0.
     *
     * @param VM 
     * @param GPUGroup 
     * @param device 
     * @param otherConfig 
     * @param type  First published in XenServer 6.2 SP1 Tech-Preview.
     * @return reference to the newly created object
     */
    public static VGPU create(Connection c, VM VM, GPUGroup GPUGroup, String device, Map<String, String> otherConfig, VGPUType type) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.create";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(VM), Marshalling.toXMLRPC(GPUGroup), Marshalling.toXMLRPC(device), Marshalling.toXMLRPC(otherConfig), Marshalling.toXMLRPC(type)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toVGPU(result);
    }

    /**
     * 
     * First published in XenServer 6.0.
     *
     * @return Task
     */
    public Task destroyAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.VGPU.destroy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * 
     * First published in XenServer 6.0.
     *
     */
    public void destroy(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.destroy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Return a list of all the VGPUs known to the system.
     * First published in XenServer 6.0.
     *
     * @return references to all objects
     */
    public static Set<VGPU> getAll(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_all";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfVGPU(result);
    }

    /**
     * Return a map of VGPU references to VGPU records for all VGPUs known to the system.
     * First published in XenServer 6.0.
     *
     * @return records of all objects
     */
    public static Map<VGPU, VGPU.Record> getAllRecords(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "VGPU.get_all_records";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfVGPUVGPURecord(result);
    }

}