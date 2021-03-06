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



#include <stddef.h>
#include <stdlib.h>

#include "xen_internal.h"
#include "xen_pgpu_dom0_access_internal.h"
#include <xen/api/xen_common.h>
#include <xen/api/xen_gpu_group.h>
#include <xen/api/xen_host.h>
#include <xen/api/xen_pci.h>
#include <xen/api/xen_pgpu.h>
#include <xen/api/xen_pgpu_xen_pgpu_record_map.h>
#include <xen/api/xen_string_string_map.h>
#include <xen/api/xen_vgpu.h>
#include <xen/api/xen_vgpu_type.h>
#include <xen/api/xen_vgpu_type_int_map.h>


XEN_FREE(xen_pgpu)
XEN_SET_ALLOC_FREE(xen_pgpu)
XEN_ALLOC(xen_pgpu_record)
XEN_SET_ALLOC_FREE(xen_pgpu_record)
XEN_ALLOC(xen_pgpu_record_opt)
XEN_RECORD_OPT_FREE(xen_pgpu)
XEN_SET_ALLOC_FREE(xen_pgpu_record_opt)


static const struct_member xen_pgpu_record_struct_members[] =
    {
        { .key = "uuid",
          .type = &abstract_type_string,
          .offset = offsetof(xen_pgpu_record, uuid) },
        { .key = "PCI",
          .type = &abstract_type_ref,
          .offset = offsetof(xen_pgpu_record, pci) },
        { .key = "GPU_group",
          .type = &abstract_type_ref,
          .offset = offsetof(xen_pgpu_record, gpu_group) },
        { .key = "host",
          .type = &abstract_type_ref,
          .offset = offsetof(xen_pgpu_record, host) },
        { .key = "other_config",
          .type = &abstract_type_string_string_map,
          .offset = offsetof(xen_pgpu_record, other_config) },
        { .key = "supported_VGPU_types",
          .type = &abstract_type_ref_set,
          .offset = offsetof(xen_pgpu_record, supported_vgpu_types) },
        { .key = "enabled_VGPU_types",
          .type = &abstract_type_ref_set,
          .offset = offsetof(xen_pgpu_record, enabled_vgpu_types) },
        { .key = "resident_VGPUs",
          .type = &abstract_type_ref_set,
          .offset = offsetof(xen_pgpu_record, resident_vgpus) },
        { .key = "supported_VGPU_max_capacities",
          .type = &abstract_type_string_int_map,
          .offset = offsetof(xen_pgpu_record, supported_vgpu_max_capacities) },
        { .key = "dom0_access",
          .type = &xen_pgpu_dom0_access_abstract_type_,
          .offset = offsetof(xen_pgpu_record, dom0_access) },
        { .key = "is_system_display_device",
          .type = &abstract_type_bool,
          .offset = offsetof(xen_pgpu_record, is_system_display_device) },
        { .key = "compatibility_metadata",
          .type = &abstract_type_string_string_map,
          .offset = offsetof(xen_pgpu_record, compatibility_metadata) }
    };

const abstract_type xen_pgpu_record_abstract_type_ =
    {
       .typename = STRUCT,
       .struct_size = sizeof(xen_pgpu_record),
       .member_count =
           sizeof(xen_pgpu_record_struct_members) / sizeof(struct_member),
       .members = xen_pgpu_record_struct_members
    };


const abstract_type xen_pgpu_record_set_abstract_type_ =
    {
       .typename = SET,
        .child = &xen_pgpu_record_abstract_type_
    };


static const struct struct_member xen_pgpu_xen_pgpu_record_members[] =
{
    {
        .type = &abstract_type_string,
        .offset = offsetof(xen_pgpu_xen_pgpu_record_map_contents, key)
    },
    {
        .type = &xen_pgpu_record_abstract_type_,
        .offset = offsetof(xen_pgpu_xen_pgpu_record_map_contents, val)
    }
};

const abstract_type abstract_type_string_xen_pgpu_record_map =
{
    .typename = MAP,
    .struct_size = sizeof(xen_pgpu_xen_pgpu_record_map_contents),
    .members = xen_pgpu_xen_pgpu_record_members
};


void
xen_pgpu_record_free(xen_pgpu_record *record)
{
    if (record == NULL)
    {
        return;
    }
    free(record->handle);
    free(record->uuid);
    xen_pci_record_opt_free(record->pci);
    xen_gpu_group_record_opt_free(record->gpu_group);
    xen_host_record_opt_free(record->host);
    xen_string_string_map_free(record->other_config);
    xen_vgpu_type_record_opt_set_free(record->supported_vgpu_types);
    xen_vgpu_type_record_opt_set_free(record->enabled_vgpu_types);
    xen_vgpu_record_opt_set_free(record->resident_vgpus);
    xen_vgpu_type_int_map_free(record->supported_vgpu_max_capacities);
    xen_string_string_map_free(record->compatibility_metadata);
    free(record);
}


