package com.cakey.operationtime.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime monOpen;

    @Column(name = "mon_close", nullable = true)
    private LocalDateTime monClose;

    @Column(name = "tue_open", nullable = true)
    private LocalDateTime tueOpen;

    @Column(name = "tue_close", nullable = true)
    private LocalDateTime tueClose;

    @Column(name = "wed_open", nullable = true)
    private LocalDateTime wedOpen;

    @Column(name = "wed_close", nullable = true)
    private LocalDateTime wedClose;

    @Column(name = "thu_open", nullable = true)
    private LocalDateTime thuOpen;

    @Column(name = "thu_close", nullable = true)
    private LocalDateTime thuClose;

    @Column(name = "fri_open", nullable = true)
    private LocalDateTime friOpen;

    @Column(name = "fri_close", nullable = true)
    private LocalDateTime friClose;

    @Column(name = "sat_open", nullable = true)
    private LocalDateTime satOpen;

    @Column(name = "sat_close", nullable = true)
    private LocalDateTime satClose;

    @Column(name = "sun_open", nullable = true)
    private LocalDateTime sunOpen;

    @Column(name = "sun_close", nullable = true)
    private LocalDateTime sunClose;

    public static StoreOperationTime createStoreOperationTime(
            final long storeId,
            final LocalDateTime monOpen,
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
            final LocalDateTime sunClose
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
