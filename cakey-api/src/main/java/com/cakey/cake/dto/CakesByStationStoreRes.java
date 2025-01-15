package com.cakey.cake.dto;

import java.util.List;

public record CakesByStationStoreRes(
     Long cakeIdCursor,
     int cakeCount,
     List<CakeInfo> cakes
) {
    public static CakesByStationStoreRes from(final Long cakeIdCursor,
                                              final int cakeCount,
                                              final List<CakeInfo> cakes) {
        return new CakesByStationStoreRes(cakeIdCursor, cakeCount, cakes);
    }
}
