package com.cakey.cake.controller;

import com.cakey.cake.service.CakeService;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.common.response.SuccessCode;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cake")
@RequiredArgsConstructor
public class CakeController {
    private final CakeService cakeService;

    //해당역 스토어들의 디자인 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakesByStationStore(
        @RequestHeader(value = "Authorization", required = false) final Long userId,
        @RequestParam(value = "station", required = true) final Station station,
        @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
        @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
       return ApiResponseUtil.success(SuccessCode.OK, cakeService.getLatestCakesByStationStore(userId, station, cakeIdCursor, size));
    }

    //해당역 스토어들의 디자인 조회(인기순)
    @GetMapping("/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularCakesByStationStore(
            @RequestHeader(value = "Authorization", required = false) final Long userId,
            @RequestParam(value = "station", required = true) final Station station,
            @RequestParam(value = "cakeLikesCursor", required = false) final Integer cakeLikesCursor,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(SuccessCode.OK, cakeService.getPopularCakesByStationStore(userId, station, cakeLikesCursor,cakeIdCursor, size));
    }

    @GetMapping("/lank")
    public ResponseEntity<BaseResponse<?>> getLankCakesByStationStore(@RequestHeader(required = false) final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, cakeService.getCakeByLank(userId));
    }

    @PostMapping("/likes/{cakeId}")
    public ResponseEntity<BaseResponse<?>> postCakeLike(
            @PathVariable(value = "cakeId") final Long cakeId,
            @RequestHeader final Long userId
    ) {
        cakeService.postCakeLike(cakeId, userId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }
}
