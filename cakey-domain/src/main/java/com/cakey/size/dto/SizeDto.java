package com.cakey.size.dto;

public record SizeDto(
        String sizeName,
        int price
) {
    public SizeDto of(String sizeName, int price) {
        return new SizeDto(sizeName, price);
    }
}
