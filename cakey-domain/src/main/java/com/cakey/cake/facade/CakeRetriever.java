package com.cakey.cake.facade;

import com.cakey.cake.domain.Cake;
import com.cakey.cake.dto.CakeDesignDto;
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

    public List<Cake> findAllByStoreId(final Long storeId) {
        return cakeRepository.findAllByStoreId(storeId);
    }

}
