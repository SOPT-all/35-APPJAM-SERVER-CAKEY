package com.cakey.store.facade;

import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
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
}
