package com.cakey.store.dto;

import java.util.List;

public record StoreCoordinateListRes(
        List<StoreCoordinate> stores
) {

    public static StoreCoordinateListRes from(final List<StoreCoordinate> storeCoordinateList) {
        return new StoreCoordinateListRes(storeCoordinateList);
    }
}
