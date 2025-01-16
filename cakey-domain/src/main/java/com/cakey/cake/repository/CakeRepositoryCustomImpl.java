package com.cakey.cake.repository;

import com.cakey.cake.domain.QCake;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.dto.QCakeInfoDto;
import com.cakey.cakelike.domain.QCakeLikes;
import com.cakey.common.exception.NotFoundException;
import com.cakey.store.domain.QStore;
import com.cakey.store.domain.Station;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Expr;

import java.util.List;

@RequiredArgsConstructor
public class CakeRepositoryCustomImpl implements CakeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QCake cake = QCake.cake;
    QStore store = QStore.store;
    QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

    //가게 메인이미지 조회
    @Override
    public List<CakeMainImageDto> findMainImageByStoreIds(List<Long> storeIds) {
        return queryFactory.select(Projections.constructor(CakeMainImageDto.class,
                        cake.storeId,
                        cake.id,
                        cake.imageUrl))
                .from(cake)
                .where(cake.storeId.in(storeIds)
                        .and(cake.isMainImage.isTrue()))
                .fetch();
    }

    //해당역 스토어의 디자인(케이크) 조회(최신순)
    @Override
    public List<CakeInfoDto> findLatestCakesByStation(final Long userId,
                                                      final Station station,
                                                      final Long cakeIdCursor,
                                                      final int size) {

        QCake cake = QCake.cake;
        QStore store = QStore.store;
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

        // 서브쿼리: 좋아요 개수 계산
        final Expression<Integer> likeCountSubQuery = JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

        // 커서 조건
        final BooleanExpression cursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.lt(cakeIdCursor) // 아이디커서보다 작은 아이디인 케이크 조회
                : null;

        // 좋아요 여부 조건
        final BooleanExpression likeCondition = userId != null
                ? cakeLikes.userId.eq(userId) // 유저 ID가 있을 때만 좋아요 조건 추가
                : null;

        // 메인 쿼리
        JPQLQuery<CakeInfoDto> query = queryFactory.selectDistinct( // 중복 제거를 위해 selectDistinct 추가
                        new QCakeInfoDto(
                                cake.id,
                                store.id,
                                store.name,
                                store.station,
                                isLikedExpression(userId, cake.id), // 유저의 좋아요 여부 확인
                                cake.imageUrl,
                                likeCountSubQuery, // 좋아요 개수 서브쿼리
                                Expressions.nullExpression(),
                                Expressions.asBoolean(false)))
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .leftJoin(cakeLikes).on(cakeLikes.cakeId.eq(cake.id).and(likeCondition)) // 좋아요 조건 추가
                .where(store.station.eq(station)
                        .and(cursorCondition)) // 역 조건 및 커서 조건 추가
                .orderBy(cake.id.desc()) // 케이크 아이디 내림차순 정렬
                .limit(size + 1);

        /// 쿼리 실행
        List<CakeInfoDto> cakes = query.fetch();

        if(cakes.isEmpty()) {
            throw new NotFoundException();
        }

        ///마지막 데이터
        if(cakes.size() > size) {
            cakes = cakes.subList(0, size); /// limit 수만큼 자르기
        } else {
            final CakeInfoDto lastItem = cakes.get(cakes.size() - 1);
            lastItem.setLastData(true);
        }
        return cakes;
    }

    //해당역 디자인(케이크) 조회(인기순)
    @Override
    public List<CakeInfoDto> findPopularCakesByStation(final Long userId,
                                                       final Station station,
                                                       final Integer likesCursor,
                                                       final Long cakeIdCursor,
                                                       final int size) {


        /// 좋아요 개수를 계산하는 서브쿼리
        final JPQLQuery<Integer> cakeLikesCountSubQuery =
                JPAExpressions
                        .select(cakeLikes.count().intValue()) /// 좋아요 개수를 Integer로 변환
                        .from(cakeLikes)
                        .where(cakeLikes.cakeId.eq(cake.id));

        /// 좋아요 여부 서브쿼리
        final BooleanExpression isLikedExpression = getIsLikedExpression(userId);

        /// 역 조건
        final BooleanExpression stationCondition = station != Station.ALL
                ? store.station.eq(station)
                : null;

        /// 좋아요 커서 조건 처리
        final BooleanExpression likesCursorCondition = (likesCursor != null && likesCursor > 0)
                ? cakeLikesCountSubQuery.lt(likesCursor)
                : null;

        /// cakeIdCursor 조건 처리
        final BooleanExpression cakeIdCursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.gt(cakeIdCursor)
                : null;

        /// 정렬 가능한 숫자 표현식으로 변환
        final NumberExpression<Integer> cakeLikesOrderExpression = Expressions.asNumber(
                JPAExpressions
                        .select(cakeLikes.count().intValue())
                        .from(cakeLikes)
                        .where(cakeLikes.cakeId.eq(cake.id))
        );

        /// 쿼리 실행
        JPQLQuery<CakeInfoDto> query = queryFactory
                .select(new QCakeInfoDto(
                        cake.id,
                        store.id,
                        store.name,
                        store.station,
                        isLikedExpression, /// 유저의 좋아요 여부
                        cake.imageUrl,
                        cakeLikesCountSubQuery, /// 좋아요 개수 서브쿼리
                        cake.id, /// cursor로 사용할 cakeId
                        Expressions.asBoolean(false)))
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .leftJoin(cakeLikes).on(cakeLikes.cakeId.eq(cake.id)) /// 좋아요 조건 추가
                .where(stationCondition) /// 역 조건
                .groupBy(cake.id);

        /// 조건 처리
        if (likesCursor == null && cakeIdCursor == null) {

            /// 1. 아이디커서와 좋아요커서 둘 다 없을 때
            query.orderBy(
                    cakeLikesOrderExpression.desc(), /// 케이크 좋아요 내림차순
                    cake.id.asc() /// 같은 좋아요 개수면 ID 오름차순
            );
        } else if (likesCursor != null && likesCursor == 0 && cakeIdCursor != null && cakeIdCursor > 0) {

            /// 2. 좋아요커서가 0이고, 아이디커서가 0보다 클 때
            query.having(cakeLikesOrderExpression.eq(0).and(cakeIdCursorCondition));
            query.orderBy(cake.id.asc()); /// 아이디 오름차순
        } else if (likesCursor != null && likesCursor == 0 && (cakeIdCursor == null || cakeIdCursor <= 0)) {

            /// 3. 좋아요커서가 0이고, 아이디커서가 없거나 0 이하일 때 (예외 처리)
            throw new IllegalArgumentException("Invalid cursor combination: likesCursor=0 and cakeIdCursor=0 or null");
        } else if (likesCursor != null && likesCursor > 0 && (cakeIdCursor == null || cakeIdCursor == 0)) {

            /// 4. 좋아요커서가 0보다 크고, 아이디커서가 없을 때
            query.having(likesCursorCondition); /// 좋아요 수가 likesCursor보다 작은 케이크 조회
            query.orderBy(
                    cakeLikesOrderExpression.desc(), /// 케이크 좋아요 내림차순
                    cake.id.asc() /// 같은 좋아요 개수면 ID 오름차순
            );
        } else if (likesCursor != null && likesCursor > 0 && cakeIdCursor != null && cakeIdCursor > 0) {
            /// 5. 좋아요커서가 0보다 크고, 아이디커서가 0보다 클 때
            query.having(
                    cakeLikesOrderExpression.eq(likesCursor).and(cakeIdCursorCondition) /// 좋아요 수가 likesCursor와 같고, cakeIdCursor보다 큰 케이크
                            .or(cakeLikesOrderExpression.lt(likesCursor)) /// 이후 좋아요 수가 likesCursor보다 작은 케이크
            );
            query.orderBy(
                    cakeLikesOrderExpression.desc(), /// 케이크 좋아요 내림차순
                    cake.id.asc() /// 같은 좋아요 개수면 ID 오름차순
            );
        }

        /// 제한 조건 설정
        query.limit(size + 1);

        /// 쿼리 실행
        List<CakeInfoDto> cakes = query.fetch();

        if(cakes.isEmpty()) {
            throw new NotFoundException();
        }

        /// 좋아요 수 비교 및 Cursor 설정
        if (cakes.size() > size) {
            final CakeInfoDto lastItem = cakes.get(size - 1); /// limit번째 데이터
            final CakeInfoDto extraItem = cakes.get(size);    /// limit + 1번째 데이터

            if (lastItem.getCakeLikeCount() == (extraItem.getCakeLikeCount())) {

                /// 좋아요 수가 같으면 limit번째 데이터의 cakeId를 Cursor로 설정
                lastItem.setCakeIdCursor(lastItem.getCakeId());
            } else {

                /// 좋아요 수가 다르면 Cursor를 null로 설정
                lastItem.setCakeIdCursor(null);
            }
            cakes = cakes.subList(0, size); /// limit 수만큼 자르기
        } else { ///마지막 데이터 조회했을때
            final CakeInfoDto lastItem = cakes.get(cakes.size() - 1);
            lastItem.setLastData(true);
        }
        return cakes;
    }

    //찜한 디자인 조회(최신순)
    @Override
    public List<CakeInfoDto> findLatestLikedCakesByUser(final long userId,
                                                        final Long cakeIdCursor,
                                                        final int size) {
        // 좋아요 개수 계산 서브쿼리
        final Expression<Integer> likeCountSubQuery = JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

        // 커서 조건
        final BooleanExpression cursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.lt(cakeIdCursor)
                : null;

        /// 메인 쿼리
        JPQLQuery<CakeInfoDto> query = queryFactory.selectDistinct(
                        new QCakeInfoDto(
                                cake.id,
                                store.id,
                                store.name,
                                store.station,
                                Expressions.asBoolean(true), /// 유저가 좋아요한 케이크이므로 true
                                cake.imageUrl,
                                likeCountSubQuery, // 좋아요 개수
                                Expressions.nullExpression(),
                                Expressions.asBoolean(false)
                        )
                )
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .join(cakeLikes).on(cakeLikes.cakeId.eq(cake.id).and(cakeLikes.userId.eq(userId))) // 좋아요한 케이크 필터
                .where(cursorCondition)
                .orderBy(cake.id.desc())
                .limit(size + 1);

        List<CakeInfoDto> results = query.fetch();

        if(results.isEmpty()) {
            throw new NotFoundException();
        }

        ///마지막 데이터 여부 설정
        if (results.size() > size) {
            results = results.subList(0, size);
        } else {
            results.get(results.size() - 1).setLastData(true);
        }
        return results;
    }


    //찜한 디자인(케이크) 조회(인기순)
    @Override
    public List<CakeInfoDto> findPopularLikedCakesByUser(final long userId,
                                                         final Long cakeIdCursor,
                                                         final Integer cakeLikesCursor,
                                                         final int size) {
        /// 좋아요 개수를 계산하는 서브쿼리
        final JPQLQuery<Integer> cakeLikesCountSubQuery =
                JPAExpressions
                        .select(cakeLikes.count().intValue()) /// 좋아요 개수를 Integer로 변환
                        .from(cakeLikes)
                        .where(cakeLikes.cakeId.eq(cake.id));

        /// 정렬 가능한 숫자 표현식으로 변환
        final NumberExpression<Integer> cakeLikesOrderExpression = Expressions.asNumber(
                JPAExpressions
                        .select(cakeLikes.count().intValue())
                        .from(cakeLikes)
                        .where(cakeLikes.cakeId.eq(cake.id))
        );

        // 커서 조건
        BooleanExpression cursorCondition = (cakeLikesCursor != null && cakeLikesCursor > 0)
                ? cakeLikesOrderExpression.lt(cakeLikesCursor) /// 좋아요 수 기준 커서
                : null;

        // 메인 쿼리
        JPQLQuery<CakeInfoDto> query = queryFactory.selectDistinct(
                        new QCakeInfoDto(
                                cake.id,
                                store.id,
                                store.name,
                                store.station,
                                Expressions.asBoolean(true), // 유저가 좋아요한 데이터이므로 true
                                cake.imageUrl,
                                cakeLikesCountSubQuery, // 좋아요 개수
                                cake.id,
                                Expressions.asBoolean(false))
                )
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .join(cakeLikes).on(cakeLikes.cakeId.eq(cake.id).and(cakeLikes.userId.eq(userId))) // 좋아요 필터
                .where(cursorCondition) // 커서 조건
                .orderBy(cakeLikesOrderExpression.desc(), cake.id.asc()) // 좋아요 내림차순 정렬, 동일 좋아요 수일 경우 cakeId로 정렬
                .limit(size + 1); // 추가 데이터 여부 확인을 위해 limit + 1

        List<CakeInfoDto> results = query.fetch();

        if(results.isEmpty()) {
            throw new NotFoundException();
        }

        /// 좋아요 수 비교 및 Cursor 설정
        if (results.size() > size) {
            final CakeInfoDto lastItem = results.get(size - 1); /// limit번째 데이터
            final CakeInfoDto extraItem = results.get(size);    /// limit + 1번째 데이터

            if (lastItem.getCakeLikeCount() == (extraItem.getCakeLikeCount())) {

                /// 좋아요 수가 같으면 limit번째 데이터의 cakeId를 Cursor로 설정
                lastItem.setCakeIdCursor(lastItem.getCakeId());
            } else {

                /// 좋아요 수가 다르면 Cursor를 null로 설정
                lastItem.setCakeIdCursor(null);
            }
            results = results.subList(0, size); /// limit 수만큼 자르기
        } else { ///마지막 데이터 조회했을때
            final CakeInfoDto lastItem = results.get(results.size() - 1);
            lastItem.setLastData(true);
        }
        return results;
    }





    // 좋아요 개수를 계산하는 서브쿼리
    private JPQLQuery<Integer> getCakeLikesCountSubQuery() {
        return JPAExpressions
                .select(cakeLikes.count().intValue()) // 좋아요 개수를 Integer로 변환
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));
    }

    // 좋아요 여부 서브쿼리
    private BooleanExpression getIsLikedExpression(final Long userId) {
        if (userId != null) {
            return JPAExpressions.selectOne()
                    .from(cakeLikes)
                    .where(cakeLikes.cakeId.eq(cake.id).and(cakeLikes.userId.eq(userId)))
                    .exists();
        } else {
            return Expressions.asBoolean(false);
        }
    }

    private BooleanExpression isLikedExpression(final Long userId, final NumberPath<Long> cakeIdPath) {
        if (userId != null) {
            return JPAExpressions.selectOne()
                    .from(cakeLikes)
                    .where(cakeLikes.cakeId.eq(cakeIdPath).and(cakeLikes.userId.eq(userId)))
                    .exists();
        } else {
            return Expressions.asBoolean(false);
        }
    }
}
