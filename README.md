# QuickBite — Food Delivery Service

**QuickBite** — это полнофункциональный сервис доставки еды из кафе и ресторанов. Система построена по микросервисной архитектуре с использованием Kafka для обмена сообщениями, Keycloak для безопасности, PostgreSQL для хранения данных и Next.js для фронтенда.

---

## 📋 Содержание

1. [Основной функционал](#1-основной-функционал)
2. [Архитектура системы](#2-архитектура-системы)
3. [Микросервисы](#3-микросервисы)
4. [Как работает система](#4-как-работает-система)
5. [Docker и Docker Compose - простое объяснение](#5-docker-и-docker-compose---простое-объяснение)
6. [Запуск проекта](#6-запуск-проекта)
7. [Тестирование](#7-тестирование)
8. [Kafka - обмен сообщениями](#8-kafka---обмен-сообщениями)
9. [Безопасность (Keycloak)](#9-безопасность-keycloak)
10. [База данных](#10-база-данных)
11. [API документация (Swagger)](#11-api-документация-swagger)
12. [Технологический стек](#12-технологический-стек)

---

## 1. Основной функционал

### Что делает приложение

* **🎨 Современный веб-интерфейс** — красивый лендинг с 3D анимацией и удобный интерфейс на Next.js
* **Просмотр ресторанов и меню** — пользователи могут просматривать доступные рестораны, категории еды и меню
* **Создание заказов** — пользователи могут создавать заказы из выбранных блюд
* **Оплата заказов** — автоматическая обработка платежей при создании заказа
* **Уведомления** — автоматические уведомления о создании заказа, оплате и других событиях
* **Управление пользователями** — регистрация и управление профилями пользователей
* **Администраторский функционал** — управление ресторанами, меню, заказами и пользователями

### Целевая аудитория

* **Пользователи** — создание заказов, отслеживание доставки, оплата
* **Администраторы ресторанов** — управление меню и заказами
* **Системные администраторы** — мониторинг и управление всеми сервисами

---

## 2. Архитектура системы

### Общая структура проекта

```
QuickBite/
├─ frontend-service/        # Next.js фронтенд (порт 3000) ⭐ НОВОЕ
├─ order-service/           # Сервис управления заказами (порт 8080)
├─ restaurant-service/      # Сервис ресторанов и меню (порт 8081)
├─ user-service/            # Сервис управления пользователями (порт 8083)
├─ payment-service/         # Сервис обработки платежей (порт 8084)
├─ notification-service/    # Сервис уведомлений (порт 8085)
├─ keycloak/                # Keycloak конфигурация
└─ docker-compose.yml       # Инфраструктура (PostgreSQL, Kafka, Keycloak)
```

### Диаграмма архитектуры

```
                        ┌─────────────────────────────┐
                        │   Frontend (Next.js) :3000  │
                        │   🎨 Веб-интерфейс          │
                        └──────────────┬──────────────┘
                                       │ HTTP + JWT
┌──────────────────────────────────────┼──────────────────────────────────────┐
│                        Keycloak (8082)                                       │
│                    (Аутентификация)                                          │
└───────────────────────┬───────────────────────────────────────────────────────┘
                        │ JWT Tokens
        ┌───────────────┼───────────────┬───────────────┬───────────────┐
        │               │               │               │               │
        v               v               v               v               v
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│Order Service │ │Restaurant    │ │User Service  │ │Payment       │ │Notification │
│   :8080      │ │Service :8081 │ │   :8083      │ │Service :8084 │ │Service :8085 │
└──────┬───────┘ └──────┬───────┘ └──────┬───────┘ └──────┬───────┘ └──────┬───────┘
       │                 │                 │               │               │
       │                 │                 │               │               │
       v                 v                 v               v               v
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│PostgreSQL    │ │PostgreSQL    │ │PostgreSQL    │ │PostgreSQL    │ │PostgreSQL    │
│Orders :5432  │ │Restaurant    │ │Users :5434   │ │Payments :5435│ │Notifications│
│              │ │:5433         │ │              │ │              │ │:5436         │
└──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘
       │                 │                 │               │               │
       └─────────────────┴─────────────────┴───────────────┴───────────────┘
                                    │
                                    v
                          ┌─────────────────┐
                          │  Kafka :9092    │
                          │  (Event Bus)    │
                          └─────────────────┘
```

---

## 3. Микросервисы

### 3.0. Frontend Service (Порт: 3000) ⭐ НОВОЕ

**Назначение:** Веб-интерфейс для пользователей и администраторов

**Технологии:**
* Next.js 14 (App Router)
* TypeScript
* Tailwind CSS
* Keycloak (авторизация)
* Vanta.js (3D анимации)
* Framer Motion (анимации)

**Функциональность:**
* 🏠 Лендинг с 3D анимацией птиц
* 🔐 Авторизация через Keycloak
* 📝 Регистрация пользователей
* 🍽️ Каталог ресторанов с поиском
* 🍕 Меню с корзиной покупок
* 📦 Управление заказами
* 💳 Оплата заказов
* 👤 Профиль пользователя
* 🔔 Уведомления

**Маршруты:**
* `/` - Главная (лендинг)
* `/restaurants` - Каталог ресторанов
* `/restaurants/:id` - Меню ресторана
* `/cart` - Корзина
* `/orders` - Мои заказы
* `/orders/:id` - Детали заказа
* `/profile` - Профиль
* `/notifications` - Уведомления
* `/auth/register` - Регистрация

### 3.1. Order Service (Порт: 8080)

**Назначение:** Управление жизненным циклом заказов

**Endpoints:**
* `POST /api/orders` — создать заказ (USER, ADMIN)
* `GET /api/orders/{id}` — получить заказ по ID (USER, ADMIN)
* `GET /api/orders?userId={userId}` — список заказов пользователя (USER, ADMIN)
* `PATCH /api/orders/{id}/status` — изменить статус заказа (ADMIN)

**База данных:** `quickbite_orders` (PostgreSQL:5432)
* Таблицы: `orders`, `order_items`

**Kafka:**
* **Публикует:** `OrderCreatedEvent` → топик `order-created`
* **Слушает:** `UserCreatedEvent` → топик `user-events`

**Swagger:** http://localhost:8080/swagger-ui/index.html

---

### 3.2. Restaurant Service (Порт: 8081)

**Назначение:** Управление ресторанами, меню и категориями

**Endpoints:**

*Публичные (без авторизации):*
* `GET /api/restaurants` — список ресторанов
* `GET /api/restaurants/{id}` — детали ресторана
* `GET /api/restaurants/search?keyword=X` — поиск ресторанов
* `GET /api/categories` — список категорий
* `GET /api/menu-items/restaurant/{restaurantId}` — меню ресторана
* `GET /api/menu-items/category/{categoryId}` — блюда по категории
* `GET /api/menu-items/prices?ids=1,2,3` — получить цены блюд

*Только для администраторов:*
* `POST /api/restaurants` — добавить ресторан (ADMIN)
* `PUT /api/restaurants/{id}` — обновить ресторан (ADMIN)
* `DELETE /api/restaurants/{id}` — удалить ресторан (ADMIN)
* `POST /api/categories` — создать категорию (ADMIN)
* `POST /api/menu-items` — добавить блюдо (ADMIN)
* `PUT /api/menu-items/{id}` — обновить блюдо (ADMIN)
* `DELETE /api/menu-items/{id}` — удалить блюдо (ADMIN)

**База данных:** `quickbite_restaurant` (PostgreSQL:5433)
* Таблицы: `restaurants`, `menu_items`, `categories`, `menu_item_categories`

**Kafka:**
* **Публикует:** `MenuUpdatedEvent` → топик `menu-updated`

**Swagger:** http://localhost:8081/swagger-ui/index.html

---

### 3.3. User Service (Порт: 8083)

**Назначение:** Управление пользователями

**Endpoints:**
* `POST /api/users` — создать пользователя (ADMIN)
* `GET /api/users/{id}` — получить пользователя (USER, ADMIN)
* `GET /api/users` — список всех пользователей (ADMIN)
* `PUT /api/users/{id}` — обновить пользователя (USER, ADMIN)
* `DELETE /api/users/{id}` — удалить пользователя (ADMIN)

**База данных:** `quickbite_users` (PostgreSQL:5434)
* Таблицы: `users`

**Kafka:**
* **Публикует:** `UserCreatedEvent` → топик `user-events`

**Swagger:** http://localhost:8083/swagger-ui/index.html

---

### 3.4. Payment Service (Порт: 8084) ⭐ НОВЫЙ

**Назначение:** Обработка платежей за заказы

**Endpoints:**
* `POST /api/payments` — обработать платеж (USER, ADMIN)
* `GET /api/payments/{id}` — получить платеж по ID (USER, ADMIN)
* `GET /api/payments/order/{orderId}` — получить платеж по заказу (USER, ADMIN)
* `GET /api/payments/user/{userId}` — список платежей пользователя (USER, ADMIN)
* `GET /api/payments` — список всех платежей (ADMIN)

**База данных:** `quickbite_payments` (PostgreSQL:5435)
* Таблицы: `payments`

**Kafka:**
* **Слушает:** `OrderCreatedEvent` → топик `order-created` (автоматически создает pending платеж)
* **Публикует:** `PaymentCompletedEvent` → топик `payment-completed`

**Swagger:** http://localhost:8084/swagger-ui/index.html

**Как работает:**
1. Когда создается заказ, Payment Service автоматически создает pending платеж
2. Пользователь может обработать платеж через API
3. После успешной оплаты публикуется событие `PaymentCompletedEvent`

---

### 3.5. Notification Service (Порт: 8085) ⭐ НОВЫЙ

**Назначение:** Отправка уведомлений пользователям

**Endpoints:**
* `GET /api/notifications/user/{userId}` — получить уведомления пользователя (USER, ADMIN)
* `GET /api/notifications` — получить все уведомления (ADMIN)

**База данных:** `quickbite_notifications` (PostgreSQL:5436)
* Таблицы: `notifications`

**Kafka:**
* **Слушает:**
  * `OrderCreatedEvent` → топик `order-created` (отправляет уведомление о создании заказа)
  * `PaymentCompletedEvent` → топик `payment-completed` (отправляет уведомление об оплате)
  * `UserCreatedEvent` → топик `user-events` (отправляет приветственное уведомление)

**Swagger:** http://localhost:8085/swagger-ui/index.html

**Типы уведомлений:**
* `ORDER_CREATED` — уведомление о создании заказа
* `PAYMENT_COMPLETED` — уведомление об успешной оплате
* `WELCOME` — приветственное уведомление для новых пользователей

---

## 4. Как работает система

### Пример полного потока: Создание заказа

1. **Пользователь создает заказ** → `POST /api/orders` (Order Service)
   - Order Service создает заказ в базе данных
   - Order Service публикует событие `OrderCreatedEvent` в Kafka

2. **Payment Service получает событие** → слушает топик `order-created`
   - Автоматически создает pending платеж для заказа

3. **Notification Service получает событие** → слушает топик `order-created`
   - Отправляет уведомление пользователю о создании заказа

4. **Пользователь оплачивает заказ** → `POST /api/payments` (Payment Service)
   - Payment Service обрабатывает платеж
   - Payment Service публикует событие `PaymentCompletedEvent` в Kafka

5. **Notification Service получает событие оплаты** → слушает топик `payment-completed`
   - Отправляет уведомление пользователю об успешной оплате

### Схема взаимодействия через Kafka

```
Order Service          Payment Service        Notification Service
     │                        │                        │
     │  OrderCreatedEvent     │                        │
     ├────────────────────────>│                        │
     │                        │                        │
     │                        │  OrderCreatedEvent     │
     ├────────────────────────────────────────────────>│
     │                        │                        │
     │                        │  PaymentCompletedEvent │
     │                        ├────────────────────────>│
     │                        │                        │
```

---

## 5. Docker и Docker Compose - простое объяснение

### Что такое Docker?

**Docker** — это инструмент, который позволяет упаковать приложение и все его зависимости в "контейнер". Контейнер — это как легкий виртуальный компьютер, который работает одинаково на любой машине.

**Простая аналогия:**
- Представьте, что ваше приложение — это мебель (стол, стул, лампа)
- Docker — это коробка, в которую вы упаковываете всю мебель
- Эта коробка может быть открыта и использована на любой машине, и мебель будет работать одинаково

### Что такое Docker Compose?

**Docker Compose** — это инструмент, который позволяет запускать несколько контейнеров одновременно и управлять ими как одной системой.

**Простая аналогия:**
- Если Docker — это коробка для одной вещи
- Docker Compose — это склад, где все коробки организованы и работают вместе

### В нашем проекте

В файле `docker-compose.yml` мы определяем:

1. **Базы данных PostgreSQL** (5 штук):
   - `postgres-orders` — для Order Service
   - `postgres-restaurants` — для Restaurant Service
   - `postgres-users` — для User Service
   - `postgres-payments` — для Payment Service
   - `postgres-notifications` — для Notification Service

2. **Kafka и Zookeeper**:
   - `zookeeper` — координатор для Kafka
   - `kafka` — брокер сообщений
   - `kafka-ui` — веб-интерфейс для мониторинга Kafka

3. **Keycloak**:
   - Сервис аутентификации и авторизации

4. **Микросервисы** (5 штук):
   - `order-service`
   - `restaurant-service`
   - `user-service`
   - `payment-service`
   - `notification-service`

### Как Docker Compose работает

Когда вы запускаете `docker-compose up`, происходит следующее:

1. **Docker читает файл `docker-compose.yml`**
2. **Создает сеть** — все контейнеры могут общаться друг с другом
3. **Создает volumes** — постоянное хранилище для баз данных
4. **Запускает контейнеры** в правильном порядке:
   - Сначала базы данных (они должны быть готовы)
   - Потом Kafka и Zookeeper
   - Потом Keycloak
   - В конце микросервисы

### Преимущества Docker

✅ **Изоляция** — каждое приложение работает в своем контейнере
✅ **Портативность** — работает одинаково на любой машине
✅ **Простота** — одна команда запускает всю систему
✅ **Масштабируемость** — легко добавить больше экземпляров сервиса

---

## 6. Запуск проекта

### Предварительные требования

* Java 17+
* Maven 3.8+
* Docker & Docker Compose

### Шаги запуска

#### Шаг 1: Запустить инфраструктуру (Docker)

```bash
docker-compose up -d
```

Эта команда:
- Запускает все базы данных PostgreSQL
- Запускает Kafka и Zookeeper
- Запускает Keycloak
- Создает сеть для связи между сервисами

**Проверить статус:**
```bash
docker-compose ps
```

Должны быть запущены:
- postgres-orders, postgres-restaurants, postgres-users, postgres-payments, postgres-notifications
- zookeeper, kafka, kafka-ui
- keycloak

#### Шаг 2: Настроить Keycloak

1. Откройте http://localhost:8082/admin
   - Username: `admin`
   - Password: `admin`

2. Создайте Realm `quickbite` (см. раздел 8)

3. Создайте Client `backend-api`

4. Создайте роли: `ROLE_USER`, `ROLE_ADMIN`

5. Создайте тестового пользователя

#### Шаг 3: Запустить микросервисы

**Order Service:**
```bash
cd order-service
./mvnw spring-boot:run
```

**Restaurant Service:**
```bash
cd restaurant-service
./mvnw spring-boot:run
```

**User Service:**
```bash
cd user-service
./mvnw spring-boot:run
```

**Payment Service:**
```bash
cd payment-service
./mvnw spring-boot:run
```

**Notification Service:**
```bash
cd notification-service
./mvnw spring-boot:run
```

**Frontend Service:**
```bash
cd frontend-service
npm install
npm run dev
```

### Остановка

**Остановить инфраструктуру:**
```bash
docker-compose down
```

**Остановить с удалением данных:**
```bash
docker-compose down -v
```

---

## 7. Тестирование

### 📊 Статистика тестов

QuickBite имеет **48 тестов** на 5 микросервисах:

| Сервис | Unit | Integration | Kafka | Всего |
|--------|------|-------------|-------|-------|
| order-service | 7 | 2 | 1 | **10** |
| restaurant-service | 7 | 2 | - | **9** |
| user-service | 7 | 4 | - | **11** |
| payment-service | 7 | 2 | - | **9** |
| notification-service | 7 | 2 | - | **9** |
| **ИТОГО** | **35** | **12** | **1** | **48** |

### Запуск тестов

**Все тесты всех сервисов:**
```bash
mvn clean verify
```

**Тесты конкретного сервиса:**
```bash
cd order-service
mvn test
```

**Только unit тесты:**
```bash
mvn test
```

**Только integration тесты:**
```bash
mvn integration-test
```

### Обязательные тесты при билде

✅ **Тесты ОБЯЗАТЕЛЬНЫ для успешного билда!**

- Сборка **НЕ ПРОЙДЁТ** если хотя бы один тест упал
- Сборка **НЕ ПРОЙДЁТ** если тестов не найдено
- Используется Maven Surefire (unit) и Failsafe (integration) плагины

### Технологии тестирования

- **JUnit 5** - фреймворк тестирования
- **Mockito** - моки и стабы
- **Testcontainers** - PostgreSQL в Docker для интеграционных тестов
- **Spring Boot Test** - тестирование Spring приложений
- **@SpringBootTest** - полный контекст приложения
- **@WithMockUser** - тестирование безопасности

📖 **Подробная документация:** [TESTING.md](TESTING.md)

---

## 8. Kafka - обмен сообщениями

### Что такое Kafka?

**Apache Kafka** — это система обмена сообщениями, которая позволяет сервисам общаться друг с другом асинхронно (не ждать ответа).

**Простая аналогия:**
- Представьте почтовый ящик
- Один сервис кладет письмо (публикует событие)
- Другие сервисы читают письма из ящика (слушают события)

### Топики в нашем проекте

| Топик | Публикует | Слушает | Описание |
|-------|-----------|---------|----------|
| `order-created` | Order Service | Payment Service, Notification Service | Событие создания заказа |
| `payment-completed` | Payment Service | Notification Service | Событие успешной оплаты |
| `user-events` | User Service | Order Service, Notification Service | Событие создания пользователя |
| `menu-updated` | Restaurant Service | - | Событие обновления меню |

### Как это работает

1. **Order Service создает заказ** → публикует `OrderCreatedEvent` в топик `order-created`
2. **Payment Service слушает** топик `order-created` → автоматически создает pending платеж
3. **Notification Service слушает** топик `order-created` → отправляет уведомление пользователю
4. **Payment Service обрабатывает платеж** → публикует `PaymentCompletedEvent` в топик `payment-completed`
5. **Notification Service слушает** топик `payment-completed` → отправляет уведомление об оплате

### Kafka UI

Мониторинг сообщений: http://localhost:8090

Здесь вы можете:
- Просматривать все топики
- Видеть сообщения в реальном времени
- Проверять статус consumer groups

---

## 9. Безопасность (Keycloak)

### Что такое Keycloak?

**Keycloak** — это сервис для управления пользователями, аутентификации и авторизации.

**Простая аналогия:**
- Keycloak — это охранник на входе
- Он проверяет вашу личность (аутентификация)
- Он проверяет, куда вы можете войти (авторизация)

### Настройка Keycloak

#### 1. Войти в Keycloak Admin Console

Откройте http://localhost:8082/admin
- **Username:** admin
- **Password:** admin

#### 2. Создать Realm `quickbite`

1. Нажмите на dropdown в левом верхнем углу (где написано "master")
2. Нажмите "Create Realm"
3. **Realm name:** quickbite
4. **Enabled:** ON
5. Нажмите "Create"

#### 3. Создать Client `backend-api`

1. В realm `quickbite` перейдите в **Clients** → **Create client**
2. **Client ID:** backend-api
3. **Client authentication:** ON
4. **Valid redirect URIs:** `http://localhost:8080/*`, `http://localhost:8081/*`, `http://localhost:8083/*`, `http://localhost:8084/*`, `http://localhost:8085/*`
5. **Web origins:** `*`
6. Сохранить

#### 4. Создать Roles

1. Перейдите в **Realm roles** → **Create role**
2. Создайте роли:
   - `ROLE_USER`
   - `ROLE_ADMIN`

#### 5. Создать тестового пользователя

1. Перейдите в **Users** → **Add user**
2. **Username:** testuser
3. **Email:** testuser@example.com
4. **Email verified:** ON
5. Сохранить

6. Перейти на вкладку **Credentials**
   - Установить пароль: `password`
   - **Temporary:** OFF

7. Перейти на вкладку **Role mappings**
   - **Assign role** → выбрать `ROLE_USER` и `ROLE_ADMIN`

#### 6. Получить JWT токен

```bash
curl -X POST http://localhost:8082/realms/quickbite/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=backend-api" \
  -d "client_secret=YOUR_CLIENT_SECRET" \
  -d "username=testuser" \
  -d "password=password"
```

**Получить client_secret:**
1. В Keycloak: **Clients** → **backend-api** → вкладка **Credentials**
2. Скопировать **Client secret**

#### 7. Использование JWT в Swagger UI

1. Откройте Swagger UI (например: http://localhost:8080/swagger-ui/index.html)
2. Нажмите кнопку **Authorize** (замок в правом верхнем углу)
3. В поле **Value** введите: `Bearer YOUR_ACCESS_TOKEN`
4. Нажмите **Authorize**

---

## 10. База данных

### Структура баз данных

Каждый микросервис имеет свою базу данных PostgreSQL:

| Сервис | База данных | Порт | Таблицы |
|--------|-------------|------|---------|
| Order Service | `quickbite_orders` | 5432 | `orders`, `order_items` |
| Restaurant Service | `quickbite_restaurant` | 5433 | `restaurants`, `menu_items`, `categories`, `menu_item_categories` |
| User Service | `quickbite_users` | 5434 | `users` |
| Payment Service | `quickbite_payments` | 5435 | `payments` |
| Notification Service | `quickbite_notifications` | 5436 | `notifications` |

### Миграции (Flyway)

Все изменения в базе данных версионируются через Flyway миграции:

- `V1__create_*.sql` — создание таблиц
- `V2__insert_*.sql` — вставка тестовых данных (если есть)

Миграции выполняются автоматически при запуске сервиса.

---

## 11. API документация (Swagger)

Каждый микросервис имеет свою Swagger документацию:

| Сервис | Swagger UI URL |
|--------|----------------|
| Order Service | http://localhost:8080/swagger-ui/index.html |
| Restaurant Service | http://localhost:8081/swagger-ui/index.html |
| User Service | http://localhost:8083/swagger-ui/index.html |
| Payment Service | http://localhost:8084/swagger-ui/index.html |
| Notification Service | http://localhost:8085/swagger-ui/index.html |

В Swagger UI вы можете:
- Просматривать все доступные endpoints
- Тестировать API прямо из браузера
- Видеть примеры запросов и ответов
- Авторизоваться с JWT токеном

---

## 12. Технологический стек

### Backend
* **Spring Boot 3.5.8** — фреймворк для создания микросервисов
* **Spring Data JPA** — работа с базой данных
* **Spring Security** — безопасность
* **Spring Kafka** — интеграция с Kafka
* **Flyway** — миграции базы данных

### База данных
* **PostgreSQL 15** — реляционная база данных

### Messaging
* **Apache Kafka 3.9.1** — обмен сообщениями между сервисами

### Security
* **Keycloak 25.0.1** — аутентификация и авторизация
* **JWT** — токены для доступа к API

### Documentation
* **SpringDoc OpenAPI 3** — автоматическая генерация API документации

### Infrastructure
* **Docker** — контейнеризация
* **Docker Compose** — оркестрация контейнеров

### Build Tool
* **Maven** — управление зависимостями и сборка проекта

### Java
* **Java 17** — язык программирования

---

## 📝 Примеры использования

### Создать заказ

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

### Обработать платеж

```bash
curl -X POST http://localhost:8084/api/payments \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "paymentMethod": "CARD",
    "cardNumber": "1234567890123456",
    "cardHolderName": "John Doe",
    "expiryDate": "12/25",
    "cvv": "123"
  }'
```

### Получить уведомления пользователя

```bash
curl -X GET http://localhost:8085/api/notifications/user/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🎯 Итоговая архитектура

Теперь у нас есть **полнофункциональный backend** с 5 микросервисами:

1. **Order Service** — управление заказами
2. **Restaurant Service** — управление ресторанами и меню
3. **User Service** — управление пользователями
4. **Payment Service** — обработка платежей ⭐
5. **Notification Service** — отправка уведомлений ⭐

Все сервисы:
- ✅ Общаются через Kafka
- ✅ Имеют свои базы данных
- ✅ Защищены через Keycloak
- ✅ Документированы через Swagger
- ✅ Запускаются через Docker Compose

---

## 🚀 Дальнейшее развитие

* Delivery Service — управление доставкой и курьерами
* Rating Service — отзывы и рейтинги
* Analytics Service — аналитика и отчеты
* API Gateway — единая точка входа
* Service Discovery — автоматическое обнаружение сервисов
* Distributed Tracing — отслеживание запросов между сервисами
* Monitoring — Prometheus + Grafana

---

## 📞 Поддержка

Если у вас возникли вопросы или проблемы:
1. Проверьте логи сервисов
2. Проверьте статус контейнеров: `docker-compose ps`
3. Проверьте Kafka UI: http://localhost:8090
4. Проверьте Swagger документацию для каждого сервиса

---

**Удачной разработки! 🎉**
