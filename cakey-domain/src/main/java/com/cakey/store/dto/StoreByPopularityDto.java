package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import lombok.Builder;

@Builder
public record StoreByPopularityDto(
        Long storeId,
        String storeName,
        Long storeLikesCount,
        Station station
) {
    public StoreByPopularityDto of(final Long storeId, final String storeName, final Long storeLikesCount, final Station station) {
        return StoreByPopularityDto.builder()
                .storeId(storeId)
                .storeName(storeName)
                .storeLikesCount(storeLikesCount)
                .station(station)
                .build();
    }
}
