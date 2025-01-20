package com.cakey.client.kakao.api;

import com.cakey.client.dto.KakaoUserInfoDto;
import com.cakey.client.kakao.api.dto.KakaoAccessTokenRes;
import com.cakey.client.kakao.api.dto.KakaoUserDto;
import com.cakey.client.SocialType;
import com.cakey.jwt.domain.UserRole;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoSocialService {
    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;

    private static final String AUTH_CODE = "authorization_code";
    private static final String REDIRECT_URI = "http://localhost:5173/kakao/redirection";

    @Value("${kakao.clientId}")
    private String clientId;

    public KakaoUserDto getKakaoUserInfo(final String authorizationCode) {
        String kakaoAccessToken;
        try {
            // 인가 코드로 카카오 Access Token 받아오기
            kakaoAccessToken = getOAuth2Authentication(authorizationCode);
        } catch (FeignException e) {
            throw new RuntimeException("authentication code expired");
        }

        String contentType = MediaType.APPLICATION_FORM_URLENCODED.toString();
        // Access Token으로 유저 정보 불러오기
        return getUserInfo(kakaoAccessToken, contentType);
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

    // 카카오 유저 정보 받아오기
    private KakaoUserDto getUserInfo(
            final String accessToken,
            final String contentType
    ) {
        return kakaoApiClient.getUserInformation("Bearer " + accessToken, contentType);
    }

    private KakaoUserInfoDto getLoginDto(
            final SocialType socialType,
            final KakaoUserDto userResponse,
            final UserRole userRole
            ) {
        return KakaoUserInfoDto.of(
                userResponse.id().toString(),
                userResponse.kakaoAccount().profile().nickname(),
                socialType,
                userRole,
                userResponse.kakaoAccount().email()
        );
    }
}
