package com.cakey.cakelikes.controller;

import com.cakey.cakelikes.service.CakeLikesService;
import com.cakey.common.resolver.user.UserId;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.SuccessCode;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/cake/likes")
public class CakeLikesController {

    private final CakeLikesService cakeLikesService;

    //찜한 디자인(케이크) 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakeLikedByUser(
            @UserId @Min(value = 1, message = "userId는 1이상이어야 합니다.") final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size
    ) {
        return ApiResponseUtil.success(SuccessCode.OK, cakeLikesService.getLatestCakeLikedByUser(userId, cakeIdCursor, size));
    }

    //찜한 디자인(케이크) 조회(인기순)
    @GetMapping("/popularity")
    public ResponseEntity<BaseResponse<?>> getPopularCakeLikedByUser(
            @UserId @Min(value = 1, message = "userId는 1이상이어야 합니다.") final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "cakeLikesCursor", required = false) final Integer cakeLikesCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size

    ) {
        return ApiResponseUtil.success(SuccessCode.OK, cakeLikesService.getPopularLikedCakesByUser(userId, cakeIdCursor, cakeLikesCursor, size));
    }

    //케이크 좋아요 등록
    @PostMapping("/{cakeId}")
    public ResponseEntity<BaseResponse<?>> postCakeLikes(
            @PathVariable(value = "cakeId") @Min(value = 1, message = "cakeId는 1이상이어야합니다.") final Long cakeId,
            @UserId @Min(value = 1, message = "userId는 1이상이어야합니다.") final long userId
    ) {
        cakeLikesService.postCakeLike(cakeId, userId);
        return ApiResponseUtil.success(SuccessCode.CREATED);
    }

    //케이크 좋아요 취소
    @DeleteMapping("/{cakeId}")
    public ResponseEntity<BaseResponse<?>> deleteCakeLikes(
            @PathVariable(value = "cakeId") @Min(value = 1, message = "cakeId는 1이상이어야합니다.")final long cakeId,
            @UserId @Min(value = 1, message = "userId는 1이상이어야 합니다.") final long userId
    ) {
        cakeLikesService.deleteCakeLikes(cakeId, userId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }
}
