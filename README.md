# Cinema Management System

A full-stack cinema management platform built with Spring Boot, featuring REST APIs, role-based authentication, and a modern UI with interactive animations.

##  Features

- **Cinema & Movie Management** - Full CRUD operations for cinemas, movies, and screens
- **Ticket Booking System** - Purchase and manage tickets with real-time availability
- **Role-Based Access Control** - Admin and visitor roles with different permissions
- **REST API** - Complete RESTful API with proper HTTP status codes and validation
- **Interactive UI** - Animations, alerts, and confetti effects for better UX

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3
- Spring Security (JWT Authentication)
- Spring Data JPA
- PostgreSQL / H2
- Gradle

**Frontend**
- Thymeleaf
- Bootstrap 5
- JavaScript (SweetAlert2, Anime.js, Confetti.js)

**Testing & DevOps**
- JUnit 5
- Mockito
- GitLab CI/CD

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Frontend                              │
│              (Thymeleaf + Bootstrap + JS)                   │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                     Controllers                              │
│         (MVC Controllers + REST API Controllers)            │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                    Service Layer                             │
│              (Business Logic + Validation)                   │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                   Repository Layer                           │
│                   (Spring Data JPA)                          │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                      Database                                │
│               (PostgreSQL / H2)                              │
└─────────────────────────────────────────────────────────────┘
```

## Domain Model

```
Cinema (1) ◄───► (N) CinemaScreen (N) ◄───► (N) Movie
                                                  │
                                                  │
User (1) ◄───► (N) Ticket (N) ◄───► (1) Movie ◄──┘
```

- **Cinema** - Theater locations with capacity and screens
- **Movie** - Films with title, release date, genre, and rating
- **CinemaScreen** - Individual screens with type (IMAX, 3D, Standard) and size
- **Ticket** - Bookings linking users, movies, with showtime and pricing
- **User** - Accounts with roles (Administrator/Visitor)

## Getting Started

### Prerequisites
- Java 17+
- Gradle

### Installation

```bash
# Clone the repository
git clone https://github.com/lizakolosova/cinema_project_part_two.git
cd cinema_project_part_two

# Build the project
./gradlew clean build

# Run the application
./gradlew bootRun
```

The application will be available at `http://localhost:8080`

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with test profile
./gradlew test -Dspring.profiles.active=test
```

## API Endpoints

### Movies
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/movies` | Get all movies |
| GET | `/api/movies?title={title}` | Search movies by title |
| DELETE | `/api/movies/{id}` | Delete a movie |

### Cinemas
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cinemas` | Get all cinemas |
| DELETE | `/api/cinemas/{id}` | Delete a cinema |

### Tickets
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tickets` | Create a ticket |
| PATCH | `/api/tickets/{id}` | Update a ticket |
| DELETE | `/api/tickets/{id}` | Delete a ticket |

## Authentication

The system uses role-based access control:

| Role | Permissions |
|------|-------------|
| **Administrator** | Full access - manage cinemas, movies, and all tickets |
| **Visitor** | View content, manage own tickets only |
| **Anonymous** | View cinemas, movies, and tickets |

## Testing

The project includes comprehensive tests:

- **MVC Integration Tests** - Controller layer testing
- **API Integration Tests** - REST endpoint testing
- **Unit Tests with Mocking** - Service layer testing with Mockito
- **Role Verification Tests** - Security and authorization testing
