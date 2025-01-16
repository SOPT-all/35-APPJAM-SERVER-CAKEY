package com.cakey.cake.service;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeByPopularityDto;
import com.cakey.cake.dto.CakeInfo;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeListByPopularityRes;
import com.cakey.cake.dto.CakesLatestByStationStoreRes;
import com.cakey.cake.dto.CakesPopularByStationStoreRes;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.cakelike.domain.CakeLikes;
import com.cakey.cakelike.facade.CakeLikesFacade;
import com.cakey.common.exception.NotFoundException;
import com.cakey.exception.CakeyApiException;
import com.cakey.store.domain.Station;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.digester.AbstractObjectCreationFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CakeService {
    private final CakeFacade cakeFacade;
    private final CakeLikesFacade cakeLikesFacade;

    //해당역 스토어의 케이크들 조회(최신순)
    public CakesLatestByStationStoreRes getLatestCakesByStationStore(final Long userId,
                                                                     final Station station,
                                                                     final Long cakeIdCursor,
                                                                     final int size) {
        ///커서 페이지네이션 데이터조회
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findLatestCakesByStation(userId, station, cakeIdCursor, size);

        ///해당역 케이크 개수
        final int cakeCountByStation = cakeFacade.countCakesByStation(station);

        ///마지막 데이터 여부
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final boolean isLastData = cakeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();

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

        return CakesLatestByStationStoreRes.from(nextCursor, cakeCountByStation, isLastData, cakes);
    }

    //해당역 디자인(케이크) 조회(인기순)
    public CakesPopularByStationStoreRes getPopularCakesByStationStore(final Long userId,
                                                                       final Station station,
                                                                       final Integer cakeLikesCursor,
                                                                       final Long cakeIdCursor,
                                                                       final int size) {
        //커서페이지네이션으로 케이크 조회
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findPopularCakesByStation(userId, station, cakeLikesCursor, cakeIdCursor, size);

        if(cakeInfoDtos.isEmpty()) {
            throw new NotFoundException();
        }

        ///커서 업데이트
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final int nextLikesCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeLikeCount();
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();
        final boolean isLastData = cakeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();

        /// 전체 케이크 수 계산
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

        return CakesPopularByStationStoreRes.from(nextLikesCursor, nextCakeIdCursor, totalCakeCount, isLastData, cakes);
    }

    public CakeListByPopularityRes getCakeByPopularity(final Long userId) {
        List<CakeByPopularityDto> cakeByPopularityDtos = cakeFacade.findCakeByPopularity(userId);
        return new CakeListByPopularityRes(cakeByPopularityDtos);
    }

    @Transactional
    public void postCakeLike(final Long cakeId, final Long userId) {
        Cake cake = cakeFacade.findById(cakeId);
        if (!cakeLikesFacade.existsCakeLikesByCakeIdAndUserId(cakeId, userId)) {
            CakeLikes cakeLikes = CakeLikes.createCakeLikes(cakeId, userId);
            cakeLikesFacade.saveCakeLikes(cakeLikes);
        } else {
            //todo: 추후 구체적인 예외 처리
            throw new RuntimeException("Cake like already exists");
        }
    }
}
