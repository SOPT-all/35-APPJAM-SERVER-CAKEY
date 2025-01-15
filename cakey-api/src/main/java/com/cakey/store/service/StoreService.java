package com.cakey.store.service;

import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.*;
import com.cakey.store.facade.StoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreFacade storeFacade;
    private final CakeFacade cakeFacade;

    public List<StoreCoordinate> getStoreCoordinateList(final Station station) {
        final List<StoreCoordianteDto> storeCoordianteDtoList = storeFacade.findCoordinatesByStation(station);

        return storeCoordianteDtoList.stream()
                .map(storeCoordianteDto -> StoreCoordinate.of(
                        storeCoordianteDto.id(),
                        storeCoordianteDto.latitude(),
                        storeCoordianteDto.longitude())
                ).toList();
    }

    //스토어 리스트 조회(인기순) API
    public StoreInfoListRes getStoreInfoListByStationAndLikes(final Long userId,
                                                              final Station station,
                                                              final int likesCursor,
                                                              final Long lastStoreId,
                                                              final int size) {

        final List<StoreInfoDto> storeInfoDtos = storeFacade.findStoreInfoByStationAndLikes(userId, station, likesCursor, lastStoreId, size);

        // 조회한 store들의 ID 추출
        final List<Long> storeIds = storeInfoDtos.stream()
                .map(StoreInfoDto::getStoreId)
                .toList();

        // 스토어 ID들로 대표 이미지(4개)를 조회
        final List<CakeMainImageDto> cakeMainImageDtos = cakeFacade.findMainImageByStoreIds(storeIds);

        // 이미지를 storeId 기준으로 그룹화
        final Map<Long, List<CakeMainImageDto>> imageMap = cakeMainImageDtos.stream()
                .collect(Collectors.groupingBy(CakeMainImageDto::getStoreId));

        // StoreInfo 생성 및 이미지 매칭
        final List<StoreInfo> storeInfos = storeInfoDtos.stream()
                .map(storeInfoDto -> {
                    // storeId에 해당하는 이미지 리스트 가져오기
                    final List<StoreInfo.StoreMainImage> images = imageMap.getOrDefault(storeInfoDto.getStoreId(), List.of())
                            .stream()
                            .map(image -> StoreInfo.StoreMainImage.of(image.getImageId(), image.getImageUrl()))
                            .toList();

                    // StoreInfo 생성
                    return StoreInfo.of(
                            storeInfoDto.getStoreId(),
                            storeInfoDto.getName(),
                            Station.valueOf(String.valueOf(storeInfoDto.getStation())),
                            storeInfoDto.getAddress(),
                            storeInfoDto.getStoreLikesCount(),
                            storeInfoDto.isLiked(),
                            images
                    );
                })
                .toList();

        //스토어 개수 조회
        final int storeCount = (station == Station.ALL)
                ? storeFacade.countAllStores()
                : storeFacade.countStoresByStation(station);

        final int nextLikesCursor = calculateNextCursor(storeInfoDtos);

        final Long LastStoreId = calculateLastStoreId(storeInfoDtos);

        // StoreInfoListRes 반환
        return StoreInfoListRes.of(nextLikesCursor, LastStoreId, storeCount, storeInfos);
    }

    private int calculateNextCursor(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.isEmpty() ? -1 : storeInfoDtos.get(storeInfoDtos.size() - 1).getStoreLikesCount();
    }

    private Long calculateLastStoreId(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.isEmpty() ? null : storeInfoDtos.get(storeInfoDtos.size() - 1).getStoreId();
    }
}
