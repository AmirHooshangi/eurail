package com.eurail.controller;

import com.eurail.model.Animal;
import com.eurail.model.AnimalFavoriteRoom;
import com.eurail.model.Room;
import com.eurail.repository.AnimalFavoriteRoomRepository;
import com.eurail.repository.AnimalRepository;
import com.eurail.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalFavoriteRoomRepository animalFavoriteRoomRepository;

    private Room room;
    private Animal animal;

    @BeforeEach
    void setUp() {
        animalFavoriteRoomRepository.deleteAll();
        animalRepository.deleteAll();
        roomRepository.deleteAll();

        room = new Room();
        room.setTitle("Green Room");
        room = roomRepository.save(room);

        animal = new Animal();
        animal.setTitle("Tiger");
        animal.setLocated(LocalDate.of(2024, 1, 15));
        animal = animalRepository.save(animal);
    }

    @Test
    void createRoom_success() throws Exception {
        String request = objectMapper.writeValueAsString(new com.eurail.dto.RoomRequest("Blue Room"));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Blue Room"));
    }

    @Test
    void createRoom_duplicateTitle_returnsBadRequest() throws Exception {
        String request = objectMapper.writeValueAsString(new com.eurail.dto.RoomRequest("Green Room"));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRoomById_success() throws Exception {
        mockMvc.perform(get("/api/rooms/{id}", room.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Green Room"));
    }

    @Test
    void getAllRooms_success() throws Exception {
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void favoriteRoom_success() throws Exception {
        mockMvc.perform(post("/api/rooms/{roomId}/animals/{animalId}/favorite", room.getId(), animal.getId()))
                .andExpect(status().isCreated());
    }

    @Test
    void favoriteRoom_alreadyFavorited_returnsBadRequest() throws Exception {
        AnimalFavoriteRoom favorite = new AnimalFavoriteRoom(animal, room);
        animalFavoriteRoomRepository.save(favorite);

        mockMvc.perform(post("/api/rooms/{roomId}/animals/{animalId}/favorite", room.getId(), animal.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unfavoriteRoom_success() throws Exception {
        AnimalFavoriteRoom favorite = new AnimalFavoriteRoom(animal, room);
        animalFavoriteRoomRepository.save(favorite);

        mockMvc.perform(delete("/api/rooms/{roomId}/animals/{animalId}/favorite", room.getId(), animal.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void unfavoriteRoom_notFavorited_returnsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/rooms/{roomId}/animals/{animalId}/favorite", room.getId(), animal.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFavoriteRooms_success() throws Exception {
        AnimalFavoriteRoom favorite = new AnimalFavoriteRoom(animal, room);
        animalFavoriteRoomRepository.save(favorite);

        mockMvc.perform(get("/api/rooms/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Green Room"))
                .andExpect(jsonPath("$[0].count").value(1));
    }

    @Test
    void getFavoriteRooms_onlyReturnsFavoritedRooms() throws Exception {
        Room unfavoritedRoom = new Room();
        unfavoritedRoom.setTitle("Unfavorited Room");
        roomRepository.save(unfavoritedRoom);

        AnimalFavoriteRoom favorite = new AnimalFavoriteRoom(animal, room);
        animalFavoriteRoomRepository.save(favorite);

        mockMvc.perform(get("/api/rooms/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Green Room"));
    }
}

