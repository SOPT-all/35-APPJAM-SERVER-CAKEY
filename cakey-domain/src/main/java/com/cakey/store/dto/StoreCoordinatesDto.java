package com.cakey.store.dto;

import com.querydsl.core.annotations.QueryProjection;

public record StoreCoordinatesDto(
        long storeId,
        double latitude,
        double longitude
) {
    @QueryProjection
    public StoreCoordinatesDto {}
}
