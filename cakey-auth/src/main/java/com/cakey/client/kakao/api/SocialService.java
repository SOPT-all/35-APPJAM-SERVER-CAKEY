package com.cakey.client.kakao.api;

import com.cakey.client.dto.KakaoUserInfoRes;
import com.cakey.client.dto.LoginReq;

public interface SocialService {
    KakaoUserInfoRes login(final String authorizationToken, final LoginReq loginReq);
}
