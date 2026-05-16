# 🏨 Room Reservation API

REST API для системи резервації кімнат, побудована на Java 17 + Spring Boot 3.x з підтримкою ролей та Form-Based аутентифікації.

---

## Tech Stack

- **Java 17** / **Spring Boot 3.x**
- **Spring Security** (Form-Based Auth)
- **Spring Data JPA** + **PostgreSQL**
- **Maven**

---

## Ролі

| Роль | Можливості |
|------|-----------|
| `ADMIN` | CRUD усіх резервацій, фільтрація по кімнаті / userId, пагінація |
| `USER` | Перегляд своїх резервацій, створення нової для себе |

---

## Endpoints

### Auth
| Method | URL | Доступ |
|--------|-----|--------|
| `POST` | `/login` | All |
| `POST` | `/logout` | All |

### Reservations
| Method | URL | Доступ | Опис |
|--------|-----|--------|------|
| `GET` | `/api/reservations` | ADMIN | Усі резервації (з фільтрами) |
| `GET` | `/api/reservations/{id}` | ADMIN | Резервація за ID |
| `POST` | `/api/reservations` | ADMIN | Створити резервацію |
| `PUT` | `/api/reservations/{id}` | ADMIN | Оновити резервацію |
| `DELETE` | `/api/reservations/{id}` | ADMIN | Видалити резервацію |
| `GET` | `/api/profile/dashboard` | USER | Свої резервації |
| `POST` | `/api/profile/newReservation` | User | Створити резервацію |

### Query-параметри для ADMIN (GET /api/reservations)

```
roomId    – фільтр по кімнаті
userId    – фільтр по користувачу
page      – номер сторінки (від 0)
size      – кількість записів на сторінці
```

## Структура проекту

```
src/
└── main/
    ├── java/
    │   └── org.example.springtest1/
    │       ├── reservations/
    │       │   ├── api/
    │       │   ├── availability/
    │       │   ├── db/
    │       │   └── service/
    │       ├── users/
    │       │   ├── api/
    │       │   ├── db/
    │       │   └── services/
    │       ├── web/
    │       │   ├── ErrorResponseDto
    │       │   ├── GlobalErrorHandler
    │       │   ├── SecurityConfig
    │       │   └── ViewController
    │       └── SpringTest1Application
    └── resources/
        ├── static/
        ├── templates/
        └── application.properties
```
