package com.cakey.client.kakao.api;

import com.cakey.Constants;
import com.cakey.client.dto.KakaoUserInfoDto;
import com.cakey.client.kakao.api.dto.KakaoAccessTokenRes;
import com.cakey.client.kakao.api.dto.KakaoUserDto;
import com.cakey.client.SocialType;
import com.cakey.exception.AuthKakaoException;
import com.cakey.jwt.domain.UserRole;
import com.cakey.rescode.ErrorBaseCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoSocialService {
    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;


    @Value("${kakao.clientId}")
    private String clientId;

    public KakaoUserDto getKakaoUserInfo(final String authorizationCode, final String redirectUri) {
        final String kakaoAccessToken;
        try {
            // 인가 코드로 카카오 Access Token 받아오기
            kakaoAccessToken = getOAuth2Authentication(authorizationCode, redirectUri);
        } catch (FeignException e) {
            log.error(e.getMessage(), e);
            throw new AuthKakaoException();
        }

        final String contentType = MediaType.APPLICATION_FORM_URLENCODED.toString();
        // Access Token으로 유저 정보 불러오기
        return getUserInfo(kakaoAccessToken, contentType);
    }

    private String getOAuth2Authentication(
            final String authorizationCode,
            final String redirectUri
    ) {
        final KakaoAccessTokenRes response = kakaoAuthApiClient.getOAuth2AccessToken(
                Constants.AUTHCODE,
                clientId,
                redirectUri,
                authorizationCode
        );
        return response.accessToken();
    }

    // 카카오 유저 정보 받아오기
    private KakaoUserDto getUserInfo(
            final String accessToken,
            final String contentType
    ) {
        return kakaoApiClient.getUserInformation(Constants.BEARER + accessToken, contentType);
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
