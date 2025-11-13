package com.eurail.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "animal_favorite_rooms", indexes = {
    @Index(name = "idx_animal_favorite_rooms_room_id", columnList = "room_id"),
    @Index(name = "idx_animal_favorite_rooms_animal_id", columnList = "animal_id")
})
@EntityListeners(AuditingEntityListener.class)
public class AnimalFavoriteRoom {
    
    @EmbeddedId
    private AnimalFavoriteRoomId id;
    
    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false, insertable = false, updatable = false)
    private Animal animal;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, insertable = false, updatable = false)
    private Room room;
    
    @CreatedDate
    @Column(name = "favorited_at", nullable = false, updatable = false)
    private LocalDateTime favoritedAt;
    
    public AnimalFavoriteRoom() {
    }
    
    public AnimalFavoriteRoom(AnimalFavoriteRoomId id, Animal animal, Room room, LocalDateTime favoritedAt) {
        this.id = id;
        this.animal = animal;
        this.room = room;
        this.favoritedAt = favoritedAt;
    }
    
    public AnimalFavoriteRoom(Animal animal, Room room) {
        this.id = new AnimalFavoriteRoomId(animal.getId(), room.getId());
        this.animal = animal;
        this.room = room;
    }
    
    public AnimalFavoriteRoomId getId() {
        return id;
    }
    
    public void setId(AnimalFavoriteRoomId id) {
        this.id = id;
    }
    
    public Animal getAnimal() {
        return animal;
    }
    
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public LocalDateTime getFavoritedAt() {
        return favoritedAt;
    }
    
    public void setFavoritedAt(LocalDateTime favoritedAt) {
        this.favoritedAt = favoritedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalFavoriteRoom that = (AnimalFavoriteRoom) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "AnimalFavoriteRoom{" +
                "id=" + id +
                ", animal=" + animal +
                ", room=" + room +
                ", favoritedAt=" + favoritedAt +
                '}';
    }
}
