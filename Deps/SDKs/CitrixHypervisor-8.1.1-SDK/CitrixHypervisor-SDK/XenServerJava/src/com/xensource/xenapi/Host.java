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
 * A physical host
 * First published in XenServer 4.0.
 *
 * @author Citrix Systems, Inc.
 */
public class Host extends XenAPIObject {

    /**
     * The XenAPI reference (OpaqueRef) to this object.
     */
    protected final String ref;

    /**
     * For internal use only.
     */
    Host(String ref) {
       this.ref = ref;
    }

    /**
     * @return The XenAPI reference (OpaqueRef) to this object.
     */
    public String toWireString() {
       return this.ref;
    }

    /**
     * If obj is a Host, compares XenAPI references for equality.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj instanceof Host)
        {
            Host other = (Host) obj;
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
     * Represents all the fields in a Host
     */
    public static class Record implements Types.Record {
        public String toString() {
            StringWriter writer = new StringWriter();
            PrintWriter print = new PrintWriter(writer);
            print.printf("%1$20s: %2$s\n", "uuid", this.uuid);
            print.printf("%1$20s: %2$s\n", "nameLabel", this.nameLabel);
            print.printf("%1$20s: %2$s\n", "nameDescription", this.nameDescription);
            print.printf("%1$20s: %2$s\n", "memoryOverhead", this.memoryOverhead);
            print.printf("%1$20s: %2$s\n", "allowedOperations", this.allowedOperations);
            print.printf("%1$20s: %2$s\n", "currentOperations", this.currentOperations);
            print.printf("%1$20s: %2$s\n", "APIVersionMajor", this.APIVersionMajor);
            print.printf("%1$20s: %2$s\n", "APIVersionMinor", this.APIVersionMinor);
            print.printf("%1$20s: %2$s\n", "APIVersionVendor", this.APIVersionVendor);
            print.printf("%1$20s: %2$s\n", "APIVersionVendorImplementation", this.APIVersionVendorImplementation);
            print.printf("%1$20s: %2$s\n", "enabled", this.enabled);
            print.printf("%1$20s: %2$s\n", "softwareVersion", this.softwareVersion);
            print.printf("%1$20s: %2$s\n", "otherConfig", this.otherConfig);
            print.printf("%1$20s: %2$s\n", "capabilities", this.capabilities);
            print.printf("%1$20s: %2$s\n", "cpuConfiguration", this.cpuConfiguration);
            print.printf("%1$20s: %2$s\n", "schedPolicy", this.schedPolicy);
            print.printf("%1$20s: %2$s\n", "supportedBootloaders", this.supportedBootloaders);
            print.printf("%1$20s: %2$s\n", "residentVMs", this.residentVMs);
            print.printf("%1$20s: %2$s\n", "logging", this.logging);
            print.printf("%1$20s: %2$s\n", "PIFs", this.PIFs);
            print.printf("%1$20s: %2$s\n", "suspendImageSr", this.suspendImageSr);
            print.printf("%1$20s: %2$s\n", "crashDumpSr", this.crashDumpSr);
            print.printf("%1$20s: %2$s\n", "crashdumps", this.crashdumps);
            print.printf("%1$20s: %2$s\n", "patches", this.patches);
            print.printf("%1$20s: %2$s\n", "updates", this.updates);
            print.printf("%1$20s: %2$s\n", "PBDs", this.PBDs);
            print.printf("%1$20s: %2$s\n", "hostCPUs", this.hostCPUs);
            print.printf("%1$20s: %2$s\n", "cpuInfo", this.cpuInfo);
            print.printf("%1$20s: %2$s\n", "hostname", this.hostname);
            print.printf("%1$20s: %2$s\n", "address", this.address);
            print.printf("%1$20s: %2$s\n", "metrics", this.metrics);
            print.printf("%1$20s: %2$s\n", "licenseParams", this.licenseParams);
            print.printf("%1$20s: %2$s\n", "haStatefiles", this.haStatefiles);
            print.printf("%1$20s: %2$s\n", "haNetworkPeers", this.haNetworkPeers);
            print.printf("%1$20s: %2$s\n", "blobs", this.blobs);
            print.printf("%1$20s: %2$s\n", "tags", this.tags);
            print.printf("%1$20s: %2$s\n", "externalAuthType", this.externalAuthType);
            print.printf("%1$20s: %2$s\n", "externalAuthServiceName", this.externalAuthServiceName);
            print.printf("%1$20s: %2$s\n", "externalAuthConfiguration", this.externalAuthConfiguration);
            print.printf("%1$20s: %2$s\n", "edition", this.edition);
            print.printf("%1$20s: %2$s\n", "licenseServer", this.licenseServer);
            print.printf("%1$20s: %2$s\n", "biosStrings", this.biosStrings);
            print.printf("%1$20s: %2$s\n", "powerOnMode", this.powerOnMode);
            print.printf("%1$20s: %2$s\n", "powerOnConfig", this.powerOnConfig);
            print.printf("%1$20s: %2$s\n", "localCacheSr", this.localCacheSr);
            print.printf("%1$20s: %2$s\n", "chipsetInfo", this.chipsetInfo);
            print.printf("%1$20s: %2$s\n", "PCIs", this.PCIs);
            print.printf("%1$20s: %2$s\n", "PGPUs", this.PGPUs);
            print.printf("%1$20s: %2$s\n", "PUSBs", this.PUSBs);
            print.printf("%1$20s: %2$s\n", "sslLegacy", this.sslLegacy);
            print.printf("%1$20s: %2$s\n", "guestVCPUsParams", this.guestVCPUsParams);
            print.printf("%1$20s: %2$s\n", "display", this.display);
            print.printf("%1$20s: %2$s\n", "virtualHardwarePlatformVersions", this.virtualHardwarePlatformVersions);
            print.printf("%1$20s: %2$s\n", "controlDomain", this.controlDomain);
            print.printf("%1$20s: %2$s\n", "updatesRequiringReboot", this.updatesRequiringReboot);
            print.printf("%1$20s: %2$s\n", "features", this.features);
            print.printf("%1$20s: %2$s\n", "iscsiIqn", this.iscsiIqn);
            print.printf("%1$20s: %2$s\n", "multipathing", this.multipathing);
            print.printf("%1$20s: %2$s\n", "uefiCertificates", this.uefiCertificates);
            return writer.toString();
        }

