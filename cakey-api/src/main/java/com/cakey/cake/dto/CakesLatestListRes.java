package com.cakey.cake.dto;

import java.util.List;

public record CakesLatestListRes(
     Long nextCakeIdCursor,
     int cakeCount,
     boolean isLastData,
     List<CakeInfo> cakes
) {
    public static CakesLatestListRes from(final Long nextCakeIdCursor,
                                          final int cakeCount,
                                          final boolean isLastData,
                                          final List<CakeInfo> cakes) {
        return new CakesLatestListRes(nextCakeIdCursor, cakeCount, isLastData, cakes);
    }
}
