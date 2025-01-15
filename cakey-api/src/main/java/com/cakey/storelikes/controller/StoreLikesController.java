package com.cakey.storelikes.controller;

import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.common.response.SuccessCode;
import com.cakey.storelikes.service.StoreLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store/likes")
@RequiredArgsConstructor
public class StoreLikesController {
    private final StoreLikesService storeLikesService;

    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getLatestStoreByUserLikes(
            @RequestHeader(value = "userId", required = true) final long userId,
            @RequestParam(value = "storeIdCursor", defaultValue = "0", required = false) final Long storeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeLikesService.getLatestStoreLikesByUser(userId, storeIdCursor, size));

    }

}
