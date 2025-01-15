package com.cakey.store.dto;

import com.cakey.size.dto.SizeDto;
import java.util.List;

public record StoreAllSizeAndTasteRes(
        List<SizeDto> sizeDtoList,
        String taste
) {
    public static StoreAllSizeAndTasteRes of(List<SizeDto> sizeDtoList, String taste) {
        return new StoreAllSizeAndTasteRes(sizeDtoList, taste);
    }
}