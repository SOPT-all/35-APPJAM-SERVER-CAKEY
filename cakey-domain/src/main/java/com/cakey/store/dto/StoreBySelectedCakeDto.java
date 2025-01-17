package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class StoreBySelectedCakeDto {
    private final Long storeId;
    private final String storeName;
    private final Station station;

    @QueryProjection
    public StoreBySelectedCakeDto(final Long storeId,
                                  final String storeName,
                                  final Station station) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.station = station;
    }
}
