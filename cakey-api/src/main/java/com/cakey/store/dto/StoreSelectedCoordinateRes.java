package com.cakey.store.dto;

public record StoreSelectedCoordinateRes(
        Long storeId,
        Double latitude,
        Double longitude
) {
    public static StoreSelectedCoordinateRes of(Long storeId, Double latitude, Double longitude) {
        return new StoreSelectedCoordinateRes(storeId, latitude, longitude);
    }
}
