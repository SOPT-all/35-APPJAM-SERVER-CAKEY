package com.cakey.store.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Station {
    ALL("전체", "ALL",0, 0),
    HONGDAE("홍대입구역", "HONGDAE",111.111111, 111.111111),
    TEST("테스트역", "TEST", 222.222222, 222.222222),
    ;

    @JsonValue
    private final String stationKrName;
    private final String stationEnName;
    private final double latitude;
    private final double longitude;
}
