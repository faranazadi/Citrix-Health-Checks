<#
    Emails a health report of Citrix XenApp/XenDesktop 7.x environment

    Guy Leech, 2018
    Faran Azadi, 2020-21
#>

<#
.SYNOPSIS

Send a HTML email report of some Citrix health checks such as machines not rebooted recently, machines not powered up, not registered, in maintenance mode, users disconnected for too long and file share capacities.
Also includes application groups and desktops with tag restrictions.

.DESCRIPTION

.NOTES

Uses local PowerShell cmdlets for PVS, DDCs and VMware, as well as Active Directory, so run from a machine where both PVS and Studio consoles and the VMware PowerCLI are installed.
Uses an additional module, AdditionalFunctions.psm1, which contains helper functions. I haven't had time to move the rest of the helper functions in this script.
To configure the checks edit .\Config\ChecksConfiguration.xml

All naming conventions have been followed as per Microsoft's guidance found here: https://docs.microsoft.com/en-gb/powershell/scripting/developer/cmdlet/approved-verbs-for-windows-powershell-commands?view=powershell-7

#>

[CmdletBinding()]
Param()

# Makes sure that the script uses the strictest available version, even when new versions are added to PowerShell
Set-StrictMode -Version Latest

# The list of supported TLS versions (used to only be TLS1.2, but this caused errors in the URL checks because client X use an old version of TLS)
[Net.ServicePointManager]::SecurityProtocol = "tls,tls11,tls12,tls13"

#==================================================#
#               VARIABLE DECLARATIONS              #
#==================================================#
$allFailedMachines = $null
$taggedApplicationGroups = New-Object System.Collections.ArrayList 
$taggedDesktops = New-Object System.Collections.ArrayList
$formattedDate = Get-Date -Format dd-MM-yy 
$formattedTime = Get-Date -Format HH.mm.ss

# Required directories
$scriptRootDir = Split-Path $MyInvocation.InvocationName
$logo1Dir = "$scriptRootDir\Resources\logo1.png"
$logo2Dir = "$scriptRootDir\Resources\logo2.png"
$logRootDir = "$scriptRootDir\Logs\$($formattedDate)" 
$transcriptLog =  "$scriptRootDir\Logs\$($formattedDate)\TranscriptLogs (T$($formattedTime)).txt" 
$terminatedSessionsLog = "$scriptRootDir\Logs\$($formattedDate)\TerminatedSessions (T$($formattedTime)).csv"
$citrixStudioLog = "$scriptRootDir\Logs\$($formattedDate)\CitrixStudioLogs (T$($formattedTime)).csv"
$reportOutputPath = "$scriptRootDir\Logs\$($formattedDate)\HealthChecks (T$($formattedTime)).html"


# Prepare for script outputs
# Check the directory for the logs exists, and create one if not
if (-not (Test-Path -LiteralPath $logRootDir)) {
    try {
        New-Item -Path $logRootDir -ItemType Directory -ErrorAction SilentlyContinue | Out-Null
    } catch {
        Write-Error "Unable to create a directory for today's log files."
        
    }
    Write-Host "[$(Get-Date)][INFO] Successfully created today's log file directory."
}
else {
    Write-Host "[$(Get-Date)][INFO] $logRootDir already existed."
}

# If the log file exists or isn't empty, add on to the end of the existing logs instead of completely overwriting
if(![string]::IsNullOrEmpty($transcriptLog))
{
    Start-Transcript -Append $transcriptLog

    # Get the Date/Time of when the script commences
    $scriptStart = Get-Date
    Write-Host "[$(Get-Date)][INFO] Script started..."
}

# Load the contents of the configuration file
Write-Host "[$(Get-Date)][INFO] Loading contents of configuration file..."
[xml]$configFile = Get-Content ($scriptRootDir + "\Config\ChecksConfiguration.xml") # Stores contents of config file in a variable to be accessed later

#==================================================#
#          MAIN/CONTROL FUNCTION OF SCRIPT         #
#==================================================#
function main {
    Initialize-ScriptParamsAndVars

    # Select a 'primary' DDC that will be used to grab information from
    $primaryDDC = Get-Random -InputObject $ddcs
    Write-Host "The DDC that has been selected is: $primaryDDC"

    Import-ScriptDependencies

    # Do any cleaning up here before any actual checks or stats are collected
    # TODO: finish the restarting of failed machines
    Disconnect-IdleSessions -disconnectedThresholdMins 7200 -ddc $primaryDDC -forceIt -transcriptLog $terminatedSessionsLog

    Read-LicenseUsage

    if ($isUsingSMTPAuth) {
        $SMTPPassword = Decrypt-SMTPPassword
    }

    Invoke-InfrastructureChecks

    Stop-Script
}

#==================================================#
#               HELPER FUNCTIONS                   #
#==================================================#
Function Get-SMTPPassword {
    # WARNING: The AES key file can be used to decrypt the password and therefore requires additional (NTFS) protection from unauthorized access before being implemented into production. 
    $keyFile = $scriptRootDir + "\Config\AES256_KEY.key"
    $passwordFile = $scriptRootDir + "\Config\AES256_PASSWORD_FILE.txt"

    Write-Host "[$(Get-Date)][INFO] Decrypting and loading SMTP password..."

    # Convert the standard encrypted password stored in the password file to a secure string using the AES key file
    $securePassword = ((Get-Content $passwordFile) | ConvertTo-SecureString -Key (Get-Content $keyFile))
        
    # Write the secure password to unmanaged memory (specifically to a binary or basic string)         
    $securePasswordInMemory = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword);
        
    # Read the plain-text password from memory and store it in a variable             
    return $passwordAsString = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($securePasswordInMemory); 

    # Delete the password from the unmanaged memory (for security reasons)
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($securePasswordInMemory);
}

Function Initialize-ScriptParamsAndVars {
	$configFile.Settings.Variables.Variable | ForEach-Object {
		
        # Set Variables contained in XML file
		$varValue = $_.Value
		$createVariable = $True # Default value to create XML content as Variable

		switch ($_.Type) {
			# Format data types for each variable - this is equivalent to casting in other languages I believe? 
			'[string]' { $varValue = [string]$varValue } # Fixed-length string of Unicode characters
			'[char]' { $varValue = [char]$varValue } # A Unicode 16-bit character
			'[byte]' { $varValue = [byte]$varValue } # An 8-bit unsigned character
            '[bool]' { If ($varValue.ToLower() -eq 'false'){$varValue = [bool]$False} ElseIf ($varValue.ToLower() -eq 'true'){$varValue = [bool]$True} } # A boolean True/False value
			'[int]' { $varValue = [int]$varValue } # 32-bit signed integer
			'[long]' { $varValue = [long]$varValue } # 64-bit signed integer
			'[decimal]' { $varValue = [decimal]$varValue } # A 128-bit decimal value
			'[single]' { $varValue = [single]$varValue } # Single-precision 32-bit floating point number
			'[double]' { $varValue = [double]$varValue } # Double-precision 64-bit floating point number
			'[DateTime]' { $varValue = [DateTime]$varValue } # Date and Time
			'[Array]' { $varValue = [Array]$varValue.Split(',') } # Array
			'[Command]' { $varValue = Invoke-Expression $varValue; $createVariable = $False } # Command
		}

        # Create varaible with the specified name, value and scope within the script
		If ($createVariable) { 
            New-Variable -Name $_.Name -Value $varValue -Scope $_.Scope -Force 
            Write-Host "[$(Get-Date)][INFO] Created variable $($_.Name) with a value of $varValue"
        }
	}
}

