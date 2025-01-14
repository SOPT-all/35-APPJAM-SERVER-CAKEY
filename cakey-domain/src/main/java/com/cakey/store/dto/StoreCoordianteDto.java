package com.cakey.store.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

public record StoreCoordianteDto(long id, double latitude, double longitude) {
    @QueryProjection
    public StoreCoordianteDto {
    }
}