        /**
         * Convert a host.Record to a Map
         */
        public Map<String,Object> toMap() {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("uuid", this.uuid == null ? "" : this.uuid);
            map.put("name_label", this.nameLabel == null ? "" : this.nameLabel);
            map.put("name_description", this.nameDescription == null ? "" : this.nameDescription);
            map.put("memory_overhead", this.memoryOverhead == null ? 0 : this.memoryOverhead);
            map.put("allowed_operations", this.allowedOperations == null ? new LinkedHashSet<Types.HostAllowedOperations>() : this.allowedOperations);
            map.put("current_operations", this.currentOperations == null ? new HashMap<String, Types.HostAllowedOperations>() : this.currentOperations);
            map.put("API_version_major", this.APIVersionMajor == null ? 0 : this.APIVersionMajor);
            map.put("API_version_minor", this.APIVersionMinor == null ? 0 : this.APIVersionMinor);
            map.put("API_version_vendor", this.APIVersionVendor == null ? "" : this.APIVersionVendor);
            map.put("API_version_vendor_implementation", this.APIVersionVendorImplementation == null ? new HashMap<String, String>() : this.APIVersionVendorImplementation);
            map.put("enabled", this.enabled == null ? false : this.enabled);
            map.put("software_version", this.softwareVersion == null ? new HashMap<String, String>() : this.softwareVersion);
            map.put("other_config", this.otherConfig == null ? new HashMap<String, String>() : this.otherConfig);
            map.put("capabilities", this.capabilities == null ? new LinkedHashSet<String>() : this.capabilities);
            map.put("cpu_configuration", this.cpuConfiguration == null ? new HashMap<String, String>() : this.cpuConfiguration);
            map.put("sched_policy", this.schedPolicy == null ? "" : this.schedPolicy);
            map.put("supported_bootloaders", this.supportedBootloaders == null ? new LinkedHashSet<String>() : this.supportedBootloaders);
            map.put("resident_VMs", this.residentVMs == null ? new LinkedHashSet<VM>() : this.residentVMs);
            map.put("logging", this.logging == null ? new HashMap<String, String>() : this.logging);
            map.put("PIFs", this.PIFs == null ? new LinkedHashSet<PIF>() : this.PIFs);
            map.put("suspend_image_sr", this.suspendImageSr == null ? new SR("OpaqueRef:NULL") : this.suspendImageSr);
            map.put("crash_dump_sr", this.crashDumpSr == null ? new SR("OpaqueRef:NULL") : this.crashDumpSr);
            map.put("crashdumps", this.crashdumps == null ? new LinkedHashSet<HostCrashdump>() : this.crashdumps);
            map.put("patches", this.patches == null ? new LinkedHashSet<HostPatch>() : this.patches);
            map.put("updates", this.updates == null ? new LinkedHashSet<PoolUpdate>() : this.updates);
            map.put("PBDs", this.PBDs == null ? new LinkedHashSet<PBD>() : this.PBDs);
            map.put("host_CPUs", this.hostCPUs == null ? new LinkedHashSet<HostCpu>() : this.hostCPUs);
            map.put("cpu_info", this.cpuInfo == null ? new HashMap<String, String>() : this.cpuInfo);
            map.put("hostname", this.hostname == null ? "" : this.hostname);
            map.put("address", this.address == null ? "" : this.address);
            map.put("metrics", this.metrics == null ? new HostMetrics("OpaqueRef:NULL") : this.metrics);
            map.put("license_params", this.licenseParams == null ? new HashMap<String, String>() : this.licenseParams);
            map.put("ha_statefiles", this.haStatefiles == null ? new LinkedHashSet<String>() : this.haStatefiles);
            map.put("ha_network_peers", this.haNetworkPeers == null ? new LinkedHashSet<String>() : this.haNetworkPeers);
            map.put("blobs", this.blobs == null ? new HashMap<String, Blob>() : this.blobs);
            map.put("tags", this.tags == null ? new LinkedHashSet<String>() : this.tags);
            map.put("external_auth_type", this.externalAuthType == null ? "" : this.externalAuthType);
            map.put("external_auth_service_name", this.externalAuthServiceName == null ? "" : this.externalAuthServiceName);
            map.put("external_auth_configuration", this.externalAuthConfiguration == null ? new HashMap<String, String>() : this.externalAuthConfiguration);
            map.put("edition", this.edition == null ? "" : this.edition);
            map.put("license_server", this.licenseServer == null ? new HashMap<String, String>() : this.licenseServer);
            map.put("bios_strings", this.biosStrings == null ? new HashMap<String, String>() : this.biosStrings);
            map.put("power_on_mode", this.powerOnMode == null ? "" : this.powerOnMode);
            map.put("power_on_config", this.powerOnConfig == null ? new HashMap<String, String>() : this.powerOnConfig);
            map.put("local_cache_sr", this.localCacheSr == null ? new SR("OpaqueRef:NULL") : this.localCacheSr);
            map.put("chipset_info", this.chipsetInfo == null ? new HashMap<String, String>() : this.chipsetInfo);
            map.put("PCIs", this.PCIs == null ? new LinkedHashSet<PCI>() : this.PCIs);
            map.put("PGPUs", this.PGPUs == null ? new LinkedHashSet<PGPU>() : this.PGPUs);
            map.put("PUSBs", this.PUSBs == null ? new LinkedHashSet<PUSB>() : this.PUSBs);
            map.put("ssl_legacy", this.sslLegacy == null ? false : this.sslLegacy);
            map.put("guest_VCPUs_params", this.guestVCPUsParams == null ? new HashMap<String, String>() : this.guestVCPUsParams);
            map.put("display", this.display == null ? Types.HostDisplay.UNRECOGNIZED : this.display);
            map.put("virtual_hardware_platform_versions", this.virtualHardwarePlatformVersions == null ? new LinkedHashSet<Long>() : this.virtualHardwarePlatformVersions);
            map.put("control_domain", this.controlDomain == null ? new VM("OpaqueRef:NULL") : this.controlDomain);
            map.put("updates_requiring_reboot", this.updatesRequiringReboot == null ? new LinkedHashSet<PoolUpdate>() : this.updatesRequiringReboot);
            map.put("features", this.features == null ? new LinkedHashSet<Feature>() : this.features);
            map.put("iscsi_iqn", this.iscsiIqn == null ? "" : this.iscsiIqn);
            map.put("multipathing", this.multipathing == null ? false : this.multipathing);
            map.put("uefi_certificates", this.uefiCertificates == null ? "" : this.uefiCertificates);
            return map;
        }

