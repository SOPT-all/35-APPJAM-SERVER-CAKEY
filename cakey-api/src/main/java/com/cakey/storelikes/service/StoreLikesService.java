package com.cakey.storelikes.service;

import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreInfo;
import com.cakey.store.dto.StoreInfoDto;
import com.cakey.store.facade.StoreFacade;
import com.cakey.storelikes.dto.StoreLatestLikedByUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreLikesService {

    private final StoreFacade storeFacade;
    private final CakeFacade cakeFacade;

    public StoreLatestLikedByUser getLatestStoreLikesByUser(final long userId,
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

        //스토어 개수 조회
        final int storeCount = storeInfos.size();

        //마지막 스토어 아이디
        final Long lastStoreId = storeFacade.calculateLastStoreId(storeInfoDtos);

        return StoreLatestLikedByUser.fromStoreInfo(lastStoreId, storeCount, storeInfos);
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
