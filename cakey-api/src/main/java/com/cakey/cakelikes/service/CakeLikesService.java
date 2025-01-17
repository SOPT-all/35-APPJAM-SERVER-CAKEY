package com.cakey.cakelikes.service;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeInfo;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.cakelike.domain.CakeLikes;
import com.cakey.cakelike.facade.CakeLikesFacade;
import com.cakey.cakelike.facade.CakeLikesRemover;
import com.cakey.cakelike.facade.CakeLikesRetriever;
import com.cakey.cakelikes.dto.CakeLikedLatestRes;
import com.cakey.cakelikes.dto.CakeLikedPopularRes;
import com.cakey.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CakeLikesService {

    private final CakeFacade cakeFacade;
    private final CakeLikesFacade cakeLikesFacade;
    private final CakeLikesRemover cakeLikesRemover;

    //찜한 디자인(케이크) 조회(최신순)
    public CakeLikedLatestRes getLatestCakeLikedByUser(final long userId,
                                                       final Long cakeIdCursor,
                                                       final Integer size) {

        // 페이지네이션 조회
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findLatestLikedCakesByUser(userId, cakeIdCursor, size);

        //마지막 데이터인지
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final boolean isLastData = cakeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();

        //찜한 전체 디자인 개수
        final int allCakesUserLikedCount = cakeLikesFacade.countByUserId(userId);

        //마지막 cakeId
        final long lastCakeId = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeId();

        //데이터변환
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

        return CakeLikedLatestRes.from(lastCakeId, allCakesUserLikedCount, isLastData, cakes);
    }

    //찜한 디자인(케이크) 조회(인기순)
    public CakeLikedPopularRes getPopularLikedCakesByUser(final long userId,
                                                          final Long cakeIdCursor,
                                                          final Integer cakeLikesCursor,
                                                          final int size) {

        // 페이지네이션 조회
        final List<CakeInfoDto> cakeInfoDtos = cakeFacade.findPopularLikedCakesByUser(userId, cakeIdCursor,cakeLikesCursor, size);

        //마지막 데이터인지
        final int lastCakeInfoDtosIndex = cakeInfoDtos.size() - 1;
        final int nextLikesCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeLikeCount();
        final Long nextCakeIdCursor = cakeInfoDtos.get(lastCakeInfoDtosIndex).getCakeIdCursor();
        final boolean isLastData = cakeInfoDtos.get(lastCakeInfoDtosIndex).isLastData();

        //찜한 전체 디자인 개수
        final int allCakesUserLikedCount = cakeLikesFacade.countByUserId(userId);

        //데이터변환
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

        return CakeLikedPopularRes.from(nextLikesCursor, nextCakeIdCursor, allCakesUserLikedCount, isLastData, cakes);
    }



    @Transactional
    public void postCakeLike(final long cakeId, final long userId) {
        Cake cake = cakeFacade.findById(cakeId);
        if (!cakeLikesFacade.existsCakeLikesByCakeIdAndUserId(cakeId, userId)) {
            CakeLikes cakeLikes = CakeLikes.createCakeLikes(cakeId, userId);
            cakeLikesFacade.saveCakeLikes(cakeLikes);
        } else {
            //todo: 추후 구체적인 예외 처리
            throw new RuntimeException("Cake like already exists");
        }
    }



    //케이크 좋아요 취소
    @Transactional
    public void deleteCakeLikes(final long cakeId, final long userId) {
        CakeLikes cakeLikes = cakeLikesFacade.getCakeLikesByCakeIdAndUserId(cakeId, userId);
        cakeLikesFacade.removeCakeLikes(cakeLikes);
    }

}
