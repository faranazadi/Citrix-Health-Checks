# Enable default switches, like Verbose & Debug for script call
[CmdletBinding()]
# Declaring script parameters
Param()

Set-StrictMode -Version Latest

$Script:NSURLProtocol = "http" # This WAS https but certificate has expired for x, as a result it will fail to create a proper SSL session so will have to be http for now

# Copied from Citrix's Module to ensure correct scoping of variables and functions
    function Connect-NSAppliance {
        <#
        .SYNOPSIS
            Connect to NetScaler Appliance
        .DESCRIPTION
            Connect to NetScaler Appliance. A custom web request session object will be returned
        .PARAMETER NSAddress
            NetScaler Management IP address
        .PARAMETER NSName
            NetScaler DNS name or FQDN
        .PARAMETER NSUserName
            UserName to access the NetScaler appliance
        .PARAMETER NSPassword
            Password to access the NetScaler appliance
        .PARAMETER Timeout
            Timeout in seconds to for the token of the connection to the NetScaler appliance. 900 is the default admin configured value.
        .EXAMPLE
            $Session = Connect-NSAppliance -NSAddress IP HERE
        .EXAMPLE
            $Session = Connect-NSAppliance -NSName mynetscaler.mydomain.com
        .EXAMPLE (Bedfont)
            $Session = Connect-NSAppliance -NSAddress IP HERE -NSUserName nsroot -NSPassword Password123!
        .OUTPUTS
            CustomPSObject
        .NOTES
            Copyright (c) Citrix Systems, Inc. All rights reserved.
        #>
        [CmdletBinding()]
        param (
            [Parameter(Mandatory=$true,ParameterSetName='Address')] [string]$NSAddress,
            [Parameter(Mandatory=$true,ParameterSetName='Name')] [string]$NSName,
            [Parameter(Mandatory=$false)] [string]$NSUserName="nsroot", 
            [Parameter(Mandatory=$false)] [string]$NSPassword="nsroot",
            [Parameter(Mandatory=$false)] [int]$Timeout=900
        )
        Write-Verbose "$($MyInvocation.MyCommand): Enter"

        if ($PSCmdlet.ParameterSetName -eq 'Address') {
            Write-Verbose "Validating IP Address"
            $IPAddressObj = New-Object -TypeName System.Net.IPAddress -ArgumentList 0
            if (-not [System.Net.IPAddress]::TryParse($NSAddress,[ref]$IPAddressObj)) {
                throw "'$NSAddress' is an invalid IP address"
            }
            $nsEndpoint = $NSAddress
        } elseif ($PSCmdlet.ParameterSetName -eq 'Name') {
            $nsEndpoint = $NSName
        }


        $login = @{"login" = @{"username"=$NSUserName;"password"=$NSPassword;"timeout"=$Timeout}}
        $loginJson = ConvertTo-Json $login
    
        try {
            Write-Verbose "Calling Invoke-RestMethod for login"
            $response = Invoke-RestMethod -Uri "$($Script:NSURLProtocol)://$nsEndpoint/nitro/v1/config/login" -Body $loginJson -Method POST -SessionVariable saveSession -ContentType application/json
                
            if ($response.severity -eq "ERROR") {
                throw "Error. See response: `n$($response | Format-List * | Out-String)"
            } else {
                Write-Verbose "Response:`n$(ConvertTo-Json $response | Out-String)"
            }
        }
        catch [Exception] {
            throw $_
        }

        $nsSession = New-Object -TypeName PSObject
        $nsSession | Add-Member -NotePropertyName Endpoint -NotePropertyValue $nsEndpoint -TypeName String
        $nsSession | Add-Member -NotePropertyName WebSession  -NotePropertyValue $saveSession -TypeName Microsoft.PowerShell.Commands.WebRequestSession

        Write-Verbose "$($MyInvocation.MyCommand): Exit"

        Write-Host "Successfully connected to NetScaler: $NSAddress"
        return $nsSession
    }

    Write-Host "Connecting to NetScaler..."
    $nsSession = Connect-NSAppliance -NSAddress 10.207.99.11 -NSUserName ebb3 -NSPassword ebb3123!
    Write-Host "Contents of nsSession variable: $nsSession"

    function Confirm-SessionExists {
        param(
            $sessionToCheck = $script:nsSession
        )

        if ($null -eq $sessionToCheck) {
            throw 'There is no active NetScaler session stored in the nsSession variable.'
        }
    }

