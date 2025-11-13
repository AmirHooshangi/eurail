package com.eurail.repository;

import com.eurail.model.Animal;
import com.eurail.model.AnimalFavoriteRoom;
import com.eurail.model.AnimalFavoriteRoomId;
import com.eurail.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalFavoriteRoomRepository extends JpaRepository<AnimalFavoriteRoom, AnimalFavoriteRoomId> {
    Optional<AnimalFavoriteRoom> findByAnimalIdAndRoomId(Long animalId, Long roomId);
    
    Optional<AnimalFavoriteRoom> findByAnimalAndRoom(Animal animal, Room room);
    
    List<AnimalFavoriteRoom> findByAnimal(Animal animal);
    
    @Modifying
    @Transactional
    void deleteByAnimalAndRoom(Animal animal, Room room);
}

