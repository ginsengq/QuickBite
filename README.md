# QuickBite — Food Delivery Service

**QuickBite** — это сервис доставки еды из кафе и ресторанов. Пользователи могут просматривать категории еды, рестораны, создавать заказы и отслеживать их статус.

Система построена по микросервисной архитектуре с интеграцией Kafka, Keycloak для безопасности и PostgreSQL с Flyway для хранения данных.

---

## 1. Основной функционал

### Что делает приложение

* Просмотр категорий еды
* Просмотр ресторанов и меню
* Создание заказов
* Отслеживание статуса заказов
* Авторизация и роль-based доступ
* Администраторский функционал для управления системой

### Целевая аудитория

* **Пользователи** — создание заказов, отслеживание доставки
* **Администраторы ресторанов** — управление меню и заказами
* **Системные администраторы** — мониторинг и управление сервисами

### Основные функции

| Функция              | Описание                                    |
| -------------------- | ------------------------------------------- |
| **Заказы**           | Создание, изменение, удаление, отслеживание |
| **Меню и категории** | Просмотр и обновление (Admin)               |
| **Уведомления**      | Логи, события через Kafka                   |
| **Безопасность**     | JWT / OAuth2 через Keycloak                 |

---

## 2. Архитектура и структура кода

### Общая структура проекта

```
QuickBite/
├─ order-service/           # Сервис управления заказами
│  ├─ src/main/java/com/example/order_service/
│  │  ├─ controller/        # REST API контроллеры
│  │  ├─ service/           # Бизнес-логика
│  │  ├─ repository/        # Spring Data JPA репозитории
│  │  ├─ entity/            # Сущности / модели
│  │  ├─ dto/               # Request/Response DTOs
│  │  ├─ config/            # Настройки Kafka, Keycloak, Security
│  │  ├─ exception/         # Обработка ошибок, @ControllerAdvice
│  │  ├─ mapper/            # Мапперы между Entity и DTO
│  │  ├─ client/            # REST клиенты для других сервисов
│  │  └─ event/             # Kafka event publishers
│  ├─ src/main/resources/
│  │  ├─ application.properties
│  │  └─ db/migration/      # Flyway миграции
│  └─ src/test/java/com/example/order_service/
│     └─ kafka/             # Kafka integration tests
│
├─ user-service/            # Сервис управления пользователями
│  ├─ src/main/java/com/example/user_service/
│  │  ├─ controller/        # REST API контроллеры
│  │  ├─ service/           # Бизнес-логика
│  │  ├─ repository/        # Spring Data JPA репозитории
│  │  ├─ entity/            # Сущности (User)
│  │  ├─ dto/               # Request/Response DTOs
│  │  ├─ config/            # Настройки Kafka, Security, Swagger
│  │  ├─ exception/         # Обработка ошибок
│  │  ├─ mapper/            # Мапперы
│  │  └─ event/             # Kafka event publishers/consumers
│  ├─ src/main/resources/
│  │  ├─ application.properties
│  │  └─ db/migration/      # Flyway миграции
│  └─ src/test/java/com/example/user_service/
│     ├─ service/           # Unit tests (Mockito)
│     └─ integration/       # Integration tests (Testcontainers)
│
├─ restaurant-service/      # Сервис ресторанов и меню
│  ├─ src/main/java/com/example/restaurant_service/
│  │  ├─ controller/        # REST API контроллеры
│  │  ├─ service/           # Бизнес-логика
│  │  ├─ repository/        # Spring Data JPA репозитории
│  │  ├─ entity/            # Сущности (Restaurant, MenuItem, Category)
│  │  ├─ dto/               # Request/Response DTOs
│  │  ├─ config/            # Настройки Kafka, Security, Swagger
│  │  ├─ exception/         # Обработка ошибок
│  │  ├─ mapper/            # Мапперы
│  │  └─ event/             # Kafka events
│  └─ src/main/resources/
│     ├─ application.properties
│     └─ db/migration/      # Flyway миграции
│
├─ keycloak/                # Keycloak конфигурация
└─ docker-compose.yml       # Инфраструктура (PostgreSQL, Kafka, Keycloak)
```

