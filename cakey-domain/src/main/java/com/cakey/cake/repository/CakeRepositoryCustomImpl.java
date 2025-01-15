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
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CakeRepositoryCustomImpl implements CakeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    final QCake cake = QCake.cake;
    final QStore store = QStore.store;
    final QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

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

    //해당역 스토어의 디자인(케이크) 조회
    @Override
    public List<CakeInfoDto> findCakesByStation(final Long userId, final Station station, final Long cakeIdCursor, final int size) {

        // 서브쿼리: 좋아요 개수 계산
        final Expression<Integer> likeCountSubQuery = JPAExpressions.select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

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
                        likeCountSubQuery)) // 서브쿼리로 계산된 좋아요 개수
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .leftJoin(cakeLikes).on(cakeLikes.cakeId.eq(cake.id).and(likeCondition)) // 좋아요 조건 추가
                .where(store.station.eq(station)
                        .and(cursorCondition))
                .orderBy(cake.id.desc()) // 동적 정렬 적용
                .limit(size)
                .fetch();
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
