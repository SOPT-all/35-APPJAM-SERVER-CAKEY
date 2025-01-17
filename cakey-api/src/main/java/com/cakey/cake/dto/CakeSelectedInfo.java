package com.cakey.cake.dto;

import lombok.Builder;

@Builder
public record CakeSelectedInfo(
        long cakeId,
        boolean isLiked,
        String imageUrl
) {
    public static CakeSelectedInfo of(final long cakeId,
                                      boolean isLiked,
                                      final String imageUrl) {
        return CakeSelectedInfo.builder()
                .cakeId(cakeId)
                .isLiked(isLiked)
                .imageUrl(imageUrl)
                .build();
    }
}
