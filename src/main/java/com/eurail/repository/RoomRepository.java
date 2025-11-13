package com.eurail.repository;

import com.eurail.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByTitle(String title);
    
    @Query("SELECT r.title, COUNT(afr.animal.id) " +
           "FROM Room r " +
           "JOIN AnimalFavoriteRoom afr ON r.id = afr.room.id " +
           "GROUP BY r.title")
    List<Object[]> findFavoriteRoomsWithCount();
}

