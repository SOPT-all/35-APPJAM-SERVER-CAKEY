package com.cakey.size.dto;

public record SizeDto(
        String sizeName,
        int price
) {
    public SizeDto of(final String sizeName, final int price) {
        return new SizeDto(sizeName, price);
    }
}
