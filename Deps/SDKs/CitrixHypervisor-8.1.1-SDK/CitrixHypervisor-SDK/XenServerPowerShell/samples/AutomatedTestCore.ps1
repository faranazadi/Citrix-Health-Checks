#
# Copyright (c) Citrix Systems, Inc.
# All rights reserved.
# 
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions
# are met:
# 
#   1) Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
# 
#   2) Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials
#      provided with the distribution.
# 
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
# "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
# LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
# FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
# COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
# INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
# (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
# SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
# HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
# STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
# OF THE POSSIBILITY OF SUCH DAMAGE.
#

# Powershell Automated Tests

Param([Parameter(Mandatory=$true)][String]$out_xml,
       [Parameter(Mandatory=$true)][String]$svr,
       [Parameter(Mandatory=$true)][String]$usr,
       [Parameter(Mandatory=$true)][String]$pwd,
       [Parameter(Mandatory=$true)][String]$sr_svr,
       [Parameter(Mandatory=$true)][String]$sr_path)

# Initial Setup

[Net.ServicePointManager]::SecurityProtocol='tls,tls11,tls12'
$BestEffort = $false
$NoWarnCertificates = $true
$info = $true
$warn = $true
$err = $true
$prog = $false

$Eap = $ErrorActionPreference
$Vp = $VerbosePreference
$Wp = $WarningPreference
$Ep = $ErrorPreference

$ErrorActionPreference = "Stop"
$VerbosePreference="Continue"
$WarningPreference="Continue"
$ErrorPreference="Continue"
$ErrorVariable

# End Initial Setup

# Helper Functions

function log_info([String]$msg)
{
  process
  {
    if($info)
	{
	  write-verbose $msg
	}
  }
}

function log_warn([String]$msg)
{
  process
  {
    if($warn)
	{
      write-warning $msg
	}
  }
}

function log_error([String]$msg)
{
  process
  {
    if($err) 
	{
      write-error $msg
	}
  }
}

function escape_for_xml([String]$content)
{
  return $content.replace("&", "&amp;").replace("'", "&apos;").replace('"', "&quot;").replace("<", "&lt;").replace(">", "&gt;")
}

function prep_xml_output([String]$out_file)
{
  $date = Get-Date
  "<results>" > $out_file
  ("<testrun>Test Run Info: PowerShell bindings test {0}</testrun>" -f $date) >> $out_file
  "<group>" >> $out_file
}

function close_xml_output([String]$out_file)
{
  "</group>" >> $out_file
  "</results>" >> $out_file
}

function add_result([String]$out_file,[String]$cmd, [String]$test_name, [Exception]$err)
{
  $out_cmd = escape_for_xml $cmd
  $out_test_name = escape_for_xml $test_name
  $out_err = escape_for_xml $err
  "<test>" >> $out_file
  ("<name>{0}</name>" -f $out_test_name) >> $out_file
  if (($err -ne $null))
  {
    "<state>Fail</state>" >> $out_file
	"<log>" >> $out_file
    ("Cmd: '{0}'" -f $out_cmd) >> $out_file
	("Exception: {0}" -f $out_err) >> $out_file
	"</log>" >> $out_file
  }
  else
  {
    "<state>Pass</state>" >> $out_file
    "<log />" >> $out_file
  }
  "</test>" >> $out_file
}


