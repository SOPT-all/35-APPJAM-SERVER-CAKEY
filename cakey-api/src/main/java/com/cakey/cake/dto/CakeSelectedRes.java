package com.cakey.cake.dto;

import com.cakey.store.domain.Station;
import lombok.Builder;

import java.util.List;

@Builder
public record CakeSelectedRes(
        long storeId,
        String storeName,
        Station station,
        List<CakeSelectedInfo> cake) {

        public static CakeSelectedRes of(final long storeId,
                                         final String storeName,
                                         final Station station,
                                         final List<CakeSelectedInfo> cake) {
                return CakeSelectedRes.builder()
                        .storeId(storeId)
                        .storeName(storeName)
                        .station(station)
                        .cake(cake)
                        .build();
        }
}

