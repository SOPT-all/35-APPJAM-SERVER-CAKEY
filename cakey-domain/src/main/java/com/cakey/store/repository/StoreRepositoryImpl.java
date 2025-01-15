package com.cakey.store.repository;

import com.cakey.store.domain.QStore;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.*;
import com.cakey.storelike.domain.QStoreLike;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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

    //지하철역 스토어 조회(인기순)
    @Override
    public List<StoreInfoDto> findStoreInfoByStationAndLikes(final Long userId,
                                                             final Station station,
                                                             final int likesCursor,
                                                             final Long lastStoreId,
                                                             final int size) {
        final QStore store = QStore.store;
        final QStoreLike storeLike = QStoreLike.storeLike;

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
                .or(storeLike.id.count().eq(Long.valueOf(likesCursor)).and(store.id.lt(lastStoreId))) // storeId가 작아지는 순서
                : null;

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
                .orderBy(storeLike.id.count().desc(), store.id.desc()) // 좋아요 수 -> storeId순(최신순) 정렬
                .limit(size)
                .fetch();
    }

    //지하철역 스토어 조회(최신순)
    @Override
    public List<StoreInfoDto> findStoreInfoByStationAndLatest(final Long userId,
                                                              final Station station,
                                                              final Long storeIdCursor,
                                                              final int size) {
        final QStore store = QStore.store;
        final QStoreLike storeLike = QStoreLike.storeLike;


        BooleanExpression isLikedExpression = userId != null
                ? JPAExpressions.selectOne()
                .from(storeLike)
                .where(storeLike.storeId.eq(store.id).and(storeLike.userId.eq(userId)))
                .exists()
                : Expressions.asBoolean(false);

        return queryFactory.select(new QStoreInfoDto(
                        store.id,
                        store.name,
                        store.station,
                        store.address,
                        isLikedExpression,
                        storeLike.id.count().intValue()
                ))
                .from(store)
                .leftJoin(storeLike).on(storeLike.storeId.eq(store.id))
                .where(storeIdCursorCondition(storeIdCursor), stationCondition(station))
                .groupBy(store.id)
                .orderBy(store.id.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<StoreInfoDto> findStoresLikedByUser(final long userId,
                                                    final Long storeIdCursor,
                                                    final int size) {
        QStore store = QStore.store;
        QStoreLike storeLike = QStoreLike.storeLike;

        // 좋아요 개수를 계산하는 서브쿼리
        Expression<Integer> storeLikesCountSubQuery = JPAExpressions
                .select(storeLike.id.count().intValue()) // 좋아요 개수를 Integer로 변환
                .from(storeLike)
                .where(storeLike.storeId.eq(store.id));

        // 커서 조건 처리
        BooleanExpression cursorCondition = (storeIdCursor == null || storeIdCursor == 0)
                ? null // 조건 없음
                : store.id.lt(storeIdCursor); // 조건 추가

        // 메인 쿼리
        return queryFactory
                .select(new QStoreInfoDto(
                        store.id,
                        store.name,
                        store.station,
                        store.address,
                        Expressions.asBoolean(true), // 좋아요 여부는 항상 true
                        storeLikesCountSubQuery // 서브쿼리를 활용한 좋아요 개수
                ))
                .from(store)
                .leftJoin(storeLike).on(storeLike.storeId.eq(store.id)) // LEFT JOIN 사용
                .where(storeLike.userId.eq(userId)
                        .and(cursorCondition)) // 동적 조건 적용
                .groupBy(store.id, store.name, store.station, store.address) // GROUP BY로 데이터 그룹화
                .orderBy(store.id.desc()) // 최신 순 정렬
                .limit(size)
                .fetch();
    }






    private BooleanExpression storeIdCursorCondition(Long storeIdCursor) {
        if (storeIdCursor == null || storeIdCursor == 0) {
            return null;
        }
        return QStore.store.id.lt(storeIdCursor);
    }

    private BooleanExpression stationCondition(Station station) {
        if (station == null || station == Station.ALL) {
            return null;
        }
        return QStore.store.station.eq(station);
    }
}

