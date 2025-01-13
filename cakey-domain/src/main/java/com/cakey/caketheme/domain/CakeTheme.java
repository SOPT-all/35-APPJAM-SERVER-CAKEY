package com.cakey.caketheme.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Table(name = "cake_theme")
@Entity
@Getter
public class CakeTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cake_theme_id")
    private Long id;

    @Column(name = "cake_id")
    private long cakeId;

    @Column(name = "theme_name")
    @Enumerated(EnumType.STRING)
    private ThemeName themeName;

    public static CakeTheme createCakeThem(final long cakeId, final ThemeName themeName) {
        return CakeTheme.builder()
                .cakeId(cakeId)
                .themeName(themeName)
                .build();
    }
}
