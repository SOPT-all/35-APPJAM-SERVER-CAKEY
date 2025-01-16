package com.cakey.cake.dto;

import java.util.List;

public record CakesPopularByStationStoreRes(
        int cakeLikesCursor,
        Long cakeIdCursor,
        int cakeCount,
        List<CakeInfo> cakes
) {
    public static CakesPopularByStationStoreRes from(final Integer cakeLikesCursor,
                                                     final Long cakeIdCursor,
                                                     final int cakeCount,
                                                     final List<CakeInfo> cakes) {
        return new CakesPopularByStationStoreRes(cakeLikesCursor, cakeIdCursor, cakeCount, cakes);
    }
}
