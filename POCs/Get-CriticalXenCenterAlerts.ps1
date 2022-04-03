Add-PSSnapIn Citrix*
Import-Module Citrix*

#[array]$xenServerHosts = @("")
#[array]$deliveryControllers = @("")
#[array]$hypervisorAlerts = @()

<#$hypervisorAlerts += foreach ($deliveryController in $deliveryControllers) {
    Get-BrokerHypervisorAlert -AdminAddress $deliveryController
}#>

<#$hypervisorAlerts += foreach ($entry in $xenServerHosts) {
    Get-BrokerHypervisorAlert -HostingServerName $entry
}

Write-Host $hypervisorAlerts#>

#$hypervisorAlerts = Get-BrokerHypervisorAlert -AdminAddress HYPERVISOR -Filter {Time -ge '1:0'}

<#If ($hypervisorAlerts) {
    Write-Host $hypervisorAlerts
} else {
    Write-Host "There are no hypervisor alerts to report on."
}#>

Get-BrokerHypervisorAlert 