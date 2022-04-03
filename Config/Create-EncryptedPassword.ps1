## Define variables
$Directory = $PSScriptRoot 
$KeyFile = Join-Path $Directory  "AES256_KEY.key"
$PasswordFile = Join-Path $Directory "AES256_PASSWORD_FILE.txt"

## Display instructions to user
Write-Host "~ GENERATE A SECURE SMTP PASSWORD ~"
Write-Host ""
Write-Host "This script creates a 256-bit AES key file and a password file"
Write-Host "containing the password you enter below. Please enter the password manually i.e. do not copy and paste."
Write-Host ""
Write-Host "Two files will be generated in the directory $($Directory):"
Write-Host "-$($KeyFile)"
Write-Host "-$($PasswordFile)"
Write-Host ""
Write-Host "Enter password manually and press ENTER:"
$Password = Read-Host -AsSecureString

Write-Host ""

# Create the AES key file
try {
	$Key = New-Object Byte[] 32
	[Security.Cryptography.RNGCryptoServiceProvider]::Create().GetBytes($Key)
	$Key | out-file $KeyFile
        $KeyFileCreated = $True
	Write-Host "The key file $KeyFile was created successfully"
} catch {
	write-Host "An error occurred trying to create the key file $KeyFile (error: $($Error[0])"
}

Start-Sleep 2

# Add the plaintext password to the password file (and encrypt it based on the AES key file)
If ($KeyFileCreated -eq $True) {
	try {
		$Key = Get-Content $KeyFile
		$Password | ConvertFrom-SecureString -key $Key | Out-File $PasswordFile
		Write-Host "The key file $PasswordFile was created successfully"
	} catch {
		write-Host "An error occurred trying to create the password file $PasswordFile (error: $($Error[0])"
	}
}

Write-Host ""
write-Host "Press any key to exit script..."

$x = $host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")