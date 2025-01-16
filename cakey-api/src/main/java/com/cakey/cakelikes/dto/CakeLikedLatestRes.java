package com.cakey.cakelikes.dto;

import com.cakey.cake.dto.CakeInfo;

import java.util.List;

public record CakeLikedLatestRes(
        Long nextCakeIdCursor,
        int cakeCount,
        boolean isLastData,
        List<CakeInfo> cakes
) {
    public static CakeLikedLatestRes from(final Long nextCakeIdCursor,
                                          final int cakeCount,
                                          final boolean isLastData,
                                          final List<CakeInfo> cakes) {
        return new CakeLikedLatestRes(nextCakeIdCursor, cakeCount, isLastData, cakes);
    }
}


