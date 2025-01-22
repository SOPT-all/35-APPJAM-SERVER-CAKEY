package com.cakey.store.facade;

import com.cakey.store.domain.Station;
import com.cakey.store.domain.Store;
import com.cakey.store.dto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreFacade {

    private final StoreRetriever storeRetriever;

    public List<StoreCoordinatesDto> findCoordinatesByStation(final Station station) {
        return storeRetriever.findCoordinateByStation(station);
    }

    //스토어 조회(인기순)
    public List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                             final Station station,
                                                             final Integer likesCursor,
                                                             final Long storeIdCursor,
                                                             final int size) {
        return storeRetriever.findStoreInfoByStationAndLikes(userId, station, likesCursor, storeIdCursor, size);
    }

    //스토어 조회(최신순)
    public List<StoreInfoDto> findStoreInfoByStationAndLatest(final Long userId,
                                                              final Station station,
                                                              final Long storeCursorId,
                                                              final int size) {

        return storeRetriever.findStoreInfoByStationAndLatest(userId, station, storeCursorId, size);
    }
    //찜한 스토어 조회(최신순)
    public List<StoreInfoDto> findLatestStoresLikedByUser(final Long userId,
                                                          final Long storeIdCursor,
                                                          final int size) {
        return storeRetriever.findStoresLikedByUser(userId, storeIdCursor, size);
    }

    //찜한 스토어 조회(인기순)
    public List<StoreInfoDto> findPopularityStoresLikedByUser(final long userId,
                                                                          final Integer likesCursor,
                                                                          final Long storeIdCursor,
                                                                          final int size) {
        return storeRetriever.findPopularityStoresLikedByUser(userId, likesCursor, storeIdCursor, size);
    }

    // 조회한 store들의 ID 추출
    public List<Long> getStoreIds(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.stream()
                .map(StoreInfoDto::getStoreId)
                .toList();
    }

    //찜한 스토어 좌표 조회
    public List<StoreCoordinatesDto> findLikedStoreCoordinatesByUserId(final Long userId) {
        return storeRetriever.findLikedStoreCoordinatesByUserId(userId);
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
    public StoreKakaoLinkDto findById(final long storeId) {
        Store store = storeRetriever.findById(storeId);
        return new StoreKakaoLinkDto(store.getOpenKakaoUrl());
    }

    //스토어 맛
    public StoreTasteDto findTaste(final Long storeId) {
        Store store = storeRetriever.findById(storeId);
        return new StoreTasteDto(store.getTaste());
    }

    //스토어 상세정보
    public StoreDetailInfoDto findStoreDetailInfo(final long storeId) {
        Store store = storeRetriever.findById(storeId);
        return new StoreDetailInfoDto(store.getAddress(), store.getPhone());
    }

    //선택된 케이크의 스토어 정보 조회
    public StoreBySelectedCakeDto findStoreBySelectedCakeId(final Long cakeId) {
        return storeRetriever.findStoreBySelectedCakeId(cakeId);
    }

    //선택한 스토어 조회
    public StoreSelectedDto findStoreInfoById(long storeId, Long userId) {
        return storeRetriever.findStoreInfoById(storeId, userId);
    }

    public List<StoreByPopularityDto> findStoreListByLank() {
        return storeRetriever.findStoreListByLank();
    }

    public Store findStoreById(final long storeId) {
        return storeRetriever.findById(storeId);
    }
}