Function Import-ScriptDependencies {
    <#
    .SYNOPSIS
    Loads all snapins, modules and libraries required by the script 

    #>

    ForEach($snapin in $snapins)
    {
        Write-Host "[$(Get-Date)][INFO] Adding snapins..."
        Add-PSSnapin $snapin -ErrorAction Continue
        Write-Host "[$(Get-Date)][INFO] Added snapin: $snapin"
    }

    ForEach($module in $modules)
    {
        Write-Host "[$(Get-Date)][INFO] Importing modules..."
        Import-Module $module -ErrorAction SilentlyContinue
        Write-Host "[$(Get-Date)][INFO] Imported module: $module"
        
        [bool]$loaded = $?

        # Only check script folder if not an absolute or UNC path
        if(!$loaded -and $module -notmatch '^[a-z]:\\' -and  $module -notmatch '^\\\\') 
        {
            # Try same folder as the script if there is no path in the module name
            Import-Module (Join-Path (& {Split-Path -Path $myInvocation.ScriptName -Parent}) $module) -ErrorAction Continue
            Write-Host "[$(Get-Date)][INFO] Imported module: $module"
            $loaded = $?
        }

        if(!$loaded)
        {
            Write-Warning "Unable to load module `"$module`" so functionality may be limited"
        }
    }

    # Check that NuGet package provider is installed, if not, then install it
    if (!(Get-PackageProvider | Where-Object {$_.Name -eq "NuGet"})) {
        Write-Host "Default NuGet package provider not installed."
        Write-Host "Installing NuGet package provider."
        Install-PackageProvider -Name "NuGet" -Confirm:$false -Force
        Set-PSRepository -Name "PSGallery" -InstallationPolicy "Trusted" 
    }

    # Check if Posh SSH module is installed, if not, then install it
    # This module is used for checking the dom0 memory usage on XenServer hosts
    if (!((Get-Module -ListAvailable *) | Where-Object {$_.Name -eq "Posh-SSH"})) {
        Write-Host "SSH module not found, installing missing module."
        Install-Module -Name Posh-SSH -Confirm:$false -Force
    }
}

Function Read-LicenseUsage {
    <#
    .SYNOPSIS
    Grabs the amount of concurrent licenses in use from the license server.

    #>

    # Get Certhash from the License Server specified in the config file
    $certHash = (Get-LicCertificate -AdminAddress $licenseServer).certhash

    # Obtain all License Inforamtion from License Server
    $licenseInfo = Get-LicInventory -AdminAddress $licenseServer -Certhash $certHash

    # Filter and display all checked out licenses for Citrix Virtual Apps and Desktops Premium
    $licenseReport = $licenseInfo | Where-Object {$_.LocalizedLicenseProductName -eq "Citrix Virtual Apps and Desktops Premium"} | Select-Object LocalizedLicenseModel, LicensesInUse
    $groupLicenseReport = $licenseReport | Group-Object -Property LocalizedLicenseModel -AsHashTable
    $tab = @{}
    
    $groupLicenseReport.keys | ForEach-Object {
        $tab += @{ $_ = ($groupLicenseReport[$_] | Measure-Object -Property LicensesInUse -sum) }
    }

    # Display license usage on console
    $Global:noOfConcurrentLicenses = $tab["Concurrent"].sum
    $Global:noOfUserDevLicenses = $tab["User/Device"].sum

    Write-Host "[$(Get-Date)][INFO] Concurrent License Count: $($noOfConcurrentLicenses)" 
    Write-Host "[$(Get-Date)][INFO] User/Device License Count: $($noOfUserDevLicenses)" 
}

Function Test-InternalURL {
    [cmdletbinding()]
	param(
		[string]$url = ""
	)

    try 
    {
        Write-Host "[$(Get-Date)][INFO] Checking $($url)..."
        $httpResponse = Invoke-WebRequest -Uri $url -UseBasicParsing # Uses basic parsing incase IE hasn't been set up, causes an error if not
    } catch {
        $statusCode = $_.Exception
        Write-Error "[$(Get-Date)] Could not connect to $($url)"
    }

    if ($null -ne $httpResponse)
    {
        $statusCode = $httpResponse.StatusCode.ToString() # Get the status code from the HTTP response
        
        if ($statusCode -eq '200') {
            $statusCode = '200 OK (ONLINE)'
            $onlineURLs += $url
        }
        
        if ($statusCode -ne '200 OK (ONLINE)') {
            $statusCode = 'Did not respond with 200 OK - potentially offline.'
            $potentiallyOfflineURLs += $url
        }
    }
            
return [pscustomobject]@{
        "URL" = $url
        "HTTP Response" = $statusCode
    }  
}  
 
function Get-DiskFreeSpace {
	[cmdletbinding()]
	param(
		[parameter(mandatory=$true,position=0,ValueFromPipeLine=$true)]
		[validatescript({(Test-Path $_ -IsValid)})]
		[string]$path,
		[parameter(mandatory=$false,position=1)]
		[string]$unit="gb"
	)
	
	begin{
		switch($unit){
			"byte" {$unitVal = 1;break}
			"kb" {$unitVal = 1kb;break}
			"mb" {$unitVal = 1mb;break}
			"gb" {$unitVal = 1gb;break}
			"tb" {$unitVal = 1tb;break}
			"pb" {$unitVal = 1pb;break}
			default {$unitVal = 1;break}
		}
		
		$typeDefinition = @'
[DllImport("kernel32.dll", CharSet = CharSet.Auto, SetLastError = true)]
[return: MarshalAs(UnmanagedType.Bool)]
public static extern bool GetDiskFreeSpaceEx(string lpDirectoryName,
	out ulong lpFreeBytesAvailable,
	out ulong lpTotalNumberOfBytes,
	out ulong lpTotalNumberOfFreeBytes);
'@
	
	}
	process{
		$freeBytesAvail = New-Object System.UInt64
		$totalNoBytes = New-Object System.UInt64
		$totalNoFreeBytes = New-Object System.UInt64
		
		$type = Add-Type -MemberDefinition $typeDefinition -Name Win32Utils -Namespace GetDiskFreeSpaceEx -PassThru
		
		$result = $type::GetDiskFreeSpaceEx($path,([ref]$freeBytesAvail),([ref]$totalNoBytes),([ref]$totalNoFreeBytes))
		
		#$freeBytes = {if($result){$freeBytesAvail/$unitVal}else{"N/A"}}.invoke()[0]
		$totalBytes = {if($result){$totalNoBytes/$unitVal}else{"N/A"}}.invoke()[0]
		$totalFreeBytes = {if($result){$totalNoFreeBytes/$unitVal}else{"N/A"}}.invoke()[0]
	    
        return [pscustomobject]@{
            "UNC Path" = $path
			"Online" = $result
			"Total Capacity `($unit`)" = [math]::Round($totalBytes, 2)
			"Total Free Space `($unit`)" = [math]::Round($totalFreeBytes, 2)
		    "Free Space (%)" = [math]::Round(($totalFreeBytes/$totalBytes) * 100, 1)
        } 
    }
}  

function Get-CitrixLogs {
    <#
    .SYNOPSIS

    Produce grid view or csv report of Citrix XenApp/XenDesktop admin logs such as from actions in Studio or Director

    .PARAMETER ddc

    The Delivery Controller to connect to, defaults to the local machine which must have the Citrix Studio PowerShell modules available

    .PARAMETER username

    Only return records for the specified user

    .PARAMETER operation

    Only return records which match the specified operation such as "log off" or "shadow"

    .PARAMETER start

    Only return records created on or after the given date/time

    .PARAMETER end

    Only return records created on or before the given date/time

    .PARAMETER last

    Only return records created in the last x seconds/minutes/hours/days/weeks years, e.g. 1d for 1 day or 12h for 12 hours

    .PARAMETER outputfile

    Write the returned records to the csv file named

    .PARAMETER gridview

    Display the returned results in an on screen filterable and sortable grid view

    .PARAMETER configChange

    Only return records which are for configuration changes

    .PARAMETER adminActions

    Only return records for administrative actions like shadowing or logging off

    .PARAMETER studioOnly

    Only return records for operations performed via Studio

    .PARAMETER directorOnly

    Only return records for operations performed via Director

    .PARAMETER maxRecordCount

    Returns at most this number of records. If more records are available than have been returned then a warning message will be displayed.

    .EXAMPLE

    Get-CitrixLogs -username manuel -gridview -last 14d -operation "Shadow"

    Show all shadow operations performed by the user manuel in the last 14 days and display in a grid view

    .EXAMPLE

    Get-CitrixLogs -start "01/01/2018" -end "31/01/2018" -configChange -outputfile c:\temp\citrix.changes.csv

    Show all configuration changes made between the 1st and 31st of January 2018 and write the results to c:\temp\citrix.changes.csv

    #>

    [CmdletBinding()]

    Param
    (
        [string]$ddc = 'localhost', #localhost when not otherwise specified or running directly on Delivery Controller
        [string]$username,
        [switch]$configChange,
        [switch]$adminAction,
        [switch]$studioOnly,
        [switch]$directorOnly,
        [string]$operation,
        [Parameter(Mandatory=$true, ParameterSetName = "TimeSpan")]
        [datetime]$start,
        [Parameter(Mandatory=$false, ParameterSetName = "TimeSpan")]
        [datetime]$end = [datetime]::Now,
        [Parameter(Mandatory=$true, ParameterSetName = "Last")]
        [string]$last,
        [string]$outputFile,
        [int]$maxRecordCount = 5000,
        [switch]$gridview
    )

    if($studioOnly -and $directorOnly)
    {
        Throw "Cannot specify both -studioOnly and -directorOnly"
    }

    if(![string]::IsNullOrEmpty($last))
    {
        [long]$multiplier = 0
        switch( $last[-1] )
        {
            "s" { $multiplier = 1 }
            "m" { $multiplier = 60 }
            "h" { $multiplier = 3600 }
            "d" { $multiplier = 86400 }
            "w" { $multiplier = 86400 * 7 }
            "y" { $multiplier = 86400 * 365 }
            default { Throw "Unknown multiplier `"$($last[-1])`"" }
        }
        if($last.Length -le 1)
        {
            $start = $end.AddHours(-$multiplier)
        }
        else
        {
            $start = $end.AddSeconds(-(($last.Substring(0, $last.Length - 1) -as [long]) * $multiplier))
        }
    }
    elseif(!$PSBoundParameters['start'])
    {
        $start = (Get-Date).AddDays(-7)
    }

    Add-PSSnapin -Name 'Citrix.ConfigurationLogging.Admin.*'

    if(!(Get-Command -Name 'Get-LogHighLevelOperation' -ErrorAction SilentlyContinue))
    {
        Throw "Unable to find the Citrix Get-LogHighLevelOperation cmdlet required"
    }

    [hashtable]$queryparams = @{
        'AdminAddress' = $primaryDDC
        'SortBy' = '-StartTime'
        'MaxRecordCount' = $maxRecordCount
        'ReturnTotalRecordCount' = $true
    }
    if($configChange -and !$adminAction)
    {
        $queryparams.Add('OperationType', 'ConfigurationChange')
    }
    elseif(!$configChange -and $adminAction)
    {
        $queryparams.Add('OperationType', 'AdminActivity')
    }
    if(![string]::IsNullOrEmpty($username))
    {
        if($username.IndexOf('\') -lt 0)
        {
            $username = $env:USERDOMAIN + '\' + $username
        }
        $queryparams.Add('User', $username)
    }
    if($directorOnly)
    {
        $queryparams.Add('Source', 'Citrix Director')
    }
    if($studioOnly)
    {
        $queryparams.Add('Source', 'Studio')
    }

    $recordCount = $null

     Write-Host "[$(Get-Date)][INFO] Retrieving the specified high level Citrix Studio logs..."
    [array]$results = @( Get-LogHighLevelOperation -Filter { StartTime -ge $start -and EndTime -le $end }  @queryparams -ErrorAction SilentlyContinue -ErrorVariable RecordCount | ForEach-Object -Process `
    {
        if([string]::IsNullOrEmpty($operation) -or $_.Text -match $operation)
        {
            $result = [pscustomobject][ordered]@{
                'Started' = $_.StartTime
                'Duration (s)' = [math]::Round((New-TimeSpan -Start $_.StartTime -End $_.EndTime).TotalSeconds, 2)
                'User' = $_.User
                'From' = $_.AdminMachineIP
                'Operation' = $_.text
                'Source' = $_.Source
                'Type' = $_.OperationType
                'Targets' = $_.TargetTypes -join ','
                'Successful' = $_.IsSuccessful
            }
            if(!$configChange)
            {
                Add-Member -InputObject $result -NotePropertyMembers @{
                    'Target Process' = $_.Parameters[ 'ProcessName' ]
                    'Target Machine' = $_.Parameters[ 'MachineName' ]
                    'Target User' = $_.Parameters[ 'UserName' ]
                }
            }
            $result
        }
    } )

    if($recordCount -and $recordCount.Count)
    {
        if($recordCount[0] -match 'Returned (\d+) of (\d+) items')
        {
            if([int]$matches[1] -lt [int]$matches[2])
            {
                Write-Warning "Only retrieved $($matches[1]) of a total of $($matches[2]) items, use -maxRecordCount to return more"
            }
            ## else we got all the records
        }
        else
        {
            Write-Error $recordCount[0]
        }
    }

    if(!$results -or !$results.Count)
    {
        Write-Warning "No log entries found between $(Get-Date $start -Format G) and $(Get-Date $end -Format G)"
    }
    else
    {
        if(![string]::IsNullOrEmpty($outputFile))
        {
            Write-Host "[$(Get-Date)][INFO] Exporting specified Citrix Studio logs to: $($outputFile)..."
            $results | Export-Csv -Path $outputFile -NoTypeInformation -Force ## Overwrites existing file 
        }
        elseif($gridview)
        {
            $selected = $results | Out-GridView -Title "$($results.Count) events from $(Get-Date $start -Format G) and $(Get-Date $end -Format G)" -PassThru
            if($selected)
            {
                $selected | clip.exe
            }
        }
        else
        {
            $results
        }
    }
}

Function Disconnect-IdleSessions
 {
    <#
    .SYNOPSIS

    Find disconnected sessions disconnected over a specified threshold and terminate them. Can also terminate specified processes in case they are preventing logoff.

    .PARAMETER disconnectedThresholdMins

    The disconnection time in minutes (7200 for McLaren) at which the session will be disconnected

    .PARAMETER ddc

    The Desktop Delivery Controller to query to get disconnected session information

    .PARAMETER forceIt

    Do not prompt for confirmation before terminating stuck processes and disconnecting processes

    .PARAMETER transcriptLog

    A csv file that will be appended to with details of the sessions terminated

    .PARAMETER processesToKill

    A comma separated list of processes to terminate if they are running in the user's session

    .EXAMPLE

    Disconnect-IdleSessions -disconnectedThresholdMins 7200 -ddc ctxddc01 -processesToKill stuckprocess,anotherstuckprocess -transcriptLog c:\blah\TerminatedSessions.csv

    End all disconnected sessions found via delivery controller ctxddc01 which have been disconnected for more than 7200 mins.
    If there are processes called stuckprocess and anotherstuckprocess still running, terminate these and THEN terminate the session itself.
    Results will be appended to the CSV file c:\blah\TerminatedSessions.csv

    #>

    [CmdletBinding(SupportsShouldProcess=$True,ConfirmImpact='High')]

    Param
    (
	    [int]$disconnectedThresholdMins = 7200,
        [string]$ddc = 'localhost',
        [switch]$forceIt,
        [string]$transcriptLog,
        [string[]]$processesToKill
    )

    if($processesToKill -and $processesToKill -contains 'csrss')
    {
        Write-Error 'Kiling csrss causes BSoDs so not continuing'
        return
    }

    if($forceIt)
    {
         $ConfirmPreference = 'None'
    }

    # reform incase flattened by scheduled task engine
    if($processesToKill -and $processesToKill.Count -eq 1 -and $processesToKill.IndexOf(',') -ge 0)
    {
        $processesToKill = $processesToKill -split ','
    }

    $disconnected = @(Get-BrokerSession -AdminAddress $primaryDDC -SessionState 'Disconnected' | Where-Object { $_.SessionStateChangeTime -lt (Get-Date).AddMinutes(-$disconnectedThresholdMins) -or ($_.UserName -match 'Josh.Perry') })
    
    Write-Verbose "Got $($disconnected.Count) disconnected sessions over $disconnectedThresholdMins mins`n$($disconnected | Select-Object username, UntrustedUserName, HostedMachineName, StartTime, SessionStateChangeTime | Format-Table -AutoSize | Out-String)"

    if($disconnected -and $disconnected.Count -gt 0)
    {
        [array]$processes = @()
        if($processesToKill -and $processesToKill.Count)
        {
            ForEach($session in $disconnected)
            {
                [string]$username = $session.Username
                if([string]::IsNullOrEmpty($username))
                {
                    $username = $session.UntrustedUsername
                }
                $username = ($username -split '\\')[-1] ## strip domain name off
                if(![string]::IsNullOrEmpty($session.HostedMachineName) -and ![string]::IsNullOrEmpty($username ))
                {
                    if((quser /server:$($session.HostedMachineName) | Select-Object -skip 1| Where-Object{ $_ -match "[^a-z0-9_]$username\s+(\d+)\s" }))
                    {
                        [int]$sessionId = $Matches[1].Trim()
                        if( $sessionId -gt 0 )
                        {
                            ## have to remote it as doesn't return session ids if run via -ComputerName. Can't check username as may be system processes in that session
                            $processes = @(Invoke-Command -ComputerName $session.HostedMachineName -ScriptBlock { Get-Process -IncludeUserName -Name $using:killProcesses | Where-Object { $_.SessionId -eq $using:sessionId } })
                            if($processes -and $processes.Count)
                            {
                                Add-Member -InputObject $session -MemberType NoteProperty -Name ProcessesKilled -Value (($processes | Select-Object -ExpandProperty Name) -join ',')
                                if($PSCmdlet.ShouldProcess("Session $sessionId for $username on $($session.HostedMachineName)", "Kill processes $(($processes | Select-Object -ExpandProperty Name) -join ',')"))
                                {
                                    Invoke-Command -ComputerName $session.HostedMachineName -ScriptBlock { $using:processes | Stop-Process -Force -PassThru }
                                }
                            }
                            else
                            {
                                Write-Warning "Found no $($processesToKill -join ',') processes to kill in session $sessionId for $username on $($session.HostedMachineName)"
                            }
                        }
                        else
                        {
                            Write-Warning "Failed to get session id via quser for $username on $($session.HostedMachineName)"
                        }
                    }
                    else
                    {
                        Write-Warning "Failed to get session via quser for $username on $($session.HostedMachineName)"
                    }
                }
                else
                {
                    Write-Warning "Couldn't get username `"$username`" or host `"$($session.HostedMachineName)`""
                }
            }
        }
        if($PSCmdlet.ShouldProcess("$($disconnected.Count) disconnected sessions", 'Log off'))
        {
            if(![String]::IsNullOrEmpty($transcriptLog))
            {    
                $disconnected | Select-Object -Property @{n='Sampled';e={Get-Date}},Username,StartTime,UntrustedUserName,SessionStateChangeTime,HostedMachineName,ClientName,ClientAddress,CatalogName,DesktopGroupName,ControllerDNSName,HostingServerName,ProcessesKilled | Export-Csv -Force -NoTypeInformation -Append -Path $transcriptLog
             
            }

            $disconnected | Stop-BrokerSession
        }
    }
}

Function Get-Dom0MemUsage {
    $xenUserName = ""
    $xenPassword = ""
    $allSSHOutputs = @{}
    $formattedSSHOutputs = @()

    $warning = 200
    $caution = 1000

    $securePassword = ConvertTo-SecureString $xenPassword -AsPlainText -Force
    $hostCredential = New-Object System.Management.Automation.PSCredential ($xenUserName, $securePassword)

    $webClient = New-Object System.Net.WebClient
    $webClient.Proxy.Credentials = [System.Net.CredentialCache]::DefaultNetworkCredentials

    Foreach ($entry in $xenServerList)
    {
        try {
        Write-Host "Connecting to $entry..."
            $sshSession = New-SSHSession -ComputerName $entry -Credential $hostCredential -AcceptKey
            Write-Host "Connected to $entry..."

            $sshOutput = $(Invoke-SSHCommand -Index $sshSession.SessionId -Command "free -m").Output
            $allSSHOutputs.Add($entry, $sshOutput)
            Write-Host "SSH command invoked and result stored for $entry"
        } catch {
            Write-Host "Exception occurred during SSH session."
        }

        Get-SSHSession | Remove-SSHSession | Out-Null
    }


    Foreach ($entry in $allSSHOutputs.Keys)
    {
    write-Host "Current: $entry"
        $statusMessage = "N/a"
        $lines = $allSSHOutputs[$entry] -split "\r?\n"
        $line = $lines[1] -split "\s+"
             
        Write-Host "Host: " -ForegroundColor white -NoNewline
        Write-Host $entry -NoNewline
        Write-Host "  Free Memory: " -ForegroundColor white -NoNewline
                               
        If ([int]$line[3] -le $caution -AND [int]$line[3] -gt $warning) {
            Write-Host $line[3] -ForegroundColor Yellow -NoNewline
            Write-Host "M" -ForegroundColor Yellow -NoNewline
            Write-Host "  *CAUTION*" -ForegroundColor Yellow
            $statusMessage = "CAUTION"
        }
        ElseIf ([int]$line[3] -le $warning) {
            Write-Host $line[3] -ForegroundColor red -NoNewline
            Write-Host "M" -ForegroundColor red -NoNewline
            Write-Host "  *WARNING*" -ForegroundColor red
            $statusMessage = "WARNING"
        }
        Else {
            Write-Host $line[3] -NoNewline
            Write-Host "M" -NoNewline
            Write-Host "  *OK*"
            $statusMessage = "OK"
        }
           $formattedSSHOutputs +=  [PSCustomObject] @{
            "Host" = $entry
            "Free memory (mb)" = $line[3]
            "Status" = $statusMessage   
            }
    }

    return $formattedSSHOutputs

}

Function Invoke-InfrastructureChecks {
    Write-Host "[$(Get-Date)][INFO] Getting information from $($primaryDDC)..."
    [array]$machines = @( Get-BrokerMachine -AdminAddress $primaryDDC -MaxRecordCount $maxRecords | Where-Object { $_.MachineName -notmatch $excludedMachines } )
    [array]$users = @( Get-BrokerSession -AdminAddress $primaryDDC -MaxRecordCount $maxRecords  | Where-Object { $_.MachineName -notmatch $excludedMachines } )
    [array]$XenAppDeliveryGroups = @( Get-BrokerDesktopGroup -AdminAddress $primaryDDC -SessionSupport MultiSession )
    [int]$registeredMachines = $machines | Where-Object { $_.RegistrationState -eq 'Registered' } | Measure-Object | Select-Object -ExpandProperty Count

    $emailBody += "`tGot a total of $($machines.Count) machines from $primaryDDC with $(($users | Where-Object { $_.SessionState -eq 'Active' }).Count) users active and $(($users | Where-Object { $_.SessionState -eq 'Disconnected' }).Count) disconnected`n"

    # See what if any app groups are tag restricted and then get number of available tagged machines
    if((Get-Command -Name Get-BrokerTag -ErrorAction SilentlyContinue) `
        -and (Get-Command -Name Get-BrokerApplicationGroup -ErrorAction SilentlyContinue)) ## came later on in 7.x so not necessarily present
    {
        [array]$allApplicationGroups = @(Get-BrokerApplicationGroup -AdminAddress $primaryDDC)
        Get-BrokerTag -AdminAddress $primaryDDC | ForEach-Object `
        {
            $tag = $_
            if(!$excludedTags -or $excludedTags -notcontains $tag.Name)
            {
                ## Now find all app groups restricted by this tag
                $allApplicationGroups | Where-Object { $_.RestrictToTag -eq $tag.Name } | ForEach-Object `
                {
                    $applicationGroup = $_
                    ## now find workers with this tag
                    [int]$taggedMachinesAvailable = $machines | Where-Object { $_.Tags -contains $tag.Name -and $_.InmaintenanceMode -eq $false -and $_.RegistrationState -eq 'Registered' -and $_.WindowsConnectionSetting -eq 'LogonEnabled' -and $_.FaultState -eq 'None' } | Measure-Object | Select-Object -ExpandProperty Count
                    [int]$taggedMachinesTotal = $machines | Where-Object { $_.Tags -contains $tag.Name } | Measure-Object | Select-Object -ExpandProperty Count
                    $null = $taggedApplicationGroups.Add([pscustomobject]@{'Application Group' = $applicationGroup.Name; 'Tag' = $tag.Name; 'Tag Description' = $tag.Description;
                        'Machines available' = $taggedMachinesAvailable; 'Total machines tagged' = $taggedMachinesTotal; 'Percentage Available' = [math]::Round(($taggedMachinesAvailable / $taggedMachinesTotal) * 100 )})
                }
            }
            ## Now check if any delivery groups have desktops which are tag restricted
            $XenAppDeliveryGroups | ForEach-Object `
            {
                $deliveryGroup = $_
                Get-BrokerEntitlementPolicyRule -DesktopGroupUid $deliveryGroup.uid -AdminAddress $primaryDDC -RestrictToTag $tag.Name | ForEach-Object `
                {
                    $desktop = $_
                    [int]$taggedMachinesAvailable = $machines | Where-Object { $_.DesktopGroupName -eq $deliveryGroup.Name -and $_.Tags -contains $tag.Name -and $_.InmaintenanceMode -eq $false -and $_.RegistrationState -eq 'Registered' -and $_.WindowsConnectionSetting -eq 'LogonEnabled' } | Measure-Object | Select-Object -ExpandProperty Count
                    [int]$taggedMachinesTotal = $machines | Where-Object { $_.DesktopGroupName -eq $deliveryGroup.Name -and $_.Tags -contains $tag.Name } | Measure-Object | Select-Object -ExpandProperty Count              
                    $null = $taggedDesktops.Add( [pscustomobject]@{ 'Delivery Group' = $deliveryGroup.Name ; 'Published Desktop' = $desktop.PublishedName ; 'Description' = $desktop.Description ; 'Enabled' = $desktop.Enabled ; 'Tag' = $tag.Name ; 'Tag Description' = $tag.Description ;
                        'Machines available' = $taggedMachinesAvailable ; 'Total machines tagged' = $taggedMachinesTotal ; 'Percentage Available' = [math]::Round( ( $taggedMachinesAvailable / $taggedMachinesTotal ) * 100 ) } )
                }
            }
        }
    }

    $poweredOnUnregistered += @($machines | Where-Object { $( $_.PowerState -eq 'On' ) -and $_.RegistrationState -eq 'Unregistered' -and ! $_.InMaintenanceMode } | Select-Object @{n='Machine Name';e={($_.MachineName -split '\\')[-1]}},DesktopGroupName,CatalogName,InMaintenanceMode )
    $allFailedMachines += @($machines | Where-Object { $_.FaultState -ne 'None' } | Select-Object @{n='Machine Name';e={($_.MachineName -split '\\')[-1]}},DesktopGroupName,CatalogName,FaultState,LastConnectionUser,LastDeregistrationReason,LastDeregistrationTime,SummaryState )
    
    $failedConnections += @($machines | Where-Object { $_.LastConnectionFailure -ne 'None'} | Sort-Object LastConnectionFailure -Descending | Select-Object @{n='User';e={($_.LastConnectionUser -split '\\')[-1]}},LastConnectionTime,LastConnectionFailure,DesktopGroupName,CatalogName,SummaryState )
    
    $notPoweredOn += @($machines | Where-Object { $($_.PowerState -eq 'Off' ) } | Select-Object @{n='Machine Name';e={($_.MachineName -split '\\')[-1]}},DesktopGroupName,CatalogName,InMaintenanceMode )
    
    [int]$inMaintenanceMode = $machines | Where-Object { $_.InMaintenanceMode -eq $true } | Measure-Object | Select-Object -ExpandProperty Count
    [int]$inMaintenanceModeAndOn = $machines | Where-Object { $_.InMaintenanceMode -eq $true -and $($_.PowerState -eq 'On') } | Measure-Object | Select-Object -ExpandProperty Count

    ## Retrieve all ongoing hypervisor connections, grab the Uid, then use this to grab alerts from each hypervisor 
    <#$hypervisorConnections = Get-BrokerHypervisorConnection
    $hypervisorGuids = @($hypervisorConnections | Select-Object -Property Uid)
    $hypervisorAlerts += ForEach ($uid in $hypervisorGuids) {
        Get-BrokerHypervisorAlert -Uid
    } 

    Write-Host "Hypervisor alerts: $hypervisorAlerts"#>

    # Restart all failed machines now that they have been stored in $allFailedMachines/reported on
    #Write-Host "[$(Get-Date)][INFO] Attempting to restart all failed VMs..."

    ## Create a queue of containing the names of the machines to be restarted as New-BrokerHostingPowerAction only takes hostnames
    ## TODO: test this
    #$machineNames += @($machines | Select-Object @{n='Machine Name';e={($_.MachineName -split '\\')[-1]}})
    #ForEach ($machine in $machineNames) {
    #    Write-Host "Machine name: " $machine
    #}

    #$failedMachinesRestartQueue += @($allFailedMachines | Select-Object -Property MachineName)
    #$failedMachinesRestartQueue | New-BrokerHostingPowerAction -Action Restart


    $emailBody += "`t$inMaintenanceMode machines are in maintenance mode ($($inMaintenanceModeAndOn) of which are powered on)`n"
    $emailBody += "`t$registeredMachines registered machines, out of a total of $($machines.Count)`n"
    $emailBody += "`t$($poweredOnUnregistered.Count) machines are powered on but unregistered ($([math]::round(( $poweredOnUnregistered.Count / $machines.Count) * 100))%)`n"
    $emailBody += "`t$($notPoweredOn.Count) machines are not powered on ($([math]::round(($notPoweredOn.Count / $machines.Count) * 100))%)`n"

    ## Find sessions and users disconnected more than certain number of minutes
    if($disconnectedMinutes) {
        $longDisconnectedUsers += @( $users | Where-Object { $_.SessionState -eq 'Disconnected' -and $_.SessionStateChangeTime -lt (Get-Date).AddMinutes( -$disconnectedMinutes ) } | Select-Object UserName,UntrustedUserName,@{n='Machine Name';e={($_.MachineName -split '\\')[-1]}},StartTime,SessionStateChangeTime,IdleDuration,DesktopGroupName )
        $emailBody += "`t$($longDisconnectedUsers.Count) users have been disconnected over 5 days (any terminated sessions can be found in TerminatedSessions.csv attached)`n" #$disconnectedMinutes
    }

    ## Retrieve delivery group stats - separate for VDI and XenApp as we are interested in subtly different things
    $deliveryGroupStatsVDI += Get-BrokerDesktopGroup -AdminAddress $primaryDDC -SessionSupport SingleSession | Sort-Object PublishedName | Select-Object @{'n'='Delivery Controller';'e'={$primaryDDC}},PublishedName,Enabled,InMaintenanceMode,DesktopsAvailable,DesktopsDisconnected,DesktopsInUse,@{n='% available';e={[math]::Round( $_.DesktopsAvailable / ($_.DesktopsAvailable + $_.DesktopsDisconnected + $_.DesktopsInUse) * 100 )}},DesktopsPreparing,DesktopsUnregistered | Where-Object { $deliveryGroupsToCheck -match $_.PublishedName }
    $deliveryGroupStatsXenApp += $XenAppDeliveryGroups | ForEach-Object `
    {
        $deliveryGroup = $_.Name
        [string]$rebootState = $null
        [string]$lastRebootsEnded = $null
        Get-BrokerRebootCycle -DesktopGroupName $deliveryGroup -AdminAddress $primaryDDC | Sort-Object -Property StartTime -Descending | Select-Object -First 1 | ForEach-Object `
        {
            if(![string]::IsNullOrEmpty($rebootState))
            {
                $rebootState += ','
            }
            $rebootState += $_.State.ToString()
            if(![string]::IsNullOrEmpty($lastRebootsEnded))
            {
                $lastRebootsEnded += ','
            }
            if($_.EndTime)
            {
                $lastRebootsEnded += (Get-Date $_.EndTime -Format G).ToString()
            }
        }
        if([string]::IsNullOrEmpty($rebootState))
        {
            $rebootState = 'No schedule'
        }
        [int]$availableServers = ($machines | Where-Object { $_.DesktopGroupName -eq $deliveryGroup -and $_.RegistrationState -eq 'Registered' -and $_.InMaintenanceMode -eq $false -and $_.WindowsConnectionSetting -eq 'LogonEnabled' } | Measure-Object).Count
        Select-Object -InputObject $_ -Property @{'n'='Delivery Controller';'e'={$primaryDDC}},PublishedName,Description,Enabled,InMaintenanceMode,
            @{n='Available Servers';e={$availableServers}},
            @{n='Total Servers';e={$_.TotalDesktops}},
            @{n='% machines available';e={[math]::Round(($availableServers/$_.TotalDesktops) * 100 )}},
            @{n='Total Sessions';e={$_.Sessions}},
            @{n='Disconnected Sessions';e={$_.DesktopsDisconnected}},
            TotalApplications,TotalApplicationGroups,
            @{n='Restart State';e={$rebootState}},
            @{n='Restarts Ended';e={$lastRebootsEnded}}
    }

    ## only do this for XenApp as doesn't make sense for single user OS in VDI
    if($XenAppDeliveryGroups -and $XenAppDeliveryGroups.Count)
    {
        [array]$highestUserCounts = @($machines | Where-Object { $_.SessionCount -gt 1 } |Sort-Object SessionCount -Descending | Select-Object -First $topCount -Property @{n='Machine Name';e={($_.MachineName -split '\\')[-1]}},SessionCount,DesktopGroupName,@{n='Tags';e={$_.Tags -join ', '}})
        if($highestUserCounts.Count)
        {
            $highestUsedMachines += $highestUserCounts
            $emailBody += "`tHighest number of concurrent users is $($highestUserCounts[0].SessionCount) on $($highestUserCounts[0].'Machine Name')`n"
        }

        [array]$highestLoadIndices = @($machines | Where-Object { $_.InMaintenanceMode -eq $false -and $_.LoadIndex -ge 1} | Sort-Object LoadIndex -Descending | Select-Object -First $topCount -Property @{n='Machine Name';e={($_.MachineName -split '\\')[-1]}},SessionCount,LoadIndex,@{n='Load Indexes';e={$_.LoadIndexes -join ','}},DesktopGroupName,@{n='Tags';e={$_.Tags -join ', '}})
        if($highestLoadIndices.Count)
        {
            $highestLoadIndexes += $highestLoadIndices
            $emailBody += "`tHighest load index is $($highestLoadIndices[0].LoadIndex) on $($highestLoadIndices[0].'Machine Name') with $($highestLoadIndices[0].SessionCount) sessions`n"
        }
    }

    $checkedURLs += ForEach($url in $urlsToCheck)
    {
        Test-InternalURL -url $url
    }

    $fileShares = ForEach($UNC in $UNCs)
    {
        Write-Host "[$(Get-Date)][INFO] Calculating free space on $($UNC)..."
        Get-DiskFreeSpace "$($UNC)" -unit tb 
    }

    $dom0MemResults = Get-Dom0MemUsage

    ## Display the number of URLs that responded with 200 OK and the number of those that didn't
    $onlineURLs = $onlineURLs | Where-Object { $_ }
    $potentiallyOfflineURLs = $potentiallyOfflineURLs | Where-Object { $_ }

    if ($onlineURLs.Count -eq 0) {
        $emailBody += "`t0 URLs online out of $($urlsToCheck.Count) that were checked`n"
        } else {
            $emailBody += "`t$($onlineURLs.Count) URL(s) online out of $($urlsToCheck.Count) that were checked`n"
        }

    if ($potentiallyOfflineURLs.Count -eq 0) {
        $emailBody += "`t0 URLs are potentially offline (did NOT respond with 200 OK)`n"
        } else {
            $emailBody += "`t$($potentiallyOfflineURLs.Count) URL(s) are potentially offline (did NOT respond with 200 OK)`n"
        }
    
    $emailBody += "`tThere are currently $($noOfConcurrentLicenses) Concurrent licenses in use and 0 User/Device licenses in use`n"
    $emailBody += "`tCitrix Studio logs from the past 24 hours can be found in CitrixStudioLogs.csv attached`n"
    $emailBody += "`t$($allFailedMachines.Count) machines have failed, of which, $($failedMachinesRestartQueue.Count) have been added to the restart queue`n"
    $emailBody += "`tThere have been $($failedConnections.Count) failed user connections reported on $primaryDDC"
    
    $sites += Get-BrokerSite -AdminAddress $primaryDDC | Select-Object Name,@{'n'='Delivery Controller';'e'={$primaryDDC}},PeakConcurrentLicenseUsers,TotalUniqueLicenseUsers,LicensingGracePeriodActive,LicensingOutOfBoxGracePeriodActive,LicensingGraceHoursLeft,LicensedSessionsActiv

    Get-CitrixLogs -ddc $primaryDDC -last 24h -outputfile $citrixStudioLog

    # Begin the process of preparing the email report
    if($emailTo -and $emailTo.Count -and ![string]::IsNullOrEmpty($mailServer))
    {
        if($emailTo.Count -eq 1 -and $emailTo[0].IndexOf(',') -ge 0)
        {
            $emailTo = $emailTo[0] -split ','
        }
    
        if(![string]::IsNullOrEmpty($emailQualifier))
        {
            $emailSubject = $emailQualifier + ' ' + $emailSubject + ' ' + $(Get-Date -Format F)
        }

        [string]$style = "<style>BODY{font-family: Arial; font-size: 10pt;}"
        $style += "TABLE{border: 1px solid black; border-collapse: collapse;}"
        $style += "TH{border: 1px solid black; background: #dddddd; padding: 5px;}"
        $style += "TD{border: 1px solid black; padding: 5px;}"
        $style += "</style>"

        $htmlBody = "<center><img src='cid:logo1.png'><br><img src='cid:logo.png'>"

        ## ConvertTo-Html only works for objects, not raw text
        [string]$htmlBody += "<h2>Summary</h2>`n" + $emailBody -split "`n" | ForEach-Object { "<p>$($_ -replace '\t' , '&nbsp;&nbsp;&nbsp;&nbsp;')</p>`n" }

        $htmlBody += $sites | ConvertTo-Html -Fragment -PreContent '<h2>Site Information<h2>'| Out-String

        if($deliveryGroupStatsVDI -and $deliveryGroupStatsVDI.Count)
        {
            $htmlBody += $deliveryGroupStatsVDI | ConvertTo-Html -Fragment -PreContent "<h2>Summary of $($deliveryGroupStatsVDI.Count) VDI Delivery Groups<h2>" | Out-String
        }

        if($taggedApplicationGroups -and $taggedApplicationGroups.Count)
        {
            $htmlBody += $taggedApplicationGroups | Sort-Object Tag  | ConvertTo-Html -Fragment -PreContent "<h2>$($taggedApplicationGroups.Count) tag restricted application groups<h2>" | Out-String
        }

        if($taggedDesktops -and $taggedDesktops.Count)
        {
            $htmlBody += $taggedDesktops | Sort-Object Tag  | ConvertTo-Html -Fragment -PreContent "<h2>$($taggedDesktops.Count) tag restricted published desktops<h2>" | Out-String
        }

        if($poweredOnUnregistered -and $poweredOnUnregistered.Count -gt 0)
        {
            $htmlBody += $poweredOnUnregistered | Sort-Object DesktopGroupName| ConvertTo-Html -Fragment -PreContent "<h2>$($poweredOnUnregistered.Count) machine(s) powered on and unregistered<h2>" | Out-String
        }

        if($highestUsedMachines -and $highestUsedMachines.Count -gt 0)
        {
            $htmlBody += $highestUsedMachines | Sort-Object SessionCount -Descending| ConvertTo-Html -Fragment -PreContent "<h2>Machines with highest number of users (SessionCount > 1)<h2>" | Out-String
        }

        if($highestLoadIndexes -and $highestLoadIndexes.Count -gt 0)
        {
            $htmlBody += $highestLoadIndexes | Sort-Object LoadIndex -Descending| ConvertTo-Html -Fragment -PreContent "<h2>Machines with highest load indexes (LoadIndex > 1)<h2>" | Out-String
        }

        if($fileShares -and $fileShares.Count -gt 0)
        {
            $htmlBody += $fileShares  | Sort-Object 'Percentage Free Space' | ConvertTo-Html -Fragment -PreContent "<h2>Fileshare Statistics<h2>" | Out-String
        }

        if($allFailedMachines -and $allFailedMachines.Count -gt 0)
        {
            $htmlBody += $allFailedMachines | ConvertTo-Html -Fragment -PreContent "<h2>Failed Machines<h2>" | Out-String
             
        }

        if($failedConnections -and $failedConnections.Count -gt 0)
        {
            $htmlBody += $failedConnections | ConvertTo-Html -Fragment -PreContent "<h2>Failed User Connections<h2>" | Out-String
        }

        $htmlBody += $checkedURLs  | ConvertTo-Html -Fragment -PreContent "<h2>Internal URL Statuses<h2>" | Out-String

        $htmlBody += $dom0MemResults  | ConvertTo-Html -Fragment -PreContent "<h2>Control Domain (Dom0) Memory Usage Stats<h2>" | Out-String

        ## Convert the post content (i.e. anything after the headers specified above) to HTML and export report to path specified in config file 
        $htmlBody = ConvertTo-Html -PostContent $htmlBody -Head $style
        Write-Host "[$(Get-Date)][INFO] Exporting HTML e-mail report to: $($reportOutputPath)"
        $htmlBody | Out-File -FilePath $reportOutputPath; 

        ## Check whether the e-mail attachments actually exist, as Send-MailMessage errors out when trying to send a file that doesn't exist 
        ## This can occur when there haven't been any logs in Citrix Studio in the past 24 hours (i.e. over the weekend) 
        [bool]$citrixStudioLogExists = $False
        [bool]$terminatedSessionsLogExists = $False
        [bool]$attachmentsExist = $False

        if (Test-Path -LiteralPath $citrixStudioLog) {
            $citrixStudioLogExists = $true
            Write-Host "[$(Get-Date)][INFO] Citrix Studio log file exists. Sending as attachment on e-mail report."
        }
        else {
            $citrixStudioLogExists = $false
            Write-Host "[$(Get-Date)][INFO] Citrix Studio log file does not exist. Will proceed to send e-mail report without logs attached."
        }

        if (Test-Path -LiteralPath $terminatedSessionsLog) {
            $terminatedSessionsLogExists = $true
            Write-Host "[$(Get-Date)][INFO] Terminated sessions log file exists. Sending as attachment on e-mail report."
        }
        else {
            $terminatedSessionsLogExists = $false
            Write-Host "[$(Get-Date)][INFO] Terminated sessions log file does not exist. Will proceed to send e-mail report without logs attached."
        }

        # In this scenario, both log files would be sent (including the logo file)
        if ($citrixStudioLogExists -and $terminatedSessionsLogExists) {
            $attachmentsExist = $true
        }

        ## Send the e-mail report to the addreses specified in the configuration file
        if ($automaticallySendReport) {
            Write-Host "[$(Get-Date)][INFO] Sending e-mail report to $($emailTo)..."
            if ($attachmentsExist) {
                Send-MailMessage -Subject $emailSubject -BodyAsHtml -Body $htmlBody -From $env:Computername@domain.com -To $emailTo -SmtpServer $mailserver -Attachments $logo1Dir, $logo2Dir, $citrixStudioLog, $terminatedSessionsLog -Priority High               
            }  
            if ($citrixStudioLogExists -and !$terminatedSessionsLogExists) {
                Send-MailMessage -Subject $emailSubject -BodyAsHtml -Body $htmlBody -From $env:Computername@domain.com -To $emailTo -SmtpServer $mailserver -Attachments $logo1Dir, $logo2Dir, $citrixStudioLog -Priority High
            }
            if ($terminatedSessionsLogExists -and !$citrixStudioLogExists) {
                Send-MailMessage -Subject $emailSubject -BodyAsHtml -Body $htmlBody -From $env:Computername@domain.com -To $emailTo -SmtpServer $mailserver -Attachments $logo1Dir, $logo2Dir, $terminatedSessionsLog -Priority High
            }
        }  
    }
}

Function Stop-Script {
    ## Originally caused script to crash as it tried to stop the transript even if it wasn't transcripting
    if(![string]::IsNullOrEmpty($transcriptLog))
    {
            Try {
                $scriptEnd = Get-Date
                $scriptRuntime =  $scriptEnd - $scriptStart | Select-Object TotalSeconds
                $scriptRuntimeInSeconds = $scriptRuntime.TotalSeconds
                Write-Host "[$(Get-Date)][INFO] Script was running for $($scriptRuntimeInSeconds) seconds."
            
                Write-Host "[$(Get-Date)][INFO] Stopping script transcript..."
            
                Stop-Transcript
            } catch {
                Write-Host "[$(Get-Date)][ERROR] An error occurred when trying to stop script transcript."
        }
    }
}


#===================================================
#               SCRIPT ENTRY POINT
#===================================================
main





