package com.cakey.operationtime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Table(name = "store_operation_time")
@Entity
@Getter
public class StoreOperationTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_operation_time_id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private long storeId;

    @Column(name = "mon_open", nullable = true)
    private String monOpen;

    @Column(name = "mon_close", nullable = true)
    private String monClose;

    @Column(name = "tue_open", nullable = true)
    private String tueOpen;

    @Column(name = "tue_close", nullable = true)
    private String tueClose;

    @Column(name = "wed_open", nullable = true)
    private String wedOpen;

    @Column(name = "wed_close", nullable = true)
    private String wedClose;

    @Column(name = "thu_open", nullable = true)
    private String thuOpen;

    @Column(name = "thu_close", nullable = true)
    private String thuClose;

    @Column(name = "fri_open", nullable = true)
    private String friOpen;

    @Column(name = "fri_close", nullable = true)
    private String friClose;

    @Column(name = "sat_open", nullable = true)
    private String satOpen;

    @Column(name = "sat_close", nullable = true)
    private String satClose;

    @Column(name = "sun_open", nullable = true)
    private String sunOpen;

    @Column(name = "sun_close", nullable = true)
    private String sunClose;

    public static StoreOperationTime createStoreOperationTime(
            final long storeId,
            final String monOpen,
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
            final String sunClose
            ) {
        return StoreOperationTime.builder()
                .storeId(storeId)
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
