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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AnimalFavoriteRoomRepository animalFavoriteRoomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private Animal animal;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setTitle("Green Room");
        room.setCreated(LocalDateTime.now());
        room.setUpdated(LocalDateTime.now());

        animal = new Animal();
        animal.setId(1L);
        animal.setTitle("Tiger");
    }

    @Test
    void createRoom_success() {
        RoomRequest request = new RoomRequest("Blue Room");
        Room newRoom = new Room();
        newRoom.setId(2L);
        newRoom.setTitle("Blue Room");

        when(roomRepository.findByTitle("Blue Room")).thenReturn(Optional.empty());
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);

        RoomResponse response = roomService.createRoom(request);

        assertNotNull(response);
        assertEquals("Blue Room", response.title());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void createRoom_duplicateTitle_throwsException() {
        RoomRequest request = new RoomRequest("Green Room");

        when(roomRepository.findByTitle("Green Room")).thenReturn(Optional.of(room));

        assertThrows(IllegalArgumentException.class, () -> roomService.createRoom(request));
        verify(roomRepository, never()).save(any());
    }

    @Test
    void getRoomById_success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomResponse response = roomService.getRoomById(1L);

        assertNotNull(response);
        assertEquals("Green Room", response.title());
    }

    @Test
    void getRoomById_notFound_throwsException() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> roomService.getRoomById(999L));
    }

    @Test
    void favoriteRoom_success() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(animalFavoriteRoomRepository.findByAnimalIdAndRoomId(1L, 1L)).thenReturn(Optional.empty());
        when(animalFavoriteRoomRepository.save(any(AnimalFavoriteRoom.class))).thenReturn(new AnimalFavoriteRoom(animal, room));

        roomService.favoriteRoom(1L, 1L);

        verify(animalFavoriteRoomRepository).save(any(AnimalFavoriteRoom.class));
    }

    @Test
    void favoriteRoom_alreadyFavorited_throwsException() {
        AnimalFavoriteRoom existing = new AnimalFavoriteRoom(animal, room);

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(animalFavoriteRoomRepository.findByAnimalIdAndRoomId(1L, 1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> roomService.favoriteRoom(1L, 1L));
        verify(animalFavoriteRoomRepository, never()).save(any());
    }

    @Test
    void unfavoriteRoom_success() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(animalFavoriteRoomRepository.findByAnimalIdAndRoomId(1L, 1L)).thenReturn(Optional.of(new AnimalFavoriteRoom(animal, room)));

        roomService.unfavoriteRoom(1L, 1L);

        verify(animalFavoriteRoomRepository).deleteById(any(AnimalFavoriteRoomId.class));
    }

    @Test
    void unfavoriteRoom_notFavorited_throwsException() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(animalFavoriteRoomRepository.findByAnimalIdAndRoomId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> roomService.unfavoriteRoom(1L, 1L));
        verify(animalFavoriteRoomRepository, never()).deleteById(any());
    }

    @Test
    void getFavoriteRooms_success() {
        Object[] result1 = new Object[]{"Green Room", 4L};
        Object[] result2 = new Object[]{"Big Room", 1L};
        List<Object[]> results = List.of(result1, result2);

        when(roomRepository.findFavoriteRoomsWithCount()).thenReturn(results);

        List<FavoriteRoomResponse> response = roomService.getFavoriteRooms();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Green Room", response.get(0).title());
        assertEquals(4L, response.get(0).count());
    }
}

