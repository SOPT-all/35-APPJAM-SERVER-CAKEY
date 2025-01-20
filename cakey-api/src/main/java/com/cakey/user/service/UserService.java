package com.cakey.user.service;


import com.cakey.client.SocialType;
import com.cakey.client.dto.LoginReq;
import com.cakey.client.kakao.api.KakaoSocialService;
import com.cakey.client.kakao.api.dto.KakaoUserDto;
import com.cakey.client.kakao.api.dto.UserCreateDto;
import com.cakey.jwt.auth.JwtProvider;
import com.cakey.common.exception.NotFoundException;
import com.cakey.exception.CakeyApiException;
import com.cakey.jwt.domain.Token;
import com.cakey.jwt.domain.UserRole;
import com.cakey.user.domain.User;
import com.cakey.user.dto.LoginSuccessRes;
import com.cakey.user.dto.UserInfoDto;
import com.cakey.user.dto.UserInfoRes;
import com.cakey.user.facade.UserFacade;
import com.cakey.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
    private final KakaoSocialService kakaoSocialService;

    private final static String ACCESS_TOKEN = "accessToken";
    private final static String REFRESH_TOKEN = "refreshToken";
    private final JwtProvider jwtProvider;


    public LoginSuccessRes login(
            final String authorizationCode,
            final LoginReq loginReq
    ) {
        //카카오 유저정보
        final KakaoUserDto kakaoUserInfo;

        if (loginReq.socialType().equals(SocialType.KAKAO)) {
            kakaoUserInfo = kakaoSocialService.getKakaoUserInfo(authorizationCode);
        } else {
            throw new CakeyApiException(); //todo: exception 변경
        }

        //플랫폼 타입
        final SocialType socialType = loginReq.socialType();

        //플랫폼 아이디
        final long platformId = kakaoUserInfo.id();

        //이미 우리 유저인지 확인해서 userId 뽑기
        final Long userId = userFacade.findUserIdFromSocialTypeAndPlatformId(socialType, platformId);

        if (userId == null) { //유저 처음 가입
            //유저생성
            final UserCreateDto userCreateDto = UserCreateDto.of(kakaoUserInfo.kakaoAccount().profile().nickname(),
                    UserRole.USER, loginReq.socialType(), kakaoUserInfo.id(), kakaoUserInfo.kakaoAccount().email());
            final long savedUserId = userFacade.createUser(userCreateDto);

            final Token newToken = jwtProvider.issueToken(savedUserId);

            //리프레시토큰 캐시에 저장
            updateRefreshToken(newToken.getRefreshToken(), savedUserId);

            //쿠키설정
            setAccessCookie(newToken.getAccessToken());
            setRefreshCookie(newToken.getRefreshToken());

            return LoginSuccessRes.of(
                    savedUserId,
                    kakaoUserInfo.kakaoAccount().profile().nickname());
        } else { //전에 이미 우리 유저

            //리프레시 토큰 캐시 삭제
            deleteRefreshToken(userId);

            final Token newToken = jwtProvider.issueToken(userId);

            //리프레시토큰 캐시 저장
            updateRefreshToken(newToken.getRefreshToken(), userId);

            //쿠키 설정
            setAccessCookie(newToken.getAccessToken());
            setRefreshCookie(newToken.getRefreshToken());

            return LoginSuccessRes.of(
                    userId,
                    kakaoUserInfo.kakaoAccount().profile().nickname());
        }
    }

    @CacheEvict(value = "refresh")
    public void deleteRefreshToken(final long userId) { }


    private void updateRefreshToken(final String refreshToken, final long userId) {
        jwtProvider.generateRefreshToken(userId);
    }

    public ResponseCookie setAccessCookie(final String accessToken ) {
        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN, accessToken)
                .maxAge(60 * 60 * 7 * 24) //액세스 토큰 기간
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        return accessCookie;
    }

    public ResponseCookie setRefreshCookie(final String refreshToken) {
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .maxAge(60 * 60 * 7 * 24) //리프레시 토큰 기간
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        return refreshCookie;
    }




    public UserInfoRes getUserInfo(final Long userId) {
        final UserInfoDto userInfoDto;
        try {
            userInfoDto = userFacade.findById(userId);
        } catch (NotFoundException e) {
            //todo: 추후 구체적인 예외처리
            throw e;
        }
        return UserInfoRes.from(userInfoDto);

    }

}