### Микросервисы

#### 1. User Service (Port: 8083)

* **Endpoints:**
    * `POST /api/users` — создать пользователя (ADMIN)
    * `GET /api/users/{id}` — получить пользователя (USER, ADMIN)
    * `GET /api/users` — список всех пользователей (ADMIN)
    * `PUT /api/users/{id}` — обновить пользователя (USER, ADMIN)
    * `DELETE /api/users/{id}` — удалить пользователя (ADMIN)
* **Database:** `quickbite_users` (PostgreSQL:5434)
* **Kafka:** Producer → `OrderCreatedEvent`
* **Security:** JWT через Keycloak
* **Validation:** `@Valid`, глобальный `@ControllerAdvice`
* **Swagger:** http://localhost:8080/swagger-ui.html
* **Tests:**
    * 2 Kafka integration tests (KafkaIntegrationTest) ✅

#### 3. Restaurant & Menu Service (Port: 8081)
    * 7 unit tests (UserServiceImplTest) ✅
    * 4 integration tests (UserControllerIntegrationTest) ✅

#### 2. Order Service (Port: 8080)

* **Endpoints:**
    * `POST /api/orders` — создать заказ (USER, ADMIN)
    * `GET /api/orders/{id}` — получить заказ (USER, ADMIN)
    * `GET /api/orders?userId={userId}` — список заказов пользователя (USER, ADMIN)
    * `PATCH /api/orders/{id}/status` — изменить статус заказа (ADMIN)
* **Database:** `quickbite_orders` (PostgreSQL:5432)
    * Tables: `orders`, `order_items`
* **Kafka:** Producer → `OrderCreatedEvent`
* **Security:** JWT через Keycloak
* **Validation:** `@Valid`, глобальный `@ControllerAdvice`
* **Swagger:** http://localhost:8080/swagger-ui.html

#### 2. Restaurant & Menu Service (Port: 8081)

* **Endpoints:**
    * `GET /api/restaurants` — список ресторанов (Public)
    * `GET /api/restaurants/{id}` — детали ресторана (Public)
    * `GET /api/restaurants/search?keyword=X` — поиск ресторанов (Public)
    * `POST /api/restaurants` — добавить ресторан (ADMIN)
    * `PUT /api/restaurants/{id}` — обновить ресторан (ADMIN)
    * `DELETE /api/restaurants/{id}` — удалить ресторан (ADMIN)
    * `GET /api/categories` — список категорий (Public)
    * `POST /api/categories` — создать категорию (ADMIN)
    * `GET /api/menu-items/restaurant/{restaurantId}` — меню ресторана (Public)
    * `GET /api/menu-items/category/{categoryId}` — блюда по категории (Public)
    * `POST /api/menu-items` — добавить блюдо (ADMIN)
    * `PUT /api/menu-items/{id}` — обновить блюдо (ADMIN)
    * `DELETE /api/menu-items/{id}` — удалить блюдо (ADMIN)
    * `GET /api/menu-items/prices?ids=1,2,3` — получить цены блюд
* **Database:** `quickbite_restaurant` (PostgreSQL:5433)
    * Tables: `restaurants`, `menu_items`, `categories`, `menu_item_categories`
* **Kafka:** Producer → `MenuUpdatedEvent`
* **Security:** JWT через Keycloak
* **Swagger:** http://localhost:8081/swagger-ui.html

## 3. Kafka Integration

* **События:** 
    * `OrderCreatedEvent` — создание заказа (Order Service → User Service)
    * `UserCreatedEvent` — создание пользователя (User Service → Order Service)
    * `MenuUpdatedEvent` — обновление меню (Restaurant Service)
* **Топики:** `order-events`, `user-events`, `menu-updated`
* **Логика:** 
    * Синхронизация данных между сервисами
    * Уведомления и логирование событий
    * Event-driven архитектура
* **Kafka UI:** http://localhost:8090 (для мониторинга)
* **Kafka Broker:** localhost:9092
* **Zookeeper:** localhost:2181
* **Профили:** 
    * По умолчанию используется `LoggingOrderEventPublisher` (без Kafka)
    * Для Kafka активируйте профиль `kafka`
    * По умолчанию используется `LoggingOrderEventPublisher` (без Kafka)
    * Для Kafka активируйте профиль `kafka`

