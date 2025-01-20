package com.cakey.store.dto;

import java.util.List;


public record AllStationRes(
        List<StationInfo> stations
) {
    public static AllStationRes from(List<StationInfo> stations) {
            return new AllStationRes(stations);
    }

    public record StationInfo (
            String stationEnName,
            String stationKrName,
            double latitude,
            double longitude) {

        public static StationInfo of(final String stationEnName,
                                     final String stationKrName,
                                     final double latitude,
                                     final double longitude) {
            return new StationInfo(stationEnName, stationKrName, latitude, longitude);
        }
    }
}
