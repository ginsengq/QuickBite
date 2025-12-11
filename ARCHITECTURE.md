# QuickBite - Technical Architecture

## Overview

QuickBite is a microservices-based food delivery platform implementing modern enterprise patterns with Spring Boot, PostgreSQL, Apache Kafka, and Keycloak.

## Architecture Diagram

```
┌─────────────┐
│   Keycloak  │ (Authentication & Authorization)
│   :8082     │
└──────┬──────┘
       │ JWT Tokens
       │
       ├──────────────────┬──────────────────┐
       │                  │                  │
       v                  v                  v
┌──────────────┐   ┌──────────────┐   ┌──────────────┐
│ Order Service│   │Restaurant Svc│   │ Future: User │
│    :8080     │   │    :8081     │   │   Service    │
└──────┬───────┘   └──────┬───────┘   └──────────────┘
       │                  │
       │                  │
       v                  v
┌──────────────┐   ┌──────────────┐
│  PostgreSQL  │   │  PostgreSQL  │
│ orders :5432 │   │  rest. :5433 │
└──────────────┘   └──────────────┘
       │                  │
       └────────┬─────────┘
                │
                v
        ┌──────────────┐
        │  Kafka :9092 │
        │  Events Bus  │
        └──────────────┘
```

## Component Details

### 1. Order Service

**Responsibility:** Manage customer orders lifecycle

**Technology Stack:**
- Spring Boot 3.5.8
- Spring Data JPA
- Spring Security OAuth2 Resource Server
- Spring Kafka
- Flyway
- PostgreSQL
- Lombok
- SpringDoc OpenAPI

**Domain Model:**
```
Order
├── id: Long
├── userId: Long
├── restaurantId: Long
├── status: OrderStatus (CREATED, COOKING, ON_THE_WAY, DELIVERED, CANCELED)
├── totalPrice: Long
├── createdAt: Instant
├── updatedAt: Instant
└── items: List<OrderItem>

OrderItem
├── id: Long
├── orderId: Long
├── menuItemId: Long
├── quantity: Integer
└── price: Long
```

**API Endpoints:**
- `POST /api/orders` - create order
- `GET /api/orders/{id}` - get order by id
- `GET /api/orders?userId={id}` - get user orders
- `PATCH /api/orders/{id}/status` - update order status

**Security:**
- All endpoints require JWT authentication
- Status updates require ADMIN role
- Order creation requires USER or ADMIN role

**Events Published:**
- `OrderCreatedEvent` → order-created topic
- `OrderStatusChangedEvent` → order-status topic

### 2. Restaurant Service

**Responsibility:** Manage restaurants, menus, and food categories

**Technology Stack:**
- Spring Boot 3.5.8
- Spring Data JPA
- Spring Security OAuth2 Resource Server
- Spring Kafka
- Flyway
- PostgreSQL
- Lombok
- SpringDoc OpenAPI

**Domain Model:**
```
Restaurant
├── id: Long
├── name: String
├── description: String
├── address: String
├── phoneNumber: String
├── isActive: Boolean
├── rating: Double
├── createdAt: Instant
├── updatedAt: Instant
└── menuItems: List<MenuItem>

MenuItem
├── id: Long
├── name: String
├── description: String
├── price: Long
├── imageUrl: String
├── isAvailable: Boolean
├── restaurantId: Long
├── categories: List<Category>
├── createdAt: Instant
└── updatedAt: Instant

Category
├── id: Long
├── name: String (unique)
├── description: String
├── imageUrl: String
├── createdAt: Instant
└── updatedAt: Instant
```

**API Endpoints:**

*Public:*
- `GET /api/restaurants` - list all restaurants
- `GET /api/restaurants/{id}` - restaurant details
- `GET /api/restaurants/search?keyword=X` - search restaurants
- `GET /api/categories` - list all categories
- `GET /api/menu-items/restaurant/{id}` - restaurant menu
- `GET /api/menu-items/category/{id}` - items by category
- `GET /api/menu-items/prices?ids=1,2,3` - get prices

*Admin Only:*
- `POST /api/restaurants` - create restaurant
- `PUT /api/restaurants/{id}` - update restaurant
- `DELETE /api/restaurants/{id}` - soft delete restaurant
- `POST /api/categories` - create category
- `POST /api/menu-items` - create menu item
- `PUT /api/menu-items/{id}` - update menu item
- `DELETE /api/menu-items/{id}` - soft delete menu item

**Events Published:**
- `MenuUpdatedEvent` → menu-updated topic

### 3. Keycloak (Identity & Access Management)

**Configuration:**
- Realm: `quickbite`
- Client: `backend-api` (confidential)
- Roles: `user`, `admin`
- Protocol: OpenID Connect
- Token Type: JWT

**Flow:**
1. User authenticates with Keycloak
2. Keycloak issues JWT token
3. Client includes token in Authorization header
4. Services validate token and extract roles
5. Role-based access control applied

### 4. Apache Kafka (Event Streaming)

**Topics:**
- `order-created` - new order notifications
- `order-status` - order status updates
- `menu-updated` - menu changes

**Usage Pattern:**
- Asynchronous event-driven communication
- Decouples services
- Enables scalability
- Provides audit trail

**Profiles:**
- Default: Uses `LoggingOrderEventPublisher` (stub)
- `kafka`: Uses `KafkaOrderEventPublisher` (real Kafka)

