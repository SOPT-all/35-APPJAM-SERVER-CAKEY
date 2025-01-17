package com.cakey.cake.controller;

import com.cakey.cake.domain.DayCategory;
import com.cakey.cake.service.CakeService;
import com.cakey.caketheme.domain.ThemeName;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.common.response.SuccessCode;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cake")
@RequiredArgsConstructor
public class CakeController {
    private final CakeService cakeService;

    //해당역 스토어들의 디자인 조회(최신순)
    @GetMapping("/station/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakesByStationStore(
        @RequestHeader(value = "Authorization", required = false) final Long userId,
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
            @RequestHeader(value = "Authorization", required = false) final Long userId,
            @RequestParam(value = "station", required = true) final Station station,
            @RequestParam(value = "cakeLikesCursor", required = false) final Integer cakeLikesCursor,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(SuccessCode.OK,
                cakeService.getPopularCakesByStationStore(userId, station, cakeLikesCursor,cakeIdCursor, size));
    }

    @GetMapping("/Rank")
    public ResponseEntity<BaseResponse<?>> getRankCakesByStationStore(@RequestHeader(required = false) final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK,
                cakeService.getCakeByRank(userId));
    }

    @PostMapping("/likes/{cakeId}")
    public ResponseEntity<BaseResponse<?>> postCakeLike(
            @PathVariable(value = "cakeId") final Long cakeId,
            @RequestHeader final Long userId
    ) {
        cakeService.postCakeLike(cakeId, userId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    //선택 디자인 조회
    @GetMapping("/{cakeId}/select")
    public ResponseEntity<BaseResponse<?>> getCakeSelect(
            @RequestHeader(value = "Authorization", required = false) final Long userId,
            @PathVariable(value = "cakeId") final long cakeId,
            @RequestParam(value = "dayCategory") final DayCategory dayCategory,
            @RequestParam(value = "theme") final ThemeName themeName
            ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                cakeService.getSelectedCakes(cakeId, dayCategory, themeName, userId)
        );
    }

    //디자인 둘러보기(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakes(
            @RequestHeader(value = "Authorization", required = false) final Long userId,
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
            @RequestHeader(value = "Authorization", required = false) final Long userId,
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
    @GetMapping("/store-liked/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakesByLikedStore(
            @RequestHeader(value = "Authorization", required = true) final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", required = false, defaultValue = "10") final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                cakeService.getLatestCakeByStoreLiked(userId, cakeIdCursor, size)
        );
    }

    //찜한 스토어의 디자인 조회(인기순)
    @GetMapping("/store-liked/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularCakesByLikedStore(
            @RequestHeader(value = "Authorization", required = true) final long userId,
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
