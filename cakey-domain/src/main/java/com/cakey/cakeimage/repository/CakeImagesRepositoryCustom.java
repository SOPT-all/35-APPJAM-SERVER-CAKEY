package com.cakey.cakeimage.repository;

import com.cakey.cakeimage.dto.CakeImageDto;

import java.util.List;

public interface CakeImagesRepositoryCustom {
    List<CakeImageDto> findMainImagesByStoreIds(final List<Long> storeIds);
}
