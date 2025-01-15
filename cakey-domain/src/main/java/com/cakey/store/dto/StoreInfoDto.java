package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;


@Getter
public class StoreInfoDto {
    private final Long storeId;
    private final String name;
    private final Station station;
    private final String address;
    private final boolean isLiked;
    private final int storeLikesCount;

    @QueryProjection
    public StoreInfoDto(Long storeId,
                        String name,
                        Station station,
                        String address,
                        boolean isLiked,
                        int nextCursor) {
        this.storeId = storeId;
        this.name = name;
        this.station = station;
        this.address = address;
        this.isLiked = isLiked;
        this.storeLikesCount = nextCursor;
    }
}
