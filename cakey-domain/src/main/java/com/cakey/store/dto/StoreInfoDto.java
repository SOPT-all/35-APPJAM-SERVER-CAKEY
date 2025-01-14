package com.cakey.store.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;


@Getter
public class StoreInfoDto {
    private final Long storeId;
    private final String name;
    private final String station;
    private final String address;
    private final boolean isLiked;
    private final int nextCursor;

    @QueryProjection
    public StoreInfoDto(Long storeId,
                        String name,
                        String station,
                        String address,
                        boolean isLiked,
                        int nextCursor) {
        this.storeId = storeId;
        this.name = name;
        this.station = station;
        this.address = address;
        this.isLiked = isLiked;
        this.nextCursor = nextCursor;
    }
}
