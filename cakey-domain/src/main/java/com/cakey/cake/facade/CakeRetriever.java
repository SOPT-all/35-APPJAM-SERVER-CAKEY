package com.cakey.cake.facade;

import com.cakey.cake.dto.CakeMainImageDto;
import com.cakey.cake.repository.CakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CakeRetriever {
    private final CakeRepository cakeRepository;

    public List<CakeMainImageDto> findMainImageByStoreIds(final List<Long> storeIds) {
        return cakeRepository.findMainImageByStoreIds(storeIds);
    }

}
