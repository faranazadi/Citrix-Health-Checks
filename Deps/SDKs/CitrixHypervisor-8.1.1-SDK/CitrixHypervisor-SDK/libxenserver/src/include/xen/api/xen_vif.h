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


#ifndef XEN_VIF_H
#define XEN_VIF_H

#include "xen_string_vif_operations_map_internal.h"
#include "xen_vif_ipv4_configuration_mode_internal.h"
#include "xen_vif_ipv6_configuration_mode_internal.h"
#include "xen_vif_locking_mode_internal.h"
#include "xen_vif_operations_internal.h"
#include <xen/api/xen_common.h>
#include <xen/api/xen_network.h>
#include <xen/api/xen_network_decl.h>
#include <xen/api/xen_string_set.h>
#include <xen/api/xen_string_string_map.h>
#include <xen/api/xen_string_vif_operations_map.h>
#include <xen/api/xen_task_decl.h>
#include <xen/api/xen_vif_decl.h>
#include <xen/api/xen_vif_ipv4_configuration_mode.h>
#include <xen/api/xen_vif_ipv6_configuration_mode.h>
#include <xen/api/xen_vif_locking_mode.h>
#include <xen/api/xen_vif_metrics.h>
#include <xen/api/xen_vif_metrics_decl.h>
#include <xen/api/xen_vif_operations.h>
#include <xen/api/xen_vif_xen_vif_record_map.h>
#include <xen/api/xen_vm.h>
#include <xen/api/xen_vm_decl.h>


/*
 * The VIF class.
 * 
 * A virtual network interface.
 */


/**
 * Free the given xen_vif.  The given handle must have been allocated
 * by this library.
 */
extern void
xen_vif_free(xen_vif vif);


typedef struct xen_vif_set
{
    size_t size;
    xen_vif *contents[];
} xen_vif_set;

/**
 * Allocate a xen_vif_set of the given size.
 */
extern xen_vif_set *
xen_vif_set_alloc(size_t size);

/**
 * Free the given xen_vif_set.  The given set must have been allocated
 * by this library.
 */
extern void
xen_vif_set_free(xen_vif_set *set);


typedef struct xen_vif_record
{
    xen_vif handle;
    char *uuid;
    struct xen_vif_operations_set *allowed_operations;
    xen_string_vif_operations_map *current_operations;
    char *device;
    struct xen_network_record_opt *network;
    struct xen_vm_record_opt *vm;
    char *mac;
    int64_t mtu;
    xen_string_string_map *other_config;
    bool currently_attached;
    int64_t status_code;
    char *status_detail;
    xen_string_string_map *runtime_properties;
    char *qos_algorithm_type;
    xen_string_string_map *qos_algorithm_params;
    struct xen_string_set *qos_supported_algorithms;
    struct xen_vif_metrics_record_opt *metrics;
    bool mac_autogenerated;
    enum xen_vif_locking_mode locking_mode;
    struct xen_string_set *ipv4_allowed;
    struct xen_string_set *ipv6_allowed;
    enum xen_vif_ipv4_configuration_mode ipv4_configuration_mode;
    struct xen_string_set *ipv4_addresses;
    char *ipv4_gateway;
    enum xen_vif_ipv6_configuration_mode ipv6_configuration_mode;
    struct xen_string_set *ipv6_addresses;
    char *ipv6_gateway;
} xen_vif_record;

/**
 * Allocate a xen_vif_record.
 */
extern xen_vif_record *
xen_vif_record_alloc(void);

/**
 * Free the given xen_vif_record, and all referenced values.  The given
 * record must have been allocated by this library.
 */
extern void
xen_vif_record_free(xen_vif_record *record);


typedef struct xen_vif_record_opt
{
    bool is_record;
    union
    {
        xen_vif handle;
        xen_vif_record *record;
    } u;
} xen_vif_record_opt;

/**
 * Allocate a xen_vif_record_opt.
 */
extern xen_vif_record_opt *
xen_vif_record_opt_alloc(void);

/**
 * Free the given xen_vif_record_opt, and all referenced values.  The
 * given record_opt must have been allocated by this library.
 */
extern void
xen_vif_record_opt_free(xen_vif_record_opt *record_opt);


typedef struct xen_vif_record_set
{
    size_t size;
    xen_vif_record *contents[];
} xen_vif_record_set;

/**
 * Allocate a xen_vif_record_set of the given size.
 */
extern xen_vif_record_set *
xen_vif_record_set_alloc(size_t size);

