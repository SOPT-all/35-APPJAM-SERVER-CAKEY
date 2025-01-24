package com.cakey.store.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Station {
    ALL("전체", "ALL",0, 0),
    KONKUK("건대입구역", "KONKUK", 37.540456, 127.070499),
    GUUI("구의역", "GUUI", 37.537077, 127.085916),
    MAGOK("마곡역", "MAGOK", 37.560245, 126.825491),
    MOKDONG("목동역", "MOKDONG", 37.526065, 126.864931),
    MUNJEONG("문정역", "MUNJEONG", 37.484943, 127.122948),
    MUWAKJAE("무악재역", "MUWAKJAE", 37.582661, 126.950231),
    SEOKCHON("석촌역", "SEOKCHON", 37.505094, 127.106988),
    SUNGSHIN("성신여대입구역", "SUNGSHIN", 37.592624, 127.016403),
    SEOULFOREST("서울숲역", "SEOULFOREST", 37.544581, 127.044132),
    SANGWANGSIMNI("상왕십리역", "SANGWANGSIMNI", 37.564354, 127.029354),
    SINDORIM("신도림역", "SINDORIM", 37.508980, 126.891391),
    SILLIM("신림역", "SILLIM", 37.484201, 126.929715),
    CHILDRENSGRANDPARK("어린이대공원역", "CHILDRENSGRANDPARK", 37.548014, 127.074565),
    YEONSINNAE("연신내역", "YEONSINNAE", 37.619001, 126.921008),
    EWHA("이대역", "EWHA", 37.556733, 126.946013),
    JUNGGOK("중곡역", "JUNGGOK", 37.565923, 127.084350),
    HANSEONGBAEKJE("한성백제역", "HANSEONGBAEKJE", 37.516078, 127.115655),
    
    ;


    @JsonValue
    private final String stationKrName;
    private final String stationEnName;
    private final double latitude;
    private final double longitude;
}
