# 🏨 Room Reservation API

REST API for a room reservation system built with Java 17 + Spring Boot 3.x, with role-based access control and Form-Based authentication.

---

## Tech Stack

- **Java 17** / **Spring Boot 3.x**
- **Spring Security** (Form-Based Auth)
- **Spring Data JPA** + **PostgreSQL**
- **Maven**

---

## Roles

| Role | Permissions |
|------|-------------|
| `ADMIN` | Full CRUD on all reservations, filtering by room / userId, pagination |
| `USER` | View own reservations, create a new reservation for themselves |

---

## Endpoints

### Auth
| Method | URL | Access |
|--------|-----|--------|
| `POST` | `/login` | All |
| `POST` | `/logout` | All |

### Reservations
| Method | URL | Access | Description |
|--------|-----|--------|-------------|
| `GET` | `/api/reservations` | ADMIN | Get all reservations (with filters) |
| `GET` | `/api/reservations/{id}` | ADMIN | Get reservation by ID |
| `POST` | `/api/reservations` | ADMIN | Create a reservation |
| `PUT` | `/api/reservations/{id}` | ADMIN | Update a reservation |
| `DELETE` | `/api/reservations/{id}` | ADMIN | Delete a reservation |
| `GET` | `/api/profile/dashboard` | USER | View own reservations |
| `POST` | `/api/profile/newReservation` | USER | Create a reservation |

### Query Parameters for ADMIN (GET /api/reservations)

```
roomId    – filter by room
userId    – filter by user
page      – page number (starting from 0)
size      – number of records per page
```

---

## Project Structure

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
