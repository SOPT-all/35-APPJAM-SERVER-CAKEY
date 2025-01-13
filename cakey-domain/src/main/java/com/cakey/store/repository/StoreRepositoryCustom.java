package com.cakey.store.repository;

import com.cakey.store.domain.Station;
import com.cakey.store.dto.StoreCoordianteDto;

import java.util.List;

public interface StoreRepositoryCustom {
    List<StoreCoordianteDto> findStoreCoordinatesByStation(final Station station);
}
