package com.cakey.size.facade;

import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.size.domain.Size;
import com.cakey.size.repository.SizeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SizeRetriever {

    private final SizeRepository sizeRepository;

    public List<Size> findSizeAllByStoreIdAndOrderByPriceAsc(final Long storeId) {
        List<Size> response = sizeRepository.findSizeNameAndPriceByStoreIdOrderByPriceAsc(storeId);
        if (response.isEmpty()) {
            throw new NotFoundBaseException();
        }
        return response;
    }
}
