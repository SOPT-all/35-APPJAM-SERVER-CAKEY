package com.cakey.operationtime.facade;

import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.operationtime.domain.StoreOperationTime;
import com.cakey.operationtime.repository.StoreOperationTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreOperationTimeRetriever {

    private final StoreOperationTimeRepository storeOperationTimeRepository;

    public StoreOperationTime findStoreOperationTimeByStoreId(final Long storeId) {
        return storeOperationTimeRepository.findByStoreId(storeId)
                .orElseThrow(NotFoundBaseException::new);
    }

}
