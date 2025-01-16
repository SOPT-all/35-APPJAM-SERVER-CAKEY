package com.cakey.cake.dto;

import java.util.List;

public record CakesLatestByStationStoreRes(
     Long cakeIdCursor,
     int cakeCount,
     List<CakeInfo> cakes
) {
    public static CakesLatestByStationStoreRes from(final Long cakeIdCursor,
                                                    final int cakeCount,
                                                    final List<CakeInfo> cakes) {
        return new CakesLatestByStationStoreRes(cakeIdCursor, cakeCount, cakes);
    }
}
