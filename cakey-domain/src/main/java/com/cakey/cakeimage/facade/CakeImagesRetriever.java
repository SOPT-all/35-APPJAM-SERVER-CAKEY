package com.cakey.cakeimage.facade;

import com.cakey.cakeimage.dto.CakeImageDto;
import com.cakey.cakeimage.repository.CakeImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CakeImagesRetriever {
    private final CakeImagesRepository cakeImagesRepository;

    public List<CakeImageDto> findMainImagesByStoreIds(final List<Long> storeIds) {
        return cakeImagesRepository.findMainImagesByStoreIds(storeIds);
    }
}
