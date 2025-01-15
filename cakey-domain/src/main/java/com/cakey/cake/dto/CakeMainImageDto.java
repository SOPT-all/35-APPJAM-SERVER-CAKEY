package com.cakey.cake.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CakeMainImageDto {
    private final long storeId;
    private final long imageId;
    private final String imageUrl;

    @QueryProjection
    public CakeMainImageDto(final long storeId, final long imageId, final String imageUrl) {
        this.storeId = storeId;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }
}
