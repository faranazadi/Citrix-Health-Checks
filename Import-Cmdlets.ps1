function Import-Cmdlets {
    # Get definition files
    #$PSModule = $ExecutionContext.SessionState.Module
    #$PSModuleRoot = $PSModule.ModuleBase
    $scriptRootDir = Split-Path $MyInvocation.InvocationName

    $cmdlets = @(Get-ChildItem -Path C:\Automated Infrastructure Checks\Private\Deps\Cmdlets\*.ps1 -ErrorAction SilentlyContinue)

    Write-Host $cmdlets

    # Dot source the files
    foreach($cmdlet in $cmdlets) {
        try {
            . $cmdlet.fullname
            Write-Host "Imported $cmdlet.fullname"
        } catch {
            Write-Error -Message "Failed to import function $($cmdlet.fullname): $_"
        }
    }

    # Make the cmdlets available
    Export-ModuleMember -Function $cmdlets.Basename
}