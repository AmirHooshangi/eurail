package com.eurail.service;

import com.eurail.dto.AnimalRequest;
import com.eurail.dto.AnimalResponse;
import com.eurail.model.Animal;
import com.eurail.model.AnimalRoom;
import com.eurail.model.Room;
import com.eurail.repository.AnimalRepository;
import com.eurail.repository.AnimalRoomRepository;
import com.eurail.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    
    private final AnimalRepository animalRepository;
    private final RoomRepository roomRepository;
    private final AnimalRoomRepository animalRoomRepository;
    
    public AnimalService(AnimalRepository animalRepository, RoomRepository roomRepository, AnimalRoomRepository animalRoomRepository) {
        this.animalRepository = animalRepository;
        this.roomRepository = roomRepository;
        this.animalRoomRepository = animalRoomRepository;
    }
    
    public AnimalResponse createAnimal(AnimalRequest request) {
        if (animalRepository.findByTitle(request.title()).isPresent()) {
            throw new IllegalArgumentException("Animal '" + request.title() + "' already exists");
        }
        
        Animal animal = new Animal();
        animal.setTitle(request.title());
        animal.setLocated(request.located());
        animal = animalRepository.save(animal);
        
        return toResponse(animal);
    }
    
    public AnimalResponse getAnimalById(Long id) {
        Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + id));
        return toResponse(animal);
    }
    
    public List<AnimalResponse> getAllAnimals() {
        return animalRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
    
    public AnimalResponse updateAnimal(Long id, AnimalRequest request) {
        Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + id));
        
        if (!animal.getTitle().equals(request.title()) && 
            animalRepository.findByTitle(request.title()).isPresent()) {
            throw new IllegalArgumentException("Animal '" + request.title() + "' already exists");
        }
        
        animal.setTitle(request.title());
        animal.setLocated(request.located());
        animal = animalRepository.save(animal);
        
        return toResponse(animal);
    }
    
    public void deleteAnimal(Long id) {
        Animal animal = animalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + id));
        animalRepository.delete(animal);
    }
    
    @Transactional
    public AnimalResponse placeAnimalInRoom(Long animalId, Long roomId) {
        Animal animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + animalId));
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        animalRoomRepository.findByAnimalId(animalId).ifPresent(ar -> 
            animalRoomRepository.deleteByAnimalId(animalId)
        );
        
        AnimalRoom animalRoom = new AnimalRoom();
        animalRoom.setAnimal(animal);
        animalRoom.setRoom(room);
        animalRoomRepository.save(animalRoom);
        
        return toResponse(animal);
    }
    
    @Transactional
    public void removeAnimalFromRoom(Long animalId) {
        animalRepository.findById(animalId)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + animalId));
        animalRoomRepository.deleteByAnimalId(animalId);
    }
    
    public Page<AnimalResponse> getAnimalsInRoom(Long roomId, Pageable pageable) {
        return animalRepository.findByRoomId(roomId, pageable)
            .map(this::toResponse);
    }
    
    private AnimalResponse toResponse(Animal animal) {
        return animalRoomRepository.findByAnimalId(animal.getId())
            .map(ar -> new AnimalResponse(
                animal.getId(),
                animal.getTitle(),
                animal.getLocated(),
                animal.getCreated(),
                animal.getUpdated(),
                ar.getRoom().getId(),
                ar.getRoom().getTitle()
            ))
            .orElse(new AnimalResponse(
                animal.getId(),
                animal.getTitle(),
                animal.getLocated(),
                animal.getCreated(),
                animal.getUpdated(),
                null,
                null
            ));
    }
}