---

## 4. Безопасность (Keycloak)

* **URL:** http://localhost:8082
* **Admin Console:** http://localhost:8082/admin (admin/admin)
* **Realm:** `quickbite`
* **Роли:** `ROLE_USER`, `ROLE_ADMIN`
* **Клиент:** `backend-api` (confidential)
* **Интеграция:** Spring Security OAuth2 Resource Server

### Настройка Keycloak:

## 5. База данных & Flyway

* **СУБД:** PostgreSQL 15
* **User Service DB:** `quickbite_users` (localhost:5434)
* **Order Service DB:** `quickbite_orders` (localhost:5432)
* **Restaurant Service DB:** `quickbite_restaurant` (localhost:5433)
* **Миграции:** Flyway (автоматически при запуске)
    * User Service: `V1__create_users_table.sql`
    * Order Service: `V1__create_orders_tables.sql`
    * Restaurant Service: 
        * `V1__create_restaurant_tables.sql`
        * `V2__insert_sample_data.sql` (тестовые данные)
* **Репозитории:** Spring Data JPA
* **Order Service DB:** `quickbite_orders` (localhost:5432)
* **Restaurant Service DB:** `quickbite_restaurant` (localhost:5433)
* **Миграции:** Flyway (автоматически при запуске)
    * Order Service: `V1__create_orders_tables.sql`
    * Restaurant Service: 
        * `V1__create_restaurant_tables.sql`
        * `V2__insert_sample_data.sql` (тестовые данные)
* **Репозитории:** Spring Data JPA

## 6. Swagger / OpenAPI

* **User Service:** http://localhost:8083/swagger-ui.html
* **Order Service:** http://localhost:8080/swagger-ui.html
* **Restaurant Service:** http://localhost:8081/swagger-ui.html
* Автоматическая генерация схем API и моделей DTO
* Примеры запросов и ответов для всех эндпоинтов
* JWT авторизация встроена в Swagger UIоделей DTO
* Примеры запросов и ответов для всех эндпоинтов
* JWT авторизация встроена в Swagger UI

---

## 7. Запуск проекта

### Предварительные требования:

* Java 17+
* Maven 3.8+
* Docker & Docker Compose

### Шаги запуска:

1. **Запустить инфраструктуру:**
```bash
docker-compose up -d
```

2. **Подождать пока все сервисы запустятся** (проверить статус):
```bash
docker-compose ps
```
3. **Настроить Keycloak** (см. раздел 4)

4. **Запустить User Service:**
```bash
cd user-service
./mvnw spring-boot:run
```

5. **Запустить Order Service:**
```bash
cd order-service
./mvnw spring-boot:run
```

6. **Запустить Restaurant Service:**
```bash
cd restaurant-service
./mvnw spring-boot:run
```vnw spring-boot:run
```

### Остановка:
```bash
## 8. Технологический стек

* **Backend:** Spring Boot 3.5.8
* **Security:** Spring Security + OAuth2 Resource Server
* **Database:** PostgreSQL 15
* **Migration:** Flyway
* **Messaging:** Apache Kafka 3.9.1
* **Auth:** Keycloak 25.0.1
* **Documentation:** SpringDoc OpenAPI 3
* **Build:** Maven
* **Java:** 17
* **Testing:**
    * JUnit 5.12.2
    * Mockito 5.17.0
    * Testcontainers 1.19.3
## 9. Тестирование

### Запуск тестов

**User Service:**
```bash
cd user-service
./mvnw test
```

**Order Service:**
```bash
cd order-service
./mvnw test -Dtest=KafkaIntegrationTest
```

### Покрытие тестами

| Сервис        | Unit Tests | Integration Tests | Kafka Tests | Итого |
|---------------|------------|-------------------|-------------|-------|
| User Service  | 7 ✅        | 4 ✅               | -           | 11    |
| Order Service | -          | -                 | 2 ✅         | 2     |
| **TOTAL**     | **7**      | **4**             | **2**       | **13** |

