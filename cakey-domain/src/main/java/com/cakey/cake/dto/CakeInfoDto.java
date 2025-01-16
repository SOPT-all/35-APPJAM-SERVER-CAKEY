package com.cakey.cake.dto;

import com.cakey.store.domain.Station;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CakeInfoDto {
    private long cakeId;
    private long storeId;
    private String storeName;
    private Station station;
    private boolean isLiked;
    private String imageUrl;
    private int cakeLikeCount;
    private Long cakeIdCursor;
    private boolean isLastData;

    @QueryProjection
    public CakeInfoDto(long cakeId,
                       long storeId,
                       String storeName,
                       Station station,
                       boolean isLiked,
                       String imageUrl,
                       int cakeLikeCount,
                       Long cakeIdCursor,
                       boolean isLastData) {
        this.cakeId = cakeId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.station = station;
        this.isLiked = isLiked;
        this.imageUrl = imageUrl;
        this.cakeLikeCount = cakeLikeCount;
        this.cakeIdCursor = cakeIdCursor;
        this.isLastData = isLastData;
    }
}