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

    //스토어 좌표 리스트 조회
    @GetMapping("/coordinate-list")
    public ResponseEntity<BaseResponse<?>> getStoreCoordinateList(
            @RequestParam(value = "station", required = true) final Station station
            ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                StoreCoordinateListRes.from(storeService.getStoreCoordinateList(station)));
    }

    //스토어 정보 리스트 조회(인기순)
    @GetMapping("/popularity")
    public ResponseEntity<BaseResponse<?>> getStoreInfoListByStationAndLikes(
            //todo: @UserId final Long userId //다음 피알에서 추가예정
            @RequestHeader(value = "Authorization", required = false) final Long userId,
            @RequestParam(value = "station", required = true) final Station station,
            @RequestParam(value = "likesCursor", required = false) final int likesCursor,
            @RequestParam(value = "storeIdCursor", required = false) final Long storeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeService.getStoreInfoListByStationAndLikes(
                        userId,
                        station,
                        likesCursor,
                        storeIdCursor,
                        size));
    }

    //스토어 정보 리스트 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getStoreInfoListByStationAndLatest(
            //todo: @UserId final Long userId //다음 피알에서 추가예정
            @RequestHeader(value = "Authorization", required = false) final Long userId,
            @RequestParam(value = "station", required = true) final Station station,
            @RequestParam(value = "storeIdCursor", required = false) final Long storeIdCursor,
            @RequestParam(value = "size", defaultValue = "10", required = false) final int size
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeService.getStoreInfoListByStationAndLatest(
                        userId,
                        station,
                        storeIdCursor,
                        size));
    }

    //전체 지하철역 조회
    @GetMapping("/station")
    public ResponseEntity<BaseResponse<?>> getAllStation() {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeService.getAllStation());
    }

    //스토어 카카오 오픈채팅 링크 조회
    @GetMapping("/{storeId}/kakaoLink")
    public ResponseEntity<BaseResponse<?>> getKakaoLink(@PathVariable("storeId") final Long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK,
                storeService.getStoreKakaoLink(storeId));
    }

    @GetMapping("/{storeId}/design")
    public ResponseEntity<BaseResponse<?>> getAllDesign(@PathVariable("storeId") final Long storeId,
                                                        @RequestHeader(required = false) final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreAllDesign(storeId, userId));
    }

    @GetMapping("/{storeId}/size")
    public ResponseEntity<BaseResponse<?>> getSizeAndTaste(@PathVariable("storeId") final Long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreSizeAndTaste(storeId));
    }

    @GetMapping("/{storeId}/information")
    public ResponseEntity<BaseResponse<?>> getStoreInformation(@PathVariable("storeId") final Long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreDetailInfo(storeId));
    }
}
