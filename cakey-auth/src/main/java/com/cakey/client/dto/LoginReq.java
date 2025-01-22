package com.cakey.client.dto;

import com.cakey.client.SocialType;
import jakarta.validation.constraints.NotBlank;

public record LoginReq(
        @NotBlank(message = "SocialType이 잘못되었습니다.") SocialType socialType,
        @NotBlank(message = "redirectUri가 잘못되었습니다.") String redirectUri
) {
}
