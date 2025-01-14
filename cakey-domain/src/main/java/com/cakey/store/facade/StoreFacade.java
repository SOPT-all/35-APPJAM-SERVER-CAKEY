package com.cakey.store.facade;

import com.cakey.OrderBy;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
import com.cakey.store.dto.StoreInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreFacade {
    private final StoreRetriever storeRetriever;

    public List<StoreCoordianteDto> findCoordinatesByStation(final Station station) {
        return storeRetriever.findCoordinateByStation(station);
    }

    public List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                             final Station station,
                                                             final int likesCursor,
                                                             final Long lastStoreId,
                                                             final int size) {
        return storeRetriever.findStoreInfoByStationAndLikes(userId, station, likesCursor, lastStoreId, size);
    }

    public int countAllStores() {
        return storeRetriever.countAllStores();
    }

    public int countStoresByStation(final Station station) {
        return storeRetriever.countStoresByStation(station);
    }
}
