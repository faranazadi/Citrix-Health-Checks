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
 * A PCI device
 * First published in XenServer 6.0.
 *
 * @author Citrix Systems, Inc.
 */
public class PCI extends XenAPIObject {

    /**
     * The XenAPI reference (OpaqueRef) to this object.
     */
    protected final String ref;

    /**
     * For internal use only.
     */
    PCI(String ref) {
       this.ref = ref;
    }

    /**
     * @return The XenAPI reference (OpaqueRef) to this object.
     */
    public String toWireString() {
       return this.ref;
    }

    /**
     * If obj is a PCI, compares XenAPI references for equality.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj instanceof PCI)
        {
            PCI other = (PCI) obj;
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
     * Represents all the fields in a PCI
     */
    public static class Record implements Types.Record {
        public String toString() {
            StringWriter writer = new StringWriter();
            PrintWriter print = new PrintWriter(writer);
            print.printf("%1$20s: %2$s\n", "uuid", this.uuid);
            print.printf("%1$20s: %2$s\n", "clazzName", this.clazzName);
            print.printf("%1$20s: %2$s\n", "vendorName", this.vendorName);
            print.printf("%1$20s: %2$s\n", "deviceName", this.deviceName);
            print.printf("%1$20s: %2$s\n", "host", this.host);
            print.printf("%1$20s: %2$s\n", "pciId", this.pciId);
            print.printf("%1$20s: %2$s\n", "dependencies", this.dependencies);
            print.printf("%1$20s: %2$s\n", "otherConfig", this.otherConfig);
            print.printf("%1$20s: %2$s\n", "subsystemVendorName", this.subsystemVendorName);
            print.printf("%1$20s: %2$s\n", "subsystemDeviceName", this.subsystemDeviceName);
            print.printf("%1$20s: %2$s\n", "driverName", this.driverName);
            return writer.toString();
        }

        /**
         * Convert a PCI.Record to a Map
         */
        public Map<String,Object> toMap() {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("uuid", this.uuid == null ? "" : this.uuid);
            map.put("class_name", this.clazzName == null ? "" : this.clazzName);
            map.put("vendor_name", this.vendorName == null ? "" : this.vendorName);
            map.put("device_name", this.deviceName == null ? "" : this.deviceName);
            map.put("host", this.host == null ? new Host("OpaqueRef:NULL") : this.host);
            map.put("pci_id", this.pciId == null ? "" : this.pciId);
            map.put("dependencies", this.dependencies == null ? new LinkedHashSet<PCI>() : this.dependencies);
            map.put("other_config", this.otherConfig == null ? new HashMap<String, String>() : this.otherConfig);
            map.put("subsystem_vendor_name", this.subsystemVendorName == null ? "" : this.subsystemVendorName);
            map.put("subsystem_device_name", this.subsystemDeviceName == null ? "" : this.subsystemDeviceName);
            map.put("driver_name", this.driverName == null ? "" : this.driverName);
            return map;
        }

        /**
         * Unique identifier/object reference
         */
        public String uuid;
        /**
         * PCI class name
         */
        public String clazzName;
        /**
         * Vendor name
         */
        public String vendorName;
        /**
         * Device name
         */
        public String deviceName;
        /**
         * Physical machine that owns the PCI device
         */
        public Host host;
        /**
         * PCI ID of the physical device
         */
        public String pciId;
        /**
         * List of dependent PCI devices
         */
        public Set<PCI> dependencies;
        /**
         * Additional configuration
         */
        public Map<String, String> otherConfig;
        /**
         * Subsystem vendor name
         * First published in XenServer 6.2 SP1 Hotfix 11.
         */
        public String subsystemVendorName;
        /**
         * Subsystem device name
         * First published in XenServer 6.2 SP1 Hotfix 11.
         */
        public String subsystemDeviceName;
        /**
         * Driver name
         * First published in XenServer 7.5.
         */
        public String driverName;
    }

    /**
     * Get a record containing the current state of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return all fields from the object
     */
    public PCI.Record getRecord(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_record";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toPCIRecord(result);
    }

    /**
     * Get a reference to the PCI instance with the specified UUID.
     * First published in XenServer 6.0.
     *
     * @param uuid UUID of object to return
     * @return reference to the object
     */
    public static PCI getByUuid(Connection c, String uuid) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_by_uuid";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(uuid)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toPCI(result);
    }

    /**
     * Get the uuid field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public String getUuid(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_uuid";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the class_name field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public String getClazzName(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_class_name";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the vendor_name field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public String getVendorName(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_vendor_name";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the device_name field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public String getDeviceName(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_device_name";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the host field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Host getHost(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_host";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toHost(result);
    }

    /**
     * Get the pci_id field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public String getPciId(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_pci_id";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the dependencies field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Set<PCI> getDependencies(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_dependencies";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPCI(result);
    }

    /**
     * Get the other_config field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Map<String, String> getOtherConfig(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the subsystem_vendor_name field of the given PCI.
     * First published in XenServer 6.2 SP1 Hotfix 11.
     *
     * @return value of the field
     */
    public String getSubsystemVendorName(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_subsystem_vendor_name";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the subsystem_device_name field of the given PCI.
     * First published in XenServer 6.2 SP1 Hotfix 11.
     *
     * @return value of the field
     */
    public String getSubsystemDeviceName(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_subsystem_device_name";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the driver_name field of the given PCI.
     * First published in XenServer 7.5.
     *
     * @return value of the field
     */
    public String getDriverName(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_driver_name";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Set the other_config field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @param otherConfig New value to set
     */
    public void setOtherConfig(Connection c, Map<String, String> otherConfig) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.set_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(otherConfig)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Add the given key-value pair to the other_config field of the given PCI.
     * First published in XenServer 6.0.
     *
     * @param key Key to add
     * @param value Value to add
     */
    public void addToOtherConfig(Connection c, String key, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.add_to_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the given key and its corresponding value from the other_config field of the given PCI.  If the key is not in that Map, then do nothing.
     * First published in XenServer 6.0.
     *
     * @param key Key to remove
     */
    public void removeFromOtherConfig(Connection c, String key) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.remove_from_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Return a list of all the PCIs known to the system.
     * First published in XenServer 6.0.
     *
     * @return references to all objects
     */
    public static Set<PCI> getAll(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_all";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPCI(result);
    }

    /**
     * Return a map of PCI references to PCI records for all PCIs known to the system.
     * First published in XenServer 6.0.
     *
     * @return records of all objects
     */
    public static Map<PCI, PCI.Record> getAllRecords(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "PCI.get_all_records";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfPCIPCIRecord(result);
    }

}