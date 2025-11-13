package com.eurail.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AnimalResponse(
    Long id,
    String title,
    LocalDate located,
    LocalDateTime created,
    LocalDateTime updated,
    Long currentRoomId,
    String currentRoomTitle
) {}

