package com.cakey.cakelikes.dto;

import com.cakey.cake.dto.CakeInfo;

import java.util.List;

public record CakeLikedPopularRes(
        Integer nextLikeCursor,
        Long nextCakeIdCursor,
        int cakeCount,
        boolean isLastData,
        List<CakeInfo> cakes
) {
    public static CakeLikedPopularRes from(final Integer nextLikeCursor,
                                           final Long nextCakeIdCursor,
                                           final int cakeCount,
                                           final boolean isLastData,
                                           final List<CakeInfo> cakes) {
        return new CakeLikedPopularRes(nextLikeCursor, nextCakeIdCursor, cakeCount, isLastData, cakes);
    }
}
