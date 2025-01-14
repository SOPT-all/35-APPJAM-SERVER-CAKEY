package com.cakey.user.domain;

import com.cakey.client.SocialType;
import com.cakey.jwt.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Table(name = "users")
@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    public static User createUser(final String name,
                                  final UserRole role,
                                  final SocialType socialType,
                                  final String socialId) {
        return User.builder()
                .name(name)
                .role(role)
                .socialType(socialType)
                .socialId(socialId)
                .build();
    }

}
