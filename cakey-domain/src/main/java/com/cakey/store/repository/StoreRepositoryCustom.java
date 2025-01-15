package com.cakey.store.repository;

import com.cakey.OrderBy;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
import com.cakey.store.dto.StoreInfoDto;

import java.util.List;

public interface StoreRepositoryCustom {
    List<StoreCoordianteDto> findStoreCoordinatesByStation(final Station station);

    List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                      final Station station,
                                                      final int likesCursor,
                                                      final Long lastStoreId,
                                                      final int size);
}
