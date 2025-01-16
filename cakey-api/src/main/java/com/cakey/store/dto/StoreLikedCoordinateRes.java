package com.cakey.store.dto;

import java.util.List;

public record StoreLikedCoordinateRes(
        List<StoreCoordinate> stores
) {
    public static StoreLikedCoordinateRes of(final List<StoreCoordinate> stores) {
        return new StoreLikedCoordinateRes(stores);
    }
}
