package com.eurail.dto;

public record FavoriteRoomResponse(
    Long roomId,
    String roomTitle,
    Long favoriteCount
) {}

