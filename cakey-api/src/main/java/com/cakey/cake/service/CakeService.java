package com.cakey.cake.service;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.domain.DayCategory;
import com.cakey.cake.dto.*;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.cakelike.domain.CakeLikes;
import com.cakey.cakelike.facade.CakeLikesFacade;
import com.cakey.caketheme.domain.ThemeName;
import com.cakey.common.exception.NotFoundException;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreBySelectedCakeDto;
import com.cakey.store.facade.StoreFacade;
import com.cakey.store.service.StoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CakeService {
    private final CakeFacade cakeFacade;
    private final CakeLikesFacade cakeLikesFacade;
    private final StoreService storeService;
    private final StoreFacade storeFacade;

    //해당역 스토어의 케이크들 조회(최신순)
    public CakesLatestListRes getLatestCakesByStationStore(final Long userId,
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
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();


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

        return CakesLatestListRes.from(nextCakeIdCursor, cakeCountByStation, isLastData, cakes);
    }

    //해당역 디자인(케이크) 조회(인기순)
    public CakesPopularListRes getPopularCakesByStationStore(final Long userId,
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

        return CakesPopularListRes.from(nextLikesCursor, nextCakeIdCursor, totalCakeCount, isLastData, cakes);
    }

    public CakeListByPopularityRes getCakeByRank(final Long userId) {
        List<CakeByPopularityDto> cakeByPopularityDtos = cakeFacade.findCakeByRank(userId);
        return new CakeListByPopularityRes(cakeByPopularityDtos);
    }


    //선택한 디자인(케이크) 조회
    public CakeSelectedRes getSelectedCakes(final Long cakeId,
                                            final DayCategory dayCategory,
                                            final ThemeName theme,
                                            final Long userId) {

        ///스토어 정보 조회
        final StoreBySelectedCakeDto storeInfoDto = storeFacade.findStoreBySelectedCakeId(cakeId);

        ///케이크 정보 조회
        final List<CakeSelectedInfoDto> cakeSelectedInfoDtos = cakeFacade.findCakesByStoreAndConditions(
                storeInfoDto.getStoreId(),
                dayCategory,
                theme,
                userId,
                cakeId
        );

        final List<CakeSelectedInfo> cakeSelectedInfoList = cakeSelectedInfoDtos.stream()
                .map(cake -> CakeSelectedInfo.of(
                        cake.cakeId(),
                        cake.isLiked(),
                        cake.imageUrl()
                ))
                .toList();

        return CakeSelectedRes.of(
                storeInfoDto.getStoreId(),
                storeInfoDto.getStoreName(),
                storeInfoDto.getStation(),
                cakeSelectedInfoList
        );
    }

    //디자인 둘러보기 조회(최신순)
    public CakesLatestListRes findCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                          final ThemeName theme,
                                                          final Long userId,
                                                          final Long cakeIdCursor,
                                                          final int limit) {
        ///페이지네이션
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findCakesByCategoryAndTheme(dayCategory,theme, userId, cakeIdCursor, limit);

        ///케이크 전체 개수
        final int totalCakeCount = cakeFacade.countCakesByCategoryAndTheme(dayCategory,theme);

        ///마지막 데이터 여부
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final boolean isLastData = cakeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();


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

        return CakesLatestListRes.from(nextCakeIdCursor, totalCakeCount, isLastData, cakes);
    }

    //디자인 둘러보기 조회(인기순)
    public CakesPopularListRes getPopularCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                                 final ThemeName themeName,
                                                                 final Long userId,
                                                                 final Long cakeIdCursor,
                                                                 final Integer cakeLikesCursor,
                                                                 final int size) {

        ///페이지네이션
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findPopularCakesByCategoryAndTheme(dayCategory,themeName, userId, cakeIdCursor, cakeLikesCursor, size);

        ///케이크 전체개수
        final int totalCakeCount = cakeFacade.countCakesByCategoryAndTheme(dayCategory,themeName);

        ///커서 업데이트
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final int nextLikesCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeLikeCount();
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();
        final boolean isLastData = cakeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();

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

        return CakesPopularListRes.from(nextLikesCursor, nextCakeIdCursor, totalCakeCount, isLastData, cakes);
    }

    //찜한 스토어들 디자인 조회(최신순)
    public CakesLatestListRes getLatestCakeByStoreLiked(final long userId,
                                                  final Long cakeIdCursor,
                                                  final int size) {

        ///페이지네이션
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findCakesLikedByUser(userId, cakeIdCursor, size);

        ///전체 케이크 개수
        final int totalCakeCount = cakeFacade.countAllDesignsLikedByUser(userId);

        ///커서 업데이트
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();
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

        return CakesLatestListRes.from(nextCakeIdCursor, totalCakeCount, isLastData, cakes);
    }

    //찜한 스토어들 디자인 조회(인기순)
    public CakesPopularListRes getPopularCakeByStoreLiked(final long userId,
                                                           final Long cakeIdCursor,
                                                           final Integer cakeLikesCursor,
                                                           final int size) {
        ///페이지네이션
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findPopularCakesLikedByUser(userId, cakeIdCursor, cakeLikesCursor, size);

        ///전체 케이크 개수
        final int totalCakeCount = cakeFacade.countAllDesignsLikedByUser(userId);

        ///커서 업데이트
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final int nextLikesCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeLikeCount();
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();
        final boolean isLastData = cakeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();

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

        return CakesPopularListRes.from(nextLikesCursor, nextCakeIdCursor, totalCakeCount, isLastData, cakes);
    }
}
