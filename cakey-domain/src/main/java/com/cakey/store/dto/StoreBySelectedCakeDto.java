package com.cakey.store.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public record StoreBySelectedCakeDto(Long storeId, String storeName, Station station) {
    @QueryProjection
    public StoreBySelectedCakeDto {
    }
}
