package com.cakey.cake.facade;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeByPopularityDto;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.repository.CakeRepository;
import com.cakey.common.exception.NotFoundException;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CakeRetriever {
    private final CakeRepository cakeRepository;

    public List<CakeMainImageDto> findMainImageByStoreIds(final List<Long> storeIds) {
        return cakeRepository.findMainImageByStoreIds(storeIds);
    }

    public List<Cake> findAllByStoreId(final Long storeId) {
        return cakeRepository.findAllByStoreId(storeId);
    }

    public List<CakeInfoDto> findCakesByStation(final Long userId, final Station station, final Long cakeIdCursor, final int size) {
        return cakeRepository.findLatestCakesByStation(userId, station, cakeIdCursor, size);
    }

    public int countCakesByStation(final Station station) {
        return cakeRepository.countCakesByStation(station);
    }

    public List<CakeInfoDto> findPopularCakesByStation(final Long userId,
                                                       final Station station,
                                                       final Integer likesCursor,
                                                       final Long cakeIdCursor,
                                                       final int size) {
        return cakeRepository.findPopularCakesByStation(userId, station, likesCursor, cakeIdCursor, size);
    }


    public List<CakeByPopularityDto> findCakesByLank(final Long userId) {
        return cakeRepository.findCakesByLank(userId);
    }

    public Cake findById(final Long cakeId) {
        return cakeRepository.findById(cakeId)
                .orElseThrow(() -> new NotFoundException());
      
    //찜한 디자인(케이크) 조회(최신순)
    List<CakeInfoDto> findLatestLikedCakesByUser(final Long userId,
                                                 final Long cakeIdCursor,
                                                 final int size) {
        return cakeRepository.findLatestLikedCakesByUser(userId, cakeIdCursor, size);
    }
      
    public List<CakeByPopularityDto> findCakesByPopularity(final Long userId) {
        return cakeRepository.findCakesByPopularity(userId);
    }

}
