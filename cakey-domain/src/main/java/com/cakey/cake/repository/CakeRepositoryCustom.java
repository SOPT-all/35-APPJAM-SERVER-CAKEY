package com.cakey.cake.repository;

import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.store.domain.Station;

import java.util.List;

public interface CakeRepositoryCustom {
    List<CakeMainImageDto> findMainImageByStoreIds(final List<Long> storeIds);

    //해당역 케이크들 조회
    List<CakeInfoDto> findCakesByStation(final Long userId, final Station station, final Long storeIdCursor, final int size);
    }
