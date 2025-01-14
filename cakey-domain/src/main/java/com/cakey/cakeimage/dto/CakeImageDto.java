package com.cakey.cakeimage.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CakeImageDto {
    private final Long imageId;
    private final String imageUrl;

    @QueryProjection
    public CakeImageDto(Long imageId, String imageUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }
}