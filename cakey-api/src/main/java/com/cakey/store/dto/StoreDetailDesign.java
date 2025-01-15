package com.cakey.store.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StoreDetailDesign(
    long cakeId,
    String imageUrl,
    boolean isLiked
) {
    public static StoreDetailDesign of(final long cakeId,
                                       final String imageUrl,
                                       final boolean isLiked) {
        return StoreDetailDesign.builder()
                .cakeId(cakeId)
                .imageUrl(imageUrl)
                .isLiked(isLiked)
                .build();
    }


}
