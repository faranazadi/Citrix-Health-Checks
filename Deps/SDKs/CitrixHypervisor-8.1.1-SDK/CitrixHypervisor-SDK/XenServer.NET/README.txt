XenServer.NET
=============

Version 8.1.1

XenServer.NET is a complete SDK for Citrix Hypervisor, exposing the Citrix
Hypervisor API as .NET classes. It is written in C#.

It is available in the CitrixHypervisor-SDK-8.1.1.zip, which can be downloaded
from https://www.citrix.com/downloads/citrix-hypervisor/

For Citrix Hypervisor documentation, see https://docs.citrix.com/en-us/citrix-hypervisor/

XenServer.NET includes a class for every API class, and a method for each API
call, so API documentation and examples written for other languages will apply
equally well to .NET. In particular, the SDK Guide and the Management API Guide
are ideal for developers wishing to use XenServer.NET.

For community content, blogs, and downloads, visit
https://www.citrix.com/community/citrix-developer/

To network with other developers using Citrix Hypervisor visit
https://discussions.citrix.com/forum/101-hypervisor-formerly-xenserver/

XenServer.NET is free software. You can redistribute and modify it under the
terms of the BSD 2-Clause license. See LICENSE.txt for details.

This library is accompanied by pedagogical examples. These do not form
part of this library, and are licensed for redistribution and modification
under the BSD 2-Clause license.


Prerequisites
-------------

This library requires .NET 4.5 or greater.


Dependencies
------------

XenServer.NET is dependent upon the following libraries:

- XML-RPC.NET by Charles Cook (see http://xml-rpc.net).
  XML-RPC.NET is licensed under the MIT X11 license; see
  LICENSE.CookComputing.XmlRpcV2.txt for details. A patched version of the library
  (CookComputing.XmlRpcV2.dll) is shipped with XenServer.NET.

- Newtonsoft JSON.NET by James Newton-King (see https://www.newtonsoft.com/).
  JSON.NET is licensed under the MIT license; see LICENSE.Newtonsoft.Json.txt
  for details. A patched version of the library (Newtonsoft.Json.CH.dll) is
  shipped with XenServer.NET.


Downloads
---------

The CitrixHypervisor-SDK-8.1.1.zip contains the following folders that are
relevant to .NET programmers:
- XenServer.NET\bin: contains the ready compiled binaries
- XenServer.NET\src: contains the source code shipped as a Visual Studio project.
- XenServer.NET\samples: contains the examples shipped as a Visual studio solution.


Getting Started
---------------

Download and unzip the CitrixHypervisor-SDK-8.1.1.zip.

A. To use the compiled binaries in your code:
  1. Copy XenServer.dll, CookComputing.XmlRpcV2.dll and Newtonsoft.Json.CH.dll
     from the bin folder into your own workspace.
  2. In Visual Studio, add references to all DLLs from your own program.
     Project > Add Reference > Browse.
  3. You should now be ready to compile against XenServer.NET.

B. To build the source code:
  1. Copy CookComputing.XmlRpcV2.dll  and Newtonsoft.Json.CH.dll from the bin
     folder into the source code folder at the same level as the project file
     XenServer.csproj
  2. Open the project XenServer.csproj in Visual Studio.
  3. You should now be ready to build the source code.

C. To run the examples:
  1. Copy XenServer.dll, CookComputing.XmlRpcV2.dll and Newtonsoft.Json.CH.dll
     from the bin folder into the samples folder at the same level as the
     project file XenSdkSample.csproj.
  2. Open XenSdkSample.sln inside Visual Studio (2013 or greater).
  3. You should now be ready to compile the solution and run the examples.
     The solution project is a console application expecting the parameters
     <host> <username> <password> to be passed to its Main method.
