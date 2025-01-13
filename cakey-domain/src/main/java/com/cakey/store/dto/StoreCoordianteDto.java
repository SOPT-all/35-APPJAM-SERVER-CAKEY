package com.cakey.store.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class StoreCoordianteDto {
    private final long id;
    private final double latitude;
    private final double longitude;

    @QueryProjection
    public StoreCoordianteDto(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
