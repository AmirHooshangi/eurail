package com.eurail.controller;

import com.eurail.dto.AnimalRequest;
import com.eurail.dto.AnimalResponse;
import com.eurail.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {
    
    private final AnimalService animalService;
    
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }
    
    @PostMapping
    public ResponseEntity<AnimalResponse> createAnimal(@Valid @RequestBody AnimalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(animalService.createAnimal(request));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> getAnimalById(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<AnimalResponse>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponse> updateAnimal(
            @PathVariable Long id,
            @Valid @RequestBody AnimalRequest request) {
        return ResponseEntity.ok(animalService.updateAnimal(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{animalId}/rooms/{roomId}")
    public ResponseEntity<AnimalResponse> placeAnimalInRoom(
            @PathVariable Long animalId,
            @PathVariable Long roomId) {
        return ResponseEntity.ok(animalService.placeAnimalInRoom(animalId, roomId));
    }
    
    @DeleteMapping("/{animalId}/rooms")
    public ResponseEntity<Void> removeAnimalFromRoom(@PathVariable Long animalId) {
        animalService.removeAnimalFromRoom(animalId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Page<AnimalResponse>> getAnimalsInRoom(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(animalService.getAnimalsInRoom(roomId, pageable));
    }
}

