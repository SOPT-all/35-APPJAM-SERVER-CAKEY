package com.cakey.store.dto;

import java.util.List;

public record StoreLikedCoordinate(
        List<StoreCoordinate> stores
) {
    public static StoreLikedCoordinate of(final List<StoreCoordinate> stores) {
        return new StoreLikedCoordinate(stores);
    }
}
