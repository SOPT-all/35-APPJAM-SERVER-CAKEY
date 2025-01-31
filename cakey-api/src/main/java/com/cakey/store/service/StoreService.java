package com.cakey.store.service;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.exception.CakeErrorCode;
import com.cakey.cake.exception.CakeyNotFoundException;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.cakelike.facade.CakeLikesFacade;
import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.operationtime.dto.StoreOperationTimeDto;
import com.cakey.operationtime.facade.StoreOperationTimeFacade;
import com.cakey.size.dto.SizeDto;
import com.cakey.size.facade.SizeFacade;
import com.cakey.store.domain.Station;
import com.cakey.store.domain.Store;
import com.cakey.store.dto.*;
import com.cakey.store.exception.StoreErrorCode;
import com.cakey.store.exception.StoreNotfoundException;
import com.cakey.store.facade.StoreFacade;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cakey.store.exception.StoreErrorCode.STORE_KAKAO_LINK_NOT_FOUND;
import static com.cakey.store.exception.StoreErrorCode.STORE_OPERATION_TIME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreFacade storeFacade;
    private final CakeFacade cakeFacade;
    private final CakeLikesFacade cakeLikesFacade;
    private final SizeFacade sizeFacade;
    private final StoreOperationTimeFacade storeOperationTimeFacade;

    public List<StoreCoordinate> getStoreCoordinateList(final Station station) {
        final List<StoreCoordinatesDto> storeCoordianteDtoList;
        try {
            storeCoordianteDtoList = storeFacade.findCoordinatesByStation(station);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }

        return storeCoordianteDtoList.stream()
                .map(storeCoordianteDto -> StoreCoordinate.of(
                        storeCoordianteDto.storeId(),
                        storeCoordianteDto.latitude(),
                        storeCoordianteDto.longitude())
                ).toList();
    }

    //지하철역 스토어 리스트 조회(인기순)
    @Transactional(readOnly = true)
    public StoreInfoListBylikesRes getStoreInfoListByStationAndLikes(final Long userId,
                                                                     final Station station,
                                                                     final Integer likesCursor,
                                                                     final Long storeIdCursor,
                                                                     final int size) {

        final List<StoreInfoDto> storeInfoDtos;
        final Map<Long, List<CakeMainImageDto>> mainImageMap;

        try {
            storeInfoDtos = storeFacade.findStoreInfoByStationAndLikes(userId, station, likesCursor, storeIdCursor, size);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }

        /// 조회한 store들의 ID 추출
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoDtos);

        ///메인 이미지 map
        try {
            mainImageMap = cakeFacade.getMainImageMap(storeIds);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_MAIN_IMAGE_NOT_FOUND);
        }
        ///storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        ///스토어 개수 조회
        final int storeCount = storeFacade.getStoreCountByStation(station);

        ///커서 업데이트
        final int lastStoreInfoDtosIndex = storeInfoDtos.size() - 1;
        final int nextLikesCursor = storeInfoDtos.get(lastStoreInfoDtosIndex).getStoreLikesCount();
        final Long nextCakeIdCursor = storeInfoDtos.get(lastStoreInfoDtosIndex).getStoreIdCursor();
        final boolean isLastData = storeInfoDtos.get(lastStoreInfoDtosIndex).isLastData();

        ///StoreInfoListRes 반환
        return StoreInfoListBylikesRes.of(nextLikesCursor, nextCakeIdCursor, storeCount, isLastData, storeInfos);
    }

    //지하철역 스토어 리스트 조회(최신순)
    @Transactional(readOnly = true)
    public StoreInfoListByLatestRes getStoreInfoListByStationAndLatest(final Long userId,
                                                                       final Station station,
                                                                       final Long storeIdCursor,
                                                                       final int size) {

        final List<StoreInfoDto> storeInfoDtos;
        ///커서 페이지네이션
        try {
            storeInfoDtos = storeFacade.findStoreInfoByStationAndLatest(userId, station, storeIdCursor, size);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }
        /// 조회한 store들의 ID 추출
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoDtos);

        final Map<Long, List<CakeMainImageDto>> mainImageMap;
        ///메인 이미지 map
        try {
            mainImageMap = cakeFacade.getMainImageMap(storeIds);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_MAIN_IMAGE_NOT_FOUND);
        }

        ///storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        ///스토어 개수 조회
        final int storeCount = storeFacade.getStoreCountByStation(station);

        ///마지막 데이터 여부
        final int lastStoreInfoDtos = storeInfoDtos.size() - 1;
        final boolean isLastData = storeInfoDtos.get(lastStoreInfoDtos).isLastData();

        ///마지막 storeID 조회
        final Long LastStoreId = storeFacade.calculateLastStoreId(storeInfoDtos);

        return StoreInfoListByLatestRes.of(LastStoreId, storeCount, isLastData, storeInfos);
    }

    //전체 지하철역 조회
    public List<AllStationRes.StationInfo> getAllStation() {
        return Arrays.stream(Station.values())
                .map(station -> AllStationRes.StationInfo.of(
                        station.getStationEnName(),
                        station.getStationKrName(),
                        station.getLatitude(),
                        station.getLongitude()
                ))
                .collect(Collectors.toList());
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

    public StoreKakaoLinkRes getStoreKakaoLink(final long storeId) {
        final StoreKakaoLinkDto storeKakaoLinkDto;
        try {
            storeKakaoLinkDto = storeFacade.findById(storeId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(STORE_KAKAO_LINK_NOT_FOUND);
        }
        return new StoreKakaoLinkRes(storeKakaoLinkDto.kakaoLink());
    }

    @Transactional(readOnly = true)
    public StoreDetailAllDesignRes getStoreAllDesign(final long storeId, final Long userId) {

        final List<Cake> cakes;
        // 케이크 조회
        // 스토어 ID로 케이크 리스트 조회
        try {
            cakes = cakeFacade.findAllByStoreId(storeId);
        } catch (NotFoundBaseException e) {
            throw new CakeyNotFoundException(CakeErrorCode.CAKE_NOT_FOUND_ENTITY);
        }

        //좋아요 상태 설정
        List<StoreDetailDesign> designs = cakes.stream()
                .map(cake -> {
                    boolean isLiked = false;
                    if (userId != null) {
                        // userId가 null이 아니면 좋아요 상태 조회
                        isLiked = cakeLikesFacade.existsCakeLikesByCakeIdAndUserId(cake.getId(), userId);
                    } else {
                        //userId 없으면 좋아요 전체 false
                        isLiked = false;
                    }
                    // StoreDetailDesign 생성
                    return StoreDetailDesign.of(cake.getId(), cake.getImageUrl(), isLiked);
                })
                .collect(Collectors.toList());

        return new StoreDetailAllDesignRes(designs);
    }

    @Transactional(readOnly = true)
    public StoreAllSizeAndTasteRes getStoreSizeAndTaste(final long storeId) {
        final List<SizeDto> sizeList;
        try {
            sizeList = sizeFacade.findSizeAllByStoreIdAndOrderByPriceAsc(storeId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }
        return StoreAllSizeAndTasteRes.of(sizeList, storeFacade.findTaste(storeId).taste());
    }

    @Transactional(readOnly = true)
    public StoreDetailInfoRes getStoreDetailInfo(final long storeId) {
        final StoreDetailInfoDto storeDetailInfoDto;
        final StoreOperationTimeDto storeOperationTimeDto;
        try {
            storeDetailInfoDto = storeFacade.findStoreDetailInfo(storeId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }

        try {
            storeOperationTimeDto = storeOperationTimeFacade.findStoreOperationTimeByStoreId(storeId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(STORE_OPERATION_TIME_NOT_FOUND);
        }

        return StoreDetailInfoRes.of(
                storeOperationTimeDto.monOpen(),
                storeOperationTimeDto.monClose(),
                storeOperationTimeDto.tueOpen(),
                storeOperationTimeDto.tueClose(),
                storeOperationTimeDto.wedOpen(),
                storeOperationTimeDto.wedClose(),
                storeOperationTimeDto.thuOpen(),
                storeOperationTimeDto.thuClose(),
                storeOperationTimeDto.friOpen(),
                storeOperationTimeDto.friClose(),
                storeOperationTimeDto.satOpen(),
                storeOperationTimeDto.satClose(),
                storeOperationTimeDto.sunOpen(),
                storeOperationTimeDto.sunClose(),
                storeDetailInfoDto.address(),
                storeDetailInfoDto.phone()
        );
    }
    
    public StoreListByPopularityRes getStoreByRank() {
        final List<StoreByPopularityDto> storeByPopularityDtoList;
        try {
            storeByPopularityDtoList = storeFacade.findStoreListByLank();
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }
        return new StoreListByPopularityRes(storeByPopularityDtoList);
    }

    public StoreSelectedCoordinateRes getStoreSelectedCoordinate(final long storeId) {
        final Store store;
        try {
            store = storeFacade.findStoreById(storeId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }
        return StoreSelectedCoordinateRes.of(storeId, store.getLatitude(), store.getLongitude());
    }

    //선택된 스토어 조회
    public StoreSelectedRes getStoreSelected(final long storeId, final Long userId) {
        final StoreSelectedDto storeSelectedDto;
        try {
            storeSelectedDto = storeFacade.findStoreInfoById(storeId, userId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }
        return StoreSelectedRes.of(
                storeSelectedDto.storeId(),
                storeSelectedDto.storeName(),
                storeSelectedDto.address(),
                storeSelectedDto.station(),
                storeSelectedDto.isLiked(),
                storeSelectedDto.imageUrl(),
                storeSelectedDto.storeLikesCount()
        );
    }
}
