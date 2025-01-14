package com.cakey.store.repository;

import com.cakey.store.domain.QStore;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.QStoreCoordianteDto;
import com.cakey.store.dto.StoreCoordianteDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreCoordianteDto> findStoreCoordinatesByStation(final Station station) {
        QStore store = QStore.store;

        return queryFactory
                .select(new QStoreCoordianteDto(
                        store.id,
                        store.latitude,
                        store.longitude
                ))
                .from(store)
                .where(
                        station == Station.ALL ? null : store.station.eq(station) // ALL이면 조건없이 들어가므로 전체조회
                )
                .fetch();
    }
}
