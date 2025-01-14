package com.cakey.store.controller;

import com.cakey.OrderBy;
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

    //스토어 좌표 리스트 조회
    @GetMapping("/coordinate-list")
    public ResponseEntity<BaseResponse<?>> getStoreCoordinateList(
            @RequestParam(value = "station", required = true) final Station station
            ) {
        return ApiResponseUtil.success(SuccessCode.OK, StoreCoordinateListRes.from(storeService.getStoreCoordinateList(station)));
    }

    //스토어 정보 리스트 조회(인기순)
    @GetMapping()
    public ResponseEntity<BaseResponse<?>> getStoreInfoListByStationAndLikes(
            //todo: @UserId final Long userId //다음 피알에서 추가예정
            @RequestHeader(value = "Authorization", required = true) final Long userId,
            @RequestParam(value = "station", required = true) final Station station,
            @RequestParam(value = "likesCursor", defaultValue = "0", required = false) final int likesCursor,
            @RequestParam(defaultValue = "10") final int size
    ) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreInfoListByStationAndLikes(userId, station, likesCursor, size));
    }
}