bool
xen_pgpu_get_record(xen_session *session, xen_pgpu_record **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = xen_pgpu_record_abstract_type_;

    *result = NULL;
    XEN_CALL_("PGPU.get_record");

    if (session->ok)
    {
       (*result)->handle = xen_strdup_((*result)->uuid);
    }

    return session->ok;
}


bool
xen_pgpu_get_by_uuid(xen_session *session, xen_pgpu *result, char *uuid)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = uuid }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("PGPU.get_by_uuid");
    return session->ok;
}


bool
xen_pgpu_get_uuid(xen_session *session, char **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("PGPU.get_uuid");
    return session->ok;
}


bool
xen_pgpu_get_pci(xen_session *session, xen_pci *result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("PGPU.get_PCI");
    return session->ok;
}


bool
xen_pgpu_get_gpu_group(xen_session *session, xen_gpu_group *result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("PGPU.get_GPU_group");
    return session->ok;
}


bool
xen_pgpu_get_host(xen_session *session, xen_host *result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("PGPU.get_host");
    return session->ok;
}


bool
xen_pgpu_get_other_config(xen_session *session, xen_string_string_map **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string_string_map;

    *result = NULL;
    XEN_CALL_("PGPU.get_other_config");
    return session->ok;
}


bool
xen_pgpu_get_supported_vgpu_types(xen_session *session, struct xen_vgpu_type_set **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string_set;

    *result = NULL;
    XEN_CALL_("PGPU.get_supported_VGPU_types");
    return session->ok;
}


bool
xen_pgpu_get_enabled_vgpu_types(xen_session *session, struct xen_vgpu_type_set **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string_set;

    *result = NULL;
    XEN_CALL_("PGPU.get_enabled_VGPU_types");
    return session->ok;
}


bool
xen_pgpu_get_resident_vgpus(xen_session *session, struct xen_vgpu_set **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string_set;

    *result = NULL;
    XEN_CALL_("PGPU.get_resident_VGPUs");
    return session->ok;
}


bool
xen_pgpu_get_supported_vgpu_max_capacities(xen_session *session, xen_vgpu_type_int_map **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string_int_map;

    *result = NULL;
    XEN_CALL_("PGPU.get_supported_VGPU_max_capacities");
    return session->ok;
}


bool
xen_pgpu_get_dom0_access(xen_session *session, enum xen_pgpu_dom0_access *result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = xen_pgpu_dom0_access_abstract_type_;
    XEN_CALL_("PGPU.get_dom0_access");
    return session->ok;
}


bool
xen_pgpu_get_is_system_display_device(xen_session *session, bool *result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_bool;

    XEN_CALL_("PGPU.get_is_system_display_device");
    return session->ok;
}


bool
xen_pgpu_get_compatibility_metadata(xen_session *session, xen_string_string_map **result, xen_pgpu pgpu)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu }
        };

    abstract_type result_type = abstract_type_string_string_map;

    *result = NULL;
    XEN_CALL_("PGPU.get_compatibility_metadata");
    return session->ok;
}


bool
xen_pgpu_set_other_config(xen_session *session, xen_pgpu pgpu, xen_string_string_map *other_config)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu },
            { .type = &abstract_type_string_string_map,
              .u.set_val = (arbitrary_set *)other_config }
        };

    xen_call_(session, "PGPU.set_other_config", param_values, 2, NULL, NULL);
    return session->ok;
}


bool
xen_pgpu_add_to_other_config(xen_session *session, xen_pgpu pgpu, char *key, char *value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu },
            { .type = &abstract_type_string,
              .u.string_val = key },
            { .type = &abstract_type_string,
              .u.string_val = value }
        };

    xen_call_(session, "PGPU.add_to_other_config", param_values, 3, NULL, NULL);
    return session->ok;
}


bool
xen_pgpu_remove_from_other_config(xen_session *session, xen_pgpu pgpu, char *key)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = pgpu },
            { .type = &abstract_type_string,
              .u.string_val = key }
        };

    xen_call_(session, "PGPU.remove_from_other_config", param_values, 2, NULL, NULL);
    return session->ok;
}


