[array]$xenServerList = @("")
$xenUserName = ""
$xenPassword = ""
$allSSHOutputs = @{}
$freeMemory = @{}
$formattedSSHOutputs = @()

$warning = 200
$caution = 1000

$securePassword = ConvertTo-SecureString $xenPassword -AsPlainText -Force
$hostCredential = New-Object System.Management.Automation.PSCredential ($xenUserName, $securePassword)

$webClient = New-Object System.Net.WebClient
$webClient.Proxy.Credentials = [System.Net.CredentialCache]::DefaultNetworkCredentials
[Net.ServicePointManager]::SecurityProtocol = "tls12"

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
    $lines = $output[$entry] -split "\r?\n"
    $line = $lines[1] -split "\s+"
             
    Write-Host "Host: " -ForegroundColor white -NoNewline
    Write-Host $entry -NoNewline
    Write-Host "  Free Memory: " -ForegroundColor white -NoNewline
                               
    If ([int]$line[3] -le $caution -AND [int]$line[3] -gt $warning) {
        Write-Host $line[3] -ForegroundColor Yellow -NoNewline
        Write-Host "M" -ForegroundColor Yellow -NoNewline
        Write-Host "  *CAUTION*" -ForegroundColor Yellow
        $statusMessage = "*CAUTION*"
    }
    ElseIf ([int]$line[3] -le $warning) {
        Write-Host $line[3] -ForegroundColor red -NoNewline
        Write-Host "M" -ForegroundColor red -NoNewline
        Write-Host "  *WARNING*" -ForegroundColor red
        $statusMessage = "*WARNING*"
    }
    Else {
        Write-Host $line[3] -NoNewline
        Write-Host "M" -NoNewline
        Write-Host "  *OK*"
        $statusMessage = "*OK*"
    }
        #$freeMemory.Add($entry, $line[3])
       $formattedSSHOutputs +=  [PSCustomObject] @{
        "Host" = $entry
        "Free memory (mb)" = $line[3]
        "Status" = $statusMessage   
        }
}

$formattedSSHOutputs
#$freeMemory