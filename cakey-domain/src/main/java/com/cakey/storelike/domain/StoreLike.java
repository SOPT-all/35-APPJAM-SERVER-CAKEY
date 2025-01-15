package com.cakey.storelike.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Getter
@Table(name = "store_likes")
@Entity
public class StoreLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_like_id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private long storeId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    public static StoreLike createStoreLike(final long storeId, final long userId) {
        return StoreLike.builder()
                .storeId(storeId)
                .userId(userId)
                .build();
    }
}