bool
xen_pgpu_add_enabled_vgpu_types(xen_session *session, xen_pgpu self, xen_vgpu_type value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = value }
        };

    xen_call_(session, "PGPU.add_enabled_VGPU_types", param_values, 2, NULL, NULL);
    return session->ok;
}

bool
xen_pgpu_add_enabled_vgpu_types_async(xen_session *session, xen_task *result, xen_pgpu self, xen_vgpu_type value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = value }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("Async.PGPU.add_enabled_VGPU_types");
    return session->ok;
}

bool
xen_pgpu_remove_enabled_vgpu_types(xen_session *session, xen_pgpu self, xen_vgpu_type value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = value }
        };

    xen_call_(session, "PGPU.remove_enabled_VGPU_types", param_values, 2, NULL, NULL);
    return session->ok;
}

bool
xen_pgpu_remove_enabled_vgpu_types_async(xen_session *session, xen_task *result, xen_pgpu self, xen_vgpu_type value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = value }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("Async.PGPU.remove_enabled_VGPU_types");
    return session->ok;
}

bool
xen_pgpu_set_enabled_vgpu_types(xen_session *session, xen_pgpu self, struct xen_vgpu_type_set *value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string_set,
              .u.set_val = (arbitrary_set *)value }
        };

    xen_call_(session, "PGPU.set_enabled_VGPU_types", param_values, 2, NULL, NULL);
    return session->ok;
}

bool
xen_pgpu_set_enabled_vgpu_types_async(xen_session *session, xen_task *result, xen_pgpu self, struct xen_vgpu_type_set *value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string_set,
              .u.set_val = (arbitrary_set *)value }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("Async.PGPU.set_enabled_VGPU_types");
    return session->ok;
}

bool
xen_pgpu_set_gpu_group(xen_session *session, xen_pgpu self, xen_gpu_group value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = value }
        };

    xen_call_(session, "PGPU.set_GPU_group", param_values, 2, NULL, NULL);
    return session->ok;
}

bool
xen_pgpu_set_gpu_group_async(xen_session *session, xen_task *result, xen_pgpu self, xen_gpu_group value)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = value }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("Async.PGPU.set_GPU_group");
    return session->ok;
}

bool
xen_pgpu_get_remaining_capacity(xen_session *session, int64_t *result, xen_pgpu self, xen_vgpu_type vgpu_type)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = vgpu_type }
        };

    abstract_type result_type = abstract_type_int;

    XEN_CALL_("PGPU.get_remaining_capacity");
    return session->ok;
}

bool
xen_pgpu_get_remaining_capacity_async(xen_session *session, xen_task *result, xen_pgpu self, xen_vgpu_type vgpu_type)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self },
            { .type = &abstract_type_string,
              .u.string_val = vgpu_type }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("Async.PGPU.get_remaining_capacity");
    return session->ok;
}

bool
xen_pgpu_enable_dom0_access(xen_session *session, enum xen_pgpu_dom0_access *result, xen_pgpu self)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self }
        };

    abstract_type result_type = xen_pgpu_dom0_access_abstract_type_;
    XEN_CALL_("PGPU.enable_dom0_access");
    return session->ok;
}

bool
xen_pgpu_enable_dom0_access_async(xen_session *session, xen_task *result, xen_pgpu self)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("Async.PGPU.enable_dom0_access");
    return session->ok;
}

bool
xen_pgpu_disable_dom0_access(xen_session *session, enum xen_pgpu_dom0_access *result, xen_pgpu self)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self }
        };

    abstract_type result_type = xen_pgpu_dom0_access_abstract_type_;
    XEN_CALL_("PGPU.disable_dom0_access");
    return session->ok;
}

bool
xen_pgpu_disable_dom0_access_async(xen_session *session, xen_task *result, xen_pgpu self)
{
    abstract_value param_values[] =
        {
            { .type = &abstract_type_string,
              .u.string_val = self }
        };

    abstract_type result_type = abstract_type_string;

    *result = NULL;
    XEN_CALL_("Async.PGPU.disable_dom0_access");
    return session->ok;
}

bool
xen_pgpu_get_all(xen_session *session, struct xen_pgpu_set **result)
{

    abstract_type result_type = abstract_type_string_set;

    *result = NULL;
    xen_call_(session, "PGPU.get_all", NULL, 0, &result_type, result);
    return session->ok;
}


bool
xen_pgpu_get_all_records(xen_session *session, xen_pgpu_xen_pgpu_record_map **result)
{

    abstract_type result_type = abstract_type_string_xen_pgpu_record_map;

    *result = NULL;
    xen_call_(session, "PGPU.get_all_records", NULL, 0, &result_type, result);
    return session->ok;
}

