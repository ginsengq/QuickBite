# Stop Development Environment
Write-Host "🛑 Останавливаем QuickBite..." -ForegroundColor Yellow

docker-compose -f docker-compose.dev.yml down

Write-Host ""
Write-Host "✅ Все сервисы остановлены" -ForegroundColor Green
Write-Host ""
Write-Host "💡 Для запуска снова используйте: .\start-dev.ps1" -ForegroundColor Cyan
