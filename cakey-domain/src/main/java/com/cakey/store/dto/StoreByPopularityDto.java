package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import lombok.Builder;

@Builder
public record StoreByPopularityDto(
        Long storeId,
        String storeName,
        Long storeLike,
        Station station
) {
    public StoreByPopularityDto of(final Long storeId, final String storeName, final Long storeLike, final Station station) {
        return StoreByPopularityDto.builder()
                .storeId(storeId)
                .storeName(storeName)
                .storeLike(storeLike)
                .station(station)
                .build();
    }
}
