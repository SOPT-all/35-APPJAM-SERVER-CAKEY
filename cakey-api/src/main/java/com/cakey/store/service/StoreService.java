package com.cakey.store.service;

import com.cakey.cakeimage.domain.CakeImages;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
import com.cakey.store.dto.StoreCoordinate;
import com.cakey.store.dto.StoreInfoDto;
import com.cakey.store.dto.StoreInfoListRes;
import com.cakey.store.facade.StoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreFacade storeFacade;
    private final CakeImageFacade cakeImageFacade;

    public List<StoreCoordinate> getStoreCoordinateList(final Station station) {
        final List<StoreCoordianteDto> storeCoordianteDtoList = storeFacade.findCoordinatesByStation(station);

        return storeCoordianteDtoList.stream()
                .map(storeCoordianteDto -> StoreCoordinate.of(
                        storeCoordianteDto.id(),
                        storeCoordianteDto.latitude(),
                        storeCoordianteDto.longitude())
                ).toList();
    }


    public StoreInfoListRes getStoreInfoListByStationAndLikes(final Long userId,
                                                              final Station station,
                                                              final int likesCursor,
                                                              final int size) {
        // 커서 페이지 네이션
        final List<StoreInfoDto> storeInfoDtos = storeFacade.findStoreInfoByStationAndLikes(userId, station, likesCursor, size);

        //조회한 store들 id로 대표 이미지 4개씩 추출 - 4개 고정
        final List<Long> storeIds = storeInfoDtos.stream().map(
                StoreInfoDto::getStoreId
        ).toList();







        //전체 스토어 개수 추출


    }
}
