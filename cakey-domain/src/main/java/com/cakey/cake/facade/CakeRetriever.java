package com.cakey.cake.facade;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.domain.DayCategory;
import com.cakey.cake.dto.CakeByPopularityDto;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.dto.CakeSelectedInfoDto;
import com.cakey.cake.repository.CakeRepository;
import com.cakey.caketheme.domain.ThemeName;
import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CakeRetriever {
    private final CakeRepository cakeRepository;

    public List<CakeMainImageDto> findMainImageByStoreIds(final List<Long> storeIds) {
        return cakeRepository.findMainImageByStoreIds(storeIds);
    }

    public List<Cake> findAllByStoreId(final Long storeId) {
        final List<Cake> cakes = cakeRepository.findAllByStoreId(storeId);
        if(cakes.isEmpty()) {
            throw new NotFoundBaseException();
        }
        return cakes;
    }

    public List<CakeInfoDto> findCakesByStation(final Long userId, final Station station, final Long cakeIdCursor, final int size) {
        return cakeRepository.findLatestCakesByStation(userId, station, cakeIdCursor, size);
    }

    public int countCakesByStation(final Station station) {
        return cakeRepository.countCakesByStation(station);
    }

    public List<CakeInfoDto> findPopularCakesByStation(final Long userId,
                                                       final Station station,
                                                       final Integer likesCursor,
                                                       final Long cakeIdCursor,
                                                       final int size) {
        return cakeRepository.findPopularCakesByStation(userId, station, likesCursor, cakeIdCursor, size);
    }

    public List<CakeByPopularityDto> findCakesByRank(final Long userId) {
        final List<CakeByPopularityDto> cakeByPopularityDtos = cakeRepository.findCakesByRank(userId);
        if (cakeByPopularityDtos.isEmpty()) {
            throw new NotFoundBaseException();
        }
        return cakeRepository.findCakesByRank(userId);
    }

    public Cake findById(final Long cakeId) {
        return cakeRepository.findById(cakeId)
                .orElseThrow(NotFoundBaseException::new);
    }
    //찜한 디자인(케이크) 조회(최신순)
    public List<CakeInfoDto> findLatestLikedCakesByUser (final Long userId,
                                                         final Long cakeIdCursor,
                                                         final int size) {
        return cakeRepository.findLatestLikedCakesByUser(userId, cakeIdCursor, size);
    }

    //찜한 디자인(케이크) 조회(인기순)
    public List<CakeInfoDto> findPopularLikedCakesByUser(final long userId,
                                                         final Long cakeIdCursor,
                                                         final Integer cakeLikesCursor,
                                                         final int size) {
        return cakeRepository.findPopularLikedCakesByUser(userId, cakeIdCursor, cakeLikesCursor, size);
    }

    //같은 스토어, 카테고리, 테마 케이크 조회
    public List<CakeSelectedInfoDto> findCakesByStoreAndConditions(final Long storeId,
                                                                   final DayCategory dayCategory,
                                                                   final ThemeName theme,
                                                                   final Long userId,
                                                                   final Long cakeId) {
        List<CakeSelectedInfoDto> cakeSelectedInfoDtos = cakeRepository.findCakesByStoreAndConditions(
                storeId,
                dayCategory,
                theme,
                userId,
                cakeId
        );

        if (cakeSelectedInfoDtos.isEmpty()) {
            throw new NotFoundBaseException();
        }
        return cakeSelectedInfoDtos;
    }

    //디자인 둘러보기 조회(최신순)
    public List<CakeInfoDto> findCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                         final ThemeName theme,
                                                         final Long userId,
                                                         final Long cakeIdCursor,
                                                         final int limit) {
        return cakeRepository.findCakesByCategoryAndTheme(dayCategory, theme, userId, cakeIdCursor, limit);
    }

    //디자인 둘러보기 조회(인기순)
    public List<CakeInfoDto> findPopularCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                                final ThemeName themeName,
                                                                final Long userId,
                                                                final Long cakeIdCursor,
                                                                final Integer cakeLikesCursor,
                                                                final int size) {
        return cakeRepository.findPopularCakesByCategoryAndTheme(dayCategory, themeName, userId, cakeIdCursor, cakeLikesCursor, size);
    }

    //찜한 스토어들 디자인 조회(최신순)
    public List<CakeInfoDto> findCakesLikedByUser(final long userId,
                                                  final Long cakeIdCursor,
                                                  final int size) {
        return cakeRepository.findCakesLikedByUser(userId, cakeIdCursor, size);
    }

    //찜한 스토어들 디자인 조회(인기순)
    public List<CakeInfoDto> findPopularCakesLikedByUser(final long userId,
                                                         final Long cakeIdCursor,
                                                         final Integer cakeLikesCursor,
                                                         final int size) {
        return cakeRepository.findPopularCakesLikedByUser(userId, cakeIdCursor, cakeLikesCursor, size);
    }

    //카테고리, 테마에 해당하는 케이크 개수
    public int countCakesByCategoryAndTheme(final DayCategory dayCategory, final ThemeName theme) {
        return cakeRepository.countCakesByCategoryAndTheme(dayCategory, theme);
    }

    //찜한 모든 스토어 모든 디자인 개수
    public int countAllDesignsLikedByUser(final Long userId) {
        return cakeRepository.countAllDesignsLikedByUser(userId);
    }



}
