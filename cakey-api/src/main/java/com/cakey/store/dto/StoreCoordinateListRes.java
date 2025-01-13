package com.cakey.store.dto;

import lombok.Builder;
import java.util.List;

public record StoreCoordinateListRes(
        List<StoreCoordinate> storeCoordinateList
) {

    public static StoreCoordinateListRes from(final List<StoreCoordinate> storeCoordinateList) {
        return new StoreCoordinateListRes(storeCoordinateList);
    }
    
    @Builder
    public record StoreCoordinate(
            double latitude,
            double longitude
    ) {
        public static StoreCoordinate of (final double latitude,
                                          final double longitude) {
            return StoreCoordinate.builder().
                    latitude(latitude)
                    .longitude(longitude)
                    .build();
        }
    }
}
