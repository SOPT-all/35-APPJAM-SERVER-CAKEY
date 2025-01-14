package com.cakey.store.service;

import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;
import com.cakey.store.dto.StoreCoordinate;
import com.cakey.store.facade.StoreFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreFacade storeFacade;

    public List<StoreCoordinate> getStoreCoordinateList(final Station station) {
        final List<StoreCoordianteDto> storeCoordianteDtoList = storeFacade.findCoordinatesByStation(station);

        return storeCoordianteDtoList.stream()
                .map(storeCoordianteDto -> StoreCoordinate.of(
                        storeCoordianteDto.id(),
                        storeCoordianteDto.latitude(),
                        storeCoordianteDto.longitude())
                ).toList();
    }
}
