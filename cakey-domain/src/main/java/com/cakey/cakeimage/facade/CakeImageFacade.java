package com.cakey.cakeimage.facade;

import com.cakey.cakeimage.domain.CakeImages;
import com.cakey.cakeimage.dto.CakeImageDto;
import com.cakey.cakeimage.repository.CakeImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CakeImageFacade {
    private final CakeImagesRetriever cakeImagesRetriever;

    public List<CakeImageDto> findMainImagesByStoreIds(final List<Long> storeIds) {
        return cakeImagesRetriever.findMainImagesByStoreIds(storeIds);
    }
}
