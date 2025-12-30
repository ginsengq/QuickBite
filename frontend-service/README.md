# QuickBite Frontend Service

Frontend микросервис на Next.js для приложения доставки еды QuickBite.

## Технологии

- **Next.js 14** - React фреймворк с App Router
- **TypeScript** - типизация
- **Tailwind CSS** - стилизация
- **Keycloak** - авторизация и аутентификация
- **Zustand** - управление состоянием
- **Framer Motion** - анимации
- **Axios** - HTTP клиент
- **Vanta.js** - 3D анимация для лендинга

## Функциональность

### Основные возможности:
- 🏠 **Лендинг** с 3D анимацией (Vanta.js Birds)
- 🔐 **Авторизация** через Keycloak
- 📝 **Регистрация** новых пользователей
- 🍽️ **Каталог ресторанов** с поиском
- 🍕 **Меню ресторанов** с фильтрацией
- 🛒 **Корзина покупок** с управлением количеством
- 📦 **Управление заказами** (создание, просмотр, оплата)
- 💳 **Оплата заказов** (карта, наличные, кошелек)
- 👤 **Профиль пользователя** с редактированием
- 🔔 **Уведомления** о заказах и платежах

### Интеграция с микросервисами:
- **Order Service** (localhost:8080) - управление заказами
- **Restaurant Service** (localhost:8081) - рестораны и меню
- **User Service** (localhost:8083) - пользователи
- **Payment Service** (localhost:8084) - платежи
- **Notification Service** (localhost:8085) - уведомления
- **Keycloak** (localhost:8082) - авторизация

## Установка и запуск

### Локальная разработка

1. Установите зависимости:
```bash
npm install
```

2. Создайте файл `.env.local`:
```env
NEXT_PUBLIC_KEYCLOAK_URL=http://localhost:8082
NEXT_PUBLIC_KEYCLOAK_REALM=quickbite
NEXT_PUBLIC_KEYCLOAK_CLIENT_ID=backend-api

NEXT_PUBLIC_ORDER_SERVICE_URL=http://localhost:8080
NEXT_PUBLIC_RESTAURANT_SERVICE_URL=http://localhost:8081
NEXT_PUBLIC_USER_SERVICE_URL=http://localhost:8083
NEXT_PUBLIC_PAYMENT_SERVICE_URL=http://localhost:8084
NEXT_PUBLIC_NOTIFICATION_SERVICE_URL=http://localhost:8085
```

3. Запустите dev сервер:
```bash
npm run dev
```

Приложение будет доступно на http://localhost:3000

### Сборка для production

```bash
npm run build
npm start
```

### Docker

1. Сборка образа:
```bash
docker build -t quickbite-frontend .
```

2. Запуск контейнера:
```bash
docker run -p 3000:3000 \
  -e NEXT_PUBLIC_KEYCLOAK_URL=http://localhost:8082 \
  -e NEXT_PUBLIC_ORDER_SERVICE_URL=http://localhost:8080 \
  quickbite-frontend
```

## Структура проекта

```
frontend-service/
├── app/                      # Next.js App Router
│   ├── page.tsx             # Главная страница (лендинг)
│   ├── layout.tsx           # Главный layout
│   ├── globals.css          # Глобальные стили
│   ├── auth/                # Авторизация
│   │   └── register/        # Регистрация
│   ├── restaurants/         # Рестораны
│   │   └── [id]/           # Детали ресторана
│   ├── cart/               # Корзина
│   ├── orders/             # Заказы
│   │   └── [id]/          # Детали заказа
│   ├── profile/            # Профиль пользователя
│   └── notifications/      # Уведомления
├── components/              # React компоненты
│   ├── Navbar.tsx          # Навигация
│   ├── AuthProvider.tsx    # Провайдер авторизации
│   ├── RestaurantCard.tsx  # Карточка ресторана
│   └── MenuItemCard.tsx    # Карточка блюда
├── lib/                     # Утилиты и сервисы
│   ├── keycloak.ts         # Конфигурация Keycloak
│   ├── api-client.ts       # HTTP клиент
│   └── services/           # API сервисы
│       ├── order.service.ts
│       ├── restaurant.service.ts
│       ├── user.service.ts
│       ├── payment.service.ts
│       └── notification.service.ts
├── store/                   # Zustand stores
│   ├── auth.store.ts       # Состояние авторизации
│   └── cart.store.ts       # Состояние корзины
├── types/                   # TypeScript типы
│   └── index.ts
├── public/                  # Статические файлы
│   └── silent-check-sso.html
├── Dockerfile              # Docker конфигурация
├── next.config.js          # Next.js конфигурация
├── tailwind.config.ts      # Tailwind конфигурация
└── package.json
```

## API клиенты

Все микросервисы интегрированы через централизованный API клиент с автоматической подстановкой JWT токена из Keycloak.

### Примеры использования:

```typescript
// Получить список ресторанов
const restaurants = await restaurantService.getRestaurants();

// Создать заказ
const order = await orderService.createOrder({
  userId: 1,
  restaurantId: 5,
  items: [{ menuItemId: 10, quantity: 2 }]
});

// Создать платеж
const payment = await paymentService.createPayment({
  orderId: 123,
  paymentMethod: 'CARD'
});
```

## Управление состоянием

### Auth Store (Keycloak)
```typescript
const { isAuthenticated, user, login, logout } = useAuthStore();
```

### Cart Store
```typescript
const { items, addItem, removeItem, getTotalAmount } = useCartStore();
```

## Авторизация

Приложение использует Keycloak для авторизации:

1. При первом входе происходит редирект на Keycloak
2. После успешной авторизации токен сохраняется
3. Токен автоматически обновляется каждую минуту
4. Токен добавляется ко всем API запросам

## Анимации

- **Лендинг**: 3D анимация птиц (Vanta.js Birds)
- **Переходы**: Framer Motion для плавных анимаций
- **Загрузка**: Skeleton screens для лучшего UX

## Стилизация

Используется Tailwind CSS с кастомной темой:
- Основной цвет: Orange (primary)
- Адаптивный дизайн для всех устройств
- Темная тема для лендинга
- Светлая тема для основного интерфейса

## Маршруты

- `/` - Главная (лендинг)
- `/restaurants` - Список ресторанов
- `/restaurants/:id` - Детали ресторана и меню
- `/cart` - Корзина
- `/orders` - Мои заказы
- `/orders/:id` - Детали заказа
- `/profile` - Профиль пользователя
- `/notifications` - Уведомления
- `/auth/register` - Регистрация

## Разработка

### Добавление нового сервиса:

1. Создайте сервис в `lib/services/`
2. Добавьте типы в `types/index.ts`
3. Используйте в компонентах

### Добавление новой страницы:

1. Создайте файл в `app/`
2. Используйте серверные или клиентские компоненты
3. Добавьте ссылку в навигацию

## Производительность

- Server-Side Rendering (SSR)
- Incremental Static Regeneration (ISR)
- Автоматическая оптимизация изображений
- Code splitting
- Lazy loading компонентов

## Безопасность

- JWT токены для API
- CORS политика
- XSS защита
- CSRF защита
- Валидация на клиенте и сервере

