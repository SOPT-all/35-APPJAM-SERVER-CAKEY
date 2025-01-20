package com.cakey.store.dto;

import java.util.List;


public record AllStationRes(
        List<StationInfo> stations
) {
    public static AllStationRes from(List<StationInfo> stations) {
            return new AllStationRes(stations);
    }

    public record StationInfo(
            String stationEnName,
            String stationKnName,
            double latitude,
            double longitude) {

        public static StationInfo of(final String stationEnName,
                                     final String stationKnName,
                                     final double latitude,
                                     final double longitude) {
            return new StationInfo(stationEnName, stationKnName, latitude, longitude);
        }
    }
}