/**
 * Free the given xen_vif_record_set, and all referenced values.  The
 * given set must have been allocated by this library.
 */
extern void
xen_vif_record_set_free(xen_vif_record_set *set);



typedef struct xen_vif_record_opt_set
{
    size_t size;
    xen_vif_record_opt *contents[];
} xen_vif_record_opt_set;

/**
 * Allocate a xen_vif_record_opt_set of the given size.
 */
extern xen_vif_record_opt_set *
xen_vif_record_opt_set_alloc(size_t size);

/**
 * Free the given xen_vif_record_opt_set, and all referenced values. 
 * The given set must have been allocated by this library.
 */
extern void
xen_vif_record_opt_set_free(xen_vif_record_opt_set *set);


/**
 * Get a record containing the current state of the given VIF.
 */
extern bool
xen_vif_get_record(xen_session *session, xen_vif_record **result, xen_vif vif);


/**
 * Get a reference to the VIF instance with the specified UUID.
 */
extern bool
xen_vif_get_by_uuid(xen_session *session, xen_vif *result, char *uuid);


/**
 * Create a new VIF instance, and return its handle.
 */
extern bool
xen_vif_create(xen_session *session, xen_vif *result, xen_vif_record *record);

/**
 * Create a new VIF instance, and return its handle.
 */
extern bool
xen_vif_create_async(xen_session *session, xen_task *result, xen_vif_record *record);


/**
 * Destroy the specified VIF instance.
 */
extern bool
xen_vif_destroy(xen_session *session, xen_vif vif);

/**
 * Destroy the specified VIF instance.
 */
extern bool
xen_vif_destroy_async(xen_session *session, xen_task *result, xen_vif vif);


/**
 * Get the uuid field of the given VIF.
 */
extern bool
xen_vif_get_uuid(xen_session *session, char **result, xen_vif vif);


/**
 * Get the allowed_operations field of the given VIF.
 */
extern bool
xen_vif_get_allowed_operations(xen_session *session, struct xen_vif_operations_set **result, xen_vif vif);


/**
 * Get the current_operations field of the given VIF.
 */
extern bool
xen_vif_get_current_operations(xen_session *session, xen_string_vif_operations_map **result, xen_vif vif);


/**
 * Get the device field of the given VIF.
 */
extern bool
xen_vif_get_device(xen_session *session, char **result, xen_vif vif);


/**
 * Get the network field of the given VIF.
 */
extern bool
xen_vif_get_network(xen_session *session, xen_network *result, xen_vif vif);


/**
 * Get the VM field of the given VIF.
 */
extern bool
xen_vif_get_vm(xen_session *session, xen_vm *result, xen_vif vif);


/**
 * Get the MAC field of the given VIF.
 */
extern bool
xen_vif_get_mac(xen_session *session, char **result, xen_vif vif);


/**
 * Get the MTU field of the given VIF.
 */
extern bool
xen_vif_get_mtu(xen_session *session, int64_t *result, xen_vif vif);


/**
 * Get the other_config field of the given VIF.
 */
extern bool
xen_vif_get_other_config(xen_session *session, xen_string_string_map **result, xen_vif vif);


/**
 * Get the currently_attached field of the given VIF.
 */
extern bool
xen_vif_get_currently_attached(xen_session *session, bool *result, xen_vif vif);


/**
 * Get the status_code field of the given VIF.
 */
extern bool
xen_vif_get_status_code(xen_session *session, int64_t *result, xen_vif vif);


/**
 * Get the status_detail field of the given VIF.
 */
extern bool
xen_vif_get_status_detail(xen_session *session, char **result, xen_vif vif);


/**
 * Get the runtime_properties field of the given VIF.
 */
extern bool
xen_vif_get_runtime_properties(xen_session *session, xen_string_string_map **result, xen_vif vif);


/**
 * Get the qos/algorithm_type field of the given VIF.
 */
extern bool
xen_vif_get_qos_algorithm_type(xen_session *session, char **result, xen_vif vif);


/**
 * Get the qos/algorithm_params field of the given VIF.
 */
extern bool
xen_vif_get_qos_algorithm_params(xen_session *session, xen_string_string_map **result, xen_vif vif);


/**
 * Get the qos/supported_algorithms field of the given VIF.
 */
extern bool
xen_vif_get_qos_supported_algorithms(xen_session *session, struct xen_string_set **result, xen_vif vif);


/**
 * Get the metrics field of the given VIF.
 */
extern bool
xen_vif_get_metrics(xen_session *session, xen_vif_metrics *result, xen_vif vif);


