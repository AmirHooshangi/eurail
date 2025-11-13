package com.eurail.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "animal_rooms", indexes = {
    @Index(name = "idx_animal_rooms_room_id", columnList = "room_id")
})
@EntityListeners(AuditingEntityListener.class)
public class AnimalRoom {
    
    @Id
    @Column(name = "animal_id")
    private Long animalId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "animal_id", nullable = false, unique = true)
    private Animal animal;
    
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @CreatedDate
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;
    
    public AnimalRoom() {
    }
    
    public AnimalRoom(Long animalId, Animal animal, Room room, LocalDateTime assignedAt) {
        this.animalId = animalId;
        this.animal = animal;
        this.room = room;
        this.assignedAt = assignedAt;
    }
    
    public Long getAnimalId() {
        return animalId;
    }
    
    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
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
    
    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
    
    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalRoom that = (AnimalRoom) o;
        return Objects.equals(animalId, that.animalId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(animalId);
    }
    
    @Override
    public String toString() {
        return "AnimalRoom{" +
                "animalId=" + animalId +
                ", room=" + room +
                ", assignedAt=" + assignedAt +
                '}';
    }
}
