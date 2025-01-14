package com.cakey.store.dto;

import lombok.Builder;

@Builder
public record StoreCoordinate(
        long storeId,
        double latitude,
        double longitude
) {
    public static StoreCoordinate of(final long storeId,
                                     final double latitude,
                                     final double longitude) {
        return StoreCoordinate.builder()
                .storeId(storeId)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
