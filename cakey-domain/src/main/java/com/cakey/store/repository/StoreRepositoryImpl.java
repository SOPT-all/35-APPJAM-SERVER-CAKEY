package com.cakey.store.repository;

import com.cakey.cake.domain.QCake;
import com.cakey.common.exception.NotFoundException;
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

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    final QStore store = QStore.store;
    final QStoreLike storeLike = QStoreLike.storeLike;

    @Override
    public List<StoreCoordinatesDto> findStoreCoordinatesByStation(final Station station) {
        final List<StoreCoordinatesDto> dtos =
                queryFactory
                        .select(new QStoreCoordinatesDto(
                                store.id,
                                store.latitude,
                                store.longitude
                        ))
                        .from(store)
                        .where(
                                station == Station.ALL ? null : store.station.eq(station) // ALL이면 조건없이 들어가므로 전체조회
                        )
                        .fetch();

        if (dtos.isEmpty()) {
            throw new NotFoundException();
        } else {
            return dtos;
        }
    }

    //지하철역 스토어 조회(인기순)
    @Override
    public List<StoreInfoDto> findPopularitryStoreInfoByStation(final Long userId,
                                                                final Station station,
                                                                final Integer likesCursor,
                                                                final Long storeIdCursor,
                                                                final int size) {

        // 좋아요 개수를 계산하는 서브쿼리
        final Expression<Integer> storeLikesCountSubQuery = getStoreLikesCountSubQuery();

        // 좋아요 여부 서브쿼리
        final BooleanExpression isLikedExpression = isLikedExpression(userId);

        // 커서 조건 처리
        final BooleanExpression storeIdCursorCondition = (storeIdCursor == null || storeIdCursor == 0)
                ? null
                : store.id.gt(storeIdCursor); // storeIdCursor가 존재하면 store.id > storeIdCursor 조건 추가

        // 역 조건
        final BooleanExpression stationCondition = station != Station.ALL
                ? store.station.eq(station)
                : null; // 기본 조건

        // 쿼리 실행
        JPQLQuery<StoreInfoDto> query =
                queryFactory.select(new QStoreInfoDto(
                                store.id,
                                store.name,
                                store.station,
                                store.address,
                                isLikedExpression,
                                storeLikesCountSubQuery,
                                store.id, // Cursor로 사용할 storeId
                                Expressions.asBoolean(false)
                        ))
                        .from(store)
                        .leftJoin(storeLike).on(storeLike.storeId.eq(store.id))
                        .where(stationCondition) // 역 조건
                        .groupBy(store.id);

        /// likesCursor 조건 추가
        if (likesCursor != null) {
            if (likesCursor == 0) {
                // likesCursor가 0이면 처음부터 조회 (조건 없이 정렬만 적용)
                System.out.println("likesCursor가 0이므로 전체중 좋아요가 많은 순서로 조회합니다.");
            } else {
                query.having(
                        storeLike.count().lt(likesCursor) /// 좋아요 수가 likesCursor보다 작은 데이터
                                .or(storeLike.count().intValue().eq(likesCursor))
                                .and(storeIdCursorCondition) /// 좋아요 수가 같으면 storeId로 페이징
                );
            }
        } else if (storeIdCursorCondition != null) {
            /// likesCursor가 없고 storeIdCursor만 존재하면
            System.out.println("예외처리 추가"); //todo:likesCursor없이 storeIdCursor만 있으면 예외처리
        }

        query.orderBy(storeLike.id.count().desc(), store.id.asc()) /// 좋아요 수 -> storeId순(최신순) 정렬
                .limit(size + 1); /// limit + 1개 조회

        /// 쿼리 실행
        List<StoreInfoDto> stores = query.fetch();

        ///비어있는 리스트
        if (stores.isEmpty()) {
            throw new NotFoundException();
        }

        /// 좋아요 수 비교 및 Cursor 설정
        if (stores.size() > size) {
            StoreInfoDto lastItem = stores.get(size - 1); /// limit번째 데이터
            StoreInfoDto extraItem = stores.get(size);    /// limit + 1번째 데이터

            if (lastItem.getStoreLikesCount() == extraItem.getStoreLikesCount()) {
                /// 좋아요 수가 같으면 limit번째 데이터의 storeId를 Cursor로 설정
                stores.get(size - 1).setStoreIdCursor(lastItem.getStoreId());
            } else {
                /// 좋아요 수가 다르면 Cursor를 null로 설정
                stores.get(size - 1).setStoreIdCursor(null);
            }
            stores = stores.subList(0, size); /// limit 수만큼 자르기
        } else {
            final StoreInfoDto lastItem = stores.get(stores.size() - 1);
            lastItem.setLastData(true);
        }
        return stores;
    }


    //지하철역 스토어 조회(최신순)
    @Override
    public List<StoreInfoDto> findLatestStoreInfoByStation(final Long userId,
                                                           final Station station,
                                                           final Long storeIdCursor,
                                                           final int size) {

        final BooleanExpression isLikedExpression = isLikedExpression(userId);

        List<StoreInfoDto> storeInfoDtos =
                queryFactory
                        .select(new QStoreInfoDto(
                                store.id,
                                store.name,
                                store.station,
                                store.address,
                                isLikedExpression,
                                storeLike.id.count().intValue(),
                                Expressions.nullExpression(),
                                Expressions.asBoolean(false)
                        ))
                        .from(store)
                        .leftJoin(storeLike).on(storeLike.storeId.eq(store.id))
                        .where(storeIdCursorCondition(storeIdCursor), stationCondition(station))
                        .groupBy(store.id)
                        .orderBy(store.id.desc())
                        .limit(size + 1)
                        .fetch();

        if (storeInfoDtos.isEmpty()){
            throw new NotFoundException();
        }

        if (storeInfoDtos.size() > size) {
            storeInfoDtos = storeInfoDtos.subList(0, size); ///limit 수만큼 자름
        } else {
            final StoreInfoDto lastItem = storeInfoDtos.get(storeInfoDtos.size() - 1);
            lastItem.setLastData(true);
        }
        return storeInfoDtos;
    }

    //찜한 스토어 조회(최신순)
    @Override
    public List<StoreInfoDto> findLatestStoresLikedByUser(final long userId,
                                                          final Long storeIdCursor,
                                                          final int size) {

        /// 좋아요 개수를 계산하는 서브쿼리
        final Expression<Integer> storeLikesCountSubQuery = getStoreLikesCountSubQuery();

        // 커서 조건 처리
        final BooleanExpression cursorCondition = (storeIdCursor == null || storeIdCursor == 0)
                ? null // 조건 없음
                : store.id.lt(storeIdCursor); /// storeIdCursor가 존재하면 store.id > storeIdCursor 조건 추가

        /// 메인 쿼리
        List<StoreInfoDto> storeInfoDtos = queryFactory
                .select(new QStoreInfoDto(
                        store.id,
                        store.name,
                        store.station,
                        store.address,
                        Expressions.asBoolean(true), /// 좋아요 여부는 항상 true
                        storeLikesCountSubQuery,
                        Expressions.nullExpression(),
                        Expressions.asBoolean(false)
                ))
                .from(store)
                .leftJoin(storeLike).on(storeLike.storeId.eq(store.id)) /// LEFT JOIN 사용
                .where(storeLike.userId.eq(userId)
                        .and(cursorCondition)) /// 동적 조건 적용
                .groupBy(store.id, store.name, store.station, store.address) /// GROUP BY로 데이터 그룹화
                .orderBy(store.id.desc()) /// 최신 순 정렬
                .limit(size + 1)
                .fetch();

        if(storeInfoDtos.isEmpty()) {
            throw new NotFoundException();
        }

        if (storeInfoDtos.size() > size) {
            storeInfoDtos = storeInfoDtos.subList(0, size); ///limit 수만큼 자름
        } else {
            final StoreInfoDto lastItem = storeInfoDtos.get(storeInfoDtos.size() - 1);
            lastItem.setLastData(true);
        }


        return storeInfoDtos;
    }

    //찜한 스토어 조회 (인기순)
    @Override
    public List<StoreInfoDto> findPopularityStoresLikedByUser(
            final long userId,
            final Integer likesCursor, // 좋아요 수 기준 커서
            final Long storeIdCursor, // 스토어 ID 기준 커서
            final int size) {

        QStore store = QStore.store;
        QStoreLike storeLike = QStoreLike.storeLike;

        /// 좋아요 개수를 계산하는 서브쿼리
        final NumberExpression<Integer> storeLikesOrderExpression = Expressions.asNumber(
                JPAExpressions
                        .select(storeLike.count().intValue())
                        .from(storeLike)
                        .where(storeLike.storeId.eq(store.id))
        );

        /// 커서 조건 처리
        final BooleanExpression storeIdCursorCondition = (storeIdCursor != null && storeIdCursor > 0)
                ? store.id.gt(storeIdCursor) // storeIdCursor보다 큰 스토어
                : null;

        /// 기본 쿼리 생성
        final JPQLQuery<StoreInfoDto> query = queryFactory
                .select(new QStoreInfoDto(
                        store.id,
                        store.name,
                        store.station,
                        store.address,
                        Expressions.asBoolean(true), /// 좋아요 여부는 항상 true
                        storeLikesOrderExpression, /// 좋아요 개수
                        store.id, /// Cursor로 사용할 storeId
                        Expressions.asBoolean(false)))
                .from(storeLike)
                .join(store).on(store.id.eq(storeLike.storeId)) /// store와 storeLike를 조인
                .where(storeLike.userId.eq(userId)) /// 해당 사용자가 좋아요 누른 스토어만 조회
                .groupBy(store.id, store.name, store.station, store.address);

        /// 조건 처리
        if (likesCursor == null && storeIdCursor == null) {
            /// 1. 아이디커서와 좋아요커서 둘 다 없을 때
            query.orderBy(
                    storeLikesOrderExpression.desc(), /// 좋아요 개수 내림차순
                    store.id.asc() /// 같은 좋아요 개수일 경우 storeId 오름차순
            );
        } else if (likesCursor != null && likesCursor == 0 && storeIdCursor != null && storeIdCursor > 0) {
            /// 2. 좋아요커서가 0이고, 아이디커서가 0보다 클 때
            query.having(storeLikesOrderExpression.eq(0).and(storeIdCursorCondition));
            query.orderBy(store.id.asc()); /// 아이디 오름차순
        } else if (likesCursor != null && likesCursor == 0 && (storeIdCursor == null || storeIdCursor == 0)) {
            /// 3. 좋아요커서가 0이고, 아이디커서가 없거나 0일 때 (예외 처리)
            throw new IllegalArgumentException("Invalid cursor combination: likesCursor=0 and storeIdCursor=0 or null");
        } else if (likesCursor != null && likesCursor > 0 && (storeIdCursor == null || storeIdCursor == 0)) {
            /// 4. 좋아요커서가 0보다 크고, 아이디커서가 없을 때
            query.having(storeLikesOrderExpression.lt(likesCursor)); /// 좋아요 수가 likesCursor보다 작은 스토어 조회
            query.orderBy(
                    storeLikesOrderExpression.desc(), /// 좋아요 개수 내림차순
                    store.id.asc() /// 같은 좋아요 개수일 경우 storeId 오름차순
            );
        } else if (likesCursor != null && likesCursor > 0 && storeIdCursor != null && storeIdCursor > 0) {
            /// 5. 좋아요커서가 0보다 크고, 아이디커서가 0보다 클 때
            query.having(
                    storeLikesOrderExpression.eq(likesCursor).and(storeIdCursorCondition) // 좋아요 수가 likesCursor와 같고, storeIdCursor보다 큰 스토어
                            .or(storeLikesOrderExpression.lt(likesCursor)) /// 이후 좋아요 수가 likesCursor보다 작은 스토어
            );
            query.orderBy(
                    storeLikesOrderExpression.desc(), /// 좋아요 개수 내림차순
                    store.id.asc() /// 같은 좋아요 개수일 경우 storeId 오름차순
            );
        }

        /// 제한 조건 설정
        query.limit(size + 1);

        /// 쿼리 실행
        List<StoreInfoDto> stores = query.fetch();

        ///비어있는 리스트
        if (stores.isEmpty()) {
            throw new NotFoundException();
        }

        /// 좋아요 수 비교 및 Cursor 설정
        if (stores.size() > size) {
            final StoreInfoDto lastItem = stores.get(size - 1); /// limit번째 데이터
            final StoreInfoDto extraItem = stores.get(size);    /// limit + 1번째 데이터

            if (lastItem.getStoreLikesCount() == extraItem.getStoreLikesCount()) {
                /// 좋아요 수가 같으면 limit번째 데이터의 storeId를 Cursor로 설정
                stores.get(size - 1).setStoreIdCursor(lastItem.getStoreId());
            } else {
                /// 좋아요 수가 다르면 Cursor를 null로 설정
                stores.get(size - 1).setStoreIdCursor(null);
            }
            stores = stores.subList(0, size); // limit 수만큼 자르기
        } else { ///마지막 데이터 조회했을때
            final StoreInfoDto lastItem = stores.get(stores.size() - 1);
            lastItem.setLastData(true);
        }
        return stores;
    }

    //찜한 스토어 좌표 조회
    @Override
    public List<StoreCoordinatesDto> findLikedStoreCoordinatesByUserId(final Long userId) {
        return queryFactory
                .select(new QStoreCoordinatesDto(
                        store.id,
                        store.latitude,
                        store.longitude
                ))
                .from(store)
                .join(storeLike).on(store.id.eq(storeLike.storeId))
                .where(storeLike.userId.eq(userId))
                .fetch();
    }

    //선택된 케이크의 스토어 정보 조회
    @Override
    public StoreBySelectedCakeDto findStoreBySelectedCakeId(final Long cakeId) {
        QCake cake = QCake.cake;
        QStore store = QStore.store;
        return queryFactory.select(new QStoreBySelectedCakeDto(
                        store.id,
                        store.name,
                        store.station
                ))
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .where(cake.id.eq(cakeId))
                .fetchOne();
    }

    //좋아요 여부 서브쿼리
    private BooleanExpression isLikedExpression(final Long userId) {
        if (userId != null) {
            return JPAExpressions.selectOne()
                    .from(storeLike)
                    .where(storeLike.storeId.eq(store.id).and(storeLike.userId.eq(userId)))
                    .exists();
        } else {
            return Expressions.asBoolean(false);
        }
    }

    //좋아요 개수 서브쿼리
    private Expression<Integer> getStoreLikesCountSubQuery() {
        return JPAExpressions
                .select(storeLike.id.count().intValue()) // 좋아요 개수를 Integer로 변환
                .from(storeLike)
                .where(storeLike.storeId.eq(store.id));
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

