package com.cakey.cake.domain;

import jakarta.persistence.*;
import lombok.*;
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Table(name = "cakes")
@Entity
@Getter
public class Cake {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "cake_id")
        private Long id;

        @Column(name = "store_name", nullable = false)
        private long storeId;

        @Column(name = "for_day_category", nullable = false)
        private DayCategory dayCategory;

        public static Cake createCake(final long storeId, final DayCategory dayCategory) {
            return Cake.builder()
                    .storeId(storeId)
                    .dayCategory(dayCategory)
                    .build();
        }
}
