package com.cakey.store.dto;

public record StoreKakaoLinkRes(
        String kakaoLink
) {
    public static StoreKakaoLinkRes from(String kakaoLink) {
        return new StoreKakaoLinkRes(kakaoLink);
    }
}
