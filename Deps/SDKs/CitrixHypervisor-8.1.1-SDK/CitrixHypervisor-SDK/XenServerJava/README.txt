XenServerJava
=============

Version 8.1.1

XenServerJava is a complete SDK for Citrix Hypervisor, exposing the Citrix
Hypervisor API as Java classes.

It is available in the CitrixHypervisor-SDK-8.1.1.zip, which can be downloaded
from https://www.citrix.com/downloads/citrix-hypervisor/

For Citrix Hypervisor documentation, see https://docs.citrix.com/en-us/citrix-hypervisor/

XenServerJava includes a class for every API class, and a method for each API
call, so API documentation and examples written for other languages will apply
equally well to Java. In particular, the SDK Guide and the Management API Guide
are ideal for developers wishing to use XenServerJava.

For community content, blogs, and downloads, visit
https://www.citrix.com/community/citrix-developer/

To network with other developers using Citrix Hypervisor visit
https://discussions.citrix.com/forum/101-hypervisor-formerly-xenserver/

XenServerJava is free software. You can redistribute and modify it under the
terms of the BSD 2-Clause license. See LICENSE.txt for details.

This library is accompanied a number of test programs that can be used ass
pedagogical examples. These do not form part of this library, and are licensed for
redistribution and modification under the BSD 2-Clause license.


Dependencies
------------

XenServerJava is dependent upon Apache XML-RPC and WS-Commons, both by The
Apache Software Foundation. Both are licensed under the
Apache Software License 2.0. See LICENSE.Apache-2.0.txt for details.

We test with version 3.1 of Apache XML-RPC and version 1.0.2 of WS-Commons.
We recommend that you use these versions, though others may work.

Apache XML-RPC is available from http://ws.apache.org/xmlrpc/.
WS-Commons is available from http://ws.apache.org/commons/.
All the jars are shipped with this library.


Folder structure
----------------

The CitrixHypervisor-SDK-8.1.1.zip contains the following folders that are
relevant to Java programmers:
- XenServerJava/bin: contains the compiled binaries
- XenServerJava/javadoc: contains the documentation
- XenServerJava/src: contains the source code and tests.


Compiling from source
---------------------

Extract XenServerJava from CitrixHypervisor-SDK-8.1.1.zip.
Copy the dependency jars from XenServerJava/bin to XenServerJava/src.

From XenServerJava/src do:
- "make all" to build the XenServer binary
- "make docs" to build the documentation.

To build and run the tests, copy them from XenServerJava/samples to
XenServerJava/src, then from XenServerJava/src "make all" and

$ CLS_PATH=:xmlrpc-client-3.1.3.jar:xmlrpc-common-3.1.3.jar:ws-commons-util-1.0.2.jar
$ java -cp $CLS_PATH RunTests <host> <username> <password> [nfs server] [nfs path]
