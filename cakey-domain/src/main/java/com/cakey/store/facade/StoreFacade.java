package com.cakey.store.facade;

import com.cakey.store.domain.Station;
import com.cakey.store.domain.Store;
import com.cakey.store.dto.StoreCoordianteDto;
import com.cakey.store.dto.StoreInfoDto;
import com.cakey.store.dto.StoreKakaoLinkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreFacade {

    private final StoreRetriever storeRetriever;

    public List<StoreCoordianteDto> findCoordinatesByStation(final Station station) {
        return storeRetriever.findCoordinateByStation(station);
    }

    //스토어 조회(인기순)
    public List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                             final Station station,
                                                             final int likesCursor,
                                                             final Long lastStoreId,
                                                             final int size) {
        return storeRetriever.findStoreInfoByStationAndLikes(userId, station, likesCursor, lastStoreId, size);
    }

    //스토어 조회(최신순)
    public List<StoreInfoDto> findStoreInfoByStationAndLatest(final Long userId,
                                                              final Station station,
                                                              final Long storeCursorId,
                                                              final int size) {

        return storeRetriever.findStoreInfoByStationAndLatest(userId, station, storeCursorId, size);
    }
    //찜한 스토어(최신순)
    public List<StoreInfoDto> findLatestStoresLikedByUser(final Long userId,
                                                          final Long storeIdCursor,
                                                          final int size) {
        return storeRetriever.findStoresLikedByUser(userId, storeIdCursor, size);
    }

    // 조회한 store들의 ID 추출
    public List<Long> getStoreIds(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.stream()
                .map(StoreInfoDto::getStoreId)
                .toList();
    }

    //스토어 개수 조회
    public int getStoreCountByStation(final Station station) {
        return (station == Station.ALL)
                ? countAllStores()
                : countStoresByStation(station);
    }

    private int countAllStores() {
        return storeRetriever.countAllStores();
    }
    private int countStoresByStation(final Station station) {
        return storeRetriever.countStoresByStation(station);
    }

    //마지막 스토어아이디 계산
    public Long calculateLastStoreId(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.isEmpty() ? null : storeInfoDtos.get(storeInfoDtos.size() - 1).getStoreId();
    }

    //카카오 링크
    public StoreKakaoLinkDto findById(final Long storeId) {
        Store store = storeRetriever.findById(storeId);
        return new StoreKakaoLinkDto(store.getOpenKakaoUrl());
    }
}