function exec([String]$test_name, [String]$cmd, [String]$expected)
{
  trap [Exception]
  {
     add_result $out_xml $cmd $test $_.Exception
	 $fails.Add($test_name, $_.Exception)
	 break
  }
  
  log_info ("Test '{0}' Started: cmd = {1}, expected = {2}" -f $test_name,$cmd,$expected)
  $result = Invoke-Expression $cmd
  if ($result -eq $expected)
  {
    add_result $out_xml $cmd $test_name $null
	return $true
  }
  else
  {
    $exc = new-object Exception("Test '{0}' Failed: expected '{1}'; actual '{2}'" `
                                -f $test_name,$expected,$result)
    add_result $out_xml $cmd $test_name $exc
	$fails.Add($test_name, $exc)
	return $false
  }
}

# End Helper Functions

# Connect Functions

function connect_server([String]$svr, [String]$usr, [String]$pwd)
{
  log_info ("connecting to server '{0}'" -f $svr)
  $session = Connect-XenServer -Server $svr -UserName $usr -Password $pwd -PassThru

  if($session -eq $null)
  {
    return $false
  }
  return $true

}

function disconnect_server([String]$svr)
{
  log_info ("disconnecting from server '{0}'" -f $svr)
  Get-XenSession -Server $svr | Disconnect-XenServer

  if ((Get-XenSession -Server $svr) -eq $null)
  {
    return $true
  }
  return $false
}

# End Connect Functions

# VM Functions

function destroy_vm([XenAPI.VM]$vm)
{
  if ($vm -eq $null)
  {
    return
  }

  log_info ("destroying vm '{0}'" -f $vm.name_label)
  
  $vdis = @()
  
  foreach($vbd in $vm.VBDs)
  {
	if((Get-XenVBDProperty -Ref $vbd -XenProperty Mode) -eq [XenAPI.vbd_mode]::RW)
	{
      $vdis += Get-XenVBDProperty -Ref $vbd -XenProperty VDI
	}
  }
  
  Remove-XenVM -VM $vm -Async -PassThru | Wait-XenTask -ShowProgress
  
  foreach($vdi in $vdis)
  {
    Remove-XenVDI -VDI $vdi -Async -PassThru | Wait-XenTask -ShowProgress
  }
}

function install_vm([String]$name, [String]$sr_name)
{
  trap [Exception]
  {
  	trap [Exception]
	{
	  log_warn "Clean up after failed vm install unsuccessful"
	  log_info "...failed!"
	  break
	}

    log_info "Attempting to clean up after failed vm install..."

	$vms = Get-XenVM -Name $name

	foreach($vm in $vms)
  	{
  	  destroy_vm($vm)
  	}
	log_info "...success."
	break
  }

  #find a windows template
  log_info "looking for a Windows template..."
  $template = @(Get-XenVM -Name 'Windows *' | where {$_.is_a_template})[0]

  log_info ("installing vm '{0}' from template '{1}'" -f $template.name_label,$name)
  
  #clone template
  log_info ("cloning vm '{0}' to '{1}'" -f $template.name_label,$name)
  Invoke-XenVM -VM $template -XenAction Clone -NewName $name -Async `
                     -PassThru | Wait-XenTask -ShowProgress
  
  $vm = Get-XenVM -Name $name  
  $sr = Get-XenSR -Name $sr_name
  $other_config = $vm.other_config
  $other_config["disks"] = $other_config["disks"].Replace('sr=""', 'sr="{0}"' -f $sr.uuid)
  
  #add cd drive
  log_info ("creating cd drive for vm '{0}'" -f $vm.name_label)
  New-XenVBD -VM $vm -VDI $null -Userdevice 3 -Bootable $false -Mode RO `
             -Type CD -Unpluggable $true -Empty $true -OtherConfig @{} `
             -QosAlgorithmType "" -QosAlgorithmParams @{}

  Set-XenVM -VM $vm -OtherConfig $other_config
  
  #provision vm 
  log_info ("provisioning vm '{0}'" -f $vm.name_label)
  Invoke-XenVM -VM $vm -XenAction Provision -Async -PassThru | Wait-XenTask -ShowProgress
  
  return $true
}

function uninstall_vm([String]$name)
{
  log_info ("uninstalling vm '{0}'" -f $name)
   
  $vms = Get-XenVM -Name $name
  
  foreach($vm in $vms)
  {
    destroy_vm($vm)
  }
  
  return $true
}

function vm_can_boot($vm_name, [XenApi.Host[]] $servers)
{
  trap [Exception]
  {
    $script:exceptions += $_.Exception
	continue
  }

  $script:exceptions = @()
  foreach ($server in $servers)
  {
    Invoke-XenVM -Name $vm_name -XenAction AssertCanBootHere -XenHost $server
  }
  
  if ($exceptions.Length -lt $servers.Length)
  {
  	return $true
  }
  
  log_info "No suitable place to boot VM:"
  
  foreach ($excep in $script:exceptions)
  {
  	log_info ("Reason: {0}" -f $excep.Message)
  }

  return $false
}

function start_vm([String]$vm_name)
{
  if (vm_can_boot $vm_name @(Get-XenHost))
  {
  	log_info ("starting vm '{0}'" -f $vm_name)
  }

  # even if we cant start it, attempt so we get the exception, reasons have been logged in vm_can_boot
  Invoke-XenVM -Name $vm_name -XenAction Start -Async -PassThru | Wait-XenTask -ShowProgress
  return Get-XenVM -Name $vm_name | Get-XenVMProperty -XenProperty PowerState
}

function shutdown_vm([String]$vm_name)
{
  log_info ("shutting down vm '{0}'" -f $vm_name)
  Invoke-XenVM -Name $vm_name -XenAction HardShutdown -Async -PassThru | Wait-XenTask -ShowProgress
  return (Get-XenVM -Name $vm_name).power_state
}

# End VM Functions

# Host Functions

function get_master()
{
  $pool = Get-XenPool
  return Get-XenHost -Ref $pool.master
}

# End Host Functions

# SR Functions

function get_default_sr()
{
  log_info ("getting default sr")
  $pool = Get-XenPool
  return (Get-XenPool).default_SR | Get-XenSR 
}

function create_nfs_sr([String]$sr_svr, [String]$sr_path, [String]$sr_name)
{
  log_info ("creating sr {0} at {1}:{2}" -f $sr_name,$sr_svr,$sr_path)
  $master = get_master
  $sr_opq = New-XenSR -XenHost $master -DeviceConfig @{ "server"=$sr_svr; "serverpath"=$sr_path; "options"="" } `
                  -PhysicalSize 0 -NameLabel $sr_name -NameDescription "" -Type "nfs" -ContentType "" `
                  -Shared $true -SmConfig @{} -Async -PassThru `
        | Wait-XenTask -ShowProgress -PassThru

  if ($sr_opq -eq $null)
  {
    return $false
  }
  return $true
}

function detach_nfs_sr([String]$sr_name)
{
  log_info ("destroying sr {0}" -f $sr_name)

  $pbds = Get-XenPBD
  $sr_opq = (Get-XenSR -Name $sr_name).opaque_ref

  foreach($pbd in $pbds)
  {
    if(($pbd.SR.opaque_ref -eq $sr_opq) -and $pbd.currently_attached)
    {
      Invoke-XenPBD -PBD $pbd -XenAction Unplug
    }
  }
  
  $sr_opq = Remove-XenSR -Name $sr_name -Async -PassThru | Wait-XenTask -ShowProgress
 
  if ($sr_opq -eq $null)
  {
    return $true
  }
  return $false
}

# End SR Functions

# Helper Functions

function append_random_string_to([String]$toAppend, $length = 10)
{
	$randomisedString = $toAppend
	$charSet = "0123456789abcdefghijklmnopqrstuvwxyz".ToCharArray()
	for($i; $i -le $length; $i++)
	{
		$randomisedString += $charSet | Get-Random
	}
	return $randomisedString
}

# End Helper Functions

# Test List

$tests = @(
            @("Connect Server", "connect_server $svr $usr $pwd", $true),
			@("Create SR", "create_nfs_sr $sr_svr $sr_path PowerShellAutoTestSR", $true),
            @("Install VM", "install_vm PowerShellAutoTestVM PowerShellAutoTestSR", $true),
			@("Start VM", "start_vm PowerShellAutoTestVM", "Running"),
			@("Shutdown VM", "shutdown_vm PowerShellAutoTestVM", "Halted"),
			@("Uninstall VM", "uninstall_vm 'PowerShellAutoTestVM'", $true),
			@("Destroy SR", "detach_nfs_sr PowerShellAutoTestSR", $true),
            @("Disconnect Server", "disconnect_server $svr", $true)
          )
# End Test List

# Main Test Execution
$complete = 0;
$max = $tests.Count;

$fails = @{}

prep_xml_output $out_xml

$vmName = append_random_string_to "PowerShellAutoTestVM"
$srName = append_random_string_to "PowerShellAutoTestSR"

foreach($test in $tests)
{
  trap [Exception]
  {
    # we encountered an exception in running the test before it completed
	# its already been logged, so continue
	continue
  }
  $success = $false
  
  # Add randomness to the names of the test VM and SR to 
  # allow a parallel execution context
  $cmd = $test[1]
  $cmd = $cmd -replace "PowerShellAutoTestVM", $vmName
  $cmd = $cmd -replace "PowerShellAutoTestSR", $srName
  
  $success = exec $test[0] $cmd $test[2]
  if ($success)
  {
    $complete++
  }
}

close_xml_output $out_xml

$result = "Result: {0} completed out of {1}" -f $complete,$max;

write-host $result -f 2

if($fails.Count -gt 0)
{
  write-host "Failures:"
  $fails
}

$ErrorActionPreference = $Eap
$VerbosePreference = $Vp
$WarningPreference = $Wp
$ErrorPreference = $Ep

# End Main Test Execution

# SIG # Begin signature block
# MIIbhwYJKoZIhvcNAQcCoIIbeDCCG3QCAQExCzAJBgUrDgMCGgUAMGkGCisGAQQB
# gjcCAQSgWzBZMDQGCisGAQQBgjcCAR4wJgIDAQAABBAfzDtgWUsITrck0sYpfvNR
# AgEAAgEAAgEAAgEAAgEAMCEwCQYFKw4DAhoFAAQU34VeNVb/xmFM6WY+Kt+0p8At
# m/qgggqWMIIFMDCCBBigAwIBAgIQBAkYG1/Vu2Z1U0O1b5VQCDANBgkqhkiG9w0B
# AQsFADBlMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYD
# VQQLExB3d3cuZGlnaWNlcnQuY29tMSQwIgYDVQQDExtEaWdpQ2VydCBBc3N1cmVk
# IElEIFJvb3QgQ0EwHhcNMTMxMDIyMTIwMDAwWhcNMjgxMDIyMTIwMDAwWjByMQsw
# CQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3d3cu
# ZGlnaWNlcnQuY29tMTEwLwYDVQQDEyhEaWdpQ2VydCBTSEEyIEFzc3VyZWQgSUQg
# Q29kZSBTaWduaW5nIENBMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA
# +NOzHH8OEa9ndwfTCzFJGc/Q+0WZsTrbRPV/5aid2zLXcep2nQUut4/6kkPApfmJ
# 1DcZ17aq8JyGpdglrA55KDp+6dFn08b7KSfH03sjlOSRI5aQd4L5oYQjZhJUM1B0
# sSgmuyRpwsJS8hRniolF1C2ho+mILCCVrhxKhwjfDPXiTWAYvqrEsq5wMWYzcT6s
# cKKrzn/pfMuSoeU7MRzP6vIK5Fe7SrXpdOYr/mzLfnQ5Ng2Q7+S1TqSp6moKq4Tz
# rGdOtcT3jNEgJSPrCGQ+UpbB8g8S9MWOD8Gi6CxR93O8vYWxYoNzQYIH5DiLanMg
# 0A9kczyen6Yzqf0Z3yWT0QIDAQABo4IBzTCCAckwEgYDVR0TAQH/BAgwBgEB/wIB
# ADAOBgNVHQ8BAf8EBAMCAYYwEwYDVR0lBAwwCgYIKwYBBQUHAwMweQYIKwYBBQUH
# AQEEbTBrMCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20wQwYI
# KwYBBQUHMAKGN2h0dHA6Ly9jYWNlcnRzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEFz
# c3VyZWRJRFJvb3RDQS5jcnQwgYEGA1UdHwR6MHgwOqA4oDaGNGh0dHA6Ly9jcmw0
# LmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEFzc3VyZWRJRFJvb3RDQS5jcmwwOqA4oDaG
# NGh0dHA6Ly9jcmwzLmRpZ2ljZXJ0LmNvbS9EaWdpQ2VydEFzc3VyZWRJRFJvb3RD
# QS5jcmwwTwYDVR0gBEgwRjA4BgpghkgBhv1sAAIEMCowKAYIKwYBBQUHAgEWHGh0
# dHBzOi8vd3d3LmRpZ2ljZXJ0LmNvbS9DUFMwCgYIYIZIAYb9bAMwHQYDVR0OBBYE
# FFrEuXsqCqOl6nEDwGD5LfZldQ5YMB8GA1UdIwQYMBaAFEXroq/0ksuCMS1Ri6en
# IZ3zbcgPMA0GCSqGSIb3DQEBCwUAA4IBAQA+7A1aJLPzItEVyCx8JSl2qB1dHC06
# GsTvMGHXfgtg/cM9D8Svi/3vKt8gVTew4fbRknUPUbRupY5a4l4kgU4QpO4/cY5j
# DhNLrddfRHnzNhQGivecRk5c/5CxGwcOkRX7uq+1UcKNJK4kxscnKqEpKBo6cSgC
# PC6Ro8AlEeKcFEehemhor5unXCBc2XGxDI+7qPjFEmifz0DLQESlE/DmZAwlCEIy
# sjaKJAL+L3J+HNdJRZboWR3p+nRka7LrZkPas7CM1ekN3fYBIM6ZMWM9CBoYs4Gb
# T8aTEAb8B4H6i9r5gkn3Ym6hU/oSlBiFLpKR6mhsRDKyZqHnGKSaZFHvMIIFXjCC
# BEagAwIBAgIQBxz06r0ZFs937nDbPhFuhDANBgkqhkiG9w0BAQsFADByMQswCQYD
# VQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3d3cuZGln
# aWNlcnQuY29tMTEwLwYDVQQDEyhEaWdpQ2VydCBTSEEyIEFzc3VyZWQgSUQgQ29k
# ZSBTaWduaW5nIENBMB4XDTIwMDEwNjAwMDAwMFoXDTIyMDExMDEyMDAwMFowgZox
# CzAJBgNVBAYTAlVTMRAwDgYDVQQIEwdGbG9yaWRhMRgwFgYDVQQHEw9Gb3J0IExh
# dWRlcmRhbGUxHTAbBgNVBAoTFENpdHJpeCBTeXN0ZW1zLCBJbmMuMSEwHwYDVQQL
# ExhYZW5TZXJ2ZXIoR2VuZXJhbFNIQTI1NikxHTAbBgNVBAMTFENpdHJpeCBTeXN0
# ZW1zLCBJbmMuMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6gkkxi4C
# fst1yCchBQVm4ecCtjJBkKDuExSURsojYkLqxlBEb8E438Dyr/9zRcx9TIvxx9Ms
# ynwooQoUZgcCBWWJNn6/6ERtQHD0uOBojpZruOXu06bvbGCh5eKZFlajeenKFokX
# Wn7iEafQ+uHBtQr8sq19fQVlThi8GXcrJ8YPo2N9Xb3bGEabFOtdaJfct502jnlv
# nfqVhxF2RvZEZl+1B9kGonKQQSUPoitkmeuowgo8eC8tHLuE0cpgDrLR9vAbQR69
# UjED8ZkGaN5xqkaQBZTnjpcJ/wRFuX/bsqzq2sxjy6F31KXqHANIYEAtHbCnjECa
# rQPqbKWjVL6hXwIDAQABo4IBxTCCAcEwHwYDVR0jBBgwFoAUWsS5eyoKo6XqcQPA
# YPkt9mV1DlgwHQYDVR0OBBYEFLLpxzPgsGO+KY4aRGWs/kRPgFOKMA4GA1UdDwEB
# /wQEAwIHgDATBgNVHSUEDDAKBggrBgEFBQcDAzB3BgNVHR8EcDBuMDWgM6Axhi9o
# dHRwOi8vY3JsMy5kaWdpY2VydC5jb20vc2hhMi1hc3N1cmVkLWNzLWcxLmNybDA1
# oDOgMYYvaHR0cDovL2NybDQuZGlnaWNlcnQuY29tL3NoYTItYXNzdXJlZC1jcy1n
# MS5jcmwwTAYDVR0gBEUwQzA3BglghkgBhv1sAwEwKjAoBggrBgEFBQcCARYcaHR0
# cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQUzAIBgZngQwBBAEwgYQGCCsGAQUFBwEB
# BHgwdjAkBggrBgEFBQcwAYYYaHR0cDovL29jc3AuZGlnaWNlcnQuY29tME4GCCsG
# AQUFBzAChkJodHRwOi8vY2FjZXJ0cy5kaWdpY2VydC5jb20vRGlnaUNlcnRTSEEy
# QXNzdXJlZElEQ29kZVNpZ25pbmdDQS5jcnQwDAYDVR0TAQH/BAIwADANBgkqhkiG
# 9w0BAQsFAAOCAQEAo9OXy2+SUWIPgWigxkFWTHntcD2i7PJT/50Pcg1E/ckjcsOt
# n2/vyiORWt1Qixtrlr7wTGS4AUdorUdEk4QqJ1XaknOa1P/P7QcKoeQCPka+bYIb
# rsOI7Ngs3VE/974EgXN8+R5dh9AS+WhxpQD+PD1hFShm2uzRPIdPjCJ4X+CpgByX
# Eib8+MryJRG9l0rp3qKxWIe507g0dOHOhQsDw5Yi3y9xSsIY1zJ6J2fyZetIH2x5
# udZaZTCuBWHsOM21Y3nGhNeui1Mk2ReMZZh72lDWaEmdwoQONnQbpUZ0aTVBlkiS
# YGquVXG8hzDEDMutxJclhtX19nL7baTj9+T5UjGCEFswghBXAgEBMIGGMHIxCzAJ
# BgNVBAYTAlVTMRUwEwYDVQQKEwxEaWdpQ2VydCBJbmMxGTAXBgNVBAsTEHd3dy5k
# aWdpY2VydC5jb20xMTAvBgNVBAMTKERpZ2lDZXJ0IFNIQTIgQXNzdXJlZCBJRCBD
# b2RlIFNpZ25pbmcgQ0ECEAcc9Oq9GRbPd+5w2z4RboQwCQYFKw4DAhoFAKBwMBAG
# CisGAQQBgjcCAQwxAjAAMBkGCSqGSIb3DQEJAzEMBgorBgEEAYI3AgEEMBwGCisG
# AQQBgjcCAQsxDjAMBgorBgEEAYI3AgEVMCMGCSqGSIb3DQEJBDEWBBSZAwBD1NfW
# nMt7XecllARwTApKODANBgkqhkiG9w0BAQEFAASCAQArLAUSyd61vWKFfiOfUuXR
# BkYPrMY7fx+nbHQb7fqShD/r7ohl9IgURKlMFYaeT2LSLhrUblcxaEV61Sqxo4iz
# 8xp9S1fXKr4UK508GuCD1N50mCknl8PuvAgNmFxJtHwABFWBeTfM+zOywhkjZoCi
# /otfcYO/ZWDLaGBNjyLN7hl3uMMMSMlnlC7kHgYBVTvvYh5IKV+M8HjCOrRaLXqD
# 4q+ZM66xmhenz2I0KqoBp0nCicfDJJoIiSL/d4WgJNheT+sDGXbKLNOavnmlHLEW
# 4TIESeFZdnaqXrgouGF/76kh1Y1nZlTaaIvHOBufKIKFmgcAhw0BhHW+0PQ7uPZC
# oYIONzCCDjMGCisGAQQBgjcDAwExgg4jMIIOHwYJKoZIhvcNAQcCoIIOEDCCDgwC
# AQMxDzANBglghkgBZQMEAgEFADCBwgYLKoZIhvcNAQkQAQSggbIEga8wgawCAQEG
# BCoDBAUwITAJBgUrDgMCGgUABBQewvvVW0FRLE7Ccw8NkUOwO47fdAIHBZ0iIwwj
# BxgPMjAyMDAxMjcxNzA3MDZaoGSkYjBgMQswCQYDVQQGEwJVUzEdMBsGA1UEChMU
# Q2l0cml4IFN5c3RlbXMsIEluYy4xDTALBgNVBAsTBEdMSVMxIzAhBgNVBAMTGkNp
# dHJpeCBUaW1lc3RhbXAgUmVzcG9uZGVyoIIKXTCCBSQwggQMoAMCAQICEAqSXSRV
# gDYm4YegBXCaJZAwDQYJKoZIhvcNAQELBQAwcjELMAkGA1UEBhMCVVMxFTATBgNV
# BAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3LmRpZ2ljZXJ0LmNvbTExMC8G
# A1UEAxMoRGlnaUNlcnQgU0hBMiBBc3N1cmVkIElEIFRpbWVzdGFtcGluZyBDQTAe
# Fw0xODA4MDEwMDAwMDBaFw0yMzA5MDEwMDAwMDBaMGAxCzAJBgNVBAYTAlVTMR0w
# GwYDVQQKExRDaXRyaXggU3lzdGVtcywgSW5jLjENMAsGA1UECxMER0xJUzEjMCEG
# A1UEAxMaQ2l0cml4IFRpbWVzdGFtcCBSZXNwb25kZXIwggEiMA0GCSqGSIb3DQEB
# AQUAA4IBDwAwggEKAoIBAQDY1rSeHnKVXwd+GJ8X2Db29UadiWwbufxvQaHvGhAU
# HNs4nVvNqLrGa149kA9qlANRHvJ6KLdShnEHWNFs820iFOyh3jweSmhElo7R1Sdw
# VulvavlNuJtnTw/6GjcRseg7Q+zNDZTASEWSqO2jSLESJR5IO8JzUM6otI05MwTu
# 0t+IaJWqoX7kIKpICqhpnKEiF1ajZhBWlPuZKWBaqTKOsdbEgIH4DRHCIBo54/Mc
# 3VNa54eojWDMTrfILjFpNs/iijW7sR+mCwAPVQWFuNe2X9ed/+S+Ho7scVIQqdNy
# ZKFCFo0kY895tuBw/SvDUoCdAHQ6TRPGT5iCQjBYvRWHAgMBAAGjggHGMIIBwjAf
# BgNVHSMEGDAWgBT0tuEgHf4prtLkYaWyoiWyyBc1bjAdBgNVHQ4EFgQUtWN+wIV1
# Bz2mLr0v0lLFhRYrEm0wDAYDVR0TAQH/BAIwADAOBgNVHQ8BAf8EBAMCB4AwFgYD
# VR0lAQH/BAwwCgYIKwYBBQUHAwgwTwYDVR0gBEgwRjA3BglghkgBhv1sBwEwKjAo
# BggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQUzALBglghkgB
# hv1sAxUwcQYDVR0fBGowaDAyoDCgLoYsaHR0cDovL2NybDMuZGlnaWNlcnQuY29t
# L3NoYTItYXNzdXJlZC10cy5jcmwwMqAwoC6GLGh0dHA6Ly9jcmw0LmRpZ2ljZXJ0
# LmNvbS9zaGEyLWFzc3VyZWQtdHMuY3JsMIGFBggrBgEFBQcBAQR5MHcwJAYIKwYB
# BQUHMAGGGGh0dHA6Ly9vY3NwLmRpZ2ljZXJ0LmNvbTBPBggrBgEFBQcwAoZDaHR0
# cDovL2NhY2VydHMuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0U0hBMkFzc3VyZWRJRFRp
# bWVzdGFtcGluZ0NBLmNydDANBgkqhkiG9w0BAQsFAAOCAQEAa0OLR4Hbt+5mnZmD
# C+iJH2/GzVqK4rYqBnK5VX7DBBnSzSwLD2KqzKPZmZjcykxO1FcxlXcG/gn8/SEX
# w+oZiuoYRLqJvlzcwvCxkN6O1NnnXmBf8biHBWQMJkJ1zqFZeMg1iq38mpTiDvcK
# UOmw1e39Aj2vI90I9njSdrtqip0RPseSM/I+ZbI0HnnyK4hlR3du0fd2otJYvVmT
# E/SijgJNOkdGdKshu9I14aFKeDq+XJb+ZplSYJsa9YTI1YO7/eVhmOdKdvnH4ai5
# VYrtnLtCwoN9SFG9JW02DW4GNXnGtnK/BdKaVZ67eeWFX29TPNIbo/Q3mGI3hUip
# HDfusTCCBTEwggQZoAMCAQICEAqhJdbWMht+QeQF2jaXwhUwDQYJKoZIhvcNAQEL
# BQAwZTELMAkGA1UEBhMCVVMxFTATBgNVBAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UE
# CxMQd3d3LmRpZ2ljZXJ0LmNvbTEkMCIGA1UEAxMbRGlnaUNlcnQgQXNzdXJlZCBJ
# RCBSb290IENBMB4XDTE2MDEwNzEyMDAwMFoXDTMxMDEwNzEyMDAwMFowcjELMAkG
# A1UEBhMCVVMxFTATBgNVBAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3LmRp
# Z2ljZXJ0LmNvbTExMC8GA1UEAxMoRGlnaUNlcnQgU0hBMiBBc3N1cmVkIElEIFRp
# bWVzdGFtcGluZyBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAL3Q
# Mu5LzY9/3am6gpnFOVQoV7YjSsQOB0UzURB90Pl9TWh+57ag9I2ziOSXv2MhkJi/
# E7xX08PhfgjWahQAOPcuHjvuzKb2Mln+X2U/4Jvr40ZHBhpVfgsnfsCi9aDg3iI/
# Dv9+lfvzo7oiPhisEeTwmQNtO4V8CdPuXciaC1TjqAlxa+DPIhAPdc9xck4Krd9A
# Oly3UeGheRTGTSQjMF287DxgaqwvB8z98OpH2YhQXv1mblZhJymJhFHmgudGUP2U
# Kiyn5HU+upgPhH+fMRTWrdXyZMt7HgXQhBlyF/EXBu89zdZN7wZC/aJTKk+FHcQd
# PK/P2qwQ9d2srOlW/5MCAwEAAaOCAc4wggHKMB0GA1UdDgQWBBT0tuEgHf4prtLk
# YaWyoiWyyBc1bjAfBgNVHSMEGDAWgBRF66Kv9JLLgjEtUYunpyGd823IDzASBgNV
# HRMBAf8ECDAGAQH/AgEAMA4GA1UdDwEB/wQEAwIBhjATBgNVHSUEDDAKBggrBgEF
# BQcDCDB5BggrBgEFBQcBAQRtMGswJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLmRp
# Z2ljZXJ0LmNvbTBDBggrBgEFBQcwAoY3aHR0cDovL2NhY2VydHMuZGlnaWNlcnQu
# Y29tL0RpZ2lDZXJ0QXNzdXJlZElEUm9vdENBLmNydDCBgQYDVR0fBHoweDA6oDig
# NoY0aHR0cDovL2NybDQuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0QXNzdXJlZElEUm9v
# dENBLmNybDA6oDigNoY0aHR0cDovL2NybDMuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0
# QXNzdXJlZElEUm9vdENBLmNybDBQBgNVHSAESTBHMDgGCmCGSAGG/WwAAgQwKjAo
# BggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQUzALBglghkgB
# hv1sBwEwDQYJKoZIhvcNAQELBQADggEBAHGVEulRh1Zpze/d2nyqY3qzeM8GN0CE
# 70uEv8rPAwL9xafDDiBCLK938ysfDCFaKrcFNB1qrpn4J6JmvwmqYN92pDqTD/iy
# 0dh8GWLoXoIlHsS6HHssIeLWWywUNUMEaLLbdQLgcseY1jxk5R9IEBhfiThhTWJG
# JIdjjJFSLK8pieV4H9YLFKWA1xJHcLN11ZOFk362kmf7U2GJqPVrlsD0WGkNfMgB
# sbkodbeZY4UijGHKeZR+WfyMD+NvtQEmtmyl7odRIeRYYJu6DC0rbaLEfrvEJStH
# Agh8Sa4TtuF8QkIoxhhWz0E0tmZdtnR79VYzIi8iNrJLokqV2PWmjlIxggLOMIIC
# ygIBATCBhjByMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkw
# FwYDVQQLExB3d3cuZGlnaWNlcnQuY29tMTEwLwYDVQQDEyhEaWdpQ2VydCBTSEEy
# IEFzc3VyZWQgSUQgVGltZXN0YW1waW5nIENBAhAKkl0kVYA2JuGHoAVwmiWQMA0G
# CWCGSAFlAwQCAQUAoIIBGDAaBgkqhkiG9w0BCQMxDQYLKoZIhvcNAQkQAQQwLwYJ
# KoZIhvcNAQkEMSIEICUR8Roh06iNdCZGS6Cruy4mEs/Wtkg+3TLdldRg4lVhMIHI
# BgsqhkiG9w0BCRACLzGBuDCBtTCBsjCBrwQgsCrO26Gy12Ws1unFBnpWG9FU4YUy
# DBzPXmMmtqVvLqMwgYowdqR0MHIxCzAJBgNVBAYTAlVTMRUwEwYDVQQKEwxEaWdp
# Q2VydCBJbmMxGTAXBgNVBAsTEHd3dy5kaWdpY2VydC5jb20xMTAvBgNVBAMTKERp
# Z2lDZXJ0IFNIQTIgQXNzdXJlZCBJRCBUaW1lc3RhbXBpbmcgQ0ECEAqSXSRVgDYm
# 4YegBXCaJZAwDQYJKoZIhvcNAQEBBQAEggEAOAlmswjH7tIyNRi5XJtrU/cG63LR
# +slo8320ti5MXqT6fy9kDpQbwjc+wsBdSX7rtk0G4nvKVW0/QZVWsaXP/29mmYfP
# h1B/wE84E9GOpWysRHpAMKYjIr2mOY1vAkV6j70fcDinlQrGBmejn9bpwHE45YCO
# 5+tN2R2AdlSgSAjnJgJUoUp7Vt6e5EsfghGms1P1eTK0bbxZ7yKNjNcbhs8PD0cR
# bah6R9mKjnywyPhf7eAJ0KZWJJLtMJLAwoJ92GlsSrKtM65+nFQ+/C/vFa0+NxcZ
# +DEMNaJe7VGiHYtQocjzAQJF3Gym4CvmjVLIRpV7GGZ78hj7b+upmOPJQQ==
# SIG # End signature block