/**
 * Get the MAC_autogenerated field of the given VIF.
 */
extern bool
xen_vif_get_mac_autogenerated(xen_session *session, bool *result, xen_vif vif);


/**
 * Get the locking_mode field of the given VIF.
 */
extern bool
xen_vif_get_locking_mode(xen_session *session, enum xen_vif_locking_mode *result, xen_vif vif);


/**
 * Get the ipv4_allowed field of the given VIF.
 */
extern bool
xen_vif_get_ipv4_allowed(xen_session *session, struct xen_string_set **result, xen_vif vif);


/**
 * Get the ipv6_allowed field of the given VIF.
 */
extern bool
xen_vif_get_ipv6_allowed(xen_session *session, struct xen_string_set **result, xen_vif vif);


/**
 * Get the ipv4_configuration_mode field of the given VIF.
 */
extern bool
xen_vif_get_ipv4_configuration_mode(xen_session *session, enum xen_vif_ipv4_configuration_mode *result, xen_vif vif);


/**
 * Get the ipv4_addresses field of the given VIF.
 */
extern bool
xen_vif_get_ipv4_addresses(xen_session *session, struct xen_string_set **result, xen_vif vif);


/**
 * Get the ipv4_gateway field of the given VIF.
 */
extern bool
xen_vif_get_ipv4_gateway(xen_session *session, char **result, xen_vif vif);


/**
 * Get the ipv6_configuration_mode field of the given VIF.
 */
extern bool
xen_vif_get_ipv6_configuration_mode(xen_session *session, enum xen_vif_ipv6_configuration_mode *result, xen_vif vif);


/**
 * Get the ipv6_addresses field of the given VIF.
 */
extern bool
xen_vif_get_ipv6_addresses(xen_session *session, struct xen_string_set **result, xen_vif vif);


/**
 * Get the ipv6_gateway field of the given VIF.
 */
extern bool
xen_vif_get_ipv6_gateway(xen_session *session, char **result, xen_vif vif);


/**
 * Set the other_config field of the given VIF.
 */
extern bool
xen_vif_set_other_config(xen_session *session, xen_vif vif, xen_string_string_map *other_config);


/**
 * Add the given key-value pair to the other_config field of the given
 * VIF.
 */
extern bool
xen_vif_add_to_other_config(xen_session *session, xen_vif vif, char *key, char *value);


/**
 * Remove the given key and its corresponding value from the
 * other_config field of the given VIF.  If the key is not in that Map, then
 * do nothing.
 */
extern bool
xen_vif_remove_from_other_config(xen_session *session, xen_vif vif, char *key);


/**
 * Set the qos/algorithm_type field of the given VIF.
 */
extern bool
xen_vif_set_qos_algorithm_type(xen_session *session, xen_vif vif, char *algorithm_type);


/**
 * Set the qos/algorithm_params field of the given VIF.
 */
extern bool
xen_vif_set_qos_algorithm_params(xen_session *session, xen_vif vif, xen_string_string_map *algorithm_params);


/**
 * Add the given key-value pair to the qos/algorithm_params field of
 * the given VIF.
 */
extern bool
xen_vif_add_to_qos_algorithm_params(xen_session *session, xen_vif vif, char *key, char *value);


/**
 * Remove the given key and its corresponding value from the
 * qos/algorithm_params field of the given VIF.  If the key is not in that
 * Map, then do nothing.
 */
extern bool
xen_vif_remove_from_qos_algorithm_params(xen_session *session, xen_vif vif, char *key);


/**
 * Hotplug the specified VIF, dynamically attaching it to the running
 * VM.
 */
extern bool
xen_vif_plug(xen_session *session, xen_vif self);

/**
 * Hotplug the specified VIF, dynamically attaching it to the running
 * VM.
 */
extern bool
xen_vif_plug_async(xen_session *session, xen_task *result, xen_vif self);


/**
 * Hot-unplug the specified VIF, dynamically unattaching it from the
 * running VM.
 */
extern bool
xen_vif_unplug(xen_session *session, xen_vif self);

/**
 * Hot-unplug the specified VIF, dynamically unattaching it from the
 * running VM.
 */
extern bool
xen_vif_unplug_async(xen_session *session, xen_task *result, xen_vif self);


/**
 * Forcibly unplug the specified VIF.
 */
extern bool
xen_vif_unplug_force(xen_session *session, xen_vif self);

/**
 * Forcibly unplug the specified VIF.
 */
