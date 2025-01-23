package com.cakey.user.service;


import com.cakey.client.SocialType;
import com.cakey.client.dto.LoginReq;
import com.cakey.client.kakao.api.KakaoSocialService;
import com.cakey.client.kakao.api.dto.KakaoUserDto;
import com.cakey.client.kakao.api.dto.UserCreateDto;
import com.cakey.exception.AuthKakaoException;
import com.cakey.jwt.auth.JwtProvider;
import com.cakey.jwt.domain.Token;
import com.cakey.jwt.domain.UserRole;
import com.cakey.rescode.ErrorCode;
import com.cakey.user.dto.LoginSuccessRes;
import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.user.dto.UserInfoDto;
import com.cakey.user.dto.UserInfoRes;
import com.cakey.user.exception.UserBadRequestException;
import com.cakey.user.exception.UserErrorCode;
import com.cakey.user.exception.UserKakaoException;
import com.cakey.user.exception.UserNotFoundException;
import com.cakey.user.facade.UserFacade;
import com.cakey.user.facade.UserRetriever;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cloud.openfeign.aot.FeignChildContextInitializer;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
    private final KakaoSocialService kakaoSocialService;

    private final static String ACCESS_TOKEN = "accessToken";
    private final static String REFRESH_TOKEN = "refreshToken";
    private final JwtProvider jwtProvider;
    private final UserRetriever userRetriever;
    private final FeignChildContextInitializer feignChildContextInitializer;


    public LoginSuccessRes login(
            final String authorizationCode,
            final SocialType socialType,
            final String redirectUri,
            final HttpServletResponse response
    ) {
        //카카오 유저정보
        final KakaoUserDto kakaoUserInfo;

        if (socialType.equals(SocialType.KAKAO)) {
            try {
                kakaoUserInfo = kakaoSocialService.getKakaoUserInfo(authorizationCode, redirectUri);
            } catch (AuthKakaoException e) {
                throw new UserKakaoException(UserErrorCode.KAKAO_LOGIN_FAILED);
            }
        } else {
            throw new UserBadRequestException(UserErrorCode.KAKAO_LOGIN_FAILED);
        }


        //플랫폼 아이디
        final long platformId = kakaoUserInfo.id();

        //이미 우리 유저인지 확인해서 userId 뽑기
        final Long userId = userFacade.findUserIdFromSocialTypeAndPlatformId(socialType, platformId);

        if (userId == null) { //유저 처음 가입
            //유저생성
            final UserCreateDto userCreateDto = UserCreateDto.of(kakaoUserInfo.kakaoAccount().profile().nickname(),
                    UserRole.USER, socialType, kakaoUserInfo.id(), kakaoUserInfo.kakaoAccount().email());
            final long savedUserId = userFacade.createUser(userCreateDto);

            final Token newToken = jwtProvider.issueToken(savedUserId);

            //쿠키설정
            setAccessCookie(newToken.getAccessToken(), response);
            setRefreshCookie(newToken.getRefreshToken(), response);

            return LoginSuccessRes.of(
                    savedUserId,
                    kakaoUserInfo.kakaoAccount().profile().nickname());
        } else { //전에 이미 우리 유저

            //리프레시 토큰 캐시 삭제
            jwtProvider.deleteRefreshToken(userId);

            final Token newToken = jwtProvider.issueToken(userId);

            //쿠키 설정
            setAccessCookie(newToken.getAccessToken(), response);
            setRefreshCookie(newToken.getRefreshToken(), response);

            return LoginSuccessRes.of(
                    userId,
                    kakaoUserInfo.kakaoAccount().profile().nickname());
        }
    }

//    //jwt 재발급
//    public LoginSuccessRes jwtReissue(final String refreshToken) {
    ///refresh token 검증
//
//    }

    //로그아웃
    public void logout(final long userId, final HttpServletResponse response) {
        try {
            userFacade.isExistById(userId);
        } catch (NotFoundBaseException e) {
            throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND);
        }
        deleteAccessCookie(response);
        deleteRefreshCookie(response);
        jwtProvider.deleteRefreshToken(userId);
    }

    //accessToken 쿠키 삭제
    public void deleteAccessCookie(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN, "")
                .maxAge(0) // 쿠키 즉시 삭제
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", accessCookie.toString());
    }

    //refreshToken 쿠키 삭제
    public void deleteRefreshCookie(HttpServletResponse response) {
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN, "")
                .maxAge(0) // 쿠키 즉시 삭제
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    public void setAccessCookie(final String accessToken, final HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN, accessToken)
                .maxAge(30 * 24 * 60 * 60 * 1000L) /// 1달
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", accessCookie.toString());
    }

    public void setRefreshCookie(final String refreshToken, final HttpServletResponse response) {
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .maxAge(30 * 24 * 60 * 60 * 1000L) /// 1달
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    public UserInfoRes getUserInfo(final long userId) {
        final UserInfoDto userInfoDto;
        try {
            userInfoDto = userFacade.findUserInfoById(userId);
        } catch (NotFoundBaseException e) {
            throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND);
        }
        return UserInfoRes.from(userInfoDto);

    }

}