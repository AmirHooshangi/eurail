-- Initial schema
CREATE TABLE IF NOT EXISTS animals (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    located DATE NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_animals_title ON animals(title);
CREATE INDEX IF NOT EXISTS idx_animals_located ON animals(located);
CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_rooms_title ON rooms(title);
CREATE TABLE IF NOT EXISTS animal_rooms (
    animal_id BIGINT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_animal_rooms_animal FOREIGN KEY (animal_id) 
        REFERENCES animals(id) ON DELETE CASCADE,
    CONSTRAINT FK_animal_rooms_room FOREIGN KEY (room_id) 
        REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_animal_rooms_room_id ON animal_rooms(room_id);
CREATE TABLE IF NOT EXISTS animal_favorite_rooms (
    animal_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    favorited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (animal_id, room_id),
    CONSTRAINT FK_animal_favorite_rooms_animal FOREIGN KEY (animal_id) 
        REFERENCES animals(id) ON DELETE CASCADE,
    CONSTRAINT FK_animal_favorite_rooms_room FOREIGN KEY (room_id) 
        REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_animal_favorite_rooms_room_id ON animal_favorite_rooms(room_id);
CREATE INDEX IF NOT EXISTS idx_animal_favorite_rooms_animal_id ON animal_favorite_rooms(animal_id);

