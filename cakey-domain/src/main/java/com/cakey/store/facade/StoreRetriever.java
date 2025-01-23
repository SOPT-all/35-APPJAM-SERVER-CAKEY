package com.cakey.store.facade;

import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.store.domain.Station;
import com.cakey.store.domain.Store;
import com.cakey.store.dto.*;
import com.cakey.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public List<StoreCoordinatesDto> findLikedStoreCoordinatesByUserId(final Long userId) {
        return storeRepository.findLikedStoreCoordinatesByUserId(userId);
    }

    //선택된 케이크의 스토어 정보 조회
    public StoreBySelectedCakeDto findStoreBySelectedCakeId(final Long cakeId) {
        return storeRepository.findStoreBySelectedCakeId(cakeId).orElseThrow(
                NotFoundBaseException::new
        );
    }

    //선택한 스토어 조회
    @Transactional(readOnly = true)
    public StoreSelectedDto findStoreInfoById(long storeId, Long userId) {
        return storeRepository.findStoreInfoById(storeId, userId).orElseThrow(NotFoundBaseException::new);
    }


    public int countAllStores() {
        return storeRepository.countAllStores();
    }

    public int countStoresByStation(final Station station) {
        return storeRepository.countStoresByStation(station);
    }

    @Transactional(readOnly = true)
    public Store findById(final Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(NotFoundBaseException::new);
    }

    @Transactional(readOnly = true)
    public List<StoreByPopularityDto> findStoreListByLank(){
        if (storeRepository.findStoresByLikeCount().isEmpty()) {
            throw new NotFoundBaseException();
        }
        return storeRepository.findStoresByLikeCount();
    }

    //스토어 존재 여부
    public void isExistStore(final long storeId) {
        final boolean isExistStore = storeRepository.existsById(storeId);
        if (!isExistStore) {
            throw new NotFoundBaseException();
        }
    }
}
