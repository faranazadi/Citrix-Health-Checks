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



#include "xen_internal.h"
#include <xen/api/xen_common.h>
#include <xen/api/xen_string_string_map.h>
#include <xen/api/xen_string_string_string_map_map.h>


xen_string_string_string_map_map *
xen_string_string_string_map_map_alloc(size_t size)
{
    xen_string_string_string_map_map *result = calloc(1, sizeof(xen_string_string_string_map_map) +
                                                      size * sizeof(struct xen_string_string_string_map_map_contents));
    result->size = size;
    return result;
}


void
xen_string_string_string_map_map_free(xen_string_string_string_map_map *map)
{
    if (map == NULL)
    {
        return;
    }

    size_t n = map->size;
    for (size_t i = 0; i < n; i++)
    {
        free(map->contents[i].key);
        xen_string_string_map_free(map->contents[i].val);
    }

    free(map);
}
