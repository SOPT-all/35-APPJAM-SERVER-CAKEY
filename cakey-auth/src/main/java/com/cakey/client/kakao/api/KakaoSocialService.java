package com.cakey.client.kakao.api;

import com.cakey.client.dto.LoginReq;
import com.cakey.client.dto.UserInfoRes;
import com.cakey.client.kakao.api.dto.KakaoAccessTokenRes;
import com.cakey.client.kakao.api.dto.KakaoUserRes;
import com.cakey.client.SocialType;
import com.cakey.jwt.domain.UserRole;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoSocialService implements SocialService {
    private static final String AUTH_CODE = "authorization_code";
    private static final String REDIRECT_URI = "http://localhost:5173/kakao";

    @Value("${kakao.clientId}")
    private String clientId;

    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;

    @Transactional
    @Override
    public UserInfoRes login(
            final String authorizationCode,
            final LoginReq loginReq
    ) {
        String accessToken;
        try {
            // 인가 코드로 Access Token + Refresh Token 받아오기
            accessToken = getOAuth2Authentication(authorizationCode);
        } catch (FeignException e) {
            throw new RuntimeException("authentication code expired");
        }
        String contentType = MediaType.APPLICATION_FORM_URLENCODED.toString();
        // Access Token으로 유저 정보 불러오기
        return getLoginDto(loginReq.socialType(), getUserInfo(accessToken, contentType), UserRole.USER);
    }

    private String getOAuth2Authentication(
            final String authorizationCode
    ) {
        KakaoAccessTokenRes response = kakaoAuthApiClient.getOAuth2AccessToken(
                AUTH_CODE,
                clientId,
                REDIRECT_URI,
                authorizationCode
        );
        return response.accessToken();
    }

    private KakaoUserRes getUserInfo(
            final String accessToken,
            final String contentType
    ) {
        System.out.println("accessToken:" + accessToken);
        return kakaoApiClient.getUserInformation("Bearer " + accessToken, contentType);
    }

    private UserInfoRes getLoginDto(
            final SocialType socialType,
            final KakaoUserRes userResponse,
            final UserRole userRole
            ) {
        return UserInfoRes.of(
                userResponse.id().toString(),
                userResponse.kakaoAccount().profile().nickname(),
                socialType,
                userRole
        );
    }
}
