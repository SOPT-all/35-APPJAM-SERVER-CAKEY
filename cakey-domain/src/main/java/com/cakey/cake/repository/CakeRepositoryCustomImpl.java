package com.cakey.cake.repository;

import com.cakey.cake.domain.DayCategory;
import com.cakey.cake.domain.QCake;
import com.cakey.cake.dto.*;
import com.cakey.cakelike.domain.QCakeLikes;
import com.cakey.caketheme.domain.QCakeTheme;
import com.cakey.caketheme.domain.ThemeName;
import com.cakey.common.exception.NotFoundException;
import com.cakey.store.domain.QStore;
import com.cakey.store.domain.Station;
import com.cakey.store.dto.QStoreBySelectedCakeDto;
import com.cakey.store.dto.QStoreInfoDto;
import com.cakey.store.dto.StoreBySelectedCakeDto;
import com.cakey.storelike.domain.QStoreLike;
import com.querydsl.core.BooleanBuilder;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Expr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CakeRepositoryCustomImpl implements CakeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

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
        QCake cake = QCake.cake;
        QStore store = QStore.store;
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

        /// 좋아요 개수를 계산하는 서브쿼리
        final JPQLQuery<Integer> cakeLikesCountSubQuery = JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

        /// 좋아요 개수를 정렬 가능한 표현식으로 변환
        final NumberExpression<Integer> cakeLikesOrderExpression = Expressions.asNumber(cakeLikesCountSubQuery);

        /// 케이크 아이디 커서
        final BooleanExpression cakeIdCursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.gt(cakeIdCursor) : null;

        ///케이크 좋아요 커서
        final BooleanExpression cakeLikesCursorCondition = (cakeLikesCursor != null && cakeLikesCursor > 0)
                ? cakeLikesOrderExpression.lt(cakeLikesCursor) : null;

        /// 메인 쿼리 시작
        JPQLQuery<CakeInfoDto> query = queryFactory.selectDistinct(
                        new QCakeInfoDto(
                                cake.id,
                                store.id,
                                store.name,
                                store.station,
                                Expressions.asBoolean(true), /// 유저가 좋아요한 데이터이므로 true
                                cake.imageUrl,
                                cakeLikesCountSubQuery, // 좋아요 개수
                                cake.id,
                                Expressions.asBoolean(false))
                )
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .join(cakeLikes).on(cakeLikes.cakeId.eq(cake.id).and(cakeLikes.userId.eq(userId)));

        /// 다양한 커서 및 조건 처리
        if (cakeLikesCursor == null && cakeIdCursor == null) {
            /// 1. 아이디커서와 좋아요커서 둘 다 없을 때
            query.orderBy(
                    cakeLikesOrderExpression.desc(), /// 좋아요 내림차순
                    cake.id.asc() /// 같은 좋아요 개수일 경우 ID 오름차순
            );
        } else if (cakeLikesCursor != null && cakeLikesCursor == 0 && cakeIdCursor != null && cakeIdCursor > 0) {
            /// 2. 좋아요커서가 0이고 아이디커서가 0보다 클 때
            query.where(cakeLikesCursorCondition).orderBy(cake.id.asc());
        } else if (cakeLikesCursor != null && cakeLikesCursor == 0 && (cakeIdCursor == null || cakeIdCursor <= 0)) {
            /// 3. 좋아요커서가 0이고, 아이디커서가 없거나 0 이하일 때 (예외처리)
            throw new IllegalArgumentException("Invalid cursor combination: likesCursor=0 and cakeIdCursor=0 or null");
        } else if (cakeLikesCursor != null && cakeLikesCursor > 0 && (cakeIdCursor == null || cakeIdCursor == 0)) {
            /// 4. 좋아요커서가 0보다 크고, 아이디커서가 없을 때
            query.where(cakeLikesCursorCondition).orderBy(
                    cakeLikesOrderExpression.desc(), /// 좋아요 내림차순
                    cake.id.asc() /// 같은 좋아요 개수일 경우 ID 오름차순
            );
        } else if (cakeLikesCursor != null && cakeLikesCursor > 0 && cakeIdCursor != null && cakeIdCursor > 0) {
            /// 5. 좋아요커서 > 0, 아이디커서 > 0
            query.where(
                    cakeLikesOrderExpression.eq(cakeLikesCursor).and(cakeIdCursorCondition)
                            .or(cakeLikesOrderExpression.lt(cakeLikesCursor))
            ).orderBy(
                    cakeLikesOrderExpression.desc(),
                    cake.id.asc()
            );
        }

        /// 페이징 처리
        query.limit(size + 1);
        List<CakeInfoDto> results = query.fetch();

        if (results.isEmpty()) {
            throw new NotFoundException();
        }

        /// 페이징 결과 처리
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
        } else {
            results.get(results.size() - 1).setLastData(true);
        }
        return results;
    }

    //같은 store의 daycategory, theme 케이크들 조회
    @Override
    public List<CakeSelectedInfoDto> findCakesByStoreAndConditions(final Long storeId,
                                                                   final DayCategory dayCategory,
                                                                   final ThemeName theme,
                                                                   final Long userId,
                                                                   final Long cakeId) {
        QCake cake = QCake.cake;
        QCakeTheme cakeTheme = QCakeTheme.cakeTheme;

        ///첫 번째 케이크: 들어온 cakeId 정보 가져오기
        CakeSelectedInfoDto mainCake = queryFactory.select(new QCakeSelectedInfoDto(
                        cake.id,
                        isLikedByUser(cake.id, userId), /// 좋아요 여부 확인
                        cake.imageUrl
                ))
                .from(cake)
                .where(cake.id.eq(cakeId))
                .fetchOne();

        if (mainCake == null) {
            throw new IllegalArgumentException("Cake with ID " + cakeId + " not found");
        }

        ///나머지 케이크 조건 생성 (theme이 ALL일 경우 theme 조건 제외)
        BooleanBuilder whereCondition = new BooleanBuilder()
                .and(cake.storeId.eq(storeId))
                .and(cake.dayCategory.eq(dayCategory))
                .and(cake.id.ne(cakeId)); /// 첫 번째 케이크 제외

        if (!ThemeName.ALL.equals(theme)) {
            whereCondition.and(cakeTheme.themeName.eq(theme));
        }

        /// 나머지 케이크 가져오기
        List<CakeSelectedInfoDto> otherCakes = queryFactory.select(new QCakeSelectedInfoDto(
                        cake.id,
                        isLikedByUser(cake.id, userId), /// 좋아요 여부 확인
                        cake.imageUrl
                ))
                .from(cake)
                .join(cakeTheme).on(cake.id.eq(cakeTheme.cakeId))
                .where(whereCondition)
                .limit(9) /// 첫 번째 케이크를 제외하고 최대 9개
                .distinct()
                .fetch();

        /// 첫 번째 케이크 + 나머지 케이크
        List<CakeSelectedInfoDto> cakes = new ArrayList<>();
        cakes.add(mainCake); // 첫 번째 케이크 추가
        cakes.addAll(otherCakes); // 나머지 케이크 추가
        return cakes;
    }

    //디자인 둘러보기 조회(최신순)
    @Override
    public List<CakeInfoDto> findCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                         final ThemeName theme,
                                                         final Long userId,
                                                         final Long cakeIdCursor,
                                                         final int limit) {
        QCake cake = QCake.cake;
        QCakeTheme cakeTheme = QCakeTheme.cakeTheme;
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;
        QStore store = QStore.store;

        /// 좋아요 개수 계산 서브쿼리
        final Expression<Integer> likeCountSubQuery = JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

        /// 동적 조건 생성
        BooleanBuilder whereCondition = new BooleanBuilder()
                .and(cake.dayCategory.eq(dayCategory)); // dayCategory는 항상 필수 조건

        /// theme 조건 추가 (theme이 ALL이 아닌 경우에만)
        if (!ThemeName.ALL.equals(theme)) {
            whereCondition.and(cakeTheme.themeName.eq(theme));
        }

        /// cakeIdCursor 조건 추가 (cakeIdCursor가 null이 아닌 경우)
        if (cakeIdCursor != null) {
            whereCondition.and(cake.id.lt(cakeIdCursor)); /// 커서 조건: cakeIdCursor보다 작은 케이크만
        }

        /// QueryDSL 쿼리 작성
        List<CakeInfoDto> cakes = queryFactory.select(new QCakeInfoDto(
                        cake.id,
                        cake.storeId,
                        store.name,
                        store.station,
                        getIsLikedExpression(userId), // isLiked 조건
                        cake.imageUrl,
                        likeCountSubQuery, /// 좋아요 개수
                        cake.id, /// 현재 케이크 ID를 커서로 반환
                        Expressions.asBoolean(false) /// 기본적으로 isLastData는 false로 설정
                ))
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .leftJoin(cakeTheme).on(cake.id.eq(cakeTheme.cakeId)) /// CakeTheme과 조인
                .where(whereCondition)
                .orderBy(cake.id.desc()) /// cakeId를 기준으로 내림차순 정렬
                .limit(limit + 1) /// limit + 1로 추가 데이터 확인
                .fetch();

        if (cakes.isEmpty()) {
            throw new NotFoundException();
        }

        /// 마지막 데이터인지 확인하여 isLastData 필드 설정
        if (cakes.size() > limit) {
            cakes = cakes.subList(0, limit);
        } else {
            cakes.get(cakes.size() - 1).setLastData(true);
        }
        return cakes;
    }

    //디자인 둘러보기(인기순)
    @Override
    public List<CakeInfoDto> findPopularCakesByCategoryAndTheme(final DayCategory dayCategory,
                                                                final ThemeName themeName,
                                                                final Long userId,
                                                                final Long cakeIdCursor,
                                                                final Integer cakeLikesCursor,
                                                                final int size) {
        QCake cake = QCake.cake;
        QCakeTheme cakeTheme = QCakeTheme.cakeTheme;
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;
        QStore store = QStore.store;

        /// 좋아요 개수를 계산하는 서브쿼리
        final JPQLQuery<Integer> cakeLikesCountSubQuery = JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

        /// 좋아요 개수를 정렬 가능한 표현식으로 변환
        final NumberExpression<Integer> cakeLikesOrderExpression = Expressions.asNumber(cakeLikesCountSubQuery);

        /// 케이크 ID 커서 조건
        final BooleanExpression cakeIdCursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.gt(cakeIdCursor) : null;

        /// 케이크 좋아요 커서 조건
        final BooleanExpression cakeLikesCursorCondition = (cakeLikesCursor != null && cakeLikesCursor > 0)
                ? cakeLikesOrderExpression.lt(cakeLikesCursor) : null;

        /// 메인 쿼리 작성
        JPQLQuery<CakeInfoDto> query = queryFactory.selectDistinct(
                        new QCakeInfoDto(
                                cake.id,
                                store.id,
                                store.name,
                                store.station,
                                getIsLikedExpression(userId), // isLiked 조건
                                cake.imageUrl,
                                cakeLikesCountSubQuery, // 좋아요 개수
                                cake.id,
                                Expressions.asBoolean(false) // 기본적으로 isLastData는 false로 설정
                        )
                )
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .leftJoin(cakeTheme).on(cake.id.eq(cakeTheme.cakeId)); // CakeTheme 조인

        /// 조건 추가
        query.where(cake.dayCategory.eq(dayCategory)); // dayCategory는 필수 조건

        if (!ThemeName.ALL.equals(themeName)) {
            query.where(cakeTheme.themeName.eq(themeName)); // 테마 조건 추가
        }

        /// 다양한 커서 및 정렬 처리
        if (cakeLikesCursor == null && cakeIdCursor == null) {
            /// 1. 둘 다 없을 때
            query.orderBy(
                    cakeLikesOrderExpression.desc(), // 좋아요 내림차순
                    cake.id.asc() // 같은 좋아요 수에서는 ID 오름차순
            );
        } else if (cakeLikesCursor != null && cakeLikesCursor > 0 && cakeIdCursor == null) {
            /// 2. 좋아요 커서만 있을 때
            query.where(cakeLikesCursorCondition).orderBy(
                    cakeLikesOrderExpression.desc(),
                    cake.id.asc()
            );
        } else if (cakeLikesCursor != null && cakeLikesCursor > 0 && cakeIdCursor != null && cakeIdCursor > 0) {
            /// 3. 둘 다 있을 때
            query.where(
                    cakeLikesOrderExpression.eq(cakeLikesCursor).and(cakeIdCursorCondition)
                            .or(cakeLikesOrderExpression.lt(cakeLikesCursor))
            ).orderBy(
                    cakeLikesOrderExpression.desc(),
                    cake.id.asc()
            );
        }

        /// 페이징 처리
        query.limit(size + 1); // limit + 1로 추가 데이터 확인
        List<CakeInfoDto> results = query.fetch();

        /// 결과 비어있을 때 예외 처리
        if (results.isEmpty()) {
            throw new NotFoundException();
        }

        /// 페이징 결과 처리
        if (results.size() > size) {
            CakeInfoDto lastItem = results.get(size - 1); // limit번째 데이터
            CakeInfoDto extraItem = results.get(size);    // limit + 1번째 데이터

            if (lastItem.getCakeLikeCount() == extraItem.getCakeLikeCount()) {
                /// 좋아요 수가 같으면 limit번째 데이터의 cakeId를 Cursor로 설정
                lastItem.setCakeIdCursor(lastItem.getCakeId());
            } else {
                /// 좋아요 수가 다르면 Cursor를 null로 설정
                lastItem.setCakeIdCursor(null);
            }

            results = results.subList(0, size); // limit 수만큼 자르기
        } else {
            results.get(results.size() - 1).setLastData(true);
        }

        return results;
    }

    //찜한 스토어 디자인 조회(인기순)
    @Override
    public List<CakeInfoDto> findPopularCakesLikedByUser(final long userId,
                                                         final Long cakeIdCursor,
                                                         final Integer cakeLikesCursor,
                                                         final int size) {
        QCake cake = QCake.cake;
        QStore store = QStore.store;
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;
        QStoreLike storeLikes = QStoreLike.storeLike;

        /// 좋아요 개수를 계산하는 서브쿼리
        final JPQLQuery<Integer> cakeLikesCountSubQuery = JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

        /// 좋아요 개수를 정렬 가능한 표현식으로 변환
        final NumberExpression<Integer> cakeLikesOrderExpression = Expressions.asNumber(cakeLikesCountSubQuery);

        /// 케이크 ID 커서 조건
        final BooleanExpression cakeIdCursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.gt(cakeIdCursor) : null;

        /// 케이크 좋아요 커서 조건
        final BooleanExpression cakeLikesCursorCondition = (cakeLikesCursor != null && cakeLikesCursor > 0)
                ? cakeLikesOrderExpression.lt(cakeLikesCursor) : null;

        /// 메인 쿼리 작성
        JPQLQuery<CakeInfoDto> query = queryFactory.selectDistinct(
                        new QCakeInfoDto(
                                cake.id,
                                store.id,
                                store.name,
                                store.station,
                                getIsLikedExpression(userId), /// isLiked 조건
                                cake.imageUrl,
                                cakeLikesCountSubQuery, /// 좋아요 개수
                                cake.id,
                                Expressions.asBoolean(false) /// 기본적으로 isLastData는 false로 설정
                        )
                )
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .join(storeLikes).on(store.id.eq(storeLikes.storeId).and(storeLikes.userId.eq(userId))); /// 유저가 좋아요한 스토어만

        /// 커서 및 정렬 조건 추가
        if (cakeLikesCursor == null && cakeIdCursor == null) {
            /// 1. 커서가 없는 경우
            query.orderBy(
                    cakeLikesOrderExpression.desc(), /// 좋아요 내림차순
                    cake.id.asc() /// 같은 좋아요 수에서는 ID 오름차순
            );
        } else if (cakeLikesCursor != null && cakeLikesCursor > 0 && cakeIdCursor == null) {
            /// 2. 좋아요 커서만 있는 경우
            query.where(cakeLikesCursorCondition).orderBy(
                    cakeLikesOrderExpression.desc(),
                    cake.id.asc()
            );
        } else if (cakeLikesCursor != null && cakeLikesCursor > 0 && cakeIdCursor != null && cakeIdCursor > 0) {
            /// 3. 좋아요 커서와 ID 커서 둘 다 있는 경우
            query.where(
                    cakeLikesOrderExpression.eq(cakeLikesCursor).and(cakeIdCursorCondition)
                            .or(cakeLikesOrderExpression.lt(cakeLikesCursor))
            ).orderBy(
                    cakeLikesOrderExpression.desc(),
                    cake.id.asc()
            );
        }

        /// 페이징 처리
        query.limit(size + 1); // limit + 1로 추가 데이터 확인
        List<CakeInfoDto> results = query.fetch();

        /// 결과 비어있을 때 예외 처리
        if (results.isEmpty()) {
            throw new NotFoundException();
        }

        /// 페이징 결과 처리
        if (results.size() > size) {
            CakeInfoDto lastItem = results.get(size - 1); // limit번째 데이터
            CakeInfoDto extraItem = results.get(size);    // limit + 1번째 데이터

            if (lastItem.getCakeLikeCount() == extraItem.getCakeLikeCount()) {
                /// 좋아요 수가 같으면 limit번째 데이터의 cakeId를 Cursor로 설정
                lastItem.setCakeIdCursor(lastItem.getCakeId());
            } else {
                /// 좋아요 수가 다르면 Cursor를 null로 설정
                lastItem.setCakeIdCursor(null);
            }
            results = results.subList(0, size); // limit 수만큼 자르기
        } else {
            results.get(results.size() - 1).setLastData(true);
        }
        return results;
    }

    //찜한 스토어들 디자인 조회(최신순)
    @Override
    public List<CakeInfoDto> findCakesLikedByUser(final long userId,
                                                  final Long cakeIdCursor,
                                                  final int size) {
        QCake cake = QCake.cake;
        QStore store = QStore.store;
        QStoreLike storeLikes = QStoreLike.storeLike;
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

        /// 좋아요 개수 서브쿼리
        final JPQLQuery<Integer> likeCountSubQuery = JPAExpressions
                .select(cakeLikes.count().intValue())
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cake.id));

        /// 케이크 ID 커서 조건
        final BooleanExpression cakeIdCursorCondition = (cakeIdCursor != null && cakeIdCursor > 0)
                ? cake.id.lt(cakeIdCursor) // cakeId가 커서보다 작은 경우만
                : null;

        /// 메인 쿼리 작성
        JPQLQuery<CakeInfoDto> query = queryFactory.selectDistinct(
                        new QCakeInfoDto(
                                cake.id,
                                store.id,
                                store.name,
                                store.station,
                                getIsLikedExpression(userId), // isLiked 조건
                                cake.imageUrl,
                                likeCountSubQuery, // 좋아요 개수
                                cake.id, // 현재 케이크 ID를 반환
                                Expressions.asBoolean(false) // 기본적으로 isLastData는 false로 설정
                        )
                )
                .from(cake)
                .join(store).on(cake.storeId.eq(store.id))
                .join(storeLikes).on(store.id.eq(storeLikes.storeId).and(storeLikes.userId.eq(userId))); // 유저가 좋아요한 스토어만

        /// 커서 조건 추가
        if (cakeIdCursorCondition != null) {
            query.where(cakeIdCursorCondition);
        }

        /// 정렬 조건 추가 (최신순)
        query.orderBy(cake.id.desc());

        /// 페이징 처리
        query.limit(size + 1); // limit + 1로 추가 데이터 확인
        List<CakeInfoDto> results = query.fetch();

        /// 결과 페이징 처리
        if (results.size() > size) {
            CakeInfoDto lastItem = results.get(size - 1);
            lastItem.setLastData(false); // 추가 데이터가 있으므로 isLastData = false 설정
            results = results.subList(0, size); // limit 수만큼 자르기
        } else if (!results.isEmpty()) {
            results.get(results.size() - 1).setLastData(true); // 마지막 데이터로 설정
        }

        return results;
    }




    //카테고리, 테마에 해당하는 케이크 개수
    @Override
    public int countCakesByCategoryAndTheme(final DayCategory dayCategory, final ThemeName theme) {
        QCake cake = QCake.cake;
        QCakeTheme cakeTheme = QCakeTheme.cakeTheme;

        /// 동적 조건 생성
        BooleanBuilder whereCondition = new BooleanBuilder()
                .and(cake.dayCategory.eq(dayCategory)); /// dayCategory는 항상 필수 조건

        /// theme 조건 추가 (theme이 ALL이 아닌 경우에만)
        if (!ThemeName.ALL.equals(theme)) {
            whereCondition.and(cakeTheme.themeName.eq(theme));
        }

        /// 케이크 개수 조회 쿼리
        Long count = queryFactory.select(cake.count())
                .from(cake)
                .leftJoin(cakeTheme).on(cake.id.eq(cakeTheme.cakeId))
                .where(whereCondition)
                .fetchOne();

        /// 반환 값을 int로 변환
        return count != null ? Math.toIntExact(count) : 0;
    }




    private BooleanExpression isLikedByUser(NumberExpression<Long> cakeId, Long userId) {
        QCakeLikes cakeLikes = QCakeLikes.cakeLikes;

        if (userId == null) {
            return Expressions.asBoolean(false); // 항상 false 반환
        }

        // 좋아요 여부 조건 생성
        return new JPAQueryFactory(entityManager) // 서브쿼리 작성
                .selectOne()
                .from(cakeLikes)
                .where(cakeLikes.cakeId.eq(cakeId)
                        .and(cakeLikes.userId.eq(userId)))
                .exists();
    }



    // 유저의 케이크 좋아요 여부 서브쿼리
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
