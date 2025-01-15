package com.cakey.operationtime.facade;

import com.cakey.operationtime.domain.StoreOperationTime;
import com.cakey.operationtime.dto.StoreOperationTimeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreOperationTimeFacade {

    private final StoreOperationTimeRetriever storeOperationTimeRetriever;

    public StoreOperationTimeDto findStoreOperationTimeByStoreId(final Long storeId) {
        StoreOperationTime storeOperationTime = storeOperationTimeRetriever.findStoreOperationTimeByStoreId(storeId);
        return StoreOperationTimeDto.of(
                storeOperationTime.getMonOpen(),
                storeOperationTime.getMonClose(),
                storeOperationTime.getTueOpen(),
                storeOperationTime.getTueClose(),
                storeOperationTime.getWedOpen(),
                storeOperationTime.getWedClose(),
                storeOperationTime.getThuOpen(),
                storeOperationTime.getThuClose(),
                storeOperationTime.getFriOpen(),
                storeOperationTime.getFriClose(),
                storeOperationTime.getSatOpen(),
                storeOperationTime.getSatClose(),
                storeOperationTime.getSunOpen(),
                storeOperationTime.getSunClose()
                );
    }
}
