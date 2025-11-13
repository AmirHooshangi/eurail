# Zoo Management System

Spring Boot app for managing animals and rooms.

## Setup

**Docker (easiest):**
```bash
docker-compose up --build
```

**Local (dev profile with H2):**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```


App runs on `http://localhost:8080`

## API

**Animals:**
- `POST /api/animals` - Create
- `GET /api/animals` - List all
- `GET /api/animals/{id}` - Get one
- `PUT /api/animals/{id}` - Update
- `DELETE /api/animals/{id}` - Delete
- `POST /api/animals/{animalId}/rooms/{roomId}` - Place in room
- `DELETE /api/animals/{animalId}/rooms` - Remove from room
- `GET /api/animals/rooms/{roomId}?page=0&size=10&sortBy=title` - List in room

**Rooms:**
- `POST /api/rooms` - Create
- `GET /api/rooms` - List all
- `GET /api/rooms/{id}` - Get one
- `PUT /api/rooms/{id}` - Update
- `DELETE /api/rooms/{id}` - Delete
- `POST /api/rooms/{roomId}/animals/{animalId}/favorite` - Favorite
- `DELETE /api/rooms/{roomId}/animals/{animalId}/favorite` - Unfavorite
- `GET /api/rooms/favorites` - List favorites with counts


## Database

Uses Flyway for migrations. Schema in `src/main/resources/db/migration/`.

## Production Readiness Checklist

Before deploying to production, here are the top 10 things you should tackle:

1. **Add authentication and authorization** - Right now anyone can access the API. Add Spring Security with JWT tokens or OAuth2, and role-based access control (zookeepers vs admins).

2. **Proper error handling** - Replace generic `IllegalArgumentException` with custom exceptions and proper HTTP status codes. Add error codes for easier debugging.

3. **API documentation** - Add OpenAPI/Swagger docs so frontend devs know what endpoints exist and how to use them. SpringDoc OpenAPI makes this easy.

4. **Logging and monitoring** - Set up structured logging (JSON format), add correlation IDs for request tracing, and integrate with monitoring tools like Prometheus/Grafana or Datadog.

5. **Health checks and metrics** - Add Spring Boot Actuator endpoints for health, metrics, and readiness probes. Essential for Kubernetes deployments.

6. **Input validation** - We have some validation, but add more comprehensive checks. Consider adding custom validators for business rules (e.g., "located date can't be in the future").

7. **Database connection pooling** - Configure HikariCP properly with sensible pool sizes based on your load. Add connection timeout settings.

8. **Environment configuration** - Move all config to environment variables or a config server. Never hardcode secrets. Use Spring Cloud Config or similar.

9. **Rate limiting** - Add rate limiting to prevent abuse. Spring Cloud Gateway or a simple filter can handle this.

10. **Backup strategy** - Set up automated database backups, test restore procedures, and have a disaster recovery plan. Document the process.

Bonus: Add integration tests that run against a real PostgreSQL instance (Testcontainers), not just H2. H2 is great for speed but doesn't catch all PostgreSQL-specific issues.

