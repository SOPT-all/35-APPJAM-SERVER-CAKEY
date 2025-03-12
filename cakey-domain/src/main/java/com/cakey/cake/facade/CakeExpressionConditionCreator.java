package com.cakey.cake.facade;

import com.cakey.cake.domain.QCake;
import com.cakey.cakelike.domain.QCakeLikes;
import com.cakey.store.domain.QStore;
import com.cakey.store.domain.Station;
import com.querydsl.core.types.dsl.BooleanExpression;

public class CakeExpressionConditionCreator {

    //최신순 조회 - cakeId 커서 조건
    public static BooleanExpression LatestCakeIdCursorCondition(final Long cakeIdCursor,
                                                                final QCake qCake) {
        return (cakeIdCursor > 0)
                ? qCake.id.lt(cakeIdCursor) /// 아이디커서보다 작은 아이디인 케이크 조회
                : null;
    }

    //인기순 조회 - cakeId 커서 조건
    public static BooleanExpression FamousCakeIdCursorCondition(final Long cakeIdCursor,
                                                                final QCake qCake) {
        return (cakeIdCursor > 0)
                ? qCake.id.gt(cakeIdCursor)
                : null;
    }

    //유저의 cake 좋아요 여부 조건
    public static BooleanExpression cakeLikeByUserCondition(final Long userId,
                                                            final QCakeLikes qCakeLikes) {
        return userId != null
                ? qCakeLikes.userId.eq(userId) /// 유저 ID가 있을 때만 좋아요 조건 추가
                : null;
    }

    //지하철역 조건
    public static BooleanExpression stationCondition(final Station station,
                                                     final QStore qStore) {
        return qStore.station.eq(station);
    }


}
