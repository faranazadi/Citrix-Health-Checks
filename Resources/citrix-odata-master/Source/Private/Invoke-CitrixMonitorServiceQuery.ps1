function Invoke-CitrixMonitorServiceQuery {
    
    <#
    .SYNOPSIS
    Sends an OData query to the specified Citrix Delivery Controller and returns the result.
    
    .DESCRIPTION
    The Invoke-CitrixMonitorServiceQuery cmdlet is used as a private function on the citrix-odata module to send
    HTTP requests to the specified Citrix Monitor Service OData API Endpoint.
    
    This cmdlet handles HTTP errors and will gracefully interrupt execution if the HTTP request fails or returns
    unexpected data. If the request is sucessful, the cmdlet will return a PSCustomObject with the results of the
    OData Query. Sucessful results are always returned in JSON format.
    
    You can optionally specify a query in OData format to return more accurate results.
    
    .LINK
    https://github.com/karjona/citrix-odata
    
    .PARAMETER DeliveryController
    Specifies a single Citrix Virtual Apps and Desktops Delivery Controller to collect data from.
    
    .PARAMETER Credential
    Specifies a user account that has permission to send the request. The default is the current user. A
    minimum of read-only administrator permissions on Citrix Virtual Apps and Desktops are required to collect
    this data.
    
    Enter a PSCredential object, such as one generated by the Get-Credential cmdlet.
    
    .PARAMETER Endpoint
    Specifies the Citrix Monitor Service OData API Endpoint to use for the query. A list of API Endpoints can be
    found here: https://developer-docs.citrix.com/projects/monitor-service-odata-api/en/latest/
    
    .PARAMETER Query
    Query to execute on the OData Endpoint. Please note that, while not a mandatory parameter, querying some
    specific Endpoints without a query will return in a large collection of data that may negatively impact the
    performance of the script, the machine it runs on or the Citrix Delivery Controller.
    
    More information about how to create OData queries can be found here: https://www.odata.org/documentation/
    
    .COMPONENT
    citrix-odata
    #>
    
    
    [CmdletBinding()]
    [OutputType('PSCustomObject')]
    
    param(
    [Parameter(Mandatory=$true)]
    [String]
    $DeliveryController,
    
    [Parameter()]
    [PSCredential]
    $Credential,
    
    [Parameter()]
    [String]
    $Endpoint,
    
    [Parameter()]
    [String]
    $Query
    )
    
    begin {
        if ($Query) {
            if ($Query.Substring(0,1) -ne '&') {
                $Query = "&$Query"
            }
        }
    }
    
    process {
        try {
            $InvokeRestMethodParams = @{
                Uri = "http://$DeliveryController/Citrix/Monitor/OData/v3/Data/$Endpoint`?`$format=json$Query"
            }
            if ($Credential) {
                $InvokeRestMethodParams.Add("Credential", $Credential)
            } else {
                $InvokeRestMethodParams.Add("UseDefaultCredentials", $true)
            }
            
            $Result = Invoke-RestMethod @InvokeRestMethodParams
        } catch {
            $ConnectionError = $_
            $StatusCodeString = $ConnectionError.Exception.Response.StatusCode.ToString()
            $ExceptionString = $ConnectionError.Exception.Status.ToString()
            
            if ($StatusCodeString -eq 'Unauthorized' -and -Not $Credential) {
                Write-Error ("The current user does not have at least read-only administrator " +
                "permissions on $DeliveryController.")
            } elseif ($StatusCodeString -eq 'Unauthorized' -and $Credential) {
                Write-Error ("The supplied credentials do not have at least read-only administrator " +
                "permissions on $DeliveryController.")
            } elseif ($StatusCodeString -ne 'Unauthorized') {
                Write-Error ("The server on $DeliveryController responded with an error: " +
                "$($ConnectionError.Exception.Message)")
            }
            
            if ($ExceptionString -eq 'NameResolutionFailure') {
                Write-Error "Could not find host $DeliveryController."
            }
            
            Write-Error ("An error occurred while trying to connect to $DeliveryController. Check " +
            "network connectivity and that the specified host is a Citrix Delivery Controller.`r`n" +
            "$($ConnectionError.Exception.Message)")
        }
        $Result
    }
}