# Invoke-NSNitroRestApi is UPDATED (provided by Citrix)
    # [adjusted for beter DELETE function support]
    function Invoke-NSNitroRestApi {
        <#
        .SYNOPSIS
            Invoke NetScaler NITRO REST API 
        .DESCRIPTION
            Invoke NetScaler NITRO REST API 
        .PARAMETER NSSession
            An existing custom NetScaler Web Request Session object returned by Connect-NSAppliance
        .PARAMETER OperationMethod
            Specifies the method used for the web request
        .PARAMETER ResourceType
            Type of the NS appliance resource
        .PARAMETER ResourceName
            Name of the NS appliance resource, optional
        .PARAMETER Action
            Name of the action to perform on the NS appliance resource
        .PARAMETER Payload
            Payload  of the web request, in hashtable format
        .PARAMETER GetWarning
            Switch parameter, when turned on, warning message will be sent in 'message' field and 'WARNING' value is set in severity field of the response in case there is a warning.
            Turned off by default
        .PARAMETER OnErrorAction
            Use this parameter to set the onerror status for nitro request. Applicable only for bulk requests.
            Acceptable values: "EXIT", "CONTINUE", "ROLLBACK", default to "EXIT"
        .EXAMPLE
            Invoke NITRO REST API to add a DNS Server resource.
            $payload = @{ip=""}
            Invoke-NSNitroRestApi -NSSession $Session -OperationMethod POST -ResourceType dnsnameserver -Payload $payload 
        .OUTPUTS
            Only when the OperationMethod is GET:
            PSCustomObject that represents the JSON response content. This object can be manipulated using the ConvertTo-Json Cmdlet.
        .NOTES
            Copyright (c) Citrix Systems, Inc. All rights reserved.
            Copyright (c) cognition IT. All rights reserved.
            20160117: Adjusted to ensure DELETE methods can produce output as well as use the Arguments parameter
        #>
        [CmdletBinding()]
        param (
            [Parameter(Mandatory=$true)] [PSObject]$NSSession, 

            [Parameter(Mandatory=$true)] [ValidateSet("DELETE","GET","POST","PUT")] 
            [string]$OperationMethod,

            [Parameter(Mandatory=$true)] [string]$ResourceType,

            [Parameter(Mandatory=$false)] [string]$ResourceName, 

            [Parameter(Mandatory=$false)] [string]$Action,

            [Parameter(Mandatory=$false)] [ValidateScript({(($OperationMethod -eq "GET") -or ($OperationMethod -eq "DELETE"))})] 
            [hashtable]$Arguments=@{},

            [Parameter(Mandatory=$false)] [ValidateScript({$OperationMethod -ne "GET"})]
            [hashtable]$Payload=@{},

            [Parameter(Mandatory=$false)] [switch]$GetWarning=$false,

            [Parameter(Mandatory=$false)] [ValidateSet("EXIT", "CONTINUE", "ROLLBACK")] [string]$OnErrorAction="EXIT"
        )

        Write-Verbose "$($MyInvocation.MyCommand): Enter"
    
        Write-Verbose "Building URI"
        $uri = "$($Script:NSURLProtocol)://$($NSSession.Endpoint)/nitro/v1/config/$ResourceType"
        if (-not [string]::IsNullOrEmpty($ResourceName)) {
            $uri += "/$ResourceName"
        }
        if ($OperationMethod -ne "GET") {
            if (-not [string]::IsNullOrEmpty($Action)) {
                $uri += "?action=$Action"
            }
        } else {
            if ($Arguments.Count -gt 0) {
                $uri += "?args="
                $argsList = @()
                foreach ($arg in $Arguments.GetEnumerator()) {
                    $argsList += "$($arg.Name):$([System.Uri]::EscapeDataString($arg.Value))"
                }
                $uri += $argsList -join ','
            }
            #TODO: Add filter, view, and pagesize
        }
        if ($OperationMethod -eq "DELETE") {
            if ($Arguments.Count -gt 0) {
                Write-Verbose "Arguments found for DELETE"
                $uri += "?args="
                $argsList = @()
                foreach ($arg in $Arguments.GetEnumerator()) {
                    Write-verbose ("Adding " + $arg.Name + " to the list")
                    $argsList += "$($arg.Name):$([System.Uri]::EscapeDataString($arg.Value))"
                }
                $uri += $argsList -join ','
            }

        }
        Write-Verbose "URI: $uri"

        if ($OperationMethod -ne "GET") {
            Write-Verbose "Building Payload"
            $warning = if ($GetWarning) { "YES" } else { "NO" }
            $hashtablePayload = @{}
            $hashtablePayload."params" = @{"warning"=$warning;"onerror"=$OnErrorAction;<#"action"=$Action#>}
            $hashtablePayload.$ResourceType = $Payload
            #In recent versions of powershell the max value for the depth on convertto-json is 100
            #int::maxvalue returned 2147483647 and the max value it can accept is 100.
            $jsonPayload = ConvertTo-Json $hashtablePayload -Depth 100

            Write-Verbose "JSON Payload:`n$jsonPayload"
        }

        try {
            Write-Verbose "Calling Invoke-RestMethod"
            $restParams = @{
                Uri = $uri
                ContentType = "application/json"
                Method = $OperationMethod
                WebSession = $NSSession.WebSession
                ErrorVariable = "restError"
            }
        
            if ($OperationMethod -ne "GET") {
                $restParams.Add("Body",$jsonPayload)
            }

            Write-Verbose $restParams
            $response = Invoke-RestMethod @restParams
        
            if ($response) {
                if ($response.severity -eq "ERROR") {
                    throw "Error. See response: `n$($response | Format-List * | Out-String)"
                } else {
                    Write-Verbose "Response:`n$(ConvertTo-Json $response | Out-String)"
                }
            }
        }
        catch [Exception] {
            if ($ResourceType -eq "reboot" -and $restError[0].Message -eq "The underlying connection was closed: The connection was closed unexpectedly.") {
                Write-Verbose "Connection closed due to reboot"
            } else {
                throw $_
            }
        }

        Write-Verbose "$($MyInvocation.MyCommand): Exit"

        if (($OperationMethod -eq "GET") -or ($OperationMethod -eq "DELETE"))  {
            return $response
        }
    }

    function Get-NSLBVServer {
        [CmdletBinding()]
        param (
            [Parameter(Mandatory=$true)] [PSObject]$NSSession,
            [Parameter(Mandatory=$false)] [string]$Name
        )
        Begin {
            Write-Verbose "$($MyInvocation.MyCommand): Enter"
        }
        Process {
            If ($Name){
                $response = Invoke-NSNitroRestApi -NSSession $NSSession -OperationMethod GET -ResourceType lbvserver -ResourceName $Name
            }
            Else {
                $response = Invoke-NSNitroRestApi -NSSession $NSSession -OperationMethod GET -ResourceType lbvserver
            }
        }
        End {
            Write-Verbose "$($MyInvocation.MyCommand): Exit"
            If ($response.PSObject.Properties['lbvserver'])
            {
                Write-Host "response.lvserver = $response.lbvserver"
                return $response.lbvserver
            }
            else
            {
                return $null
            }
        }
    }

    #$lbvserver = Get-NSLBVServer -NSSession $Session

    function Get-NSFeature { 
     <# 
     .SYNOPSIS 
         Gets the feature status for the NetScaler appliance. 
  
     .DESCRIPTION 
         Gets the feature status for the NetScaler appliance. 
  
     .EXAMPLE 
         Get-NSFeature 
  
         Get status for all the NetScaler features. 
  
     .EXAMPLE 
         Get-NSFeature -Name 'sslvpn' 
      
         Get the status of NetScaler feature 'sslvpn'. 
  
     .EXAMPLE 
         'sslvpn', 'lb' | Get-NSFeature 
      
         Get the status of NetScaler feature 'sslvpn' and 'lb'. 
  
     .PARAMETER Session 
         The NetScaler session object. 
  
     .PARAMETER Name 
         The name or names of NetScaler features to get. 
     #> 
     [cmdletbinding()] 
     param( 
         $Session = $script:nsSession, 
 

         [parameter(ValueFromPipeline = $true, Position = 0, ValueFromPipelineByPropertyName)] 
         [string[]]$Name = @() 
     ) 
 
 
     begin { 
         Confirm-SessionExists -Session $nsSession
         $features = @() 
     } 
 
 
     process { 
         if ($Name.Count -gt 0) { 
             $all = Invoke-NSNitroRestApi -NSSession $Session -OperationMethod Get -ResourceType nsfeature -Action Get 
             foreach ($item in $Name) { 
                 $features += $all.nsfeature.$item 
             } 
             return $features 
         } else { 
             $features = Invoke-NSNitroRestApi -NSSession $Session -OperationMethod Get -ResourceType nsfeature -Action Get 
             return $features.nsfeature 
         } 
     } 
 } 

    $sslvpn = Get-NSFeature -Name 'sslvpn'
    Write-Host "SSL VPN: $sslvpn"
    
    $lb = Get-NSFeature -Name 'lb'
    Write-Host "lb: $lb"  

    function Get-NSHANode { 
        <# 
        .SYNOPSIS 
            Gets the specified HA Node object(s). 
    
        .DESCRIPTION 
            Gets the specified HA Node object(s). 
            Either returns a single object identified by its identifier (-ID parameter) 
            or a collection of objects filtered by the other parameters. Those 
            filter parameters accept either a literal value or a regexp in the form 
            "/someregexp/". 
    
        .EXAMPLE 
            Get-NSHANode 
    
            Get all HA Node objects. 
    
        .EXAMPLE 
            Get-NSHANode -ID 'foobar' 
        
            Get the HA Node named 'foobar'. 
    
        .PARAMETER Session 
            The NetScaler session object. 
    
        .PARAMETER ID 
            The identifier/name or identifiers/names of the HA Nodes to get. 
    
        .PARAMETER IPAddress 
            A filter to apply to the ipaddress property. 
    
        .PARAMETER HASync 
            A filter to apply to the hasync property. 
    
        .PARAMETER Name 
            A filter to apply to the name property. 
    
        .PARAMETER HAStatus 
            A filter to apply to the hastatus property. 
    
        .PARAMETER State 
            A filter to apply to the state property. 
    
        .NOTES 
            Nitro implementation status: partial         
    
        #> 
        [CmdletBinding(DefaultParameterSetName='get')] 
        Param( 
            $Session = $Script:nsSession, 
    
    
            [Parameter(Position=0, ParameterSetName='get')] 
            [string[]]$ID = @(), 
    
    
            [Parameter(ParameterSetName='search')] 
            [string]$IPAddress, 
    
    
            [Parameter(ParameterSetName='search')] 
            [string]$HASync, 

    
            [Parameter(ParameterSetName='search')] 
            [string]$Name, 
    
    
            [Parameter(ParameterSetName='search')] 
            [string]$HAStatus, 
    
    
            [Parameter(ParameterSetName='search')] 
            [string]$State 
    
    
        ) 
        Begin { 
            Confirm-SessionExists
        } 
    
    
        Process { 
            # Contruct a filter hash if we specified any filters 
            $Filters = @{} 
            if ($PSBoundParameters.ContainsKey('IPAddress')) { 
                $Filters['ipaddress'] = $IPAddress 
            } 
            if ($PSBoundParameters.ContainsKey('HASync')) { 
                $Filters['hasync'] = $HASync 
            } 
            if ($PSBoundParameters.ContainsKey('Name')) { 
                $Filters['name'] = $Name 
            } 
            if ($PSBoundParameters.ContainsKey('HAStatus')) { 
                $Filters['hastatus'] = $HAStatus 
            } 
            if ($PSBoundParameters.ContainsKey('State')) { 
                $Filters['state'] = $State 
            } 
            
            Invoke-NSNitroRestApi -NSSession $Session -OperationMethod GET -ResourceType hanode <#-ResourceName $ID#> -Arguments $Filters 
    
        } 
    } 

    $allHANodes = Get-NSHANode -Session $nsSession 
    Write-Host "All HA Nodes: $allHANodes"


  
    function Get-NSLBMonitor {
        <#
        .SYNOPSIS
            Gets the specified load balancer monitoring object.

        .DESCRIPTION
            Gets the specified load balancer monitoring object.

        .EXAMPLE
            Get-NSLBMonitor

            Get all load balancer monitor objects.

        .EXAMPLE
            Get-NSLBMonitor -Name 'monitor01'
        
            Get the load balancer monitor named 'monitor01'.

        .PARAMETER Session
            The NetScaler session object.

        .PARAMETER Name
            The name or names of the load balancer monitors to get.
        #>
        [cmdletbinding()]
        param(
            $Session = $script:nsSession,

            [parameter(Position = 0, ValueFromPipeline = $true, ValueFromPipelineByPropertyName = $true)]
            [string[]]$Name = @()
        )

        begin {
            Confirm-SessionExists
            $monitors = @()
        }

        process {
            if ($Name.Count -gt 0) {
                foreach ($item in $Name) {
                    $monitors = Invoke-NSNitroRestApi -NSSession $nsSession -OperationMethod Get -ResourceType lbmonitor -Action Get -ResourceName $item
                    if ($monitors.psobject.properties.name -contains 'lbmonitor') {
                        return $monitors.lbmonitor
                    }
                }
            } else {
                $monitors = Invoke-NSNitroRestApi -NSSession $nsSession -OperationMethod Get -ResourceType lbmonitor -Action Get
                if ($monitors.psobject.properties.name -contains 'lbmonitor') {
                    return $monitors.lbmonitor
                }
            }
        }
    }

    function Get-NSLBStat {
        <#
        .SYNOPSIS
            Gets the specified load balancer stat object.

        .DESCRIPTION
            Gets the specified load balancer stat object.

        .EXAMPLE
            Get-NSLBStat

            Get all load balancer stat objects.

        .EXAMPLE
            Get-NSLBStat -Name 'stat01'
        
            Get the load balancer stat named 'stat01'.

        .PARAMETER Session
            The NetScaler session object.

        .PARAMETER Name
            The name or names of the load balancer stat to get.
        #>
        [cmdletbinding()]
        param(
            $Session = $script:nsSession,

            [parameter(ValueFromPipeline = $true, ValueFromPipelineByPropertyName = $true)]
            [string[]]$Name = @()
        )

        begin {
            Confirm-SessionExists
            $stats = @()

            Write-Warning -Message 'This function is deprecated in favour of Get-NSStat and will be removed in a future major release. Please use Get-NSStat -Type <typename> instead.'
        }

        process {
            if ($Name.Count -gt 0) {
                foreach ($item in $Name) {
                    $stats = Invoke-NSNitroRestApi -Session $Session -Method Get -Type servicegroup -Stat -Resource $item
                    return $stats.servicegroup
                }
            } else {
                $stats = Invoke-NSNitroRestApi -Session $Session -Method Get -Type servicegroup -Stat
                return $stats.servicegroup
            }
        }
    }

    function Get-NSLBVirtualServer {
        <#
        .SYNOPSIS
            Gets the specified load balancer virtual server object.

        .DESCRIPTION
            Gets the specified load balancer virtual server object.

        .EXAMPLE
            Get-NSLBVirtualServer

            Get all load balancer virtual server objects.

        .EXAMPLE
            Get-NSLBVirtualServer -Name 'vserver01'

            Get the load balancer virtual server named 'vserver01'.

        .PARAMETER Session
            The NetScaler session object.

        .PARAMETER Name
            The name or names of the load balancer virtual server to get.

        .PARAMETER Port
            Filter load balancer virtual servers by port.

        .PARAMETER ServiceType
            Filter load balancer virtual servers by service type.

        .PARAMETER LBMethod
            Filter load balancer virtual servers by load balancing method.
        #>
        [cmdletbinding()]
        param(
            $Session = $script:nsSession,

            [Parameter(Position=0)]
            [string]$Name,

            [int]$Port,

            [ValidateSet('DHCPRA','DIAMTER', 'DNS', 'DNS_TCP', 'DLTS', 'FTP', 'HTTP', 'MSSQL', 'MYSQL', 'NNTP', 'PUSH','RADIUS', 'RDP', 'RTSP', 'SIP_UDP', 'SSL', 'SSL_BRIDGE', 'SSL_DIAMETER', 'SSL_PUSH', 'SSL_TCP', 'TCP', 'TFTP', 'UDP')]
            [string]$ServiceType,

            [ValidateSet('ROUNDROBIN', 'LEASTCONNECTION', 'LEASTRESPONSETIME', 'LEASTBANDWIDTH', 'LEASTPACKETS', 'CUSTOMLOAD', 'LRTM', 'URLHASH', 'DOMAINHASH', 'DESTINATIONIPHASH', 'SOURCEIPHASH', 'TOKEN', 'SRCIPDESTIPHASH', 'SRCIPSRCPORTHASH', 'CALLIDHASH')]
            [string]$LBMethod
        )

        begin {
            Confirm-SessionExists
            $response = @()
        }

        process {
            # Contruct a filter hash if we specified any filters
            $filters = @{}
            if ($PSBoundParameters.ContainsKey('Name')) {
                $filters.'name' = $Name
            }
            if ($PSBoundParameters.ContainsKey('Port')) {
                $filters.'port' = $Port
            }
            if ($PSBoundParameters.ContainsKey('ServiceType')) {
                $filters.'servicetype' = $ServiceType
            }
            if ($PSBoundParameters.ContainsKey('LBMethod')) {
                $filters.'lbmethod' = $LBMethod
            }

            # If we specified any filters, filter based on them
            # Otherwise, get everything
            if ($filters.count -gt 0) {
                $response = Invoke-NSNitroRestApi -Session $Session -Method Get -Type lbvserver -Action Get -Filters $filters
            } else {
                $response = Invoke-NSNitroRestApi -Session $Session -Method Get -Type lbvserver -Action Get
            }
            if ($response.errorcode -ne 0) { throw $response }
            if ($response.psobject.properties | Where-Object {$_.name -eq 'lbvserver'}) {
                return $response.lbvserver
            }
        }
    }

    function Get-NSStat {
        <#
        .SYNOPSIS
            Gets the specified stat object.

        .DESCRIPTION
            Gets the specified stat object.

        .EXAMPLE
            Get-NSStat -Type 'lbvserver'

            Get all stats of type lbvserver

        .EXAMPLE
            Get-NSStat -Type 'servicegroup' -Name 'sg01'

            Get the stats for service group with name sg01

        .PARAMETER Session
            The NetScaler session object.

        .PARAMETER Type
            The type of stat object to retrieve

        .PARAMETER Name
            The name or names of specific stat objects to retrieve
        #>
        [cmdletbinding()]
        param(
            $Session = $script:nsSession,

            [ValidateSet('aaa', 'appflow', 'appfw', 'appfwpolicy', 'appfwpolicylabel', 'appfwprofile', 'appqoe', 'appqoepolicy',
                'audit', 'authenticationloginschemapolicy', 'authenticationpolicy', 'authenticationpolicylabel', 'authenticationsamlidpolicy',
                'authenticationvserver', 'authorizationpolicylabel', 'autoscalepolicy', 'service', 'servicegroup', 'servicegroupmember',
                'ca', 'cache', 'cachecontentgroup', 'cachepolicy', 'cachepolicylabel', 'clusterinstance', 'clusternode', 'cmp', 'cmppolicy',
                'cmppolicylabel', 'crvserver', 'cvserver', 'dns', 'dnspolicylabel', 'dnsrecords', 'dos', 'dospolicy', 'feo', 'gslbdomain', 'gslbservice',
                'gslbsite', 'dslbvserver', 'hanode', 'icapolicy', 'ipseccounters', 'lbvserver', 'lldp', 'lsn', 'lsndslite', 'lsngroup', 'lsnnat64',
                'mediaclassification', 'interface', 'bridge', 'inat', 'inatsession', 'nat64', 'rnat', 'rnatip', 'tunnelip', 'tunnelip6', 'vlan', 'vpath',
                'vxlan', 'ns', 'nsacl', 'nsacl6', 'nslimitidentifier', 'nsmemory', 'nspartition', 'nspbr', 'nssimpleacl', 'nssimpleacl6',
                'nstrafficdomain', 'pq', 'pqpolicy', 'protocolhttp', 'protocolicmp', 'protocolicmpv6', 'protocolip', 'protocolipv6', 'protocoltcp',
                'ptotocoludp', 'qos', 'rewritepolicy', 'rewritepolicylabel', 'responderpolicy', 'responderpolicylabel', 'sc', 'scpolicy', 'snmp',
                'spilloverpolicy', 'ssl', 'sslvserver', 'streamidentifier', 'system', 'systembw', 'systemcpu', 'systemmemory', 'tmsessionpolicy',
                'tmtrafficpolicy', 'transformpolicy', 'transformpolicylabel', 'vpn', 'vpnvserver', 'pcpserver')]
            [parameter(Mandatory, ValueFromPipeline = $true, ValueFromPipelineByPropertyName = $true)]
            [string[]]$Type,

            [parameter(ValueFromPipeline = $true, ValueFromPipelineByPropertyName = $true)]
            [string[]]$Name = @()
        )

        begin {
            Confirm-SessionExists
            $stats = @()
        }

        process {
            foreach ($statType in $Type) {
                if ($Name.Count -gt 0) {
                    foreach ($item in $Name) {
                        $stats = Invoke-NSNitroRestApi -Session $Session -Method Get -Type $statType -Stat -Resource $item
                        if ($stats | Get-Member -MemberType NoteProperty | Where-Object {$_.name -eq $statType}) {
                            $stats.$statType
                        }
                    }
                } else {
                    $stats = Invoke-NSNitroRestApi -Session $Session -Method Get -Type $statType -Stat
                    if ($stats | Get-Member -MemberType NoteProperty | Where-Object {$_.name -eq $statType}) {
                        $stats.$statType
                    }
                }
            }
        }
    }

    
    # Copied from Citrix's Module to ensure correct scoping of variables and functions
    function Disconnect-NSAppliance {
        <#
        .SYNOPSIS
            Disconnect NetScaler Appliance session
        .DESCRIPTION
            Disconnect NetScaler Appliance session
        .PARAMETER NSSession
            An existing custom NetScaler Web Request Session object returned by Connect-NSAppliance
        .EXAMPLE
            Disconnect-NSAppliance -NSSession $Session
        .NOTES
            Copyright (c) Citrix Systems, Inc. All rights reserved.
        #>
        [CmdletBinding()]
        param (
            [Parameter(Mandatory=$true)] [PSObject]$NSSession
        )

        Write-Verbose "$($MyInvocation.MyCommand): Enter"

        $logout = @{"logout" = @{}}
        $logoutJson = ConvertTo-Json $logout
    
        try {
            Write-Verbose "Calling Invoke-RestMethod for logout"
            $response = Invoke-RestMethod -Uri "$($Script:NSURLProtocol)://$($NSSession.Endpoint)/nitro/v1/config/logout" -Body $logoutJson -Method POST -ContentType application/json -WebSession $NSSession.WebSession
        }
        catch [Exception] {
            throw $_
        }

        Write-Verbose "$($MyInvocation.MyCommand): Exit"
    }

    Write-Host "Disconnecting from NetScaler session..."
    Disconnect-NSAppliance -NSSession $nsSession

    