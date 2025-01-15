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

    //해당역 스토어들의 디자인 조회
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getCakesByStationStore(
        @RequestHeader(value = "Authorization", required = false) final Long userId,
        @RequestParam(value = "station", required = true) final Station station,
        @RequestParam(value = "cakeIdCursor", defaultValue = "0", required = false) final Long cakeIdCursor,
        @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
       return ApiResponseUtil.success(SuccessCode.OK, cakeService.getCakesByStationStore(userId, station, cakeIdCursor, size));
    }
}
