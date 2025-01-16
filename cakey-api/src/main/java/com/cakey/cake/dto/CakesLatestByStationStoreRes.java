package com.cakey.cake.dto;

import java.util.List;

public record CakesLatestByStationStoreRes(
     Long cakeIdCursor,
     int cakeCount,
     boolean isLastData,
     List<CakeInfo> cakes
) {
    public static CakesLatestByStationStoreRes from(final Long cakeIdCursor,
                                                    final int cakeCount,
                                                    final boolean isLastData,
                                                    final List<CakeInfo> cakes) {
        return new CakesLatestByStationStoreRes(cakeIdCursor, cakeCount, isLastData, cakes);
    }
}
