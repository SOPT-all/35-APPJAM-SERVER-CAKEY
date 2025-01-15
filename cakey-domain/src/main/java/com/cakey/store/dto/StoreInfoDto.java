package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StoreInfoDto {
    private final Long storeId;
    private final String name;
    private final Station station;
    private final String address;
    private final boolean isLiked;
    private final int storeLikesCount;
    private Long storeIdCursor;

    @QueryProjection
    public StoreInfoDto(Long storeId,
                        String name,
                        Station station,
                        String address,
                        boolean isLiked,
                        int storeLikesCount,
                        Long storeIdCursor) {
        this.storeId = storeId;
        this.name = name;
        this.station = station;
        this.address = address;
        this.isLiked = isLiked;
        this.storeLikesCount = storeLikesCount;
        this.storeIdCursor = storeIdCursor;
    }
}