        /**
         * Unique identifier/object reference
         */
        public String uuid;
        /**
         * a human-readable name
         */
        public String nameLabel;
        /**
         * a notes field containing human-readable description
         */
        public String nameDescription;
        /**
         * Virtualization memory overhead (bytes).
         */
        public Long memoryOverhead;
        /**
         * list of the operations allowed in this state. This list is advisory only and the server state may have changed by the time this field is read by a client.
         */
        public Set<Types.HostAllowedOperations> allowedOperations;
        /**
         * links each of the running tasks using this object (by reference) to a current_operation enum which describes the nature of the task.
         */
        public Map<String, Types.HostAllowedOperations> currentOperations;
        /**
         * major version number
         */
        public Long APIVersionMajor;
        /**
         * minor version number
         */
        public Long APIVersionMinor;
        /**
         * identification of vendor
         */
        public String APIVersionVendor;
        /**
         * details of vendor implementation
         */
        public Map<String, String> APIVersionVendorImplementation;
        /**
         * True if the host is currently enabled
         */
        public Boolean enabled;
        /**
         * version strings
         */
        public Map<String, String> softwareVersion;
        /**
         * additional configuration
         */
        public Map<String, String> otherConfig;
        /**
         * Xen capabilities
         */
        public Set<String> capabilities;
        /**
         * The CPU configuration on this host.  May contain keys such as "nr_nodes", "sockets_per_node", "cores_per_socket", or "threads_per_core"
         */
        public Map<String, String> cpuConfiguration;
        /**
         * Scheduler policy currently in force on this host
         */
        public String schedPolicy;
        /**
         * a list of the bootloaders installed on the machine
         */
        public Set<String> supportedBootloaders;
        /**
         * list of VMs currently resident on host
         */
        public Set<VM> residentVMs;
        /**
         * logging configuration
         */
        public Map<String, String> logging;
        /**
         * physical network interfaces
         */
        public Set<PIF> PIFs;
        /**
         * The SR in which VDIs for suspend images are created
         */
        public SR suspendImageSr;
        /**
         * The SR in which VDIs for crash dumps are created
         */
        public SR crashDumpSr;
        /**
         * Set of host crash dumps
         */
        public Set<HostCrashdump> crashdumps;
        /**
         * Set of host patches
         */
        public Set<HostPatch> patches;
        /**
         * Set of updates
         * First published in XenServer 7.1.
         */
        public Set<PoolUpdate> updates;
        /**
         * physical blockdevices
         */
        public Set<PBD> PBDs;
        /**
         * The physical CPUs on this host
         */
        public Set<HostCpu> hostCPUs;
        /**
         * Details about the physical CPUs on this host
         * First published in XenServer 5.6.
         */
        public Map<String, String> cpuInfo;
        /**
         * The hostname of this host
         */
        public String hostname;
        /**
         * The address by which this host can be contacted from any other host in the pool
         */
        public String address;
        /**
         * metrics associated with this host
         */
        public HostMetrics metrics;
        /**
         * State of the current license
         */
        public Map<String, String> licenseParams;
        /**
         * The set of statefiles accessible from this host
         * First published in XenServer 5.0.
         */
        public Set<String> haStatefiles;
        /**
         * The set of hosts visible via the network from this host
         * First published in XenServer 5.0.
         */
        public Set<String> haNetworkPeers;
        /**
         * Binary blobs associated with this host
         * First published in XenServer 5.0.
         */
        public Map<String, Blob> blobs;
        /**
         * user-specified tags for categorization purposes
         * First published in XenServer 5.0.
         */
        public Set<String> tags;
        /**
         * type of external authentication service configured; empty if none configured.
         * First published in XenServer 5.5.
         */
        public String externalAuthType;
        /**
         * name of external authentication service configured; empty if none configured.
         * First published in XenServer 5.5.
         */
        public String externalAuthServiceName;
        /**
         * configuration specific to external authentication service
         * First published in XenServer 5.5.
         */
        public Map<String, String> externalAuthConfiguration;
        /**
         * Product edition
         * First published in XenServer 5.6.
         */
        public String edition;
        /**
         * Contact information of the license server
         * First published in XenServer 5.6.
         */
        public Map<String, String> licenseServer;
        /**
         * BIOS strings
         * First published in XenServer 5.6.
         */
        public Map<String, String> biosStrings;
        /**
         * The power on mode
         * First published in XenServer 5.6.
         */
        public String powerOnMode;
        /**
         * The power on config
         * First published in XenServer 5.6.
         */
        public Map<String, String> powerOnConfig;
        /**
         * The SR that is used as a local cache
         * First published in XenServer 5.6 FP1.
         */
        public SR localCacheSr;
        /**
         * Information about chipset features
         * First published in XenServer 6.0.
         */
        public Map<String, String> chipsetInfo;
        /**
         * List of PCI devices in the host
         * First published in XenServer 6.0.
         */
        public Set<PCI> PCIs;
        /**
         * List of physical GPUs in the host
         * First published in XenServer 6.0.
         */
        public Set<PGPU> PGPUs;
        /**
         * List of physical USBs in the host
         * First published in XenServer 7.3.
         */
        public Set<PUSB> PUSBs;
        /**
         * Allow SSLv3 protocol and ciphersuites as used by older server versions. This controls both incoming and outgoing connections. When this is set to a different value, the host immediately restarts its SSL/TLS listening service; typically this takes less than a second but existing connections to it will be broken. API login sessions will remain valid.
         * First published in XenServer 7.0.
         */
        public Boolean sslLegacy;
        /**
         * VCPUs params to apply to all resident guests
         * First published in XenServer 6.1.
         */
        public Map<String, String> guestVCPUsParams;
        /**
         * indicates whether the host is configured to output its console to a physical display device
         * First published in XenServer 6.5 SP1.
         */
        public Types.HostDisplay display;
        /**
         * The set of versions of the virtual hardware platform that the host can offer to its guests
         * First published in XenServer 6.5 SP1.
         */
        public Set<Long> virtualHardwarePlatformVersions;
        /**
         * The control domain (domain 0)
         * First published in XenServer 7.1.
         */
        public VM controlDomain;
        /**
         * List of updates which require reboot
         * First published in XenServer 7.1.
         */
        public Set<PoolUpdate> updatesRequiringReboot;
        /**
         * List of features available on this host
         * First published in XenServer 7.2.
         */
        public Set<Feature> features;
        /**
         * The initiator IQN for the host
         * First published in XenServer 7.5.
         */
        public String iscsiIqn;
        /**
         * Specifies whether multipathing is enabled
         * First published in XenServer 7.5.
         */
        public Boolean multipathing;
        /**
         * The UEFI certificates allowing Secure Boot
         * First published in Citrix Hypervisor 8.1.
         */
        public String uefiCertificates;
    }

    /**
     * Get a record containing the current state of the given host.
     * First published in XenServer 4.0.
     *
     * @return all fields from the object
     */
    public Host.Record getRecord(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_record";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toHostRecord(result);
    }

