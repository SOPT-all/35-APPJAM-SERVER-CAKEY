package com.cakey.store.dto;

public record StoreTasteDto(
        String taste
) {
    public StoreTasteDto(final String taste) {
        this.taste = taste;
    }
}
