package com.cakey.client.kakao.api;

import com.cakey.client.dto.LoginReq;
import com.cakey.client.dto.UserInfoRes;

public interface SocialService {
    UserInfoRes login(final String authorizationToken, final LoginReq loginReq);
}
