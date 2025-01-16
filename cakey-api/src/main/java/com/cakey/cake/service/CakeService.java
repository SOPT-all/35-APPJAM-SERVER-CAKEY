package com.cakey.cake.service;

import com.cakey.cake.dto.CakeInfo;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakesLatestByStationStoreRes;
import com.cakey.cake.dto.CakesPopularByStationStoreRes;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CakeService {
    private final CakeFacade cakeFacade;

    //해당역 스토어의 케이크들 조회
    public CakesLatestByStationStoreRes getLatestCakesByStationStore(final Long userId,
                                                                     final Station station,
                                                                     final Long cakeIdCursor,
                                                                     final int size) {
        //커서 페이지네이션 데이터조회
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findLatestCakesByStation(userId, station, cakeIdCursor, size);

        //해당역 케이크 개수
        final int cakeCountByStation = cakeFacade.countCakesByStation(station);

        final List<CakeInfo> cakes = cakeInfoDtos.stream()
                .map(cakeInfoDto -> CakeInfo.of(
                        cakeInfoDto.getCakeId(),
                        cakeInfoDto.getStoreId(),
                        cakeInfoDto.getStoreName(),
                        cakeInfoDto.getStation(),
                        cakeInfoDto.isLiked(),
                        cakeInfoDto.getImageUrl(),
                        cakeInfoDto.getCakeLikeCount()
                ))
                .collect(Collectors.toList());

        //커서값
        final Long nextCursor = cakeInfoDtos.isEmpty()
                ? -1
                : cakeInfoDtos.get(cakes.size() - 1).getCakeId();

        return CakesLatestByStationStoreRes.from(nextCursor, cakeCountByStation, cakes);
    }

    //해당역 디자인(케이크) 조회(인기순)
    public CakesPopularByStationStoreRes getPopularCakesByStationStore(final Long userId,
                                                                       final Station station,
                                                                       final Integer cakeLikesCursor,
                                                                       final Long cakeIdCursor,
                                                                       final int size) {
        //커서페이지네이션으로 케이크 조회
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findPopularCakesByStation(userId, station, cakeLikesCursor, cakeIdCursor, size);

        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;

        // 커서 업데이트
        final int nextLikesCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeLikeCount();
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor() == null ? -1 : cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();

        // 전체 케이크 수 계산
        final int totalCakeCount = cakeFacade.countCakesByStation(station);

        final List<CakeInfo> cakes = cakeInfoDtos.stream()
                .map(dto -> CakeInfo.of(
                        dto.getCakeId(),
                        dto.getStoreId(),
                        dto.getStoreName(),
                        dto.getStation(),
                        dto.isLiked(),
                        dto.getImageUrl(),
                        dto.getCakeLikeCount()
                ))
                .toList();

        return CakesPopularByStationStoreRes.from(nextLikesCursor, nextCakeIdCursor, totalCakeCount, cakes);
    }
}
