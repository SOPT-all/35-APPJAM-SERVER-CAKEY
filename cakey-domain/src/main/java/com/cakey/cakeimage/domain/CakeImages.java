package com.cakey.cakeimage.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA에서만 사용가능하도록
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 외부에서 직접 모든 필드 받는 생성자 호출X
@Builder(access = AccessLevel.PRIVATE) //외부에서 builder 사용 X
@Table(name = "cake_images")
@Entity
@Getter
public class CakeImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cake_image_id")
    private Long id;

    @Column(name = "cake_id", nullable = false)
    private long cakeId;

    @Column(name = "store_id", nullable = false)
    private long storeId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_main_image", nullable = false)
    private boolean isMainImage;

    public static CakeImages createCakeImages(final long cakeId,
                                              final long storeId,
                                              final String imageUrl,
                                              final boolean isMainImage) {
        return CakeImages.builder()
                .cakeId(cakeId)
                .storeId(storeId)
                .imageUrl(imageUrl)
                .isMainImage(isMainImage)
                .build();
    }
}