### 5. PostgreSQL (Data Persistence)

**Order Database (port 5432):**
```sql
tables:
  - orders (id, user_id, restaurant_id, status, total_price, created_at, updated_at)
  - order_items (id, order_id, menu_item_id, quantity, price)
```

**Restaurant Database (port 5433):**
```sql
tables:
  - restaurants (id, name, description, address, phone_number, is_active, rating, created_at, updated_at)
  - menu_items (id, name, description, price, image_url, is_available, restaurant_id, created_at, updated_at)
  - categories (id, name, description, image_url, created_at, updated_at)
  - menu_item_categories (menu_item_id, category_id) -- junction table
```

## Design Patterns

### Layered Architecture
```
Controller (REST API)
    ↓
Service (Business Logic)
    ↓
Repository (Data Access)
    ↓
Database
```

### DTO Pattern
- Separate DTOs for requests and responses
- Mappers convert between Entity ↔ DTO
- Validation on request DTOs using Bean Validation

### Exception Handling
- Global `@RestControllerAdvice`
- Custom exceptions with meaningful messages
- Structured error responses
- Proper HTTP status codes

### Security Pattern
- JWT-based authentication
- Role-based authorization using `@PreAuthorize`
- Stateless sessions
- Token validation on every request

### Event-Driven Architecture
- Publisher-subscriber pattern
- Loose coupling between services
- Asynchronous communication
- Profile-based implementation (stub vs real)

## Code Quality Practices

### Naming Conventions
- Classes: PascalCase
- Methods/Variables: camelCase
- Constants: UPPER_SNAKE_CASE
- Packages: lowercase
- Comments: English, lowercase first letter

### Logging
- INFO: business operations (order created, restaurant updated)
- ERROR: exceptions and failures
- DEBUG: for hibernate SQL (development)
- Structured log messages with context

### Validation
- `@Valid` on controller methods
- Bean Validation annotations on DTOs
- Custom validation in service layer
- Meaningful error messages

### Transaction Management
- `@Transactional` on service methods
- Read-only transactions where applicable
- Proper isolation levels

### API Documentation
- Swagger/OpenAPI 3.0
- Detailed descriptions
- Example requests/responses
- Security schemes documented

## Scalability Considerations

### Horizontal Scaling
- Stateless services
- Database connections pooled
- JWT tokens don't require server-side sessions

### Caching Strategy
- Can add Redis for frequently accessed data
- Menu items, categories
- Restaurant listings

### Performance
- Database indexes on frequently queried columns
- Pagination for list endpoints (can be added)
- Lazy loading for relationships
- Flyway for optimized migrations

## Security Considerations

### Authentication & Authorization
- JWT tokens with expiration
- Role-based access control
- Keycloak centralized auth

### Data Protection
- Passwords managed by Keycloak
- HTTPS should be enabled in production
- CSRF disabled (REST API, token-based)

### Input Validation
- Bean Validation on all inputs
- SQL injection prevention (JPA)
- XSS prevention (proper content types)

## Monitoring & Observability

### Current State
- Application logs (console)
- Swagger for API testing
- Kafka UI for message monitoring
- Database logs

### Future Enhancements
- Spring Boot Actuator
- Prometheus metrics
- Grafana dashboards
- Distributed tracing (Zipkin/Jaeger)
- ELK stack for log aggregation

## Testing Strategy

### Unit Tests
- Service layer logic
- Mappers
- Validators

### Integration Tests
- Repository tests with test containers
- API tests with MockMvc
- Security tests

### E2E Tests
- Full flow testing
- Postman collections
- Automated API tests

## Deployment

### Development
- Docker Compose for infrastructure
- Local Spring Boot instances
- H2 for quick testing (optional)

### Production (Recommended)
- Kubernetes for orchestration
- Separate PostgreSQL clusters
- Kafka cluster (3+ brokers)
- Keycloak cluster with database
- Load balancer (nginx/ingress)
- Auto-scaling policies

## Future Architecture

### Planned Services
1. **User Service** - profile, preferences, addresses
2. **Payment Service** - payment processing
3. **Delivery Service** - courier management, tracking
4. **Notification Service** - email, SMS, push notifications
5. **Rating Service** - reviews and ratings

### Planned Infrastructure
1. **API Gateway** - routing, rate limiting, centralized security
2. **Service Discovery** - Eureka, Consul
3. **Config Server** - centralized configuration
4. **Circuit Breaker** - Resilience4j
5. **Message Queue** - RabbitMQ for guaranteed delivery

## Technology Choices Rationale

**Spring Boot:** Industry standard, rich ecosystem, excellent documentation

**PostgreSQL:** ACID compliance, JSON support, robust for transactional data

**Kafka:** High throughput, durability, perfect for event streaming

**Keycloak:** Feature-rich, standard-compliant, battle-tested

**Flyway:** Version control for database, safe migrations

**Docker:** Consistent environments, easy setup, production-ready

## Conclusion

QuickBite demonstrates a production-ready microservices architecture following industry best practices:
- ✅ Clean code with proper structure
- ✅ Comprehensive error handling
- ✅ Security-first approach
- ✅ Event-driven design
- ✅ Scalable architecture
- ✅ Well-documented APIs
- ✅ Database versioning
- ✅ Containerized infrastructure
