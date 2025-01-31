package com.cakey.cake.repository;

import com.cakey.cake.domain.DayCategory;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.dto.CakeSelectedDto;
import com.cakey.cake.dto.CakeSelectedInfoDto;
import com.cakey.caketheme.domain.ThemeName;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreBySelectedCakeDto;
import com.cakey.store.dto.StoreInfoDto;

import java.util.List;

public interface CakeRepositoryCustom {
    List<CakeMainImageDto> findMainImageByStoreIds(final List<Long> storeIds);

    //해당역 케이크들 조회(최신순)
    List<CakeInfoDto> findLatestCakesByStation(final Long userId,
                                               final Station station,
                                               final Long storeIdCursor,
                                               final int size);

    //해당역 케이크들 조회(인기순)
    List<CakeInfoDto> findPopularCakesByStation(final Long userId,
                                                final Station station,
                                                final Integer cakeLikesCursor,
                                                final Long cakeIdCursor,
                                                final int size);

    //찜한 케이크 조회(최신순)
    List<CakeInfoDto> findLatestLikedCakesByUser(final long userId,
                                                 final Long cakeIdCursor,
                                                 final int size);

    //찜한 케이크 조회(인기순)
    List<CakeInfoDto> findPopularLikedCakesByUser(final long userId,
                                                  final Long cakeIdCursor,
                                                  final Integer cakeLikesCursor,
                                                  final int size);

    //선택된 케이크 조회
    List<CakeSelectedInfoDto> findCakesByStoreAndConditions(final Long storeId,
                                                            final DayCategory dayCategory,
                                                            final ThemeName theme,
                                                            final Long userId,
                                                            final Long cakeId);

    //디자인 둘러보기 조회(최신순)
    List<CakeInfoDto> findCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                  final ThemeName theme,
                                                  final Long userId,
                                                  final Long cakeIdCursor,
                                                  final int limit);

    //디자인 둘러보기 조회(인기순)
    List<CakeInfoDto> findPopularCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                         final ThemeName themeName,
                                                         final Long userId,
                                                         final Long cakeIdCursor,
                                                         final Integer cakeLikesCursor,
                                                         final int size);

    //찜한 스토어 디자인 조회(최신순)
    List<CakeInfoDto> findCakesLikedByUser(final long userId,
                                           final Long cakeIdCursor,
                                           final int size);


    //찜한 스토어 디자인 조회(인기순)
    List<CakeInfoDto> findPopularCakesLikedByUser(final long userId,
                                                  final Long cakeIdCursor,
                                                  final Integer cakeLikesCursor,
                                                  final int size);


    //카테고리, 테마의 케이크 개수
    int countCakesByCategoryAndTheme(final DayCategory dayCategory,
                                     final ThemeName theme);


}
