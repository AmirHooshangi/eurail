package com.eurail.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AnimalFavoriteRoomId implements Serializable {
    @Column(name = "animal_id")
    private Long animalId;
    
    @Column(name = "room_id")
    private Long roomId;
    
    public AnimalFavoriteRoomId() {
    }
    
    public AnimalFavoriteRoomId(Long animalId, Long roomId) {
        this.animalId = animalId;
        this.roomId = roomId;
    }
    
    public Long getAnimalId() {
        return animalId;
    }
    
    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }
    
    public Long getRoomId() {
        return roomId;
    }
    
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalFavoriteRoomId that = (AnimalFavoriteRoomId) o;
        return Objects.equals(animalId, that.animalId) && Objects.equals(roomId, that.roomId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(animalId, roomId);
    }
    
    @Override
    public String toString() {
        return "AnimalFavoriteRoomId{" +
                "animalId=" + animalId +
                ", roomId=" + roomId +
                '}';
    }
}
