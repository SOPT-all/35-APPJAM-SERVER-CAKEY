package com.cakey.store.dto;

public record StoreDetailInfoDto(
        String address,
        String phone
) {
    public StoreDetailInfoDto(String address, String phone) {
        this.address = address;
        this.phone = phone;
    }
}
