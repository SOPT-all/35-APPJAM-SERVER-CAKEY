package com.cakey.store.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StoreDetailInfoRes(
        String monOpen,
        String monClose,
        String tueOpen,
        String tueClose,
        String wedOpen,
        String wedClose,
        String thuOpen,
        String thuClose,
        String friOpen,
        String friClose,
        String satOpen,
        String satClose,
        String sunOpen,
        String sunClose,
        String address,
        String phone
) {
    public static StoreDetailInfoRes of(final String monOpen,
                                        final String monClose,
                                        final String tueOpen,
                                        final String tueClose,
                                        final String wedOpen,
                                        final String wedClose,
                                        final String thuOpen,
                                        final String thuClose,
                                        final String friOpen,
                                        final String friClose,
                                        final String satOpen,
                                        final String satClose,
                                        final String sunOpen,
                                        final String sunClose,
                                        final String address,
                                        final String phone) {
        return StoreDetailInfoRes.builder()
                .monOpen(monOpen)
                .monClose(monClose)
                .tueOpen(tueOpen)
                .tueClose(tueClose)
                .wedOpen(wedOpen)
                .wedClose(wedClose)
                .thuOpen(thuOpen)
                .thuClose(thuClose)
                .friOpen(friOpen)
                .friClose(friClose)
                .satOpen(satOpen)
                .satClose(satClose)
                .sunOpen(sunOpen)
                .sunClose(sunClose)
                .address(address)
                .phone(phone)
                .build();
    }
}
