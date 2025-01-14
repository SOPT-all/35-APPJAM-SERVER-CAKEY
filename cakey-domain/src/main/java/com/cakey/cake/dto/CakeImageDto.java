package com.cakey.cake.dto;

import lombok.Getter;

@Getter
public class CakeImageDto {
    private final long imageId;
    private final String imageUrl;

    public CakeImageDto(final long imageId, final String imageUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }


}
