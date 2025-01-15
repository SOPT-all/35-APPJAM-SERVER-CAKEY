package com.cakey.store.dto;

public record StoreTasteDto(
        String taste
) {
    public StoreTasteDto(String taste) {
        this.taste = taste;
    }
}
