package com.cakey.storelikes.controller;

import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.common.response.SuccessCode;
import com.cakey.store.dto.StoreLikedCoordinateRes;
import com.cakey.storelikes.service.StoreLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store/likes")
@RequiredArgsConstructor
public class StoreLikesController {
    private final StoreLikesService storeLikesService;

    //찜한 스토어 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestStoreByUserLikes(
            @RequestHeader(value = "userId", required = true) final long userId,
            @RequestParam(value = "storeIdCursor", required = false) final Long storeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeLikesService.getLatestStoreLikesByUser(userId, storeIdCursor, size));
    }

    //찜한 스토어 조회(인기순)
    @GetMapping("/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularityStoreByUserLikes(
            @RequestHeader(value = "userId", required = true) final long userId,
            @RequestParam(value = "likesCursor", required = false) final Integer likesCursor,
            @RequestParam(value = "storeIdCursor",  required = false) final Long storeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeLikesService.getPopularityStoreByUserLikes(userId, likesCursor, storeIdCursor, size));
    }

    //찜한 스토어 좌표 조회
    @GetMapping("/coordinate")
    public ResponseEntity<BaseResponse<?>> getStoreLikedCoordinate(
            @RequestHeader(value = "Authorization", required = true) final long userId
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                StoreLikedCoordinateRes.of(storeLikesService.getLikedStoreCoordinatesByUserId(userId)));
    }

    //스토어 찜하기
    @PostMapping("/{storeId}")
    public ResponseEntity<BaseResponse<?>> postStoreLikes(
            @RequestHeader(value = "Authorization") final long userId,
            @PathVariable(value = "storeId") final long storeId) {
        storeLikesService.postStoreLikes(userId, storeId);
        return ApiResponseUtil.success(SuccessCode.CREATED);
    }

}
