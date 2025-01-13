package com.cakey.size.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Table(name = "sizes")
@Entity
@Getter
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private Long id;

    @Column(name = "store_id")
    private long storeId;

    @Column(name = "size_name")
    private String sizeName;

    @Column(name = "price")
    private int price;

    public static Size createSize(final long storeId,
                                  final String sizeName,
                                  final int price) {
        return Size.builder()
                .storeId(storeId)
                .sizeName(sizeName)
                .price(price)
                .build();
    }
}
