package com.cakey.client.dto;

import com.cakey.client.SocialType;
import jakarta.validation.constraints.NotBlank;

public record LoginReq(
         SocialType socialType,
         String redirectUri
) {
}