extern bool
xen_vif_unplug_force_async(xen_session *session, xen_task *result, xen_vif self);


/**
 * Move the specified VIF to the specified network, even while the VM
 * is running.
 */
extern bool
xen_vif_move(xen_session *session, xen_vif self, xen_network network);

/**
 * Move the specified VIF to the specified network, even while the VM
 * is running.
 */
extern bool
xen_vif_move_async(xen_session *session, xen_task *result, xen_vif self, xen_network network);


/**
 * Set the locking mode for this VIF.
 */
extern bool
xen_vif_set_locking_mode(xen_session *session, xen_vif self, enum xen_vif_locking_mode value);

/**
 * Set the locking mode for this VIF.
 */
extern bool
xen_vif_set_locking_mode_async(xen_session *session, xen_task *result, xen_vif self, enum xen_vif_locking_mode value);


/**
 * Set the IPv4 addresses to which traffic on this VIF can be
 * restricted.
 */
extern bool
xen_vif_set_ipv4_allowed(xen_session *session, xen_vif self, struct xen_string_set *value);

/**
 * Set the IPv4 addresses to which traffic on this VIF can be
 * restricted.
 */
extern bool
xen_vif_set_ipv4_allowed_async(xen_session *session, xen_task *result, xen_vif self, struct xen_string_set *value);


/**
 * Associates an IPv4 address with this VIF.
 */
extern bool
xen_vif_add_ipv4_allowed(xen_session *session, xen_vif self, char *value);

/**
 * Associates an IPv4 address with this VIF.
 */
extern bool
xen_vif_add_ipv4_allowed_async(xen_session *session, xen_task *result, xen_vif self, char *value);


/**
 * Removes an IPv4 address from this VIF.
 */
extern bool
xen_vif_remove_ipv4_allowed(xen_session *session, xen_vif self, char *value);

/**
 * Removes an IPv4 address from this VIF.
 */
extern bool
xen_vif_remove_ipv4_allowed_async(xen_session *session, xen_task *result, xen_vif self, char *value);


/**
 * Set the IPv6 addresses to which traffic on this VIF can be
 * restricted.
 */
extern bool
xen_vif_set_ipv6_allowed(xen_session *session, xen_vif self, struct xen_string_set *value);

/**
 * Set the IPv6 addresses to which traffic on this VIF can be
 * restricted.
 */
extern bool
xen_vif_set_ipv6_allowed_async(xen_session *session, xen_task *result, xen_vif self, struct xen_string_set *value);


/**
 * Associates an IPv6 address with this VIF.
 */
extern bool
xen_vif_add_ipv6_allowed(xen_session *session, xen_vif self, char *value);

/**
 * Associates an IPv6 address with this VIF.
 */
extern bool
xen_vif_add_ipv6_allowed_async(xen_session *session, xen_task *result, xen_vif self, char *value);


/**
 * Removes an IPv6 address from this VIF.
 */
extern bool
xen_vif_remove_ipv6_allowed(xen_session *session, xen_vif self, char *value);

/**
 * Removes an IPv6 address from this VIF.
 */
extern bool
xen_vif_remove_ipv6_allowed_async(xen_session *session, xen_task *result, xen_vif self, char *value);


/**
 * Configure IPv4 settings for this virtual interface.
 */
extern bool
xen_vif_configure_ipv4(xen_session *session, xen_vif self, enum xen_vif_ipv4_configuration_mode mode, char *address, char *gateway);

/**
 * Configure IPv4 settings for this virtual interface.
 */
extern bool
xen_vif_configure_ipv4_async(xen_session *session, xen_task *result, xen_vif self, enum xen_vif_ipv4_configuration_mode mode, char *address, char *gateway);


/**
 * Configure IPv6 settings for this virtual interface.
 */
extern bool
xen_vif_configure_ipv6(xen_session *session, xen_vif self, enum xen_vif_ipv6_configuration_mode mode, char *address, char *gateway);

/**
 * Configure IPv6 settings for this virtual interface.
 */
extern bool
xen_vif_configure_ipv6_async(xen_session *session, xen_task *result, xen_vif self, enum xen_vif_ipv6_configuration_mode mode, char *address, char *gateway);


/**
 * Return a list of all the VIFs known to the system.
 */
extern bool
xen_vif_get_all(xen_session *session, struct xen_vif_set **result);


/**
 * Return a map of VIF references to VIF records for all VIFs known to
 * the system.
 */
extern bool
xen_vif_get_all_records(xen_session *session, xen_vif_xen_vif_record_map **result);


#endif
