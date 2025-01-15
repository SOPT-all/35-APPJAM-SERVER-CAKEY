package com.cakey.cake.repository;

import com.cakey.cake.domain.QCake;
import com.cakey.cake.dto.CakeMainImageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CakeRepositoryCustomImpl implements CakeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CakeMainImageDto> findMainImageByStoreIds(List<Long> storeIds) {
        QCake cake = QCake.cake;

        return queryFactory.select(Projections.constructor(CakeMainImageDto.class,
                        cake.storeId,
                        cake.id,
                        cake.imageUrl))
                .from(cake)
                .where(cake.storeId.in(storeIds)
                        .and(cake.isMainImage.isTrue()))
                .fetch();
    }
}
