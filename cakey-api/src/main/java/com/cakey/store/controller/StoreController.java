package com.cakey.store.controller;

import com.cakey.caketheme.domain.ThemeName;
import com.cakey.common.resolver.user.UserId;
import com.cakey.common.response.ApiResponseUtil;
import com.cakey.common.response.BaseResponse;
import com.cakey.rescode.SuccessCode;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordinateListRes;
import com.cakey.store.service.StoreService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
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

    //스토어 정보 리스트 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<BaseResponse<?>> getStoreInfoListByStationAndLatest(
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
        log.error("----------------------testtestsetset---------------"); // todo: 추후 삭제( 로그 테스트용)
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeService.getAllStation());
    }

    //스토어 카카오 오픈채팅 링크 조회
    @GetMapping("/kakaoLink/{storeId}")
    public ResponseEntity<BaseResponse<?>> getKakaoLink(
            @PathVariable("storeId") @Min(value = 1, message = "storeId는 1이상이어야합니다.") final long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK,
                storeService.getStoreKakaoLink(storeId));
    }

    //스토어 상세 디자인 조회
    @GetMapping("/design/{storeId}")
    public ResponseEntity<BaseResponse<?>> getAllDesign(
            @PathVariable("storeId") @Min(value = 1, message = "storeId는 1이상이어야합니다.") final long storeId,
            @UserId final Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreAllDesign(storeId, userId));
    }

    //스토어 상세 사이즈/맛 조회
    @GetMapping("/{storeId}/size")
    public ResponseEntity<BaseResponse<?>> getSizeAndTaste(
            @PathVariable("storeId") @Min(value = 1, message = "storeId는 1이상이어야합니다.") final long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreSizeAndTaste(storeId));
    }

    //스토어 상세 정보 조회
    @GetMapping("/{storeId}/information")
    public ResponseEntity<BaseResponse<?>> getStoreInformation(
            @PathVariable("storeId") @Min(value = 1, message = "storeId는 1이상이어야합니다.") final long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreDetailInfo(storeId));
    }

    //인기 스토어 조회
    @GetMapping("/rank")
    public ResponseEntity<BaseResponse<?>> getStoreByRank(){
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreByRank());
    }

    //선택 스토어 좌표 조회
    @GetMapping("/{storeId}/select/coordinate")
    public ResponseEntity<BaseResponse<?>> getStoreSelectedCoordinate(
            @PathVariable @Min(value = 1, message = "storeId는 1이상이어야합니다.") final long storeId) {
        return ApiResponseUtil.success(SuccessCode.OK, storeService.getStoreSelectedCoordinate(storeId));
    }

    //선택 스토어 조회
    @GetMapping("/select/{storeId}")
    public ResponseEntity<BaseResponse<?>> getStoreSelected(
            @UserId final Long userId,
            @PathVariable @Min(value = 1, message = "storeId는 1이상이어야합니다.")final long storeId) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                storeService.getStoreSelected(storeId, userId)
        );
    }
}