### User Service Tests

**Unit Tests (UserServiceImplTest):**
## 11. Примеры использованияоздание пользователя
* ✅ `createUser_EmailAlreadyExists_ThrowsException` — дублирующий email
* ✅ `getUserById_Success` — получение пользователя по ID
* ✅ `getUserById_NotFound_ThrowsException` — несуществующий ID
* ✅ `updateUser_Success` — обновление пользователя
* ✅ `deleteUser_Success` — удаление пользователя
* ✅ `getAllUsers_Success` — получение всех пользователей

**Integration Tests (UserControllerIntegrationTest):**
* ✅ `createUser_Success` — REST API создание пользователя
* ✅ `getUserById_Success` — REST API получение пользователя
* ✅ `getUserById_NotFound` — REST API ошибка 404
* ✅ `getAllUsers_Success` — REST API список пользователей

### Order Service Tests

**Kafka Integration Tests (KafkaIntegrationTest):**
* ✅ `whenOrderCreatedEventPublished_thenMessageIsSent` — публикация одного события
* ✅ `whenMultipleOrderEventsPublished_thenAllAreSent` — публикация нескольких событий

### Технологии тестирования

* **Unit Tests:** JUnit 5 + Mockito для изоляции бизнес-логики
* **Integration Tests:** Testcontainers + PostgreSQL для реальной БД
* **Kafka Tests:** @EmbeddedKafka для тестирования сообщений
* **Assertions:** AssertJ для fluent API
* **Async Tests:** Awaitility для ожидания асинхронных операций
* **Security Tests:** @WithMockUser для авторизации

---

## 10. Лучшие практики

✅ **Реализовано:**
* Микросервисная архитектура (3 сервиса)
* Валидация данных с `@Valid`
## 12. Дальнейшее развитиелючений с `@ControllerAdvice`
* Логирование на уровнях INFO, ERROR
* Чистый код с комментариями на английском
* Swagger документация для всех API
* Flyway миграции для версионирования БД
* Security с JWT и ролями (USER, ADMIN)
* Разделение на слои: Controller → Service → Repository
* DTOs для всех запросов и ответов
* Mappers для конвертации Entity ↔ DTO
* Event-driven architecture с Kafka
* Docker Compose для инфраструктуры
* Soft delete для ресторанов и блюд
* Индексы в БД для оптимизации
* **13 автоматических тестов** (7 unit + 4 integration + 2 Kafka)
* Testcontainers для реальной БД в тестах
* Embedded Kafka для тестирования сообщений
* Глобальная обработка исключений с `@ControllerAdvice`
* Логирование на уровнях INFO, ERROR
* Чистый код с комментариями на английском
* Swagger документация для всех API
* Flyway миграции для версионирования БД
* Security с JWT и ролями
* Разделение на слои: Controller → Service → Repository
* DTOs для всех запросов и ответов
* Mappers для конвертации Entity ↔ DTO
* Event-driven architecture с Kafka
* Docker Compose для инфраструктуры
* Soft delete для ресторанов и блюд
* Индексы в БД для оптимизации

---

## 10. Примеры использования

### Получить список ресторанов:
```bash
curl http://localhost:8081/api/restaurants
```

### Создать заказ (требуется JWT):
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "restaurantId": 1,
    "items": [
      {"menuItemId": 1, "quantity": 2},
      {"menuItemId": 2, "quantity": 1}
    ]
  }'
```

### Получить категории:
```bash
curl http://localhost:8081/api/categories
```

---

## 11. Дальнейшее развитие

* ✅ User Service для управления профилями (реализовано)
* Интеграция Order Service с Restaurant Service через REST/Feign
* Реализация Payment Service
* Delivery Service с отслеживанием курьера
* Notification Service (email, SMS, push)
* Rating & Review система
* Расширение тестового покрытия (цель: 80%+)
* Real-time updates через WebSocket
* API Gateway
* Service Discovery (Eureka)
* Distributed tracing (Zipkin)
* Monitoring (Prometheus + Grafana)
* CI/CD pipeline

