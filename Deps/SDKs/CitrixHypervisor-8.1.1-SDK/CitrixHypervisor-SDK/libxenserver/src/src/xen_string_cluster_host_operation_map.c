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



#include "xen_cluster_host_operation_internal.h"
#include "xen_internal.h"
#include <xen/api/xen_common.h>
#include <xen/api/xen_string_cluster_host_operation_map.h>


xen_string_cluster_host_operation_map *
xen_string_cluster_host_operation_map_alloc(size_t size)
{
    xen_string_cluster_host_operation_map *result = calloc(1, sizeof(xen_string_cluster_host_operation_map) +
                                                           size * sizeof(struct xen_string_cluster_host_operation_map_contents));
    result->size = size;
    return result;
}


void
xen_string_cluster_host_operation_map_free(xen_string_cluster_host_operation_map *map)
{
    if (map == NULL)
    {
        return;
    }

    size_t n = map->size;
    for (size_t i = 0; i < n; i++)
    {
        free(map->contents[i].key);
        
    }

    free(map);
}


static const struct_member string_cluster_host_operation_map_struct_members[] =
    {
        { .type = &abstract_type_string,
          .offset = offsetof(xen_string_cluster_host_operation_map_contents, key) },
        { .type = &xen_cluster_host_operation_abstract_type_,
          .offset = offsetof(xen_string_cluster_host_operation_map_contents, val) },
    };

const abstract_type string_cluster_host_operation_map_abstract_type_ =
    {
       .typename = MAP,
       .struct_size = sizeof(string_cluster_host_operation_map_struct_members),
       .member_count =
           sizeof(string_cluster_host_operation_map_struct_members) / sizeof(struct_member),
       .members = string_cluster_host_operation_map_struct_members
    };
