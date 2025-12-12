# Quick Kafka Test Script
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "KAFKA INTEGRATION TEST" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan

# Send test message
Write-Host "Sending test message..." -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "http://localhost:8083/api/kafka-test/send-test-message?name=TestUser" -Method Post
    Write-Host "SUCCESS: $($result.message)" -ForegroundColor Green
    Write-Host "Topic: $($result.topic)" -ForegroundColor White
    Write-Host "`n"
} catch {
    Write-Host "FAILED: $_" -ForegroundColor Red
}

# Wait
Write-Host "Waiting 5 seconds..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Check received
Write-Host "`nChecking received messages..." -ForegroundColor Yellow
try {
    $received = Invoke-RestMethod -Uri "http://localhost:8080/api/kafka-test/received-messages" -Method Get
    Write-Host "SUCCESS: Received $($received.totalReceived) messages" -ForegroundColor Green
    
    if ($received.messages.Count -gt 0) {
        $last = $received.messages[-1]
        Write-Host "`nLast message:" -ForegroundColor Cyan
        Write-Host "  User: $($last.payload.firstName) $($last.payload.lastName)" -ForegroundColor White
        Write-Host "  Email: $($last.payload.email)" -ForegroundColor White
    }
} catch {
    Write-Host "FAILED: $_" -ForegroundColor Red
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "TEST COMPLETED!" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan
