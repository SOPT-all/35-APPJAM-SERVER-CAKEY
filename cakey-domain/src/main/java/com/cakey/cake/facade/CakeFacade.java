package com.cakey.cake.facade;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CakeFacade {
    private final CakeRetriever cakeRetriever;


    //스토어 대표 이미지 조회
    public List<CakeMainImageDto> findMainImageByStoreIds(final List<Long> storeIds) {
        return cakeRetriever.findMainImageByStoreIds(storeIds);
    }

    //메인이미지 매핑
    public Map<Long, List<CakeMainImageDto>> getMainImageMap(final List<Long> storeIds) {
        //스토어 대표 이미지 조회
        final List<CakeMainImageDto> cakeMainImageDtos = findMainImageByStoreIds(storeIds);

        // 이미지를 storeId 기준으로 그룹화
        return cakeMainImageDtos.stream()
                .collect(Collectors.groupingBy(CakeMainImageDto::getStoreId));
    }

    public int countCakesByStation(final Station station) {
        return cakeRetriever.countCakesByStation(station);
    }

    public List<CakeInfoDto> findCakesByStation(final Long userId, final Station station, final Long cakeIdCursor, final int size) {
        return cakeRetriever.findCakesByStation(userId, station, cakeIdCursor, size);
    }

    public List<Cake> findAllByStoreId(final Long storeId) {
        return cakeRetriever.findAllByStoreId(storeId);
    }
}
