package com.cakey.cake.controller;

import com.cakey.cake.domain.DayCategory;
import com.cakey.cake.service.CakeService;
import com.cakey.caketheme.domain.ThemeName;
import com.cakey.common.resolver.user.UserId;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.SuccessCode;
import com.cakey.store.domain.Station;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cake")
@RequiredArgsConstructor
@Validated
public class CakeController {
    private final CakeService cakeService;

    //해당역 스토어들의 디자인 조회(최신순)
    @GetMapping("/station/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakesByStationStore(
        @UserId final Long userId,
        @RequestParam(value = "station", required = true) final Station station,
        @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
        @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
       return ApiResponseUtil.success(SuccessCode.OK,
               cakeService.getLatestCakesByStationStore(userId, station, cakeIdCursor, size));
    }

    //해당역 스토어들의 디자인 조회(인기순)
    @GetMapping("/station/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularCakesByStationStore(
            @UserId final Long userId,
            @RequestParam(value = "station", required = true) final Station station,
            @RequestParam(value = "cakeLikesCursor", required = false) final Integer cakeLikesCursor,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(SuccessCode.OK,
                cakeService.getPopularCakesByStationStore(userId, station, cakeLikesCursor,cakeIdCursor, size));
    }

    //인기 케이크 조회
    @GetMapping("/rank")
    public ResponseEntity<BaseResponse<?>> getRankCakesByStationStore(
            @UserId final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK,
                cakeService.getCakeByRank(userId));
    }



    //선택 디자인 조회
    @GetMapping("/select/{cakeId}")
    public ResponseEntity<BaseResponse<?>> getCakeSelect(
            @UserId final Long userId,
            @PathVariable(value = "cakeId") @Min(1) final long cakeId,
            @RequestParam(value = "dayCategory") final DayCategory dayCategory,
            @RequestParam(value = "themeName") final ThemeName themeName
            ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                cakeService.getSelectedCakes(cakeId, dayCategory, themeName, userId)
        );
    }

    //디자인 둘러보기(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakes(
            @UserId final Long userId,
            @RequestParam(value = "dayCategory") final DayCategory dayCategory,
            @RequestParam(value = "themeName") final ThemeName themeName,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size
            ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                cakeService.findCakesByCategoryAndTheme(dayCategory, themeName, userId, cakeIdCursor, size)
        );
    }

    //디자인 둘러보기(인기순)
    @GetMapping("/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularCakes(
            @UserId final Long userId,
            @RequestParam(value = "dayCategory") final DayCategory dayCategory,
            @RequestParam(value = "themeName") final ThemeName themeName,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "cakeLikesCursor", required = false) final Integer cakeLikesCursor,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                cakeService.getPopularCakesByCategoryAndTheme(dayCategory, themeName, userId, cakeIdCursor, cakeLikesCursor, size)
        );
    }

    //찜한 스토어의 디자인 조회(최신순)
    @GetMapping("/store/likes/cake/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakesByLikedStore(
            @UserId final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                cakeService.getLatestCakeByStoreLiked(userId, cakeIdCursor, size)
        );
    }

    //찜한 스토어의 디자인 조회(인기순)
    @GetMapping("/store/likes/cake/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularCakesByLikedStore(
            @UserId final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "cakeLikesCursor", required = false) final Integer cakeLikesCursor,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                cakeService.getPopularCakeByStoreLiked(userId, cakeIdCursor, cakeLikesCursor, size)
        );
    }

}
