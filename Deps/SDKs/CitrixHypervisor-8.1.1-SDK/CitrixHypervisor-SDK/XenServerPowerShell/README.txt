Citrix Hypervisor PowerShell Module
===================================

Version 8.1.1

The Citrix Hypervisor PowerShell Module is a complete SDK for Citrix Hypervisor,
exposing the Citrix Hypervisor API as Windows PowerShell cmdlets.

It is available in the CitrixHypervisor-SDK-8.1.1.zip, which can be downloaded
from https://www.citrix.com/downloads/citrix-hypervisor/

For Citrix Hypervisor documentation, see https://docs.citrix.com/en-us/citrix-hypervisor/

The Citrix Hypervisor PowerShell Module includes a cmdlet for each API call,
so API documentation and examples written for other languages will apply equally
well to PowerShell. In particular, the SDK Guide and the Management API Guide
are ideal for developers wishing to use this module.

For community content, blogs, and downloads, visit
https://www.citrix.com/community/citrix-developer/

To network with other developers using Citrix Hypervisor visit
https://discussions.citrix.com/forum/101-hypervisor-formerly-xenserver/

This module is free software. You can redistribute and modify it under the
terms of the BSD 2-Clause license. See LICENSE.txt for details.

This library is accompanied by pedagogical examples. These do not form
part of this library, and are licensed for redistribution and modification
under the BSD 2-Clause license.


Prerequisites
-------------

This library requires .NET 4.5 and PowerShell 4.0.


Dependencies
------------

The Citrix Hypervisor PowerShell Module is dependent upon the following libraries:

- XML-RPC.NET by Charles Cook (see http://xml-rpc.net).
  XML-RPC.NET is licensed under the MIT X11 license; see
  LICENSE.CookComputing.XmlRpcV2.txt for details. A patched version of the library
  (CookComputing.XmlRpcV2.dll) is shipped with the Citrix Hypervisor PowerShell
  Module.

- Newtonsoft JSON.NET by James Newton-King (see https://www.newtonsoft.com/).
  JSON.NET is licensed under the MIT license; see LICENSE.Newtonsoft.Json.txt
  for details. A patched version of the library (Newtonsoft.Json.CH.dll) is
  shipped with the Citrix Hypervisor PowerShell Module.


Folder Structure
----------------

The CitrixHypervisor-SDK-8.1.1.zip contains the following folders that
are relevant to PowerShell users:

- XenServerPowerShell\XenServerPSModule: this is the Citrix Hypervisor PowerShell
  Module
- XenServerPowerShell\src: contains the C# source code for the Citrix Hypervisor
  cmdlets shipped as a Visual Studio project.
- XenServerPowerShell\samples: contains the sample scripts accompanying the module.


Getting Started
---------------

1.  Download and unzip the CitrixHypervisor-SDK-8.1.1.zip.

    Note that, if you use Internet Explorer, it is likely that the zip file will
    be marked as "blocked" during the download, in which case you will need to
    unblock it before unzipping it, in order to import the module successfully.

    To unblock the zip file, right-click on it and launch the Properties dialog.
    Click the "Unblock" button, then the Apply or OK button.

2.  Navigate to the extracted XenServer\XenServerPowerShell directory and copy
    the whole folder XenServerPSModule into your PowerShell modules directory,
    which will normally be $env:UserProfile\Documents\WindowsPowerShell\Modules
    for per-user configuration or $env:windir\system32\WindowsPowerShell\v1.0\Modules
    for system-wide configuration.

3.  Open a PowerShell prompt as administrator.

    To do this, open the Windows Start menu by clicking the Start icon, find
    the item Windows PowerShell, right click it and select Run as administrator.

4.  Determine the current execution policy:

        PS> Get-ExecutionPolicy

    If the current policy is Restricted, you need to set it to RemoteSigned:

        PS> Set-ExecutionPolicy RemoteSigned

    You should understand the security implications of this change. If you
    are unsure, see Microsoft's documentation on the matter:

        PS> Get-Help about_signing

    If the current policy is AllSigned, it will work, but will be very
    inconvenient. You probably want to change it to RemoteSigned, as above.

    If the current policy is Unrestricted or RemoteSigned, it is compatible with
    the Citrix Hypervisor PowerShell Module, so there is nothing to do.

5.  Exit the privileged instance of PowerShell.

6.  Open a PowerShell prompt as a regular user (click Start > Windows PowerShell)
    and import the Citrix Hypervisor PowerShell Module:

        PS> Import-Module XenServerPSModule

7.  If you wish to load specific environment settings when the Citrix Hypervisor
    PowerShell Module is loaded, create the file XenServerProfile.ps1 and put it
    in $env:UserProfile\Documents\WindowsPowerShell for per-user configuration
    or $env:windir\system32\WindowsPowerShell\v1.0 for system-wide configuration.

8.  For an overview of the Citrix Hypervisor PowerShell Module type:

        PS> Get-Help about_XenServer

    You can obtain a list of all available cmdlets by typing:

        PS> Get-Command -Module XenServerPSModule

    For help with a specific command use:

        PS> Get-Help [CommandName]

9.  Here is a quick example of opening a session and making a call to a server:

        PS> Connect-XenServer -Url https://<servername>
        PS> Get-XenVM
        PS> Disconnect-XenServer


Building and Debugging the Source Code
--------------------------------------

1. Copy CookComputing.XmlRpcV2.dll, Newtonsoft.Json.CH.dll, and XenServer.dll from
   the XenServerPSModule folder into the source code folder at the same level as
   the project file XenServerPowerShell.csproj.

2. Open the project XenServerPowerShell.csproj in Visual Studio (2013 or greater).

3. You should now be ready to build the source code.

4. If in Debug mode, clicking Start will launch a PowerShell prompt as an
   external process, and import the compiled XenServerPowerShell.dll as a module
   (without, however, processing the scripts, types, and formats shipped within
   the XenServerPSModule). You should now be ready to debug the cmdlets.
