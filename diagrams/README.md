# QuickBite PlantUML Диаграммы

Этот каталог содержит PlantUML диаграммы для проекта QuickBite.

## Установка PlantUML

### Вариант 1: VS Code Extension
1. Установите расширение "PlantUML" в VS Code
2. Установите Java (требуется для PlantUML)
3. Установите Graphviz: `choco install graphviz` (Windows) или `brew install graphviz` (macOS)

### Вариант 2: Online Editor
Откройте файлы `.puml` в онлайн редакторе: http://www.plantuml.com/plantuml/uml/

### Вариант 3: Командная строка
```bash
# Установка
npm install -g node-plantuml

# Генерация PNG
puml generate use-case.puml -o use-case.png
```

## Список диаграмм

### 1. Use Case Diagram (`use-case.puml`)
**Описание:** Диаграмма вариантов использования показывает основные функции системы и их связь с акторами (пользователь, администратор, владелец ресторана).

**Основные use cases:**
- Управление аккаунтом (регистрация, вход, профиль)
- Просмотр и поиск ресторанов
- Управление корзиной и заказами
- Оплата заказов
- Получение уведомлений
- Администрирование

**Как открыть:**
- В VS Code: откройте файл и нажмите `Alt+D` для предпросмотра
- Online: скопируйте содержимое в http://www.plantuml.com/plantuml/uml/

### 2. Class Diagram (`class-diagram.puml`)
**Описание:** UML диаграмма классов показывает структуру доменных моделей всех микросервисов.

**Включает:**
- **Order Service:** Order, OrderItem, OrderStatus
- **Restaurant Service:** Restaurant, MenuItem, Category
- **User Service:** User, UserRole
- **Payment Service:** Payment, PaymentStatus
- **Notification Service:** Notification, NotificationType

**Отношения:**
- Композиция (Order ◆→ OrderItem)
- Наследование и реализация интерфейсов
- Зависимости между сервисами (пунктирные линии)

### 3. Sequence Diagram (`sequence-create-order.puml`)
**Описание:** Диаграмма последовательности показывает полный flow создания и оплаты заказа.

**Основные шаги:**
1. **Аутентификация:** Получение JWT токена от Keycloak
2. **Просмотр меню:** Загрузка ресторанов и блюд
3. **Создание заказа:**
   - Запрос цен из Restaurant Service
   - Расчет totalPrice
   - Сохранение заказа (status=CREATED)
   - Публикация события "order-created" в Kafka
4. **Оплата:**
   - Создание платежа
   - Симуляция payment gateway (1 сек)
   - Публикация "payment-completed" в Kafka
5. **Обновление статуса:**
   - Order Service обрабатывает событие
   - Обновляет статус на CONFIRMED
6. **Автообновление:** Frontend polling каждые 5 секунд

### 4. Component Diagram (`component-diagram.puml`)
**Описание:** Компонентная диаграмма показывает архитектуру системы на уровне компонентов.

**Слои:**
- **Client Layer:** Web Browser
- **Frontend Layer:** Next.js, Keycloak Client
- **Auth Layer:** Keycloak Server
- **Microservices:**
  - Order Service (API, Logic, Clients, Consumers)
  - Restaurant Service
  - User Service
  - Payment Service
  - Notification Service
- **Message Broker:** Kafka с топиками
- **Databases:** 5 PostgreSQL инстансов

**Связи:**
- REST API (сплошные линии)
- Kafka Events (пунктирные линии)
- Database connections

## Как генерировать изображения

### В VS Code
1. Откройте `.puml` файл
2. Нажмите `Alt+D` для preview
3. Правый клик → "Export Current Diagram" → выберите формат (PNG, SVG)

### Командная строка (если установлен PlantUML)
```bash
# PNG
java -jar plantuml.jar use-case.puml

# SVG
java -jar plantuml.jar -tsvg use-case.puml

# Все файлы
java -jar plantuml.jar *.puml
```

### PowerShell скрипт для генерации всех диаграмм
```powershell
# Создать скрипт generate-diagrams.ps1
$files = Get-ChildItem -Filter "*.puml"
foreach ($file in $files) {
    $output = $file.BaseName + ".png"
    java -jar plantuml.jar $file.Name -o "../docs/diagrams"
    Write-Host "Generated: $output"
}
```

## Интеграция с документацией

Сгенерированные изображения можно добавить в README.md или ARCHITECTURE.md:

```markdown
## Архитектура системы

### Use Case диаграмма
![Use Case Diagram](diagrams/use-case.png)

### Компонентная диаграмма
![Component Diagram](diagrams/component-diagram.png)

### Sequence диаграмма - Создание заказа
![Sequence Diagram](diagrams/sequence-create-order.png)
```

## Редактирование

При изменении кода диаграмм:
1. Отредактируйте `.puml` файл
2. Сохраните (в VS Code с расширением PlantUML preview обновится автоматически)
3. Экспортируйте новое изображение

## Полезные ссылки

- PlantUML документация: https://plantuml.com/
- Примеры диаграмм: https://real-world-plantuml.com/
- VS Code расширение: https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml
- Online редактор: http://www.plantuml.com/plantuml/uml/
