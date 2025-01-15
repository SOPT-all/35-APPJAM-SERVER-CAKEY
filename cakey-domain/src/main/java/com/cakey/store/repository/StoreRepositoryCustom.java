package com.cakey.store.repository;

import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
import com.cakey.store.dto.StoreInfoDto;

import java.util.List;

public interface StoreRepositoryCustom {
    List<StoreCoordianteDto> findStoreCoordinatesByStation(final Station station);

    List<StoreInfoDto> findPopularitryStoreInfoByStation(final Long userId,
                                                         final Station station,
                                                         final Integer likesCursor,
                                                         final Long lastStoreId,
                                                         final int size);

    List<StoreInfoDto> findLatestStoreInfoByStation(final Long userId,
                                                    final Station station,
                                                    final Long storeIdCursor,
                                                    final int size);

    List<StoreInfoDto> findLatestStoresLikedByUser(final long userId,
                                                   final Long storeIdCursor,
                                                   final int size);

    List<StoreInfoDto>findPopularityStoresLikedByUser(final long userId,
                                                      final Integer likesCursor,
                                                      final Long storeIdCursor,
                                                      final int size);
}
