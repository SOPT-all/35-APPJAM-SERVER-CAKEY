package com.cakey.store.dto;

import java.util.List;

public record StoreListByPopularityRes(
        List<StoreByPopularityDto> storeList
) {
    public StoreListByPopularityRes of(final List<StoreByPopularityDto> storeList) {
        return new StoreListByPopularityRes(storeList);
    }
}
