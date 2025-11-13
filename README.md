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

H2 console available at `http://localhost:8080/h2-console`

App runs on `http://localhost:8080`

## API

**Animals:**
- `POST /api/animals` - Create
- `GET /api/animals` - List all
- `GET /api/animals/{id}` - Get one
- `PUT /api/animals/{id}` - Update
- `DELETE /api/animals/{id}` - Delete
- `POST /api/animals/{animalId}/rooms/{roomId}` - Place in room
- `PUT /api/animals/{animalId}/rooms/{roomId}` - Move to room
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

See [DATABASE_DESIGN.md](DATABASE_DESIGN.md) for schema details.
