package com.cakey.size.facade;

import com.cakey.size.domain.Size;
import com.cakey.size.dto.SizeDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SizeFacade {

    private final SizeRetriever sizeRetriever;

    public List<SizeDto> findSizeAllByStoreIdAndOrderByPriceAsc(final long storeId) {
        final List<Size> sizeList = sizeRetriever.findSizeAllByStoreIdAndOrderByPriceAsc(storeId);
        return sizeList.stream().map(
                size -> new SizeDto(size.getSizeName(), size.getPrice())
        ).collect(Collectors.toList());
    }
}
