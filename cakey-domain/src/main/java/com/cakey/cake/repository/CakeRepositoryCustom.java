package com.cakey.cake.repository;

import com.cakey.cake.dto.CakeMainImageDto;

import java.util.List;

public interface CakeRepositoryCustom {
    List<CakeMainImageDto> findMainImageByStoreIds(List<Long> storeIds);

}
