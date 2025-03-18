package com.cakey.cake.facade;

import com.cakey.cake.domain.QCake;
import com.cakey.cakelike.domain.QCakeLikes;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;

public class CakeSubQueryCreator {

    //케이크 좋아요 개수 서브쿼리
    public static Expression<Integer> getCakeLikeCount(final QCakeLikes qCakeLikes,
                                                       final QCake qCake) {
        return JPAExpressions
                .select(qCakeLikes.count().intValue())
                .from(qCakeLikes)
                .where(qCakeLikes.cakeId.eq(qCake.id));
    }

    //유저의 케이크좋아요 여부 서브쿼리
    //todo: 추후에 쿼리 개선해서 left join할때 가져올수있을듯
    public static BooleanExpression isUserLikeCake(final Long userId,
                                                   final NumberPath<Long> cakeId,
                                                   final QCakeLikes qCakeLikes) {
        if (userId != null) {
            return JPAExpressions.selectOne()
                    .from(qCakeLikes)
                    .where(qCakeLikes.cakeId.eq(cakeId).and(qCakeLikes.userId.eq(userId)))
                    .exists();
        } else {
            return Expressions.asBoolean(false);
        }
    }
}
