package com.cakey.cake.repository;

import com.cakey.cake.domain.QCake;
import com.cakey.cake.dto.CakeInfoDto;
import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.dto.QCakeInfoDto;
import com.cakey.cakelike.domain.QCakeLikes;
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

import java.util.List;

@RequiredArgsConstructor
public class CakeRepositoryCustomImpl implements CakeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QCake cake = QCake.cake;
    QStore store = QStore.store;
    QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

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

        // 서브쿼리: 좋아요 개수 계산
        final Expression<Integer> likeCountSubQuery = getCakeLikesCountSubQuery();

        // 커서 조건
        final BooleanExpression cursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.lt(cakeIdCursor)
                : null;

        // 좋아요 여부 조건
        final BooleanExpression likeCondition = userId != null ? cakeLikes.userId.eq(userId) : null;

        // 메인 쿼리
        return queryFactory.select(new QCakeInfoDto(
                        cake.id,
                        store.id,
                        store.name,
                        store.station,
                        isLikedExpression(userId, cake.id), // 유저 ID가 있을 때만 좋아요 여부 확인
                        cake.imageUrl,
                        likeCountSubQuery, // 서브쿼리로 계산된 좋아요 개수
                        Expressions.nullExpression()))
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .leftJoin(cakeLikes).on(cakeLikes.cakeId.eq(cake.id).and(likeCondition)) // 좋아요 조건 추가
                .where(store.station.eq(station)
                        .and(cursorCondition))
                .orderBy(cake.id.desc()) // 동적 정렬 적용
                .limit(size)
                .fetch();
    }




    @Override
    public List<CakeInfoDto> findPopularCakesByStation(final Long userId,
                                                       final Station station,
                                                       final Integer likesCursor,
                                                       final Long cakeIdCursor,
                                                       final int size) {

        QCake cake = QCake.cake;
        QStore store = QStore.store;
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

        // 좋아요 개수를 계산하는 서브쿼리
        final JPQLQuery<Integer> cakeLikesCountSubQuery =
                JPAExpressions
                        .select(cakeLikes.count().intValue()) // 좋아요 개수를 Integer로 변환
                        .from(cakeLikes)
                        .where(cakeLikes.cakeId.eq(cake.id));

        // 좋아요 여부 서브쿼리
        final BooleanExpression isLikedExpression = getIsLikedExpression(userId);

        // 커서 조건 처리
        final BooleanExpression cakeIdCursorCondition = (cakeIdCursor == null || cakeIdCursor == 0)
                ? null
                : cake.id.gt(cakeIdCursor);

        // 좋아요 커서 조건
        final BooleanExpression likesCursorCondition = (likesCursor != null && likesCursor > 0)
                ? JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id))
                .lt(likesCursor)
                : null;

        // 역 조건
        final BooleanExpression stationCondition = station != Station.ALL
                ? store.station.eq(station)
                : null;

        // 쿼리 실행
        JPQLQuery<CakeInfoDto> query = queryFactory
                .select(new QCakeInfoDto(
                        cake.id,
                        store.id,
                        store.name,
                        store.station,
                        isLikedExpression, // 유저의 좋아요 여부
                        cake.imageUrl,
                        cakeLikesCountSubQuery, // 좋아요 개수 서브쿼리
                        cake.id // cursor로 사용할 cakeId
                ))
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .leftJoin(cakeLikes).on(cakeLikes.cakeId.eq(cake.id)) // 좋아요 조건 추가
                .where(stationCondition) // 역 조건
                .groupBy(cake.id);

        // 좋아요 커서 조건 추가
        if (likesCursorCondition != null) {
            query.having(likesCursorCondition);
        }
        if (cakeIdCursorCondition != null) {
            query.having(cake.id.gt(cakeIdCursor));
        }

        // 정렬 및 제한
        query.orderBy(
                Expressions.asNumber(
                        JPAExpressions
                                .select(cakeLikes.count().intValue())
                                .from(cakeLikes)
                                .where(cakeLikes.cakeId.eq(cake.id))
                ).desc(), // 좋아요 개수 기준 내림차순 정렬
                cake.id.asc() // 좋아요 개수가 같을 경우 cakeId 기준 오름차순 정렬
        ).limit(size + 1); // limit + 1개 조회

        // 쿼리 실행
        List<CakeInfoDto> cakes = query.fetch();

        // 좋아요 수 비교 및 Cursor 설정
        if (cakes.size() > size) {
            final CakeInfoDto lastItem = cakes.get(size - 1); // limit번째 데이터
            final CakeInfoDto extraItem = cakes.get(size);    // limit + 1번째 데이터

            if (lastItem.getCakeLikeCount() == (extraItem.getCakeLikeCount())) {
                // 좋아요 수가 같으면 limit번째 데이터의 cakeId를 Cursor로 설정
                lastItem.setCakeIdCursor(lastItem.getCakeId());
            } else {
                // 좋아요 수가 다르면 Cursor를 null로 설정
                lastItem.setCakeIdCursor(null);
            }
            cakes = cakes.subList(0, size); // limit 수만큼 자르기
        } else {
            if (!cakes.isEmpty()) {
                cakes.get(cakes.size() - 1).setCakeIdCursor(null);
            }
        }
        return cakes;
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
