# reviews-service

Simple Spring Boot service for storing reviews and publishing `review-created` events to Kafka.

Environment variables (or set in application.properties):
- SPRING_DATASOURCE_URL (default in application.properties)
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SPRING_KAFKA_BOOTSTRAP_SERVERS
- SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWK_SET_URI (optional, for Keycloak JWT)

Keycloak setup

1) В админке Keycloak импортируйте `keycloak/realm-export.json` (Realm -> Add realm -> select file).
2) Или используйте PowerShell-скрипт (при запущенном Keycloak с админом admin/admin):

```powershell
cd reviews-service\keycloak
.
.\create-keycloak-resources.ps1 -KeycloakUrl http://localhost:8080 -AdminUser admin -AdminPass admin
```

3) В Keycloak создайте клиента `reviews-service-client` (type: confidential) и включите `Service Accounts` и `Direct Access Grants`.
4) Назначьте роли `ADMIN` пользователю, который будет модерировать отзывы.

Build and run with Docker:

1) Build image:

```powershell
cd reviews-service
docker build -t quickbite/reviews-service:latest .
```

2) Run container (example):

```powershell
docker run --rm -p 8085:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/quickbite \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=host.docker.internal:9092 \
  -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWK_SET_URI=http://localhost:8080/auth/realms/quickbite/protocol/openid-connect/certs \
  quickbite/reviews-service:latest
```

Run with docker-compose (local dev):

```powershell
cd reviews-service
docker-compose up --build
```

This will start Postgres, Zookeeper, Kafka and the `reviews-service`.

Running tests:

```powershell
cd reviews-service
mvn test
```

Notes:
- Flyway миграции будут выполнены при старте (docker-compose uses Postgres).
- Security: set `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWK_SET_URI` to Keycloak realm certs endpoint to enable JWT validation.
- `keycloak/realm-export.json` содержит minimal realm and roles for quick start.
