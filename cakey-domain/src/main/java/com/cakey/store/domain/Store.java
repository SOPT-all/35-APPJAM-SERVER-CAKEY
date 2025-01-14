package com.cakey.store.domain;

import com.cakey.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Getter
@Table(name = "stores")
@Entity
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "store_name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "taste", nullable = false)
    private String taste;

    @Column(name = "open_kakao_url", nullable = true)
    private String openKakaoUrl;

    @Column(name = "station", nullable = false)
    @Enumerated(EnumType.STRING)
    private Station station;

    public static Store createStore(final String name,
                                    final String address,
                                    final String phone,
                                    final double latitude,
                                    final double longitude,
                                    final String taste,
                                    final String openKakaoUrl,
                                    final Station station) {
        return Store.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .latitude(latitude)
                .longitude(longitude)
                .taste(taste)
                .openKakaoUrl(openKakaoUrl)
                .station(station)
                .build();
    }
}
