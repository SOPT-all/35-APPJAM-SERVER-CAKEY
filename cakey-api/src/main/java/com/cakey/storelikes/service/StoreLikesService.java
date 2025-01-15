package com.cakey.storelikes.service;

import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreInfo;
import com.cakey.store.dto.StoreInfoDto;
import com.cakey.store.facade.StoreFacade;
import com.cakey.storelike.facade.StoreLikeFacade;
import com.cakey.storelikes.dto.StoreLatestLikedByUserRes;
import com.cakey.storelikes.dto.StorePopularityLikedByUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

        //페이지네이션으로 스토어 조회
        final List<StoreInfoDto> storeInfoDtos = storeFacade.findLatestStoresLikedByUser(userId, storeIdCursor, size);

        //조회한 store들의 id 추출
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoDtos);

        //메인 이미지 매핑
        final Map<Long, List<CakeMainImageDto>> mainImageMap = cakeFacade.getMainImageMap(storeIds);

        //storeInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        //찜한 스토어 개수 조회
        final int storeCount = storeLikeFacade.countAllLikedStoreByUserId(userId);

        //마지막 스토어 아이디
        final Long lastStoreId = storeFacade.calculateLastStoreId(storeInfoDtos);

        return StoreLatestLikedByUserRes.fromStoreInfo(lastStoreId, storeCount, storeInfos);
    }

    //찜한 스토어 조회(인기순)
    public StorePopularityLikedByUserRes getPopularityStoreByUserLikes(final long userId,
                                                                       final int likesCursor,
                                                                       final Long storeIdCursor,
                                                                       final int size) {
        final List<StoreInfoDto> storeInfoOrderByLikesDtos = storeFacade.findPopularityStoresLikedByUser(userId, likesCursor, storeIdCursor, size);


        //조회한 store들의 id 추출
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoOrderByLikesDtos);

        //메인 이미지 매핑
        final Map<Long, List<CakeMainImageDto>> mainImageMap = cakeFacade.getMainImageMap(storeIds);

        //storeInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoOrderByLikesDtos, mainImageMap);

        //찜한 스토어 개수 조회
        final int storeCount = storeLikeFacade.countAllLikedStoreByUserId(userId);

        final Long lastStoreId = storeInfoOrderByLikesDtos.isEmpty()
                ? null // 스토어가 없으면 null 반환
                : storeFacade.calculateLastStoreId(storeInfoOrderByLikesDtos);

        // 7. 결과 DTO 조립 및 반환
        return StorePopularityLikedByUserRes.of(
                userId,
                lastStoreId != null ? lastStoreId : 0L, // null이면 기본값 0L 설정
                storeCount,
                storeInfos
        );
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

}
