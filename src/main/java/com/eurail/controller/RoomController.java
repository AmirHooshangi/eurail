package com.eurail.controller;

import com.eurail.dto.FavoriteRoomResponse;
import com.eurail.dto.RoomRequest;
import com.eurail.dto.RoomResponse;
import com.eurail.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    private final RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(roomService.createRoom(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{roomId}/animals/{animalId}/favorite")
    public ResponseEntity<Void> favoriteRoom(
            @PathVariable Long roomId,
            @PathVariable Long animalId) {
        roomService.favoriteRoom(animalId, roomId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @DeleteMapping("/{roomId}/animals/{animalId}/favorite")
    public ResponseEntity<Void> unfavoriteRoom(
            @PathVariable Long roomId,
            @PathVariable Long animalId) {
        roomService.unfavoriteRoom(animalId, roomId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteRoomResponse>> getFavoriteRooms() {
        return ResponseEntity.ok(roomService.getFavoriteRooms());
    }
}

