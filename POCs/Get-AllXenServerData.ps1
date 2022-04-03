if (!(Get-PackageProvider | Where-Object {$_.Name -eq "NuGet"})) {
    Write-Host "Default NuGet package provider not installed."
    Write-Host "Installing NuGet package provider."
    Install-PackageProvider -Name "NuGet" -Confirm:$false -Force
    Set-PSRepository -Name "PSGallery" -InstallationPolicy "Trusted" 
}

if (!((Get-Module -ListAvailable *) | Where-Object {$_.Name -eq "Posh-SSH"})) {
    Write-Host "SSH module not found, installing missing module."
    Install-Module -Name Posh-SSH -Confirm:$false -Force
}

$scriptRootDir = Split-Path $MyInvocation.InvocationName

[array]$xenServerHostList = @("")
$xenUserName = ""
$xenPassword = ""

$fileName = "XenServerHostMetrics" + (Get-Date -format 'MM.dd.yy_HH.mm').ToString()
$timeOut = 600

$delay = 30
$path = "/root/$fileName.csv"

$command = "timeout $($timeOut)s rrd2csv -s $delay > $path"

$password = ConvertTo-SecureString $XenPassword -AsPlainText -Force
$hostCredential = New-Object System.Management.Automation.PSCredential ($XenUserName, $password)

foreach ($entry in $xenServerHostList) {
    $session = New-SSHSession -ComputerName $jobHost -Credential $jobCred -AcceptKey
    Invoke-SSHCommand -Index $session.SessionId -Command $jobCommand
    Get-SSHSession | Remove-SSHSession | Out-Null
}

$localFile = "$scriptRootDir\" + $TestName + ".csv"
Get-SCPFile -ComputerName $HostName -RemoteFile $path -LocalFile $localFile  -Credential $hostCredential