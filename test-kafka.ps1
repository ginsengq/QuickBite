# Kafka Testing Script for QuickBite Microservices
# Tests message flow: User Service (Producer) -> Kafka -> Order Service (Consumer)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🧪 KAFKA INTEGRATION TEST" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check Kafka Consumer Status
Write-Host "📊 Step 1: Checking Kafka consumer status..." -ForegroundColor Yellow
try {
    $consumerStatus = Invoke-RestMethod -Uri "http://localhost:8080/api/kafka-test/consumer-status" -Method Get
    Write-Host "✅ Order Service Consumer Status:" -ForegroundColor Green
    Write-Host "   Service: $($consumerStatus.service)" -ForegroundColor White
    Write-Host "   Status: $($consumerStatus.status)" -ForegroundColor White
    Write-Host "   Group ID: $($consumerStatus.groupId)" -ForegroundColor White
    Write-Host "   Total Messages Received: $($consumerStatus.totalMessagesReceived)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "❌ Failed to check consumer status: $_" -ForegroundColor Red
    Write-Host ""
}

# Step 2: Check Kafka Producer Status
Write-Host "📊 Step 2: Checking Kafka producer status..." -ForegroundColor Yellow
try {
    $producerStatus = Invoke-RestMethod -Uri "http://localhost:8083/api/kafka-test/kafka-status" -Method Get
    Write-Host "✅ User Service Producer Status:" -ForegroundColor Green
    Write-Host "   Service: $($producerStatus.service)" -ForegroundColor White
    Write-Host "   Status: $($producerStatus.status)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "❌ Failed to check producer status: $_" -ForegroundColor Red
    Write-Host ""
}

# Step 3: Clear previous messages
Write-Host "🧹 Step 3: Clearing previous test messages..." -ForegroundColor Yellow
try {
    $clearResult = Invoke-RestMethod -Uri "http://localhost:8080/api/kafka-test/clear-messages" -Method Delete
    Write-Host "✅ Cleared $($clearResult.clearedCount) previous messages" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "⚠️  Could not clear messages (might be first run): $_" -ForegroundColor Yellow
    Write-Host ""
}

# Step 4: Send single test message
Write-Host "📤 Step 4: Sending single test message..." -ForegroundColor Yellow
try {
    $sendResult = Invoke-RestMethod -Uri "http://localhost:8083/api/kafka-test/send-test-message?name=TestUser1" -Method Post
    Write-Host "✅ Message sent successfully!" -ForegroundColor Green
    Write-Host "   Topic: $($sendResult.topic)" -ForegroundColor White
    Write-Host "   User: $($sendResult.event.firstName) $($sendResult.event.lastName)" -ForegroundColor White
    Write-Host "   Email: $($sendResult.event.email)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "❌ Failed to send message: $_" -ForegroundColor Red
    Write-Host ""
}

# Wait for message processing
Write-Host "⏳ Waiting 3 seconds for message processing..." -ForegroundColor Yellow
Start-Sleep -Seconds 3
Write-Host ""

# Step 5: Check received messages
Write-Host "📥 Step 5: Checking received messages in Order Service..." -ForegroundColor Yellow
try {
    $receivedMessages = Invoke-RestMethod -Uri "http://localhost:8080/api/kafka-test/received-messages" -Method Get
    Write-Host "✅ Messages received: $($receivedMessages.totalReceived)" -ForegroundColor Green
    
    if ($receivedMessages.messages.Count -gt 0) {
        Write-Host ""
        Write-Host "📋 Last received message:" -ForegroundColor Cyan
        $lastMessage = $receivedMessages.messages[-1]
        Write-Host "   Message #: $($lastMessage.messageNumber)" -ForegroundColor White
        Write-Host "   Topic: $($lastMessage.topic)" -ForegroundColor White
        Write-Host "   User ID: $($lastMessage.payload.userId)" -ForegroundColor White
        Write-Host "   Email: $($lastMessage.payload.email)" -ForegroundColor White
        Write-Host "   Name: $($lastMessage.payload.firstName) $($lastMessage.payload.lastName)" -ForegroundColor White
        Write-Host "   Received At: $($lastMessage.receivedAt)" -ForegroundColor White
    }
    Write-Host ""
} catch {
    Write-Host "❌ Failed to check received messages: $_" -ForegroundColor Red
    Write-Host ""
}

# Step 6: Send bulk messages
Write-Host "📤 Step 6: Sending 5 bulk test messages..." -ForegroundColor Yellow
try {
    $bulkResult = Invoke-RestMethod -Uri "http://localhost:8083/api/kafka-test/send-bulk-messages?count=5" -Method Post
    Write-Host "✅ Bulk send completed!" -ForegroundColor Green
    Write-Host "   Total Requested: $($bulkResult.totalRequested)" -ForegroundColor White
    Write-Host "   Success: $($bulkResult.successCount)" -ForegroundColor Green
    Write-Host "   Failed: $($bulkResult.failureCount)" -ForegroundColor $(if ($bulkResult.failureCount -eq 0) { "Green" } else { "Red" })
    Write-Host ""
} catch {
    Write-Host "❌ Failed to send bulk messages: $_" -ForegroundColor Red
    Write-Host ""
}

# Wait for bulk message processing
Write-Host "⏳ Waiting 5 seconds for bulk message processing..." -ForegroundColor Yellow
Start-Sleep -Seconds 5
Write-Host ""

# Step 7: Final check
Write-Host "📊 Step 7: Final message count..." -ForegroundColor Yellow
try {
    $finalStatus = Invoke-RestMethod -Uri "http://localhost:8080/api/kafka-test/consumer-status" -Method Get
    Write-Host "✅ Final Statistics:" -ForegroundColor Green
    Write-Host "   Total Messages Received: $($finalStatus.totalMessagesReceived)" -ForegroundColor White
    Write-Host "   Messages In Memory: $($finalStatus.messagesInMemory)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "❌ Failed to get final status: $_" -ForegroundColor Red
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ KAFKA TEST COMPLETED!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📝 Next Steps:" -ForegroundColor Yellow
Write-Host "   1. Check Docker logs: docker logs quickbite-order-service" -ForegroundColor White
Write-Host "   2. Open Kafka UI: http://localhost:8090" -ForegroundColor Cyan
Write-Host "   3. View topics and messages in Kafka UI" -ForegroundColor White
Write-Host "   4. Test endpoints in Swagger:" -ForegroundColor White
Write-Host "      - User Service: http://localhost:8083/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host "      - Order Service: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host ""
