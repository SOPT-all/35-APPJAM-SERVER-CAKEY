package com.cakey.cake.service;

import com.cakey.cake.dto.CakeInfo;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakesByStationStoreRes;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CakeService {
    private final CakeFacade cakeFacade;

    //해당역 스토어의 케이크들 조회
    public CakesByStationStoreRes getCakesByStationStore(final Long userId,
                                                         final Station station,
                                                         final Long cakeIdCursor,
                                                         final int size) {
        //커서 페이지네이션 데이터조회
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findCakesByStation(userId, station, cakeIdCursor, size);

        //해당역 케이크 개수
        final int cakeCountByStation = cakeFacade.countCakesByStation(station);

        final List<CakeInfo> cakes = cakeInfoDtos.stream()
                .map(cakeInfoDto -> CakeInfo.of(
                        cakeInfoDto.getCakeId(),
                        cakeInfoDto.getStoreId(),
                        cakeInfoDto.getStoreName(),
                        cakeInfoDto.getStation(),
                        cakeInfoDto.isLiked(),
                        cakeInfoDto.getImageUrl()
                ))
                .collect(Collectors.toList());

        //커서값
        final Long nextCursor = cakeInfoDtos.isEmpty()
                ? -1
                : cakeInfoDtos.get(cakes.size() - 1).getCakeId();

        return CakesByStationStoreRes.from(nextCursor, cakeCountByStation, cakes);
    }
}
