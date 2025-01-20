package com.cakey.store.controller;

import com.cakey.common.resolver.user.UserId;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.SuccessCode;
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

    //지하철역 스토어 리스트 조회(인기순)
    @GetMapping("/popularity")
    public ResponseEntity<BaseResponse<?>> getStoreInfoListByStationAndLikes(
            //todo: @UserId final Long userId //다음 피알에서 추가예정
            @UserId final Long userId,
            @RequestParam(value = "station", required = true) final Station station,
            @RequestParam(value = "likesCursor", required = false) final Integer likesCursor,
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

    //지하철역 스토어 리스트 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getStoreInfoListByStationAndLatest(
            //todo: @UserId final Long userId //다음 피알에서 추가예정
            @UserId final Long userId,
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
                                                        @UserId final Long userId) {
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

    @GetMapping("/rank")
    public ResponseEntity<BaseResponse<?>> getStoreByRank(){
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreByRank());
    }

    @GetMapping("/{storeId}/select/coordinate")
    public ResponseEntity<BaseResponse<?>> getStoreSelectedCoordinate(@PathVariable final Long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreSelectedCoordinate(storeId));
    }

    //선택 스토어 조회
    @GetMapping("/{storeId}/select")
    public ResponseEntity<BaseResponse<?>> getStoreSelected(
            @UserId final Long userId,
            @PathVariable final long storeId) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeService.getStoreSelected(storeId, userId)
        );
    }
}
