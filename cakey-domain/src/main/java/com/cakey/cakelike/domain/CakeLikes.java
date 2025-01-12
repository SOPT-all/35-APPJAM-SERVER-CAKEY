package com.cakey.cakelike.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Getter
@Table(name = "cake_likes")
@Entity
public class CakeLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cake_like_id")
    private Long id;

    @Column(name = "çake_id", nullable = false)
    private long cakeId;

    @Column(name = "user_id", nullable = false)
    private long userId;
    
    public static CakeLikes createCakeLikes(final long cakeId, final long userId) {
        return CakeLikes.builder().
                cakeId(cakeId)
                .userId(userId)
                .build();
    }
}
