package com.cakey.cake.facade;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.domain.DayCategory;
import com.cakey.cake.dto.CakeByPopularityDto;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.dto.CakeSelectedInfoDto;
import com.cakey.caketheme.domain.ThemeName;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreBySelectedCakeDto;
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

    //해당역 스토어의 디자인 조회
    public List<CakeInfoDto> findPopularCakesByStation(final Long userId,
                                                       final Station station,
                                                       final Integer likesCursor,
                                                       final Long cakeIdCursor,
                                                       final int size) {
        return cakeRetriever.findPopularCakesByStation(userId, station, likesCursor, cakeIdCursor, size);
    }

    public int countCakesByStation(final Station station) {
        return cakeRetriever.countCakesByStation(station);
    }

    public List<CakeInfoDto> findLatestCakesByStation(final Long userId, final Station station, final Long cakeIdCursor, final int size) {
        return cakeRetriever.findCakesByStation(userId, station, cakeIdCursor, size);
    }

    public List<Cake> findAllByStoreId(final Long storeId) {
        return cakeRetriever.findAllByStoreId(storeId);
    }

    public List<CakeByPopularityDto> findCakeByRank(final Long userId) {
        return cakeRetriever.findCakesByRank(userId);
    }

    public Cake findById(final Long cakeId) {
        return cakeRetriever.findById(cakeId);
    }
    //찜한 디자인(케이크) 조회(최신순)
    public List<CakeInfoDto> findLatestLikedCakesByUser(final Long userId,
                                                        final Long cakeIdCursor,
                                                        final int size) {
        return cakeRetriever.findLatestLikedCakesByUser(userId, cakeIdCursor, size);
    }

    //찜한 디자인(케이크) 조회(인기순)
    public List<CakeInfoDto> findPopularLikedCakesByUser(final long userId,
                                                  final Long cakeIdCursor,
                                                  final Integer cakeLikesCursor,
                                                  final int size) {
        return cakeRetriever.findPopularLikedCakesByUser(userId, cakeIdCursor, cakeLikesCursor, size);
    }

    //같은 스토어, 카테고리, 테마의 케이크 조회
    public List<CakeSelectedInfoDto> findCakesByStoreAndConditions(final Long storeId,
                                                                   final DayCategory dayCategory,
                                                                   final ThemeName theme,
                                                                   final Long userId,
                                                                   final Long cakeId) {
        return cakeRetriever.findCakesByStoreAndConditions(storeId, dayCategory, theme, userId, cakeId);
    }

    //디자인 둘러보기 조회(최신순)
    public List<CakeInfoDto> findCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                         final ThemeName theme,
                                                         final Long userId,
                                                         final Long cakeIdCursor,
                                                         final int limit) {
        return cakeRetriever.findCakesByCategoryAndTheme(dayCategory, theme, userId, cakeIdCursor, limit);
    }

    //디자인 둘러보기 조회(인기순)
    public List<CakeInfoDto> findPopularCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                                final ThemeName themeName,
                                                                final Long userId,
                                                                final Long cakeIdCursor,
                                                                final Integer cakeLikesCursor,
                                                                final int size) {
        return cakeRetriever.findPopularCakesByCategoryAndTheme(dayCategory, themeName, userId, cakeIdCursor, cakeLikesCursor, size);
    }

    //찜한 스토어들 디자인 조회(인기순)
    public List<CakeInfoDto> findPopularCakesLikedByUser(final long userId,
                                                         final Long cakeIdCursor,
                                                         final Integer cakeLikesCursor,
                                                         final int size) {
        return cakeRetriever.findPopularCakesLikedByUser(userId, cakeIdCursor, cakeLikesCursor, size);
    }


    //카테고리, 테마에 해당하는 케이크 개수
    public int countCakesByCategoryAndTheme(final DayCategory dayCategory, final ThemeName theme) {
        return cakeRetriever.countCakesByCategoryAndTheme(dayCategory, theme);
    }

    //찜한 모든 스토어 모든 디자인 개수
    public int countAllDesignsLikedByUser(final Long userId) {
        return cakeRetriever.countAllDesignsLikedByUser(userId);
    }


}
