package com.cakey.store.repository;

import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreBySelectedCakeDto;
import com.cakey.store.dto.StoreCoordinatesDto;
import com.cakey.store.dto.StoreInfoDto;
import com.cakey.store.dto.StoreSelectedDto;

import java.util.List;
import java.util.Optional;

public interface StoreRepositoryCustom {
    List<StoreCoordinatesDto> findStoreCoordinatesByStation(final Station station);

    //지하철역 스토어 조회(인기순)
    List<StoreInfoDto> findPopularitryStoreInfoByStation(final Long userId,
                                                         final Station station,
                                                         final Integer likesCursor,
                                                         final Long lastStoreId,
                                                         final int size);

    //지하철역 스토어 조회(최신순)
    List<StoreInfoDto> findLatestStoreInfoByStation(final Long userId,
                                                    final Station station,
                                                    final Long storeIdCursor,
                                                    final int size);

    //찜한 스토어 조회(최신순)
    List<StoreInfoDto> findLatestStoresLikedByUser(final long userId,
                                                   final Long storeIdCursor,
                                                   final int size);

    //찜한 스토어 조회(인기순)
    List<StoreInfoDto>findPopularityStoresLikedByUser(final long userId,
                                                      final Integer likesCursor,
                                                      final Long storeIdCursor,
                                                      final int size);

    //찜한 스토어 좌표 조회
    List<StoreCoordinatesDto> findLikedStoreCoordinatesByUserId(final Long userId);

    //선택한 케이크의 스토어 조회
    Optional<StoreBySelectedCakeDto> findStoreBySelectedCakeId(final long cakeId);

    //선택한 스토어 조회
    Optional<StoreSelectedDto> findStoreInfoById(final long storeId, final Long userId);
}
