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
src/main/java/com/example/quickbite/
├─ controller/       # REST API контроллеры
├─ service/          # Бизнес-логика
├─ repository/       # Spring Data JPA репозитории
├─ entity/           # Сущности / модели
├─ dto/              # Request/Response DTOs
├─ config/           # Настройки Kafka, Keycloak, Security
├─ exception/        # Обработка ошибок, Advice
```

### Микросервисы

#### 1. Order Service

* **Endpoints:**

    * `POST /orders` — создать заказ
    * `GET /orders/{id}` — получить заказ
    * `GET /orders` — список заказов
    * `PUT /orders/{id}` — обновить заказ
    * `DELETE /orders/{id}` — удалить заказ
    * `PATCH /orders/{id}/status` — изменить статус заказа
* **Database:** Orders, OrderItems, Customers
* **Kafka:** Producer → `OrderCreatedEvent`, Consumer → обновление статуса
* **Security:** ROLE_USER, ROLE_ADMIN
* **Validation:** DTO + `@Valid`, глобальный `@ControllerAdvice`

#### 2. Restaurant & Menu Service

* **Endpoints:**

    * `GET /restaurants` — список ресторанов
    * `GET /restaurants/{id}` — детали ресторана
    * `GET /categories` — список категорий
    * `GET /menu/{restaurantId}` — меню ресторана
    * `POST /restaurants` — добавить ресторан (Admin)
    * `PUT /menu/{id}` — обновить блюдо (Admin)
* **Database:** Restaurants, MenuItems, Categories
* **Kafka:** `MenuUpdatedEvent` при изменении меню

---

## 3. Kafka Integration

* **События:** `OrderCreatedEvent`, `MenuUpdatedEvent`, `PaymentCompletedEvent`
* **Логика:** изменение статуса заказа, уведомления, логирование
* **Тестирование:** Embedded Kafka

---

## 4. Безопасность (Keycloak)

* **Realm:** QuickBiteRealm
* **Роли:** ROLE_USER, ROLE_ADMIN
* **Клиент:** backend-api (confidential)
* **Интеграция с backend:** Spring Security Resource Server
* **Эндпоинты:**

    * Public: `/public/**`
    * Authenticated: `/api/user/**`
    * Admin-only: `/api/admin/**`

---

## 5. База данных & Flyway

* **СУБД:** PostgreSQL
* **Миграции:** Flyway (`V1__init.sql`, `V2__add_orders.sql`, …)
* **Репозитории:** Spring Data JPA (CrudRepository / JpaRepository)

---

## 6. Swagger / OpenAPI

* Полная документация доступна по `/swagger-ui/index.html`
* Автоматическая генерация схем API и моделей DTO
* Примеры запросов и ответов для всех эндпоинтов

