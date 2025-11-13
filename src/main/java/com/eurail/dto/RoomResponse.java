package com.eurail.dto;

import java.time.LocalDateTime;

public record RoomResponse(
    Long id,
    String title,
    LocalDateTime created,
    LocalDateTime updated
) {}

