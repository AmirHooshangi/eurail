package com.eurail.repository;

import com.eurail.model.Animal;
import com.eurail.model.AnimalRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AnimalRoomRepository extends JpaRepository<AnimalRoom, Long> {
    Optional<AnimalRoom> findByAnimalId(Long animalId);
    
    Optional<AnimalRoom> findByAnimal(Animal animal);
    
    @Modifying
    @Transactional
    void deleteByAnimalId(Long animalId);
    
    @Modifying
    @Transactional
    void deleteByAnimal(Animal animal);
}

