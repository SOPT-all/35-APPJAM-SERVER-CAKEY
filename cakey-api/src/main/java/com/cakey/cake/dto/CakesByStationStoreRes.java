package com.cakey.cake.dto;

public record CakesByStationStore(
     Long cakeIdCursor,
     int cakeCount,
     List<CakeInfo> cakes

) {

}
