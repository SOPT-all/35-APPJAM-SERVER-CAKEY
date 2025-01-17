package com.cakey.store.facade;

import com.cakey.common.exception.NotFoundException;
import com.cakey.store.domain.Station;
import com.cakey.store.domain.Store;
import com.cakey.store.dto.*;
import com.cakey.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreRetriever {
    private final StoreRepository storeRepository;

    public List<StoreCoordinatesDto> findCoordinateByStation(final Station station) {
        return storeRepository.findStoreCoordinatesByStation(station);
    }

    //스토어 조회(인기순)
    public List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                             final Station station,
                                                             final Integer likesCursor,
                                                             final Long lastStoreId,
                                                             final int size) {
        return storeRepository.findPopularitryStoreInfoByStation(userId, station, likesCursor, lastStoreId, size);
    }

    //스토어 조회(최신순)
    public List<StoreInfoDto> findStoreInfoByStationAndLatest(final Long userId,
                                                              final Station station,
                                                              final Long storeIdCursor,
                                                              final int size) {
        return storeRepository.findLatestStoreInfoByStation(userId, station, storeIdCursor, size);
    }

    //찜한 스토어 조회(최신순)
    public List<StoreInfoDto> findStoresLikedByUser(final long userId,
                                                    final Long storeIdCursor,
                                                    final int size) {
        return storeRepository.findLatestStoresLikedByUser(userId, storeIdCursor, size);
    }

    //찜한 스토어 조회(인기순)
    public List<StoreInfoDto> findPopularityStoresLikedByUser(final long userId,
                                                              final Integer likesCursor,
                                                              final Long storeIdCursor,
                                                              final int size) {
        return storeRepository.findPopularityStoresLikedByUser(userId, likesCursor, storeIdCursor, size);
    }
    //찜한 스토어 좌표 조회
    public List<StoreCoordinatesDto> findLikedStoreCoordinatesByUserId(final Long userId) {
        return storeRepository.findLikedStoreCoordinatesByUserId(userId);
    }

    //선택된 케이크의 스토어 정보 조회
    public StoreBySelectedCakeDto findStoreBySelectedCakeId(final Long cakeId) {
        return storeRepository.findStoreBySelectedCakeId(cakeId);
    }


    public int countAllStores() {
        return storeRepository.countAllStores();
    }

    public int countStoresByStation(final Station station) {
        return storeRepository.countStoresByStation(station);
    }

    public Store findById(final Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException());
    }

    public List<StoreByPopularityDto> findStoreListByLank(){
        return storeRepository.findStoresByLikeCount();
    }
}
