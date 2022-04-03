$credential = Get-Credential

$credential | Export-CliXml -Path 'C:\Automated Infrastructure Checks\Private\Config'