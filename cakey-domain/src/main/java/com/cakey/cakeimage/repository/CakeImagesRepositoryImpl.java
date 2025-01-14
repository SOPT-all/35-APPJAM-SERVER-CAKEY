package com.cakey.cakeimage.repository;

import com.cakey.cakeimage.domain.QCakeImages;
import com.cakey.cakeimage.dto.CakeImageDto;
import com.cakey.cakeimage.dto.QCakeImageDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CakeImagesRepositoryImpl implements CakeImagesRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CakeImageDto> findMainImagesByStoreIds(List<Long> storeIds) {
        QCakeImages cakeImages = QCakeImages.cakeImages;

        return queryFactory
                .select(new QCakeImageDto(
                        cakeImages.id,
                        cakeImages.imageUrl
                ))
                .from(cakeImages)
                .where(cakeImages.storeId.in(storeIds)
                        .and(cakeImages.isMainImage.eq(true)))
                .fetch();
    }
}
