package com.eurail.repository;

import com.eurail.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByTitle(String title);
    
    @Query("SELECT a FROM Animal a JOIN AnimalRoom ar ON a.id = ar.animal.id WHERE ar.room.id = :roomId")
    Page<Animal> findByRoomId(@Param("roomId") Long roomId, Pageable pageable);
}

