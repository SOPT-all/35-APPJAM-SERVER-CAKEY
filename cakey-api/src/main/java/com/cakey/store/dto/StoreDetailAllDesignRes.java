package com.cakey.store.dto;

import java.util.List;

public record StoreDetailAllDesignRes(
        List<StoreDetailDesign> storeDesignDtoList
){
    public StoreDetailAllDesignRes from(final List<StoreDetailDesign> storeDesignDtoList) {
        return new StoreDetailAllDesignRes(storeDesignDtoList);
    }
}
