# Gets relevant info about Netscalers such as the SSL cert installed, the license installed, important stats etc.   
# Uses http://github.com/devblackops/NetScaler

# Install-Module requires TLS 1.2
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

if (Get-Module -ListAvailable -Name NetScaler) {
    Write-Host "NetScaler PS module has already been installed. Will continue with script."
}
else {
    Write-Host "NetScaler PS module has not been installed on this machine. Will attempt to install now."
    try {
        Install-Module -Name NetScaler
    }
    catch {
        Write-Host "Error occurred installing NetScaler PS module."
    }
}

# NetScaler details
[string]$NSIP = ""
$username = ""
$password = ""

# Used for calculating received/transmitted bytes rate
$rxBytesRateTotal = 0
$txBytesRateTotal = 0

# Generate PSCredential for connecting to NetScaler
$securePassword = ConvertTo-SecureString $password -AsPlainText -Force
$credential = New-Object System.Management.Automation.PSCredential ($username, $securePassword)

# Let's get a session with the NetScaler going so we can grab all of our info 
$NSSession = Connect-NetScaler -IPAddress $NSIP -Credential $credential -PassThru

# ===================== SSL INFO ===================== #
# Grab any/all SSL cert(s)
# TODO: grab the amount of days till expiry 
$SSLCerts = Get-NSSSLCertificate -Session $NSSession | Where-Object {$_.certificatetype -eq "CLIENTANDSERVER_CERT" -or $_.certificatetype -eq "SRVR_CERT"}
ForEach ($cert in $SSLCerts) {
    Write-Host "SSL Cert: $($cert.cert)                                   SSL Cert Key: $($cert.certkey)                                   Days till Expiration: $($cert.daystoexpiration)"
}
# ===================== END SSL INFO ===================== #


# ===================== PERFORMANCE STATS ===================== #
# Grab all generic and 'grouped' stats. The necessary ones will be manipulated/displayed later on.
# List of statistics here: https://developer-docs.citrix.com/projects/netscaler-nitro-api/en/12.0/statistics/statistics/
$systemStats = Get-NSStat -Session $NSSession -Type 'system'
$SSLStats = Get-NSStat -Session $NSSession -Type 'ssl'
$interfaceStats = Get-NSStat -Session $NSSession -Type 'interface'

# Calculate the rate of total received/transmitted bytes
ForEach ($interfaceStat in $interfaceStats) {
    $rxBytesRateTotal = $rxBytesRateTotal + $interfaceStat.rxbytesrate
    $txBytesRateTotal = $txBytesRateTotal + $interfaceStat.txbytesrate
}

# Display all performance related info
# TODO: put these into PSCustomObjects so they can be sent out on the e-mail/HTML report
Write-Host "================= Stats for $NSIP ================="
Write-Host "CPU Usage: " ([math]::Round($systemStats.cpuusagepcnt)) "%"
Write-Host "Packet CPU Usage: " ([math]::Round($systemStats.pktcpuusagepcnt)) "%"
Write-Host "Management CPU Usage: " ([math]::Round($systemStats.mgmtcpuusagepcnt)) "%"

Write-Host "Memory Usage: " ($systemStats.memuseinmb) "mb"
Write-Host "Percentage Memory Usage: " ([math]::Round($systemStats.mmemusagepcnt)) "%"

Write-Host "Disk 0 Usage" ([math]::Round($systemStats.disk0used/$systemStats.disk0size) * 100) "%"
Write-Host "Disk 0 used space:" $systemStats.disk0used
Write-Host "Disk 1 Usage" ([math]::Truncate($systemStats.disk1used/$systemStats.disk1size) * 100) "%"

Write-Host "SSL Transaction Rate: " $SSLStats.ssltransactionsrate "/ second"

Write-Host "Received Bandwidth: $rxBytesRateTotal"
Write-Host "Transmitted Bandwidth: $txBytesRateTotal"
# ===================== END PERFORMANCE STATS ===================== #


# Terminate NetScaler session
Disconnect-NetScaler
