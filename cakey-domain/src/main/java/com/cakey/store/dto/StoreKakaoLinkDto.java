package com.cakey.store.dto;

public record StoreKakaoLinkDto(
        String kakaoLink
) {
    public StoreKakaoLinkDto(String kakaoLink) {
        this.kakaoLink = kakaoLink;
    }
}
