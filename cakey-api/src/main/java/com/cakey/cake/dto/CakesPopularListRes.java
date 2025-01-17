package com.cakey.cake.dto;

import java.util.List;

public record CakesPopularListRes(
        int cakeLikesCursor,
        Long cakeIdCursor,
        int cakeCount,
        boolean isLastData,
        List<CakeInfo> cakes
) {
    public static CakesPopularListRes from(final Integer cakeLikesCursor,
                                           final Long cakeIdCursor,
                                           final int cakeCount,
                                           boolean isLastData,
                                           final List<CakeInfo> cakes) {
        return new CakesPopularListRes(cakeLikesCursor, cakeIdCursor, cakeCount, isLastData, cakes);
    }
}
