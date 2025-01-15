package com.cakey.cake.facade;

import com.cakey.cake.dto.CakeMainImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CakeFacade {
    private final CakeRetriever cakeRetriever;

    public List<CakeMainImageDto> findMainImageByStoreIds(List<Long> storeIds) {
        return cakeRetriever.findMainImageByStoreIds(storeIds);
    }
}
