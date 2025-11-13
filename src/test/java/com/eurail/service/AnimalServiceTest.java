package com.eurail.service;

import com.eurail.dto.AnimalRequest;
import com.eurail.dto.AnimalResponse;
import com.eurail.model.Animal;
import com.eurail.model.AnimalRoom;
import com.eurail.model.Room;
import com.eurail.repository.AnimalRepository;
import com.eurail.repository.AnimalRoomRepository;
import com.eurail.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AnimalRoomRepository animalRoomRepository;

    @InjectMocks
    private AnimalService animalService;

    private Animal animal;
    private Room room;
    private AnimalRoom animalRoom;

    @BeforeEach
    void setUp() {
        animal = new Animal();
        animal.setId(1L);
        animal.setTitle("Tiger");
        animal.setLocated(LocalDate.of(2024, 1, 15));
        animal.setCreated(LocalDateTime.now());
        animal.setUpdated(LocalDateTime.now());

        room = new Room();
        room.setId(1L);
        room.setTitle("Green Room");
        room.setCreated(LocalDateTime.now());
        room.setUpdated(LocalDateTime.now());

        animalRoom = new AnimalRoom();
        animalRoom.setAnimalId(1L);
        animalRoom.setAnimal(animal);
        animalRoom.setRoom(room);
    }

    @Test
    void createAnimal_success() {
        AnimalRequest request = new AnimalRequest("Lion", LocalDate.of(2024, 2, 1));
        Animal newAnimal = new Animal();
        newAnimal.setId(2L);
        newAnimal.setTitle("Lion");
        newAnimal.setLocated(LocalDate.of(2024, 2, 1));

        when(animalRepository.findByTitle("Lion")).thenReturn(Optional.empty());
        when(animalRepository.save(any(Animal.class))).thenReturn(newAnimal);
        when(animalRoomRepository.findByAnimalId(2L)).thenReturn(Optional.empty());

        AnimalResponse response = animalService.createAnimal(request);

        assertNotNull(response);
        assertEquals("Lion", response.title());
        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void createAnimal_duplicateTitle_throwsException() {
        AnimalRequest request = new AnimalRequest("Tiger", LocalDate.of(2024, 1, 15));

        when(animalRepository.findByTitle("Tiger")).thenReturn(Optional.of(animal));

        assertThrows(IllegalArgumentException.class, () -> animalService.createAnimal(request));
        verify(animalRepository, never()).save(any());
    }

    @Test
    void getAnimalById_success() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(animalRoomRepository.findByAnimalId(1L)).thenReturn(Optional.empty());

        AnimalResponse response = animalService.getAnimalById(1L);

        assertNotNull(response);
        assertEquals("Tiger", response.title());
    }

    @Test
    void getAnimalById_notFound_throwsException() {
        when(animalRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> animalService.getAnimalById(999L));
    }

    @Test
    void updateAnimal_success() {
        AnimalRequest request = new AnimalRequest("Bengal Tiger", LocalDate.of(2024, 1, 15));

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(animalRepository.findByTitle("Bengal Tiger")).thenReturn(Optional.empty());
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);
        when(animalRoomRepository.findByAnimalId(1L)).thenReturn(Optional.empty());

        AnimalResponse response = animalService.updateAnimal(1L, request);

        assertNotNull(response);
        verify(animalRepository).save(animal);
    }

    @Test
    void deleteAnimal_success() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        animalService.deleteAnimal(1L);

        verify(animalRepository).delete(animal);
    }

    @Test
    void placeAnimalInRoom_success() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(animalRoomRepository.findByAnimalId(1L)).thenReturn(Optional.empty());
        when(animalRoomRepository.save(any(AnimalRoom.class))).thenReturn(animalRoom);
        when(animalRoomRepository.findByAnimalId(1L)).thenReturn(Optional.of(animalRoom));

        AnimalResponse response = animalService.placeAnimalInRoom(1L, 1L);

        assertNotNull(response);
        verify(animalRoomRepository).save(any(AnimalRoom.class));
    }

    @Test
    void placeAnimalInRoom_animalNotFound_throwsException() {
        when(animalRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> animalService.placeAnimalInRoom(999L, 1L));
    }

    @Test
    void placeAnimalInRoom_roomNotFound_throwsException() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> animalService.placeAnimalInRoom(1L, 999L));
    }

    @Test
    void placeAnimalInRoom_replacesExistingRoom() {
        AnimalRoom existingRoom = new AnimalRoom();
        existingRoom.setAnimalId(1L);

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room));
        when(animalRoomRepository.findByAnimalId(1L)).thenReturn(Optional.of(existingRoom));
        when(animalRoomRepository.save(any(AnimalRoom.class))).thenReturn(animalRoom);
        when(animalRoomRepository.findByAnimalId(1L)).thenReturn(Optional.of(animalRoom));

        animalService.placeAnimalInRoom(1L, 2L);

        verify(animalRoomRepository).deleteByAnimalId(1L);
        verify(animalRoomRepository).save(any(AnimalRoom.class));
    }

    @Test
    void getAnimalsInRoom_success() {
        Pageable pageable = Pageable.ofSize(10);
        Page<Animal> animalPage = new PageImpl<>(List.of(animal));

        when(animalRepository.findByRoomId(1L, pageable)).thenReturn(animalPage);
        when(animalRoomRepository.findByAnimalId(1L)).thenReturn(Optional.of(animalRoom));

        Page<AnimalResponse> response = animalService.getAnimalsInRoom(1L, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }
}

