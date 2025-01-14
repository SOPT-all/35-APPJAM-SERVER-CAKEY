package com.cakey.store.facade;

import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
import com.cakey.store.dto.StoreInfoDto;
import com.cakey.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreRetriever {
    private final StoreRepository storeRepository;

    public List<StoreCoordianteDto> findCoordinateByStation(final Station station) {
        return storeRepository.findStoreCoordinatesByStation(station);
    }

    public List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                             final Station station,
                                                             final int likesCursor,
                                                             final int size) {
        return storeRepository.findStoreInfoByStationAndLikes(userId, station, likesCursor, size);
    }

    public int countAllStores() {
        return storeRepository.countAllStores();
    }

    public int countStoresByStation(final Station station) {
        return storeRepository.countStoresByStation(station);
    }
}
