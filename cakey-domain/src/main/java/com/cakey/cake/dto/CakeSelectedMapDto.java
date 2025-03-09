package com.cakey.cake.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CakeSelectedMapDto {
    private final Long storeId;
    private final String storeName;
    private final String address;
    private final Station station;
    private final Boolean isLiked;
    private final String imageUrl;

    @QueryProjection
    public CakeSelectedMapDto(final Long storeId,
                              final String storeName, final String address,
                              final Station station,
                              final Boolean isLiked,
                              final String imageUrl) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.station = station;
        this.isLiked = isLiked;
        this.imageUrl = imageUrl;
    }
}
