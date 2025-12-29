param(
    [string]$KeycloakUrl = "http://localhost:8080",
    [string]$AdminUser = "admin",
    [string]$AdminPass = "admin"
)

# Получить токен администратора
$body = @{client_id="admin-cli"; username=$AdminUser; password=$AdminPass; grant_type="password"}
$tokenResp = Invoke-RestMethod -Method Post -Uri "$KeycloakUrl/realms/master/protocol/openid-connect/token" -Body $body
$token = $tokenResp.access_token

# Импорт realm (если требуется) - тут просто пример запроса для импорта
Write-Host "Uploading realm-export.json"
Invoke-RestMethod -Method Post -Uri "$KeycloakUrl/admin/realms" -Headers @{Authorization = "Bearer $token"} -InFile "realm-export.json" -ContentType "application/json" -ErrorAction SilentlyContinue

# Создать роли явно (если импорт не сработал)
$realm = "quickbite"
$roles = @("ADMIN","USER")
foreach ($r in $roles) {
    $roleData = @{name=$r} | ConvertTo-Json
    Invoke-RestMethod -Method Post -Uri "$KeycloakUrl/admin/realms/$realm/roles" -Headers @{Authorization = "Bearer $token"} -Body $roleData -ContentType "application/json" -ErrorAction SilentlyContinue
}

Write-Host "Keycloak resources creation attempted. Please verify in Keycloak admin console."
