package com.cakey.cakelikes.dto;

import com.cakey.cake.dto.CakeInfo;

import java.util.List;

public record CakeLikedLatestRes(
        Long cakeIdCursor,
        int cakeCount,
        boolean isLastData,
        List<CakeInfo> cakes
) {
    public static CakeLikedLatestRes from(final Long cakeIdCursor,
                                          final int cakeCount,
                                          final boolean isLastData,
                                          final List<CakeInfo> cakes) {
        return new CakeLikedLatestRes(cakeIdCursor, cakeCount, isLastData, cakes);
    }
}


