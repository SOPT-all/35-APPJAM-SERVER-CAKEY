package com.cakey.store.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Station {
    ALL("전체"),
    HONGDAE("홍대입구역"),
    TEST("테스트역"),
    ;

    @JsonValue
    private final String stationName;

}
