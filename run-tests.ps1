#!/usr/bin/env pwsh

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Running Tests for All Services" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$services = @(
    "order-service",
    "restaurant-service", 
    "user-service",
    "payment-service",
    "notification-service"
)

$failedServices = @()
$passedServices = @()

foreach ($service in $services) {
    Write-Host "========================================" -ForegroundColor Yellow
    Write-Host "Testing: $service" -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Yellow
    
    Set-Location $service
    
    $output = mvn clean test 2>&1
    $exitCode = $LASTEXITCODE
    
    if ($exitCode -eq 0) {
        Write-Host "✅ $service - PASSED" -ForegroundColor Green
        $passedServices += $service
    } else {
        Write-Host "❌ $service - FAILED" -ForegroundColor Red
        $failedServices += $service
    }
    
    Set-Location ..
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Test Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Passed: $($passedServices.Count)/$($services.Count)" -ForegroundColor Green
Write-Host "Failed: $($failedServices.Count)/$($services.Count)" -ForegroundColor Red
Write-Host ""

if ($passedServices.Count -gt 0) {
    Write-Host "✅ Passed Services:" -ForegroundColor Green
    foreach ($service in $passedServices) {
        Write-Host "   - $service" -ForegroundColor Green
    }
    Write-Host ""
}

if ($failedServices.Count -gt 0) {
    Write-Host "❌ Failed Services:" -ForegroundColor Red
    foreach ($service in $failedServices) {
        Write-Host "   - $service" -ForegroundColor Red
    }
    Write-Host ""
    exit 1
}

Write-Host "🎉 All tests passed!" -ForegroundColor Green
exit 0
