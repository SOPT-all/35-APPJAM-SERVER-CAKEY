package com.cakey.store.repository;

import com.cakey.store.domain.QStore;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.*;
import com.cakey.storelike.domain.QStoreLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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

    @Override
    public List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                             final Station station,
                                                             final int likesCursor,
                                                             final int size) {
        QStore store = QStore.store;
        QStoreLike storeLike = QStoreLike.storeLike;

        // 좋아요 여부 서브쿼리
        BooleanExpression isLikedExpression = userId != null
                ? JPAExpressions.selectOne()
                .from(storeLike)
                .where(storeLike.storeId.eq(store.id).and(storeLike.userId.eq(userId)))
                .exists()
                : Expressions.asBoolean(false);

        // 좋아요 커서 조건
        BooleanExpression cursorCondition = likesCursor > 0
                ? storeLike.id.count().lt(likesCursor)
                : null; // 기본 조건

        // 역 조건
        BooleanExpression stationCondition = station != Station.ALL
                ? store.station.eq(station)
                : null; // 기본 조건

        // 쿼리 실행
        return queryFactory
                .select(new QStoreInfoDto(
                        store.id,
                        store.name,
                        store.station,
                        store.address,
                        isLikedExpression,
                        storeLike.id.count().intValue()
                ))
                .from(store)
                .leftJoin(storeLike).on(storeLike.storeId.eq(store.id))
                .where(stationCondition) // 역 조건
                .groupBy(store.id)
                .having(likesCursor > 0 ? storeLike.id.count().lt(likesCursor) : null) // 조건 추가
                .orderBy(storeLike.id.count().desc())
                .limit(size)
                .fetch();
    }

    // 안전한 조건 결합 메서드
    private BooleanExpression combine(BooleanExpression base, BooleanExpression additional) {
        if (base == null) return additional;
        if (additional == null) return base;
        return base.and(additional);
    }
}

