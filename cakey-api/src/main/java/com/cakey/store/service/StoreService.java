package com.cakey.store.service;

import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.common.exception.NotFoundException;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.*;
import com.cakey.store.facade.StoreFacade;
import com.cakey.user.dto.UserInfoDto;
import com.cakey.user.dto.UserInfoRes;
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
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoDtos);

        //메인 이미지 map
        final Map<Long, List<CakeMainImageDto>> mainImageMap = cakeFacade.getMainImageMap(storeIds);

        //storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        //스토어 개수 조회
        final int storeCount = storeFacade.getStoreCountByStation(station);

        final int nextLikesCursor = calculateNextCursor(storeInfoDtos);

        final Long LastStoreId = storeFacade.calculateLastStoreId(storeInfoDtos);

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
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoDtos);

        //메인 이미지 map
        final Map<Long, List<CakeMainImageDto>> mainImageMap = cakeFacade.getMainImageMap(storeIds);

        //storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        //스토어 개수 조회
        final int storeCount = storeFacade.getStoreCountByStation(station);

        //마지막 storeID 조회
        final Long LastStoreId = storeFacade.calculateLastStoreId(storeInfoDtos);

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

    //스토어 id들 조회
    private List<Long> getStoreIds(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.stream()
                .map(StoreInfoDto::getStoreId)
                .toList();
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

    public StoreKakaoLinkRes getStoreKakaoLink(final Long storeId) {
        final StoreKakaoLinkDto storeKakaoLinkDto;
        try {
            storeKakaoLinkDto = storeFacade.findById(storeId);
        } catch (NotFoundException e) {
            //todo: 추후 구체적인 예외처리
            throw e;
        }
        return new StoreKakaoLinkRes(storeKakaoLinkDto.kakaoLink());
    }
}
