package com.eurail.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "animals", indexes = {
    @Index(name = "idx_animals_title", columnList = "title"),
    @Index(name = "idx_animals_located", columnList = "located")
})
@EntityListeners(AuditingEntityListener.class)
public class Animal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String title;
    
    @Column(nullable = false)
    private LocalDate located;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;
    
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated;
    
    public Animal() {
    }
    
    public Animal(Long id, String title, LocalDate located, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.title = title;
        this.located = located;
        this.created = created;
        this.updated = updated;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public LocalDate getLocated() {
        return located;
    }
    
    public void setLocated(LocalDate located) {
        this.located = located;
    }
    
    public LocalDateTime getCreated() {
        return created;
    }
    
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    
    public LocalDateTime getUpdated() {
        return updated;
    }
    
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", located=" + located +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
