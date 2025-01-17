package com.cakey.cake.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

public record CakeSelectedInfoDto(
        Long cakeId,
        boolean isLiked,
        String imageUrl
) {
    @QueryProjection
    public CakeSelectedInfoDto{}

}
