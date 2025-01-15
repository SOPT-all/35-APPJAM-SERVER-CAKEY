package com.cakey.store.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Station {
    ALL("전체", 0, 0),
    HONGDAE("홍대입구역", 111.111111, 111.111111),
    TEST("테스트역", 222.222222, 222.222222),
    ;

    @JsonValue
    private final String stationName;
    private final double latitude;
    private final double longitude;
}
