package com.eurail.service;

import com.eurail.dto.FavoriteRoomResponse;
import com.eurail.dto.RoomRequest;
import com.eurail.dto.RoomResponse;
import com.eurail.model.Animal;
import com.eurail.model.AnimalFavoriteRoom;
import com.eurail.model.AnimalFavoriteRoomId;
import com.eurail.model.Room;
import com.eurail.repository.AnimalFavoriteRoomRepository;
import com.eurail.repository.AnimalRepository;
import com.eurail.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final AnimalRepository animalRepository;
    private final AnimalFavoriteRoomRepository animalFavoriteRoomRepository;
    
    public RoomService(RoomRepository roomRepository, AnimalRepository animalRepository, AnimalFavoriteRoomRepository animalFavoriteRoomRepository) {
        this.roomRepository = roomRepository;
        this.animalRepository = animalRepository;
        this.animalFavoriteRoomRepository = animalFavoriteRoomRepository;
    }
    
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.findByTitle(request.title()).isPresent()) {
            throw new IllegalArgumentException("Room '" + request.title() + "' already exists");
        }
        
        Room room = new Room();
        room.setTitle(request.title());
        room = roomRepository.save(room);
        
        return toResponse(room);
    }
    
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        return toResponse(room);
    }
    
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        
        if (!room.getTitle().equals(request.title()) && 
            roomRepository.findByTitle(request.title()).isPresent()) {
            throw new IllegalArgumentException("Room '" + request.title() + "' already exists");
        }
        
        room.setTitle(request.title());
        room = roomRepository.save(room);
        
        return toResponse(room);
    }
    
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        roomRepository.delete(room);
    }
    
    @Transactional
    public void favoriteRoom(Long animalId, Long roomId) {
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + animalId));
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        if (animalFavoriteRoomRepository.findByAnimalIdAndRoomId(animalId, roomId).isPresent()) {
            throw new IllegalArgumentException("Already favorited");
        }
        
        AnimalFavoriteRoom favorite = new AnimalFavoriteRoom(animal, room);
        animalFavoriteRoomRepository.save(favorite);
    }
    
    @Transactional
    public void unfavoriteRoom(Long animalId, Long roomId) {
        animalRepository.findById(animalId)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + animalId));
        roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        if (animalFavoriteRoomRepository.findByAnimalIdAndRoomId(animalId, roomId).isEmpty()) {
            throw new IllegalArgumentException("Favorite not found");
        }
        
        AnimalFavoriteRoomId id = new AnimalFavoriteRoomId(animalId, roomId);
        animalFavoriteRoomRepository.deleteById(id);
    }
    
    public List<FavoriteRoomResponse> getFavoriteRooms() {
        List<Object[]> results = roomRepository.findFavoriteRoomsWithCount();
        return results.stream()
            .map(result -> {
                String title = (String) result[0];
                Long count = ((Number) result[1]).longValue();
                return new FavoriteRoomResponse(title, count);
            })
            .collect(Collectors.toList());
    }
    
    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
            room.getId(),
            room.getTitle(),
            room.getCreated(),
            room.getUpdated()
        );
    }
}

