package com.cakey.operationtime.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StoreOperationTimeDto(
        LocalDateTime monOpen,
        LocalDateTime monClose,
        LocalDateTime tueOpen,
        LocalDateTime tueClose,
        LocalDateTime wedOpen,
        LocalDateTime wedClose,
        LocalDateTime thuOpen,
        LocalDateTime thuClose,
        LocalDateTime friOpen,
        LocalDateTime friClose,
        LocalDateTime satOpen,
        LocalDateTime satClose,
        LocalDateTime sunOpen,
        LocalDateTime sunClose
) {
    public static StoreOperationTimeDto of(final LocalDateTime monOpen,
                                           final LocalDateTime monClose,
                                           final LocalDateTime tueOpen,
                                           final LocalDateTime tueClose,
                                           final LocalDateTime wedOpen,
                                           final LocalDateTime wedClose,
                                           final LocalDateTime thuOpen,
                                           final LocalDateTime thuClose,
                                           final LocalDateTime friOpen,
                                           final LocalDateTime friClose,
                                           final LocalDateTime satOpen,
                                           final LocalDateTime satClose,
                                           final LocalDateTime sunOpen,
                                           final LocalDateTime sunClose) {
        return StoreOperationTimeDto.builder()
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
                .build();
    }
}
