package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;

public record StoreSelectedDto(long storeId,
                               String storeName,
                               String address,
                               Station station,
                               boolean isLiked,
                               String imageUrl,
                               int storeLikesCount) {
    @QueryProjection
    public StoreSelectedDto {
    }

}
