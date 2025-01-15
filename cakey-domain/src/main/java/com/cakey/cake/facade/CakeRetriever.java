package com.cakey.cake.facade;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeDesignDto;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.repository.CakeRepository;
import com.cakey.store.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
        return cakeRepository.findCakesByStation(userId, station, cakeIdCursor, size);
    }

    public int countCakesByStation(final Station station) {
        return cakeRepository.countCakesByStation(station);
    }

}
