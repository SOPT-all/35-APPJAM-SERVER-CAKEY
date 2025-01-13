package com.cakey.store.controller;

import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.common.response.SuccessCode;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordinateListRes;
import com.cakey.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/coordinate-list")
    public ResponseEntity<BaseResponse<?>> getStoreCoordinateList(
            @RequestParam(value = "station", required = true) final Station station
            ) {
        final StoreCoordinateListRes response = StoreCoordinateListRes.from(storeService.getStoreCoordinateList(station));
        return ApiResponseUtil.success(SuccessCode.OK, response);
    }
}
