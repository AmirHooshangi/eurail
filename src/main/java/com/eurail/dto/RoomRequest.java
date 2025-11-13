package com.eurail.dto;

import jakarta.validation.constraints.NotBlank;

public record RoomRequest(
    @NotBlank(message = "Title is required")
    String title
) {}

