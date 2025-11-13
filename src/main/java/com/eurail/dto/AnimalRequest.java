package com.eurail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AnimalRequest(
    @NotBlank(message = "Title is required")
    String title,
    
    @NotNull(message = "Located date is required")
    LocalDate located
) {}

