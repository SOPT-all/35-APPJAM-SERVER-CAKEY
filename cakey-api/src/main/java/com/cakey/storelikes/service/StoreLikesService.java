package com.cakey.storelikes.service;

import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordinate;
import com.cakey.store.dto.StoreCoordinatesDto;
import com.cakey.store.dto.StoreInfo;
import com.cakey.store.dto.StoreInfoDto;
import com.cakey.store.exception.StoreConflictBaseException;
import com.cakey.store.exception.StoreErrorCode;
import com.cakey.store.exception.StoreNotfoundException;
import com.cakey.store.facade.StoreFacade;
import com.cakey.storelike.facade.StoreLikeFacade;
import com.cakey.storelikes.dto.StoreLatestLikedByUserRes;
import com.cakey.storelikes.dto.StorePopularityLikedByUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreLikesService {

    private final StoreFacade storeFacade;
    private final CakeFacade cakeFacade;
    private final StoreLikeFacade storeLikeFacade;

    //찜한 스토어 조회(최신순)
    public StoreLatestLikedByUserRes getLatestStoreLikesByUser(final long userId,
                                                               final Long storeIdCursor,
                                                               final int size) {

        final List<StoreInfoDto> storeInfoDtos;
        ///페이지네이션으로 스토어 조회
        try {
            storeInfoDtos = storeFacade.findLatestStoresLikedByUser(userId, storeIdCursor, size);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }

        ///조회한 store들의 id 추출
        final List<Long> storeIds = getStoreIds(storeInfoDtos);

        ///메인 이미지 매핑
        final Map<Long, List<CakeMainImageDto>> mainImageMap;
        try {
            mainImageMap = cakeFacade.getMainImageMap(storeIds);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_MAIN_IMAGE_NOT_FOUND);
        }
        ///storeInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        ///찜한 스토어 개수 조회
        final int storeCount = storeLikeFacade.countAllLikedStoreByUserId(userId);

        ///마지막 데이터인지 판별
        final int lastCakeInfoDtosIndex = storeInfoDtos.size() - 1;
        final boolean isLastData = storeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();

        ///마지막 스토어 아이디
        final Long lastStoreId = storeFacade.calculateLastStoreId(storeInfoDtos);

        return StoreLatestLikedByUserRes.fromStoreInfo(lastStoreId, storeCount, isLastData, storeInfos);
    }

    //찜한 스토어 조회(인기순)
    public StorePopularityLikedByUserRes getPopularityStoreByUserLikes(final long userId,
                                                                       final Integer likesCursor,
                                                                       final Long storeIdCursor,
                                                                       final int size) {
        final List<StoreInfoDto> storeInfoOrderByLikesDtos;
        try {
            storeInfoOrderByLikesDtos = storeFacade.findPopularityStoresLikedByUser(userId, likesCursor, storeIdCursor, size);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }

        ///조회한 store들의 id 추출
        final List<Long> storeIds = getStoreIds(storeInfoOrderByLikesDtos);

        ///메인 이미지 매핑
        final Map<Long, List<CakeMainImageDto>> mainImageMap;
        try {
            mainImageMap = cakeFacade.getMainImageMap(storeIds);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_MAIN_IMAGE_NOT_FOUND);
        }
        ///storeInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoOrderByLikesDtos, mainImageMap);

        ///찜한 스토어 개수 조회
        final int storeCount = storeLikeFacade.countAllLikedStoreByUserId(userId);

        ///커서 업데이트
        final int lastCakeInfoDtosIndex = storeInfoOrderByLikesDtos.size() - 1;
        final int storeLikesCursor = storeInfoOrderByLikesDtos.get(lastCakeInfoDtosIndex).getStoreLikesCount();
        final Long lastStoreId = storeInfoOrderByLikesDtos.get(lastCakeInfoDtosIndex).getStoreIdCursor();
        final boolean isLastData = storeInfoOrderByLikesDtos.get(lastCakeInfoDtosIndex).isLastData();

        ///결과 DTO 조립 및 반환
        return StorePopularityLikedByUserRes.of(
                storeLikesCursor,
                lastStoreId,
                storeCount,
                isLastData,
                storeInfos
        );
    }

    //조회한 store들의 id 추출
    private List<Long> getStoreIds(final List<StoreInfoDto> storeInfoDtos) {
        return storeFacade.getStoreIds(storeInfoDtos);
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

    //찜한 스토어 좌표 조회
    public List<StoreCoordinate> getLikedStoreCoordinatesByUserId(final long userId) {
        final List<StoreCoordinatesDto> storeCoordinatesDtos;
        try {
            storeCoordinatesDtos = storeFacade.findLikedStoreCoordinatesByUserId(userId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_NOT_FOUND_ENTITY);
        }
        return storeCoordinatesDtos.stream()
                .map(
                        storeCoordinatesDto -> StoreCoordinate.of(storeCoordinatesDto.storeId(), storeCoordinatesDto.latitude(), storeCoordinatesDto.longitude())
                ).collect(Collectors.toList());
    }

    //스토어 좋아요 등록
    public void postStoreLikes(final long userId, final long storeId) {
        try {
            storeLikeFacade.saveStoreLikes(userId, storeId);
        } catch (DataIntegrityViolationException e) {
            throw new StoreConflictBaseException(StoreErrorCode.STORE_LIKES_CONFLICT);
        }
    }

    //스토어 좋아요 취소
    public void deleteStoreLikes(final long userId, final long storeId) {
        try {
            storeLikeFacade.deleteStoreLikes(userId, storeId);
        } catch (NotFoundBaseException e) {
            throw new StoreNotfoundException(StoreErrorCode.STORE_LIKES_NOT_FOUND_ENTITY);
        }
    }
}
