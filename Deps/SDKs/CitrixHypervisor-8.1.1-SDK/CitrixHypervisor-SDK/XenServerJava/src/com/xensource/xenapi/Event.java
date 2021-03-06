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
 * Asynchronous event registration and handling
 * First published in XenServer 4.0.
 *
 * @author Citrix Systems, Inc.
 */
public class Event extends XenAPIObject {

    /**
     * The XenAPI reference (OpaqueRef) to this object.
     */
    protected final String ref;

    /**
     * For internal use only.
     */
    Event(String ref) {
       this.ref = ref;
    }

    /**
     * @return The XenAPI reference (OpaqueRef) to this object.
     */
    public String toWireString() {
       return this.ref;
    }

    /**
     * If obj is a Event, compares XenAPI references for equality.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj instanceof Event)
        {
            Event other = (Event) obj;
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
     * Represents all the fields in a Event
     */
    public static class Record implements Types.Record {
        public String toString() {
            StringWriter writer = new StringWriter();
            PrintWriter print = new PrintWriter(writer);
            print.printf("%1$20s: %2$s\n", "id", this.id);
            print.printf("%1$20s: %2$s\n", "timestamp", this.timestamp);
            print.printf("%1$20s: %2$s\n", "clazz", this.clazz);
            print.printf("%1$20s: %2$s\n", "operation", this.operation);
            print.printf("%1$20s: %2$s\n", "ref", this.ref);
            print.printf("%1$20s: %2$s\n", "objUuid", this.objUuid);
            print.printf("%1$20s: %2$s\n", "snapshot", this.snapshot);
            return writer.toString();
        }

        /**
         * Convert a event.Record to a Map
         */
        public Map<String,Object> toMap() {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id", this.id == null ? 0 : this.id);
            map.put("timestamp", this.timestamp == null ? new Date(0) : this.timestamp);
            map.put("class", this.clazz == null ? "" : this.clazz);
            map.put("operation", this.operation == null ? Types.EventOperation.UNRECOGNIZED : this.operation);
            map.put("ref", this.ref == null ? "" : this.ref);
            map.put("obj_uuid", this.objUuid == null ? "" : this.objUuid);
            map.put("snapshot", this.snapshot);
            return map;
        }

        /**
         * An ID, monotonically increasing, and local to the current session
         */
        public Long id;
        /**
         * The time at which the event occurred
         */
        public Date timestamp;
        /**
         * The name of the class of the object that changed
         */
        public String clazz;
        /**
         * The operation that was performed
         */
        public Types.EventOperation operation;
        /**
         * A reference to the object that changed
         */
        public String ref;
        /**
         * The uuid of the object that changed
         */
        public String objUuid;
        /**
         * The record of the database object that was added, changed or deleted
         * (the actual type will be VM.Record, VBD.Record or similar)
         */
        public Object snapshot;
    }

    /**
     * Registers this session with the event system for a set of given classes. This method is only recommended for legacy use in conjunction with event.next.
     * First published in XenServer 4.0.
     * @deprecated
     *
     * @param classes the classes for which the session will register with the event system; specifying * as the desired class will register for all classes
     * @return Task
     */
   @Deprecated public static Task registerAsync(Connection c, Set<String> classes) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.event.register";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(classes)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Registers this session with the event system for a set of given classes. This method is only recommended for legacy use in conjunction with event.next.
     * First published in XenServer 4.0.
     * @deprecated
     *
     * @param classes the classes for which the session will register with the event system; specifying * as the desired class will register for all classes
     */
   @Deprecated public static void register(Connection c, Set<String> classes) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "event.register";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(classes)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Removes this session's registration with the event system for a set of given classes. This method is only recommended for legacy use in conjunction with event.next.
     * First published in XenServer 4.0.
     * @deprecated
     *
     * @param classes the classes for which the session's registration with the event system will be removed
     * @return Task
     */
   @Deprecated public static Task unregisterAsync(Connection c, Set<String> classes) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "Async.event.unregister";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(classes)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
        return Types.toTask(result);
    }

    /**
     * Removes this session's registration with the event system for a set of given classes. This method is only recommended for legacy use in conjunction with event.next.
     * First published in XenServer 4.0.
     * @deprecated
     *
     * @param classes the classes for which the session's registration with the event system will be removed
     */
   @Deprecated public static void unregister(Connection c, Set<String> classes) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "event.unregister";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(classes)};
        Map response = c.dispatch(method_call, method_params);
        return;
    }

    /**
     * Blocking call which returns a (possibly empty) batch of events. This method is only recommended for legacy use. New development should use event.from which supercedes this method.
     * First published in XenServer 4.0.
     * @deprecated
     *
     * @return A set of events
     */
   @Deprecated public static Set<Event.Record> next(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException,
       Types.SessionNotRegistered,
       Types.EventsLost {
        String method_call = "event.next";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfEventRecord(result);
    }

    /**
     * Blocking call which returns a new token and a (possibly empty) batch of events. The returned token can be used in subsequent calls to this function.
     * First published in XenServer 6.0.
     *
     * @param classes register for events for the indicated classes
     * @param token A token representing the point from which to generate database events. The empty string represents the beginning.
     * @param timeout Return after this many seconds if no events match
     * @return a structure consisting of a token ('token'), a map of valid references per object type ('valid_ref_counts'), and a set of event records ('events').
     */
    public static EventBatch from(Connection c, Set<String> classes, String token, Double timeout) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException,
       Types.SessionNotRegistered,
       Types.EventsLost {
        String method_call = "event.from";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(classes), Marshalling.toXMLRPC(token), Marshalling.toXMLRPC(timeout)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toEventBatch(result);
    }

    /**
     * Return the ID of the next event to be generated by the system
     * First published in XenServer 4.0.
     *
     * @return the event ID
     */
    public static Long getCurrentId(Connection c) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "event.get_current_id";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toLong(result);
    }

    /**
     * Injects an artificial event on the given object and returns the corresponding ID in the form of a token, which can be used as a point of reference for database events. For example, to check whether an object has reached the right state before attempting an operation, one can inject an artificial event on the object and wait until the token returned by consecutive event.from calls is lexicographically greater than the one returned by event.inject.
     * First published in XenServer 6.1.
     *
     * @param clazz class of the object
     * @param ref A reference to the object that will be changed.
     * @return the event ID in the form of a token
     */
    public static String inject(Connection c, String clazz, String ref) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "event.inject";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(clazz), Marshalling.toXMLRPC(ref)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

}