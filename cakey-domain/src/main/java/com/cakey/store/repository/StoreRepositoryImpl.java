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
import com.querydsl.jpa.JPQLQuery;
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
    public List<StoreInfoDto> findPopularitryStoreInfoByStation(final Long userId,
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
                        storeLike.id.count().intValue(),
                        null
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
    public List<StoreInfoDto> findLatestStoreInfoByStation(final Long userId,
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
                        storeLike.id.count().intValue(),
                null
                ))
                .from(store)
                .leftJoin(storeLike).on(storeLike.storeId.eq(store.id))
                .where(storeIdCursorCondition(storeIdCursor), stationCondition(station))
                .groupBy(store.id)
                .orderBy(store.id.desc())
                .limit(size)
                .fetch();
    }

    //찜한 스토어 조회(최신순)
    @Override
    public List<StoreInfoDto> findLatestStoresLikedByUser(final long userId,
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
                        storeLikesCountSubQuery,
                        null// 서브쿼리를 활용한 좋아요 개수
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

    //찜한 스토어 조회 (인기순)
    @Override
    public List<StoreInfoDto> findPopularityStoresLikedByUser(
            final long userId,
            final Integer likesCursor, // 좋아요 수 기준 커서 (null 허용)
            final Long storeIdCursor, // 스토어 ID 기준 커서 (null 허용)
            final int size) {

        QStore store = QStore.store;
        QStoreLike storeLike = QStoreLike.storeLike;

        // 좋아요 개수를 계산하는 서브쿼리
        Expression<Integer> storeLikesCountSubQuery = JPAExpressions
                .select(storeLike.id.count().intValue()) // 좋아요 개수를 Integer로 변환
                .from(storeLike)
                .where(storeLike.storeId.eq(store.id));

        // 커서 조건 처리
        BooleanExpression storeIdCursorCondition = (storeIdCursor == null || storeIdCursor == 0)
                ? null
                : store.id.lt(storeIdCursor); // storeIdCursor가 존재하면 store.id < storeIdCursor 조건 추가

        // 기본 쿼리 생성
        JPQLQuery<StoreInfoDto> query = queryFactory
                .select(new QStoreInfoDto(
                        store.id,
                        store.name,
                        store.station,
                        store.address,
                        Expressions.asBoolean(true), // 좋아요 여부는 항상 true
                        storeLikesCountSubQuery, // 좋아요 개수
                        store.id // Cursor로 사용할 storeId
                ))
                .from(storeLike)
                .join(store).on(store.id.eq(storeLike.storeId)) // store와 storeLike를 조인
                .where(storeLike.userId.eq(userId)) // 해당 사용자가 좋아요 누른 스토어만 조회
                .groupBy(store.id, store.name, store.station, store.address);

        // likesCursor 조건 추가
        if (likesCursor != null) {
            if (likesCursor == 0) {
                // likesCursor가 0이면 처음부터 조회 (조건 없이 정렬만 적용)
                System.out.println("likesCursor가 0이므로 내가 찜한 스토어 중 좋아요가 많은 순서로 조회합니다.");
            } else {
                query.having(
                        storeLike.count().lt(likesCursor) // 좋아요 수가 likesCursor보다 작은 데이터
                                .or(storeLike.count().intValue().eq(likesCursor))
                                .and(storeIdCursorCondition) // 좋아요 수가 같으면 storeId로 페이징
                );
            }
        } else if (storeIdCursorCondition != null) {
            // likesCursor가 없고 storeIdCursor만 존재하면
            System.out.println("예외처리 추가"); //todo:likesCursor없이 storeIdCursor만 있으면 예외처리
        }

        // 정렬 조건 및 페이징 처리
        query.orderBy(storeLike.count().desc(), store.id.desc()) // 좋아요 개수 내림차순 -> storeId 내림차순
                .limit(size + 1); // limit + 1개 조회

        // 쿼리 실행
        List<StoreInfoDto> stores = query.fetch();

        // 좋아요 수 비교 및 Cursor 설정
        if (stores.size() > size) {
            StoreInfoDto lastItem = stores.get(size - 1); // limit번째 데이터
            StoreInfoDto extraItem = stores.get(size);    // limit + 1번째 데이터

            if (lastItem.getStoreLikesCount() == extraItem.getStoreLikesCount()) {
                // 좋아요 수가 같으면 limit번째 데이터의 storeId를 Cursor로 설정
                stores.get(size - 1).setStoreIdCursor(lastItem.getStoreId());
            } else {
                // 좋아요 수가 다르면 Cursor를 null로 설정
                stores.get(size - 1).setStoreIdCursor(null);
            }
            stores = stores.subList(0, size); // limit 수만큼 자르기
        }

        return stores;
    }


    private BooleanExpression storeIdCursorCondition(final Long storeIdCursor) {
        if (storeIdCursor == null || storeIdCursor == 0) {
            return null;
        }
        return QStore.store.id.lt(storeIdCursor);
    }

    private BooleanExpression stationCondition(final Station station) {
        if (station == null || station == Station.ALL) {
            return null;
        }
        return QStore.store.station.eq(station);
    }
}

