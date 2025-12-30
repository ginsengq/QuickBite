# QuickBite Development Mode - Quick Start
# Этот скрипт запускает все сервисы в development режиме с hot reload

Write-Host "🚀 Запуск QuickBite в режиме разработки..." -ForegroundColor Green
Write-Host ""

# Проверяем, запущен ли Docker
$dockerRunning = docker info 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Docker не запущен! Пожалуйста, запустите Docker Desktop." -ForegroundColor Red
    exit 1
}

Write-Host "✅ Docker запущен" -ForegroundColor Green
Write-Host ""

# Останавливаем старые контейнеры если они есть
Write-Host "🛑 Останавливаем старые контейнеры..." -ForegroundColor Yellow
docker-compose -f docker-compose.dev.yml down

Write-Host ""
Write-Host "📦 Запуск сервисов..." -ForegroundColor Cyan
Write-Host "   Это может занять несколько минут при первом запуске" -ForegroundColor Gray
Write-Host ""

# Запускаем в фоновом режиме
docker-compose -f docker-compose.dev.yml up -d

Write-Host ""
Write-Host "✅ Сервисы запущены!" -ForegroundColor Green
Write-Host ""
Write-Host "🌐 Доступные URL:" -ForegroundColor Cyan
Write-Host "   Frontend:              http://localhost:3000" -ForegroundColor White
Write-Host "   Order Service:         http://localhost:8080" -ForegroundColor White
Write-Host "   Restaurant Service:    http://localhost:8081" -ForegroundColor White
Write-Host "   Keycloak:              http://localhost:8082" -ForegroundColor White
Write-Host "   User Service:          http://localhost:8083" -ForegroundColor White
Write-Host "   Payment Service:       http://localhost:8084" -ForegroundColor White
Write-Host "   Notification Service:  http://localhost:8085" -ForegroundColor White
Write-Host "   Kafka UI:              http://localhost:8090" -ForegroundColor White
Write-Host ""
Write-Host "📝 Полезные команды:" -ForegroundColor Cyan
Write-Host "   Логи:           docker-compose -f docker-compose.dev.yml logs -f" -ForegroundColor Gray
Write-Host "   Остановить:     docker-compose -f docker-compose.dev.yml down" -ForegroundColor Gray
Write-Host "   Перезапустить:  docker-compose -f docker-compose.dev.yml restart" -ForegroundColor Gray
Write-Host ""
Write-Host "💡 Hot Reload активен - изменения в коде применятся автоматически!" -ForegroundColor Green
Write-Host ""
Write-Host "📊 Просмотр логов (нажмите Ctrl+C для выхода)..." -ForegroundColor Yellow
Write-Host ""

# Показываем логи
docker-compose -f docker-compose.dev.yml logs -f
