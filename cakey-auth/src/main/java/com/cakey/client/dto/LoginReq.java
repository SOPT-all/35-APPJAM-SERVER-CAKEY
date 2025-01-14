package com.cakey.client.dto;

import com.cakey.client.SocialType;

public record LoginReq(
        SocialType socialType
) {
}
