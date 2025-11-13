package com.eurail.controller;

import com.eurail.dto.AnimalRequest;
import com.eurail.model.Animal;
import com.eurail.model.AnimalRoom;
import com.eurail.model.Room;
import com.eurail.repository.AnimalRepository;
import com.eurail.repository.AnimalRoomRepository;
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
class AnimalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AnimalRoomRepository animalRoomRepository;

    private Animal animal;
    private Room room;

    @BeforeEach
    void setUp() {
        animalRoomRepository.deleteAll();
        animalRepository.deleteAll();
        roomRepository.deleteAll();

        animal = new Animal();
        animal.setTitle("Tiger");
        animal.setLocated(LocalDate.of(2024, 1, 15));
        animal = animalRepository.save(animal);

        room = new Room();
        room.setTitle("Green Room");
        room = roomRepository.save(room);
    }

    @Test
    void createAnimal_success() throws Exception {
        AnimalRequest request = new AnimalRequest("Lion", LocalDate.of(2024, 2, 1));

        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Lion"));
    }

    @Test
    void createAnimal_duplicateTitle_returnsBadRequest() throws Exception {
        AnimalRequest request = new AnimalRequest("Tiger", LocalDate.of(2024, 1, 15));

        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getAnimalById_success() throws Exception {
        mockMvc.perform(get("/api/animals/{id}", animal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tiger"));
    }

    @Test
    void getAnimalById_notFound_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/animals/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllAnimals_success() throws Exception {
        mockMvc.perform(get("/api/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateAnimal_success() throws Exception {
        AnimalRequest request = new AnimalRequest("Bengal Tiger", LocalDate.of(2024, 1, 15));

        mockMvc.perform(put("/api/animals/{id}", animal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bengal Tiger"));
    }

    @Test
    void deleteAnimal_success() throws Exception {
        mockMvc.perform(delete("/api/animals/{id}", animal.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void placeAnimalInRoom_success() throws Exception {
        mockMvc.perform(post("/api/animals/{animalId}/rooms/{roomId}", animal.getId(), room.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tiger"))
                .andExpect(jsonPath("$.currentRoomId").value(room.getId()));
    }

    @Test
    void placeAnimalInRoom_animalNotFound_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/animals/999/rooms/{roomId}", room.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeAnimalFromRoom_success() throws Exception {
        AnimalRoom animalRoom = new AnimalRoom();
        animalRoom.setAnimal(animal);
        animalRoom.setRoom(room);
        animalRoomRepository.save(animalRoom);

        mockMvc.perform(delete("/api/animals/{animalId}/rooms", animal.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAnimalsInRoom_success() throws Exception {
        AnimalRoom animalRoom = new AnimalRoom();
        animalRoom.setAnimal(animal);
        animalRoom.setRoom(room);
        animalRoomRepository.save(animalRoom);

        mockMvc.perform(get("/api/animals/rooms/{roomId}", room.getId())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}

