Function LogTo-Console {
    param(
        [ValidateSet("Info", "Warning", "Error")][string]$messageType = "Info", # All messages are informational, unless otherwise specified when function is called
        [Parameter(Mandatory=$true)][string]$message
    )

    $dateAndTime = Get-Date -Format g

    Write-Host "[$dateAndTime][$messageType] $message"
}