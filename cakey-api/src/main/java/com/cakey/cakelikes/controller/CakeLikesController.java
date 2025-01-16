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

    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestCakeLikedByUser(
            @RequestHeader(value = "Authorization") final long userId,
            @RequestParam(value = "cakeIdCursor", required = false) final Long cakeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size
    ) {
        return ApiResponseUtil.success(SuccessCode.OK, cakeLikesService.getLatestCakeLikedByUser(userId, cakeIdCursor, size));
    }
}
