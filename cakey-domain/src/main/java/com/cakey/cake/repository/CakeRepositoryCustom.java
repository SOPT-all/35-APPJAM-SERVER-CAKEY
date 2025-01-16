package com.cakey.cake.repository;

import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.store.domain.Station;

import java.util.List;

public interface CakeRepositoryCustom {
    List<CakeMainImageDto> findMainImageByStoreIds(final List<Long> storeIds);

    //해당역 케이크들 조회(최신순)
    List<CakeInfoDto> findLatestCakesByStation(final Long userId,
                                               final Station station,
                                               final Long storeIdCursor,
                                               final int size);

    //해당역 케이크들 조회(인기순)
    List<CakeInfoDto> findPopularCakesByStation(final Long userId,
                                                final Station station,
                                                final Integer cakeLikesCursor,
                                                final Long cakeIdCursor,
                                                final int size);

}
