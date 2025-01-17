package com.cakey.cake.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;

import java.util.List;

public record CakeSelectedDto(
        long storeId,
        String storeName,
        Station station,
        List<CakeSelectedInfoDto>cakes
) {
    @QueryProjection
    public CakeSelectedDto {}
}
