package com.cakey.cake.dto;

import java.util.List;

public record CakeListByPopularityRes(
        List<CakeByPopularityDto> cakeList
) {
    public CakeListByPopularityRes of(List<CakeByPopularityDto> cakeList) {
        return new CakeListByPopularityRes(cakeList);
    }
}