    /**
     * Get a reference to the host instance with the specified UUID.
     * First published in XenServer 4.0.
     *
     * @param uuid UUID of object to return
     * @return reference to the object
     */
    public static Host getByUuid(Connection c, String uuid) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_by_uuid";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(uuid)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toHost(result);
    }

    /**
     * Get all the host instances with the given label.
     * First published in XenServer 4.0.
     *
     * @param label label of object to return
     * @return references to objects with matching names
     */
    public static Set<Host> getByNameLabel(Connection c, String label) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_by_name_label";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(label)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfHost(result);
    }

    /**
     * Get the uuid field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public String getUuid(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_uuid";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the name/label field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public String getNameLabel(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_name_label";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the name/description field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public String getNameDescription(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_name_description";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the memory/overhead field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Long getMemoryOverhead(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_memory_overhead";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toLong(result);
    }

    /**
     * Get the allowed_operations field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<Types.HostAllowedOperations> getAllowedOperations(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_allowed_operations";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfHostAllowedOperations(result);
    }

    /**
     * Get the current_operations field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Map<String, Types.HostAllowedOperations> getCurrentOperations(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_current_operations";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringHostAllowedOperations(result);
    }

    /**
     * Get the API_version/major field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Long getAPIVersionMajor(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_API_version_major";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toLong(result);
    }

    /**
     * Get the API_version/minor field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Long getAPIVersionMinor(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_API_version_minor";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toLong(result);
    }

    /**
     * Get the API_version/vendor field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public String getAPIVersionVendor(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_API_version_vendor";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the API_version/vendor_implementation field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Map<String, String> getAPIVersionVendorImplementation(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_API_version_vendor_implementation";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the enabled field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Boolean getEnabled(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_enabled";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toBoolean(result);
    }

    /**
     * Get the software_version field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Map<String, String> getSoftwareVersion(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_software_version";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the other_config field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Map<String, String> getOtherConfig(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the capabilities field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<String> getCapabilities(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_capabilities";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfString(result);
    }

    /**
     * Get the cpu_configuration field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Map<String, String> getCpuConfiguration(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_cpu_configuration";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the sched_policy field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public String getSchedPolicy(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_sched_policy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the supported_bootloaders field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<String> getSupportedBootloaders(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_supported_bootloaders";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfString(result);
    }

    /**
     * Get the resident_VMs field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<VM> getResidentVMs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_resident_VMs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfVM(result);
    }

    /**
     * Get the logging field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Map<String, String> getLogging(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_logging";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the PIFs field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<PIF> getPIFs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_PIFs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPIF(result);
    }

    /**
     * Get the suspend_image_sr field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public SR getSuspendImageSr(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_suspend_image_sr";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSR(result);
    }

    /**
     * Get the crash_dump_sr field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public SR getCrashDumpSr(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_crash_dump_sr";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSR(result);
    }

    /**
     * Get the crashdumps field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<HostCrashdump> getCrashdumps(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_crashdumps";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfHostCrashdump(result);
    }

    /**
     * Get the patches field of the given host.
     * First published in XenServer 4.0.
     * @deprecated
     *
     * @return value of the field
     */
   @Deprecated public Set<HostPatch> getPatches(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_patches";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfHostPatch(result);
    }

    /**
     * Get the updates field of the given host.
     * First published in XenServer 7.1.
     *
     * @return value of the field
     */
    public Set<PoolUpdate> getUpdates(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_updates";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPoolUpdate(result);
    }

    /**
     * Get the PBDs field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<PBD> getPBDs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_PBDs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPBD(result);
    }

    /**
     * Get the host_CPUs field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Set<HostCpu> getHostCPUs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_host_CPUs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfHostCpu(result);
    }

    /**
     * Get the cpu_info field of the given host.
     * First published in XenServer 5.6.
     *
     * @return value of the field
     */
    public Map<String, String> getCpuInfo(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_cpu_info";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the hostname field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public String getHostname(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_hostname";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the address field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public String getAddress(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_address";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the metrics field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public HostMetrics getMetrics(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_metrics";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toHostMetrics(result);
    }

    /**
     * Get the license_params field of the given host.
     * First published in XenServer 4.0.
     *
     * @return value of the field
     */
    public Map<String, String> getLicenseParams(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_license_params";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the ha_statefiles field of the given host.
     * First published in XenServer 5.0.
     *
     * @return value of the field
     */
    public Set<String> getHaStatefiles(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_ha_statefiles";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfString(result);
    }

    /**
     * Get the ha_network_peers field of the given host.
     * First published in XenServer 5.0.
     *
     * @return value of the field
     */
    public Set<String> getHaNetworkPeers(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_ha_network_peers";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfString(result);
    }

    /**
     * Get the blobs field of the given host.
     * First published in XenServer 5.0.
     *
     * @return value of the field
     */
    public Map<String, Blob> getBlobs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_blobs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringBlob(result);
    }

    /**
     * Get the tags field of the given host.
     * First published in XenServer 5.0.
     *
     * @return value of the field
     */
    public Set<String> getTags(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_tags";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfString(result);
    }

    /**
     * Get the external_auth_type field of the given host.
     * First published in XenServer 5.5.
     *
     * @return value of the field
     */
    public String getExternalAuthType(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_external_auth_type";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the external_auth_service_name field of the given host.
     * First published in XenServer 5.5.
     *
     * @return value of the field
     */
    public String getExternalAuthServiceName(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_external_auth_service_name";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the external_auth_configuration field of the given host.
     * First published in XenServer 5.5.
     *
     * @return value of the field
     */
    public Map<String, String> getExternalAuthConfiguration(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_external_auth_configuration";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the edition field of the given host.
     * First published in XenServer 5.6.
     *
     * @return value of the field
     */
    public String getEdition(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_edition";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the license_server field of the given host.
     * First published in XenServer 5.6.
     *
     * @return value of the field
     */
    public Map<String, String> getLicenseServer(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_license_server";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the bios_strings field of the given host.
     * First published in XenServer 5.6.
     *
     * @return value of the field
     */
    public Map<String, String> getBiosStrings(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_bios_strings";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the power_on_mode field of the given host.
     * First published in XenServer 5.6.
     *
     * @return value of the field
     */
    public String getPowerOnMode(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_power_on_mode";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the power_on_config field of the given host.
     * First published in XenServer 5.6.
     *
     * @return value of the field
     */
    public Map<String, String> getPowerOnConfig(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_power_on_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the local_cache_sr field of the given host.
     * First published in XenServer 5.6 FP1.
     *
     * @return value of the field
     */
    public SR getLocalCacheSr(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_local_cache_sr";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSR(result);
    }

    /**
     * Get the chipset_info field of the given host.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Map<String, String> getChipsetInfo(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_chipset_info";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the PCIs field of the given host.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Set<PCI> getPCIs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_PCIs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPCI(result);
    }

    /**
     * Get the PGPUs field of the given host.
     * First published in XenServer 6.0.
     *
     * @return value of the field
     */
    public Set<PGPU> getPGPUs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_PGPUs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPGPU(result);
    }

    /**
     * Get the PUSBs field of the given host.
     * First published in XenServer 7.3.
     *
     * @return value of the field
     */
    public Set<PUSB> getPUSBs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_PUSBs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPUSB(result);
    }

    /**
     * Get the ssl_legacy field of the given host.
     * First published in XenServer 7.0.
     *
     * @return value of the field
     */
    public Boolean getSslLegacy(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_ssl_legacy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toBoolean(result);
    }

    /**
     * Get the guest_VCPUs_params field of the given host.
     * First published in XenServer 6.1.
     *
     * @return value of the field
     */
    public Map<String, String> getGuestVCPUsParams(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_guest_VCPUs_params";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Get the display field of the given host.
     * First published in XenServer 6.5 SP1.
     *
     * @return value of the field
     */
    public Types.HostDisplay getDisplay(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_display";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toHostDisplay(result);
    }

    /**
     * Get the virtual_hardware_platform_versions field of the given host.
     * First published in XenServer 6.5 SP1.
     *
     * @return value of the field
     */
    public Set<Long> getVirtualHardwarePlatformVersions(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_virtual_hardware_platform_versions";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfLong(result);
    }

    /**
     * Get the control_domain field of the given host.
     * First published in XenServer 7.1.
     *
     * @return value of the field
     */
    public VM getControlDomain(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_control_domain";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toVM(result);
    }

    /**
     * Get the updates_requiring_reboot field of the given host.
     * First published in XenServer 7.1.
     *
     * @return value of the field
     */
    public Set<PoolUpdate> getUpdatesRequiringReboot(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_updates_requiring_reboot";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfPoolUpdate(result);
    }

    /**
     * Get the features field of the given host.
     * First published in XenServer 7.2.
     *
     * @return value of the field
     */
    public Set<Feature> getFeatures(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_features";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfFeature(result);
    }

    /**
     * Get the iscsi_iqn field of the given host.
     * First published in XenServer 7.5.
     *
     * @return value of the field
     */
    public String getIscsiIqn(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_iscsi_iqn";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the multipathing field of the given host.
     * First published in XenServer 7.5.
     *
     * @return value of the field
     */
    public Boolean getMultipathing(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_multipathing";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toBoolean(result);
    }

    /**
     * Get the uefi_certificates field of the given host.
     * First published in Citrix Hypervisor 8.1.
     *
     * @return value of the field
     */
    public String getUefiCertificates(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_uefi_certificates";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Set the name/label field of the given host.
     * First published in XenServer 4.0.
     *
     * @param label New value to set
     */
    public void setNameLabel(Connection c, String label) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_name_label";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(label)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the name/description field of the given host.
     * First published in XenServer 4.0.
     *
     * @param description New value to set
     */
    public void setNameDescription(Connection c, String description) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_name_description";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(description)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the other_config field of the given host.
     * First published in XenServer 4.0.
     *
     * @param otherConfig New value to set
     */
    public void setOtherConfig(Connection c, Map<String, String> otherConfig) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(otherConfig)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Add the given key-value pair to the other_config field of the given host.
     * First published in XenServer 4.0.
     *
     * @param key Key to add
     * @param value Value to add
     */
    public void addToOtherConfig(Connection c, String key, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.add_to_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the given key and its corresponding value from the other_config field of the given host.  If the key is not in that Map, then do nothing.
     * First published in XenServer 4.0.
     *
     * @param key Key to remove
     */
    public void removeFromOtherConfig(Connection c, String key) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.remove_from_other_config";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the logging field of the given host.
     * First published in XenServer 4.0.
     *
     * @param logging New value to set
     */
    public void setLogging(Connection c, Map<String, String> logging) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_logging";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(logging)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Add the given key-value pair to the logging field of the given host.
     * First published in XenServer 4.0.
     *
     * @param key Key to add
     * @param value Value to add
     */
    public void addToLogging(Connection c, String key, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.add_to_logging";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the given key and its corresponding value from the logging field of the given host.  If the key is not in that Map, then do nothing.
     * First published in XenServer 4.0.
     *
     * @param key Key to remove
     */
    public void removeFromLogging(Connection c, String key) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.remove_from_logging";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the suspend_image_sr field of the given host.
     * First published in XenServer 4.0.
     *
     * @param suspendImageSr New value to set
     */
    public void setSuspendImageSr(Connection c, SR suspendImageSr) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_suspend_image_sr";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(suspendImageSr)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the crash_dump_sr field of the given host.
     * First published in XenServer 4.0.
     *
     * @param crashDumpSr New value to set
     */
    public void setCrashDumpSr(Connection c, SR crashDumpSr) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_crash_dump_sr";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(crashDumpSr)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the hostname field of the given host.
     * First published in XenServer 4.0.
     *
     * @param hostname New value to set
     */
    public void setHostname(Connection c, String hostname) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_hostname";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(hostname)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the address field of the given host.
     * First published in XenServer 4.0.
     *
     * @param address New value to set
     */
    public void setAddress(Connection c, String address) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_address";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(address)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the tags field of the given host.
     * First published in XenServer 5.0.
     *
     * @param tags New value to set
     */
    public void setTags(Connection c, Set<String> tags) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_tags";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(tags)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Add the given value to the tags field of the given host.  If the value is already in that Set, then do nothing.
     * First published in XenServer 5.0.
     *
     * @param value New value to add
     */
    public void addTags(Connection c, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.add_tags";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the given value from the tags field of the given host.  If the value is not in that Set, then do nothing.
     * First published in XenServer 5.0.
     *
     * @param value Value to remove
     */
    public void removeTags(Connection c, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.remove_tags";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the license_server field of the given host.
     * First published in XenServer 5.6.
     *
     * @param licenseServer New value to set
     */
    public void setLicenseServer(Connection c, Map<String, String> licenseServer) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_license_server";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(licenseServer)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Add the given key-value pair to the license_server field of the given host.
     * First published in XenServer 5.6.
     *
     * @param key Key to add
     * @param value Value to add
     */
    public void addToLicenseServer(Connection c, String key, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.add_to_license_server";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the given key and its corresponding value from the license_server field of the given host.  If the key is not in that Map, then do nothing.
     * First published in XenServer 5.6.
     *
     * @param key Key to remove
     */
    public void removeFromLicenseServer(Connection c, String key) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.remove_from_license_server";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the guest_VCPUs_params field of the given host.
     * First published in XenServer 6.1.
     *
     * @param guestVCPUsParams New value to set
     */
    public void setGuestVCPUsParams(Connection c, Map<String, String> guestVCPUsParams) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_guest_VCPUs_params";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(guestVCPUsParams)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Add the given key-value pair to the guest_VCPUs_params field of the given host.
     * First published in XenServer 6.1.
     *
     * @param key Key to add
     * @param value Value to add
     */
    public void addToGuestVCPUsParams(Connection c, String key, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.add_to_guest_VCPUs_params";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the given key and its corresponding value from the guest_VCPUs_params field of the given host.  If the key is not in that Map, then do nothing.
     * First published in XenServer 6.1.
     *
     * @param key Key to remove
     */
    public void removeFromGuestVCPUsParams(Connection c, String key) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.remove_from_guest_VCPUs_params";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(key)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the display field of the given host.
     * First published in XenServer 6.5 SP1.
     *
     * @param display New value to set
     */
    public void setDisplay(Connection c, Types.HostDisplay display) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_display";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(display)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Puts the host into a state in which no new VMs can be started. Currently active VMs on the host continue to execute.
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task disableAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.disable";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Puts the host into a state in which no new VMs can be started. Currently active VMs on the host continue to execute.
     * First published in XenServer 4.0.
     *
     */
    public void disable(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.disable";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Puts the host into a state in which new VMs can be started.
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task enableAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.enable";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Puts the host into a state in which new VMs can be started.
     * First published in XenServer 4.0.
     *
     */
    public void enable(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.enable";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Shutdown the host. (This function can only be called if there are no currently running VMs on the host and it is disabled.)
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task shutdownAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.shutdown";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Shutdown the host. (This function can only be called if there are no currently running VMs on the host and it is disabled.)
     * First published in XenServer 4.0.
     *
     */
    public void shutdown(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.shutdown";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Reboot the host. (This function can only be called if there are no currently running VMs on the host and it is disabled.)
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task rebootAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.reboot";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Reboot the host. (This function can only be called if there are no currently running VMs on the host and it is disabled.)
     * First published in XenServer 4.0.
     *
     */
    public void reboot(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.reboot";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Get the host xen dmesg.
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task dmesgAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.dmesg";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Get the host xen dmesg.
     * First published in XenServer 4.0.
     *
     * @return dmesg string
     */
    public String dmesg(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.dmesg";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the host xen dmesg, and clear the buffer.
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task dmesgClearAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.dmesg_clear";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Get the host xen dmesg, and clear the buffer.
     * First published in XenServer 4.0.
     *
     * @return dmesg string
     */
    public String dmesgClear(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.dmesg_clear";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Get the host's log file
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task getLogAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.get_log";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Get the host's log file
     * First published in XenServer 4.0.
     *
     * @return The contents of the host's primary log file
     */
    public String getLog(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_log";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Inject the given string as debugging keys into Xen
     * First published in XenServer 4.0.
     *
     * @param keys The keys to send
     * @return Task
     */
    public Task sendDebugKeysAsync(Connection c, String keys) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.send_debug_keys";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(keys)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Inject the given string as debugging keys into Xen
     * First published in XenServer 4.0.
     *
     * @param keys The keys to send
     */
    public void sendDebugKeys(Connection c, String keys) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.send_debug_keys";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(keys)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Run xen-bugtool --yestoall and upload the output to support
     * First published in XenServer 4.0.
     *
     * @param url The URL to upload to
     * @param options Extra configuration operations
     * @return Task
     */
    public Task bugreportUploadAsync(Connection c, String url, Map<String, String> options) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.bugreport_upload";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(url), Marshalling.toXMLRPC(options)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Run xen-bugtool --yestoall and upload the output to support
     * First published in XenServer 4.0.
     *
     * @param url The URL to upload to
     * @param options Extra configuration operations
     */
    public void bugreportUpload(Connection c, String url, Map<String, String> options) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.bugreport_upload";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(url), Marshalling.toXMLRPC(options)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * List all supported methods
     * First published in XenServer 4.0.
     *
     * @return The name of every supported method.
     */
    public static Set<String> listMethods(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.list_methods";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfString(result);
    }

    /**
     * Apply a new license to a host
     * First published in XenServer 4.0.
     *
     * @param contents The contents of the license file, base64 encoded
     * @return Task
     */
    public Task licenseApplyAsync(Connection c, String contents) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException,
       Types.LicenseProcessingError {
        String method_call = "Async.host.license_apply";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(contents)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Apply a new license to a host
     * First published in XenServer 4.0.
     *
     * @param contents The contents of the license file, base64 encoded
     */
    public void licenseApply(Connection c, String contents) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException,
       Types.LicenseProcessingError {
        String method_call = "host.license_apply";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(contents)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Apply a new license to a host
     * First published in XenServer 6.5 SP1 Hotfix 31.
     *
     * @param contents The contents of the license file, base64 encoded
     * @return Task
     */
    public Task licenseAddAsync(Connection c, String contents) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException,
       Types.LicenseProcessingError {
        String method_call = "Async.host.license_add";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(contents)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Apply a new license to a host
     * First published in XenServer 6.5 SP1 Hotfix 31.
     *
     * @param contents The contents of the license file, base64 encoded
     */
    public void licenseAdd(Connection c, String contents) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException,
       Types.LicenseProcessingError {
        String method_call = "host.license_add";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(contents)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove any license file from the specified host, and switch that host to the unlicensed edition
     * First published in XenServer 6.5 SP1 Hotfix 31.
     *
     * @return Task
     */
    public Task licenseRemoveAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.license_remove";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Remove any license file from the specified host, and switch that host to the unlicensed edition
     * First published in XenServer 6.5 SP1 Hotfix 31.
     *
     */
    public void licenseRemove(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.license_remove";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Destroy specified host record in database
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task destroyAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.destroy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Destroy specified host record in database
     * First published in XenServer 4.0.
     *
     */
    public void destroy(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.destroy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Attempt to power-on the host (if the capability exists).
     * First published in XenServer 5.0.
     *
     * @return Task
     */
    public Task powerOnAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.power_on";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Attempt to power-on the host (if the capability exists).
     * First published in XenServer 5.0.
     *
     */
    public void powerOn(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.power_on";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * This call disables HA on the local host. This should only be used with extreme care.
     * First published in XenServer 5.0.
     *
     * @param soft Disable HA temporarily, revert upon host reboot or further changes, idempotent First published in XenServer 7.1.
     */
    public static void emergencyHaDisable(Connection c, Boolean soft) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.emergency_ha_disable";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(soft)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * 
     * First published in XenServer 5.0.
     *
     * @return A set of data sources
     */
    public Set<DataSource.Record> getDataSources(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_data_sources";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfDataSourceRecord(result);
    }

    /**
     * Start recording the specified data source
     * First published in XenServer 5.0.
     *
     * @param dataSource The data source to record
     */
    public void recordDataSource(Connection c, String dataSource) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.record_data_source";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(dataSource)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Query the latest value of the specified data source
     * First published in XenServer 5.0.
     *
     * @param dataSource The data source to query
     * @return The latest value, averaged over the last 5 seconds
     */
    public Double queryDataSource(Connection c, String dataSource) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.query_data_source";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(dataSource)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toDouble(result);
    }

    /**
     * Forget the recorded statistics related to the specified data source
     * First published in XenServer 5.0.
     *
     * @param dataSource The data source whose archives are to be forgotten
     */
    public void forgetDataSourceArchives(Connection c, String dataSource) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.forget_data_source_archives";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(dataSource)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Check this host can be evacuated.
     * First published in XenServer 4.1.
     *
     * @return Task
     */
    public Task assertCanEvacuateAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.assert_can_evacuate";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Check this host can be evacuated.
     * First published in XenServer 4.1.
     *
     */
    public void assertCanEvacuate(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.assert_can_evacuate";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Return a set of VMs which prevent the host being evacuated, with per-VM error codes
     * First published in XenServer 5.0.
     *
     * @return Task
     */
    public Task getVmsWhichPreventEvacuationAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.get_vms_which_prevent_evacuation";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Return a set of VMs which prevent the host being evacuated, with per-VM error codes
     * First published in XenServer 5.0.
     *
     * @return VMs which block evacuation together with reasons
     */
    public Map<VM, Set<String>> getVmsWhichPreventEvacuation(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_vms_which_prevent_evacuation";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfVMSetOfString(result);
    }

    /**
     * Return a set of VMs which are not co-operating with the host's memory control system
     * First published in XenServer 5.6.
     * @deprecated
     *
     * @return Task
     */
   @Deprecated public Task getUncooperativeResidentVMsAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.get_uncooperative_resident_VMs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Return a set of VMs which are not co-operating with the host's memory control system
     * First published in XenServer 5.6.
     * @deprecated
     *
     * @return VMs which are not co-operating
     */
   @Deprecated public Set<VM> getUncooperativeResidentVMs(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_uncooperative_resident_VMs";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfVM(result);
    }

    /**
     * Migrate all VMs off of this host, where possible.
     * First published in XenServer 4.1.
     *
     * @return Task
     */
    public Task evacuateAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.evacuate";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Migrate all VMs off of this host, where possible.
     * First published in XenServer 4.1.
     *
     */
    public void evacuate(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.evacuate";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Re-configure syslog logging
     * First published in XenServer 4.1.
     *
     * @return Task
     */
    public Task syslogReconfigureAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.syslog_reconfigure";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Re-configure syslog logging
     * First published in XenServer 4.1.
     *
     */
    public void syslogReconfigure(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.syslog_reconfigure";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Reconfigure the management network interface
     * First published in XenServer 4.1.
     *
     * @param pif reference to a PIF object corresponding to the management interface
     * @return Task
     */
    public static Task managementReconfigureAsync(Connection c, PIF pif) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.management_reconfigure";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(pif)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Reconfigure the management network interface
     * First published in XenServer 4.1.
     *
     * @param pif reference to a PIF object corresponding to the management interface
     */
    public static void managementReconfigure(Connection c, PIF pif) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.management_reconfigure";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(pif)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Reconfigure the management network interface. Should only be used if Host.management_reconfigure is impossible because the network configuration is broken.
     * First published in XenServer 4.1.
     *
     * @param iface name of the interface to use as a management interface
     */
    public static void localManagementReconfigure(Connection c, String iface) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.local_management_reconfigure";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(iface)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Disable the management network interface
     * First published in XenServer 4.1.
     *
     */
    public static void managementDisable(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.management_disable";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Returns the management interface for the specified host
     * Experimental. First published in XenServer 6.1.
     *
     * @return Task
     */
    public Task getManagementIfaceAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.get_management_interface";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Returns the management interface for the specified host
     * Experimental. First published in XenServer 6.1.
     *
     * @return The management interface for the host
     */
    public PIF getManagementIface(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_management_interface";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toPIF(result);
    }

    /**
     * 
     * First published in XenServer 4.1.
     *
     * @return An XML fragment containing the system status capabilities.
     */
    public String getSystemStatusCapabilities(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_system_status_capabilities";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Restarts the agent after a 10 second pause. WARNING: this is a dangerous operation. Any operations in progress will be aborted, and unrecoverable data loss may occur. The caller is responsible for ensuring that there are no operations in progress when this method is called.
     * First published in XenServer 4.0.
     *
     * @return Task
     */
    public Task restartAgentAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.restart_agent";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Restarts the agent after a 10 second pause. WARNING: this is a dangerous operation. Any operations in progress will be aborted, and unrecoverable data loss may occur. The caller is responsible for ensuring that there are no operations in progress when this method is called.
     * First published in XenServer 4.0.
     *
     */
    public void restartAgent(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.restart_agent";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Shuts the agent down after a 10 second pause. WARNING: this is a dangerous operation. Any operations in progress will be aborted, and unrecoverable data loss may occur. The caller is responsible for ensuring that there are no operations in progress when this method is called.
     * First published in XenServer 5.0.
     *
     */
    public static void shutdownAgent(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.shutdown_agent";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Sets the host name to the specified string.  Both the API and lower-level system hostname are changed immediately.
     * First published in XenServer 4.1.
     *
     * @param hostname The new host name
     */
    public void setHostnameLive(Connection c, String hostname) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException,
       Types.HostNameInvalid {
        String method_call = "host.set_hostname_live";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(hostname)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Computes the amount of free memory on the host.
     * First published in XenServer 5.0.
     *
     * @return Task
     */
    public Task computeFreeMemoryAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.compute_free_memory";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Computes the amount of free memory on the host.
     * First published in XenServer 5.0.
     *
     * @return the amount of free memory on the host.
     */
    public Long computeFreeMemory(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.compute_free_memory";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toLong(result);
    }

    /**
     * Computes the virtualization memory overhead of a host.
     * First published in XenServer 5.6.
     *
     * @return Task
     */
    public Task computeMemoryOverheadAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.compute_memory_overhead";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Computes the virtualization memory overhead of a host.
     * First published in XenServer 5.6.
     *
     * @return the virtualization memory overhead of the host.
     */
    public Long computeMemoryOverhead(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.compute_memory_overhead";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toLong(result);
    }

    /**
     * This causes the synchronisation of the non-database data (messages, RRDs and so on) stored on the master to be synchronised with the host
     * First published in XenServer 5.0.
     *
     */
    public void syncData(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.sync_data";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * This causes the RRDs to be backed up to the master
     * First published in XenServer 5.0.
     *
     * @param delay Delay in seconds from when the call is received to perform the backup
     */
    public void backupRrds(Connection c, Double delay) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.backup_rrds";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(delay)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Create a placeholder for a named binary blob of data that is associated with this host
     * First published in XenServer 5.0.
     *
     * @param name The name associated with the blob
     * @param mimeType The mime type for the data. Empty string translates to application/octet-stream
     * @return Task
     */
    public Task createNewBlobAsync(Connection c, String name, String mimeType) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.create_new_blob";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(name), Marshalling.toXMLRPC(mimeType)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Create a placeholder for a named binary blob of data that is associated with this host
     * First published in XenServer 5.0.
     *
     * @param name The name associated with the blob
     * @param mimeType The mime type for the data. Empty string translates to application/octet-stream
     * @return The reference of the blob, needed for populating its data
     */
    public Blob createNewBlob(Connection c, String name, String mimeType) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.create_new_blob";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(name), Marshalling.toXMLRPC(mimeType)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toBlob(result);
    }

    /**
     * Create a placeholder for a named binary blob of data that is associated with this host
     * First published in XenServer 5.0.
     *
     * @param name The name associated with the blob
     * @param mimeType The mime type for the data. Empty string translates to application/octet-stream
     * @param _public True if the blob should be publicly available First published in XenServer 6.1.
     * @return Task
     */
    public Task createNewBlobAsync(Connection c, String name, String mimeType, Boolean _public) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.create_new_blob";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(name), Marshalling.toXMLRPC(mimeType), Marshalling.toXMLRPC(_public)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Create a placeholder for a named binary blob of data that is associated with this host
     * First published in XenServer 5.0.
     *
     * @param name The name associated with the blob
     * @param mimeType The mime type for the data. Empty string translates to application/octet-stream
     * @param _public True if the blob should be publicly available First published in XenServer 6.1.
     * @return The reference of the blob, needed for populating its data
     */
    public Blob createNewBlob(Connection c, String name, String mimeType, Boolean _public) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.create_new_blob";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(name), Marshalling.toXMLRPC(mimeType), Marshalling.toXMLRPC(_public)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toBlob(result);
    }

    /**
     * Call an API plugin on this host
     * First published in XenServer 5.0.
     *
     * @param plugin The name of the plugin
     * @param fn The name of the function within the plugin
     * @param args Arguments for the function
     * @return Task
     */
    public Task callPluginAsync(Connection c, String plugin, String fn, Map<String, String> args) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.call_plugin";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(plugin), Marshalling.toXMLRPC(fn), Marshalling.toXMLRPC(args)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Call an API plugin on this host
     * First published in XenServer 5.0.
     *
     * @param plugin The name of the plugin
     * @param fn The name of the function within the plugin
     * @param args Arguments for the function
     * @return Result from the plugin
     */
    public String callPlugin(Connection c, String plugin, String fn, Map<String, String> args) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.call_plugin";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(plugin), Marshalling.toXMLRPC(fn), Marshalling.toXMLRPC(args)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Return true if the extension is available on the host
     * First published in XenServer 7.1.
     *
     * @param name The name of the API call
     * @return Task
     */
    public Task hasExtensionAsync(Connection c, String name) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.has_extension";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(name)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Return true if the extension is available on the host
     * First published in XenServer 7.1.
     *
     * @param name The name of the API call
     * @return True if the extension exists, false otherwise
     */
    public Boolean hasExtension(Connection c, String name) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.has_extension";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(name)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toBoolean(result);
    }

    /**
     * Call an API extension on this host
     * First published in XenServer 7.1.
     *
     * @param call Rpc call for the extension
     * @return Result from the extension
     */
    public String callExtension(Connection c, String call) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.call_extension";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(call)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * This call queries the host's clock for the current time
     * First published in XenServer 5.0.
     *
     * @return The current time
     */
    public Date getServertime(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_servertime";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toDate(result);
    }

    /**
     * This call queries the host's clock for the current time in the host's local timezone
     * First published in XenServer 5.6 FP1.
     *
     * @return The current local time
     */
    public Date getServerLocaltime(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_server_localtime";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toDate(result);
    }

    /**
     * This call enables external authentication on a host
     * First published in XenServer 5.5.
     *
     * @param config A list of key-values containing the configuration data
     * @param serviceName The name of the service
     * @param authType The type of authentication (e.g. AD for Active Directory)
     */
    public void enableExternalAuth(Connection c, Map<String, String> config, String serviceName, String authType) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.enable_external_auth";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(config), Marshalling.toXMLRPC(serviceName), Marshalling.toXMLRPC(authType)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * This call disables external authentication on the local host
     * First published in XenServer 5.5.
     *
     * @param config Optional parameters as a list of key-values containing the configuration data
     */
    public void disableExternalAuth(Connection c, Map<String, String> config) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.disable_external_auth";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(config)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Retrieves recommended host migrations to perform when evacuating the host from the wlb server. If a VM cannot be migrated from the host the reason is listed instead of a recommendation.
     * First published in XenServer 5.5.
     *
     * @return Task
     */
    public Task retrieveWlbEvacuateRecommendationsAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.retrieve_wlb_evacuate_recommendations";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Retrieves recommended host migrations to perform when evacuating the host from the wlb server. If a VM cannot be migrated from the host the reason is listed instead of a recommendation.
     * First published in XenServer 5.5.
     *
     * @return VMs and the reasons why they would block evacuation, or their target host recommended by the wlb server
     */
    public Map<VM, Set<String>> retrieveWlbEvacuateRecommendations(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.retrieve_wlb_evacuate_recommendations";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfVMSetOfString(result);
    }

    /**
     * Get the installed server public TLS certificate.
     * First published in XenServer 5.5.
     *
     * @return Task
     */
    public Task getServerCertificateAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.get_server_certificate";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Get the installed server public TLS certificate.
     * First published in XenServer 5.5.
     *
     * @return The installed server public TLS certificate, in PEM form.
     */
    public String getServerCertificate(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_server_certificate";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * Change to another edition, or reactivate the current edition after a license has expired. This may be subject to the successful checkout of an appropriate license.
     * First published in XenServer 5.6.
     *
     * @param edition The requested edition
     */
    public void applyEdition(Connection c, String edition) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.apply_edition";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(edition)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Change to another edition, or reactivate the current edition after a license has expired. This may be subject to the successful checkout of an appropriate license.
     * First published in XenServer 5.6.
     *
     * @param edition The requested edition
     * @param force Update the license params even if the apply call fails First published in XenServer 6.2.
     */
    public void applyEdition(Connection c, String edition, Boolean force) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.apply_edition";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(edition), Marshalling.toXMLRPC(force)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Refresh the list of installed Supplemental Packs.
     * First published in XenServer 5.6.
     * @deprecated
     *
     * @return Task
     */
   @Deprecated public Task refreshPackInfoAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.refresh_pack_info";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Refresh the list of installed Supplemental Packs.
     * First published in XenServer 5.6.
     * @deprecated
     *
     */
   @Deprecated public void refreshPackInfo(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.refresh_pack_info";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the power-on-mode, host, user and password 
     * First published in XenServer 5.6.
     *
     * @param powerOnMode power-on-mode can be empty,iLO,wake-on-lan, DRAC or other
     * @param powerOnConfig Power on config
     * @return Task
     */
    public Task setPowerOnModeAsync(Connection c, String powerOnMode, Map<String, String> powerOnConfig) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.set_power_on_mode";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(powerOnMode), Marshalling.toXMLRPC(powerOnConfig)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Set the power-on-mode, host, user and password 
     * First published in XenServer 5.6.
     *
     * @param powerOnMode power-on-mode can be empty,iLO,wake-on-lan, DRAC or other
     * @param powerOnConfig Power on config
     */
    public void setPowerOnMode(Connection c, String powerOnMode, Map<String, String> powerOnConfig) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_power_on_mode";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(powerOnMode), Marshalling.toXMLRPC(powerOnConfig)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Set the CPU features to be used after a reboot, if the given features string is valid.
     * First published in XenServer 5.6.
     *
     * @param features The features string (32 hexadecimal digits)
     */
    public void setCpuFeatures(Connection c, String features) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_cpu_features";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(features)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Remove the feature mask, such that after a reboot all features of the CPU are enabled.
     * First published in XenServer 5.6.
     *
     */
    public void resetCpuFeatures(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.reset_cpu_features";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Enable the use of a local SR for caching purposes
     * First published in XenServer 5.6 FP1.
     *
     * @param sr The SR to use as a local cache
     */
    public void enableLocalStorageCaching(Connection c, SR sr) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.enable_local_storage_caching";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(sr)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Disable the use of a local SR for caching purposes
     * First published in XenServer 5.6 FP1.
     *
     */
    public void disableLocalStorageCaching(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.disable_local_storage_caching";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Prepare to receive a VM, returning a token which can be passed to VM.migrate.
     * First published in XenServer 6.1.
     *
     * @param network The network through which migration traffic should be received.
     * @param options Extra configuration operations
     * @return Task
     */
    public Task migrateReceiveAsync(Connection c, Network network, Map<String, String> options) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.migrate_receive";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(network), Marshalling.toXMLRPC(options)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Prepare to receive a VM, returning a token which can be passed to VM.migrate.
     * First published in XenServer 6.1.
     *
     * @param network The network through which migration traffic should be received.
     * @param options Extra configuration operations
     * @return A value which should be passed to VM.migrate
     */
    public Map<String, String> migrateReceive(Connection c, Network network, Map<String, String> options) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.migrate_receive";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(network), Marshalling.toXMLRPC(options)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * Declare that a host is dead. This is a dangerous operation, and should only be called if the administrator is absolutely sure the host is definitely dead
     * First published in XenServer 6.2.
     *
     * @return Task
     */
    public Task declareDeadAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.declare_dead";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Declare that a host is dead. This is a dangerous operation, and should only be called if the administrator is absolutely sure the host is definitely dead
     * First published in XenServer 6.2.
     *
     */
    public void declareDead(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.declare_dead";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Enable console output to the physical display device next time this host boots
     * First published in XenServer 6.5 SP1.
     *
     * @return Task
     */
    public Task enableDisplayAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.enable_display";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Enable console output to the physical display device next time this host boots
     * First published in XenServer 6.5 SP1.
     *
     * @return This host's physical display usage
     */
    public Types.HostDisplay enableDisplay(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.enable_display";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toHostDisplay(result);
    }

    /**
     * Disable console output to the physical display device next time this host boots
     * First published in XenServer 6.5 SP1.
     *
     * @return Task
     */
    public Task disableDisplayAsync(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.disable_display";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Disable console output to the physical display device next time this host boots
     * First published in XenServer 6.5 SP1.
     *
     * @return This host's physical display usage
     */
    public Types.HostDisplay disableDisplay(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.disable_display";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toHostDisplay(result);
    }

    /**
     * Enable/disable SSLv3 for interoperability with older server versions. When this is set to a different value, the host immediately restarts its SSL/TLS listening service; typically this takes less than a second but existing connections to it will be broken. API login sessions will remain valid.
     * First published in XenServer 7.0.
     *
     * @param value True to allow SSLv3 and ciphersuites as used in old XenServer versions
     * @return Task
     */
    public Task setSslLegacyAsync(Connection c, Boolean value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.set_ssl_legacy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Enable/disable SSLv3 for interoperability with older server versions. When this is set to a different value, the host immediately restarts its SSL/TLS listening service; typically this takes less than a second but existing connections to it will be broken. API login sessions will remain valid.
     * First published in XenServer 7.0.
     *
     * @param value True to allow SSLv3 and ciphersuites as used in old XenServer versions
     */
    public void setSslLegacy(Connection c, Boolean value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_ssl_legacy";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Sets the initiator IQN for the host
     * First published in XenServer 7.5.
     *
     * @param value The value to which the IQN should be set
     * @return Task
     */
    public Task setIscsiIqnAsync(Connection c, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.set_iscsi_iqn";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Sets the initiator IQN for the host
     * First published in XenServer 7.5.
     *
     * @param value The value to which the IQN should be set
     */
    public void setIscsiIqn(Connection c, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_iscsi_iqn";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Specifies whether multipathing is enabled
     * First published in XenServer 7.5.
     *
     * @param value Whether multipathing should be enabled
     * @return Task
     */
    public Task setMultipathingAsync(Connection c, Boolean value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.set_multipathing";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Specifies whether multipathing is enabled
     * First published in XenServer 7.5.
     *
     * @param value Whether multipathing should be enabled
     */
    public void setMultipathing(Connection c, Boolean value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_multipathing";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Sets the UEFI certificates on a host
     * First published in Citrix Hypervisor 8.1.
     *
     * @param value The certificates to apply to a host
     * @return Task
     */
    public Task setUefiCertificatesAsync(Connection c, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.host.set_uefi_certificates";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Sets the UEFI certificates on a host
     * First published in Citrix Hypervisor 8.1.
     *
     * @param value The certificates to apply to a host
     */
    public void setUefiCertificates(Connection c, String value) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.set_uefi_certificates";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref), Marshalling.toXMLRPC(value)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Return a list of all the hosts known to the system.
     * First published in XenServer 4.0.
     *
     * @return references to all objects
     */
    public static Set<Host> getAll(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_all";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfHost(result);
    }

    /**
     * Return a map of host references to host records for all hosts known to the system.
     * First published in XenServer 4.0.
     *
     * @return records of all objects
     */
    public static Map<Host, Host.Record> getAllRecords(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "host.get_all_records";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfHostHostRecord(result);
    }

}