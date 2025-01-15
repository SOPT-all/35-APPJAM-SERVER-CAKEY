package com.cakey.cake.dto;

public record CakeDesignDto(
        long cakeId,
        String cakeImageUrl,
        boolean isLiked
) {
    public CakeDesignDto(long cakeId, String cakeImageUrl, boolean isLiked) {
        this.cakeId = cakeId;
        this.cakeImageUrl = cakeImageUrl;
        this.isLiked = isLiked;
    }
}
