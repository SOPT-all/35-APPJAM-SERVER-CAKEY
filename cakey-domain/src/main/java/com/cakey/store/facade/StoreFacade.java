package com.cakey.store.facade;

import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
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


}
