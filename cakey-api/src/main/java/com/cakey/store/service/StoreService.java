package com.cakey.store.service;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.facade.CakeFacade;
import com.cakey.cakelike.facade.CakeLikesFacade;
import com.cakey.common.exception.NotFoundException;
import com.cakey.operationtime.dto.StoreOperationTimeDto;
import com.cakey.operationtime.facade.StoreOperationTimeFacade;
import com.cakey.size.dto.SizeDto;
import com.cakey.size.facade.SizeFacade;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.*;
import com.cakey.store.facade.StoreFacade;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreFacade storeFacade;
    private final CakeFacade cakeFacade;
    private final CakeLikesFacade cakeLikesFacade;
    private final SizeFacade sizeFacade;
    private final StoreOperationTimeFacade storeOperationTimeFacade;

    public List<StoreCoordinate> getStoreCoordinateList(final Station station) {
        final List<StoreCoordianteDto> storeCoordianteDtoList = storeFacade.findCoordinatesByStation(station);

        return storeCoordianteDtoList.stream()
                .map(storeCoordianteDto -> StoreCoordinate.of(
                        storeCoordianteDto.id(),
                        storeCoordianteDto.latitude(),
                        storeCoordianteDto.longitude())
                ).toList();
    }

    //스토어 리스트 조회(인기순)
    public StoreInfoListBylikesRes getStoreInfoListByStationAndLikes(final Long userId,
                                                                     final Station station,
                                                                     final int likesCursor,
                                                                     final Long storeIdCursor,
                                                                     final int size) {

        final List<StoreInfoDto> storeInfoDtos = storeFacade.findStoreInfoByStationAndLikes(userId, station, likesCursor, storeIdCursor, size);

        /// 조회한 store들의 ID 추출
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoDtos);

        ///메인 이미지 map
        final Map<Long, List<CakeMainImageDto>> mainImageMap = cakeFacade.getMainImageMap(storeIds);

        ///storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        ///스토어 개수 조회
        final int storeCount = storeFacade.getStoreCountByStation(station);

        final int nextLikesCursor = calculateNextCursor(storeInfoDtos);

        ///마지막 조회 데이터와 그 다음 데이터의 스토어좋아요 개수가 같을 경우에, 스토어아이디커서 내려줌
        final long lastStoreId = storeInfoDtos.get(size - 1).getStoreIdCursor() == null ? -1 : storeInfoDtos.get(size - 1).getStoreIdCursor();
        
        ///StoreInfoListRes 반환
        return StoreInfoListBylikesRes.of(nextLikesCursor, lastStoreId, storeCount, storeInfos);
    }

    //스토어 리스트 조회(최신순)
    public StoreInfoListByLatestRes getStoreInfoListByStationAndLatest(final Long userId,
                                                                       final Station station,
                                                                       final Long storeIdCursor,
                                                                       final int size) {

        ///커서 페이지네이션
        final List<StoreInfoDto> storeInfoDtos = storeFacade.findStoreInfoByStationAndLatest(userId, station, storeIdCursor, size);

        /// 조회한 store들의 ID 추출
        final List<Long> storeIds = storeFacade.getStoreIds(storeInfoDtos);

        ///메인 이미지 map
        final Map<Long, List<CakeMainImageDto>> mainImageMap = cakeFacade.getMainImageMap(storeIds);

        ///storInfo 생성
        final List<StoreInfo> storeInfos = getStoreInfo(storeInfoDtos, mainImageMap);

        ///스토어 개수 조회
        final int storeCount = storeFacade.getStoreCountByStation(station);

        ///마지막 storeID 조회
        final Long LastStoreId = storeFacade.calculateLastStoreId(storeInfoDtos);

        return StoreInfoListByLatestRes.of(LastStoreId, storeCount, storeInfos);
    }

    //전체 지하철역 조회
    public List<AllStationRes.StationInfo> getAllStation() {
        return Arrays.stream(Station.values())
                .map(station -> AllStationRes.StationInfo.of(
                        station.getStationName(),
                        station.getLatitude(),
                        station.getLongitude()
                ))
                .collect(Collectors.toList());
    }

    private int calculateNextCursor(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.isEmpty() ? -1 : storeInfoDtos.get(storeInfoDtos.size() - 1).getStoreLikesCount();
    }

    //스토어 id들 조회
    private List<Long> getStoreIds(final List<StoreInfoDto> storeInfoDtos) {
        return storeInfoDtos.stream()
                .map(StoreInfoDto::getStoreId)
                .toList();
    }

    //storeInfo 생성
    private List<StoreInfo> getStoreInfo(final List<StoreInfoDto> storeInfoDtos, final Map<Long, List<CakeMainImageDto>> imageMap) {
        return storeInfoDtos.stream()
                .map(storeInfoDto -> {
                    // storeId에 해당하는 이미지 리스트 가져오기
                    final List<StoreInfo.StoreMainImage> images = imageMap.getOrDefault(storeInfoDto.getStoreId(), List.of())
                            .stream()
                            .map(image -> StoreInfo.StoreMainImage.of(image.getImageId(), image.getImageUrl()))
                            .toList();

                    // StoreInfo 생성
                    return StoreInfo.of(
                            storeInfoDto.getStoreId(),
                            storeInfoDto.getName(),
                            Station.valueOf(String.valueOf(storeInfoDto.getStation())),
                            storeInfoDto.getAddress(),
                            storeInfoDto.getStoreLikesCount(),
                            storeInfoDto.isLiked(),
                            images
                    );
                })
                .toList();
    }

    public StoreKakaoLinkRes getStoreKakaoLink(final Long storeId) {
        final StoreKakaoLinkDto storeKakaoLinkDto;
        try {
            storeKakaoLinkDto = storeFacade.findById(storeId);
        } catch (NotFoundException e) {
            //todo: 추후 구체적인 예외처리
            throw e;
        }
        return new StoreKakaoLinkRes(storeKakaoLinkDto.kakaoLink());
    }

    public StoreDetailAllDesignRes getStoreAllDesign(final long storeId, final Long userId) {
        // 케이크 조회
        // 스토어 ID로 케이크 리스트 조회
        final List<Cake> cakes = cakeFacade.findAllByStoreId(storeId);

        //좋아요 상태 설정
        List<StoreDetailDesign> designs = cakes.stream()
                .map(cake -> {
                    boolean isLiked = false;
                    if (userId != null) {
                        // userId가 null이 아니면 좋아요 상태 조회
                        isLiked = cakeLikesFacade.existsCakeLikesByCakeIdAndUserId(cake.getId(), userId);
                    } else {
                        //userId 없으면 좋아요 전체 false
                        isLiked = false;
                    }
                    // StoreDetailDesign 생성
                    return StoreDetailDesign.of(cake.getId(), cake.getImageUrl(), isLiked);
                })
                .collect(Collectors.toList());

        return new StoreDetailAllDesignRes(designs);
    }


    public StoreAllSizeAndTasteRes getStoreSizeAndTaste(final long storeId) {
        final List<SizeDto> sizeList = sizeFacade.findSizeAllByStoreIdAndOrderByPriceAsc(storeId);
        return StoreAllSizeAndTasteRes.of(sizeList, storeFacade.findTaste(storeId).taste());
    }

    public StoreDetailInfoRes getStoreDetailInfo(final long storeId) {
        StoreDetailInfoDto storeDetailInfoDto;
        StoreOperationTimeDto storeOperationTimeDto;
        try {
            storeDetailInfoDto = storeFacade.findStoreDetailInfo(storeId);
            storeOperationTimeDto = storeOperationTimeFacade.findStoreOperationTimeByStoreId(storeId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return StoreDetailInfoRes.of(
                    storeOperationTimeDto.monOpen().format(formatter),
                    storeOperationTimeDto.monClose().format(formatter),
                    storeOperationTimeDto.tueOpen().format(formatter),
                    storeOperationTimeDto.tueClose().format(formatter),
                    storeOperationTimeDto.wedOpen().format(formatter),
                    storeOperationTimeDto.wedClose().format(formatter),
                    storeOperationTimeDto.thuOpen().format(formatter),
                    storeOperationTimeDto.thuClose().format(formatter),
                    storeOperationTimeDto.friOpen().format(formatter),
                    storeOperationTimeDto.friClose().format(formatter),
                    storeOperationTimeDto.satOpen().format(formatter),
                    storeOperationTimeDto.satClose().format(formatter),
                    storeOperationTimeDto.sunOpen().format(formatter),
                    storeOperationTimeDto.sunClose().format(formatter),
                    storeDetailInfoDto.address(),
                    storeDetailInfoDto.phone()
            );
        } catch (NotFoundException e) {
            //todo: 추후 구체적인 예외처리
            throw e;
        }
    }

}
