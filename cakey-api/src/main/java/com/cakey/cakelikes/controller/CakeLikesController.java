package com.cakey.cakelikes.controller;

import com.cakey.cakelike.domain.CakeLikes;
import com.cakey.cakelikes.service.CakeLikesService;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.common.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cake/like")
public class CakeLikesController {

    private final CakeLikesService cakeLikesService;

    //찜한 디자인(케이크) 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakeLikedByUser(
            @RequestHeader(value = "Authorization") final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size
    ) {
        return ApiResponseUtil.success(SuccessCode.OK, cakeLikesService.getLatestCakeLikedByUser(userId, cakeIdCursor, size));
    }

    //찜한 디자인(케이크) 조회(인기순)
    @GetMapping("/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularCakeLikedByUser(
            @RequestHeader(value = "Authorization") final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "cakeLikesCursor", required = false) final Integer cakeLikesCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size

    ) {
        return ApiResponseUtil.success(SuccessCode.OK, cakeLikesService.getPopularLikedCakesByUser(userId, cakeIdCursor, cakeLikesCursor, size));
    }

    //케이크 좋아요 등록
    @PostMapping("/likes/{cakeId}")
    public ResponseEntity<BaseResponse<?>> postCakeLike(
            @PathVariable(value = "cakeId") final Long cakeId,
            @RequestHeader final Long userId
    ) {
        cakeLikesService.postCakeLike(cakeId, userId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }
}
