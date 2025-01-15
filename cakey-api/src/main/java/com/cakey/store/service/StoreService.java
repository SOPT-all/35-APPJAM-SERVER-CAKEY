package com.cakey.store.service;

import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.*;
import com.cakey.store.facade.StoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    //스토어 리스트 조회(인기순)
    public StoreInfoListBylikesRes getStoreInfoListByStationAndLikes(final Long userId,
                                                                     final Station station,
                                                                     final int likesCursor,
                                                                     final Long lastStoreId,
                                                                     final int size) {

        final List<StoreInfoDto> storeInfoDtos = storeFacade.findStoreInfoByStationAndLikes(userId, station, likesCursor, lastStoreId, size);

        // 조회한 store들의 ID 추출
        final List<Long> storeIds = getStoreIds(storeInfoDtos);

        //메인 이미지 map
        final Map<Long, List<CakeMainImageDto>> mainImageMap = getMainImageMap(storeIds);

        //storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        //스토어 개수 조회
        final int storeCount = getStoreCount(station);

        final int nextLikesCursor = calculateNextCursor(storeInfoDtos);

        final Long LastStoreId = calculateLastStoreId(storeInfoDtos);

        // StoreInfoListRes 반환
        return StoreInfoListBylikesRes.of(nextLikesCursor, LastStoreId, storeCount, storeInfos);
    }

    //스토어 리스트 조회(최신순)
    public StoreInfoListByLatest getStoreInfoListByStationAndLatest(final Long userId,
                                                                    final Station station,
                                                                    final Long storeIdCursor,
                                                                    final int size) {

        //커서 페이지네이션
        final List<StoreInfoDto> storeInfoDtos = storeFacade.findStoreInfoByStationAndLatest(userId, station, storeIdCursor, size);

        // 조회한 store들의 ID 추출
        final List<Long> storeIds = getStoreIds(storeInfoDtos);

        //메인 이미지 map
        final Map<Long, List<CakeMainImageDto>> mainImageMap = getMainImageMap(storeIds);

        //storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        //스토어 개수 조회
        final int storeCount = getStoreCount(station);

        //마지막 storeID 조회
        final Long LastStoreId = calculateLastStoreId(storeInfoDtos);

        return StoreInfoListByLatest.of(LastStoreId, storeCount, storeInfos);
    }

    //전체 지하철역 조회
    public List<AllStationRes.StationInfo> getAllStation() {
        return Arrays.stream(Station.values())
                .map(station -> AllStationRes.StationInfo.of(
                        station.getStationName(),
                        station.getLatitude(),
                        station.getLongitude()
                ))
                .collect(Collectors.toList());
    }

    private int calculateNextCursor(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.isEmpty() ? -1 : storeInfoDtos.get(storeInfoDtos.size() - 1).getStoreLikesCount();
    }

    private Long calculateLastStoreId(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.isEmpty() ? null : storeInfoDtos.get(storeInfoDtos.size() - 1).getStoreId();
    }

    //스토어 id들 조회
    private List<Long> getStoreIds(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.stream()
                .map(StoreInfoDto::getStoreId)
                .toList();
    }

    //메인이미지 매핑
    private Map<Long, List<CakeMainImageDto>> getMainImageMap(final List<Long> storeIds) {
        //스토어 대표 이미지 조회
        final List<CakeMainImageDto> cakeMainImageDtos1 = cakeFacade.findMainImageByStoreIds(storeIds);

        // 이미지를 storeId 기준으로 그룹화
        return cakeMainImageDtos1.stream()
                .collect(Collectors.groupingBy(CakeMainImageDto::getStoreId));
    }

    //storeInfo 생성
    private List<StoreInfo> getStoreInfo(final List<StoreInfoDto> storeInfoDtos, final Map<Long, List<CakeMainImageDto>> imageMap) {
        return storeInfoDtos.stream()
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
    }

    //스토어 개수 조회
    private int getStoreCount(final Station station) {
        return (station == Station.ALL)
                ? storeFacade.countAllStores()
                : storeFacade.countStoresByStation(station);
    }
}
