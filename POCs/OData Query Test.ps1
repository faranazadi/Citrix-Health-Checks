$credential = Get-Credential

$users = Invoke-RestMethod -Uri "http://DDC/Citrix/Monitor/OData/v3/Data/$metadata" -Credential $credential
Write-Host $users
#$users.Content.Properties | ConvertTo-Json | Out-File "$scriptRootDir\Test.json"