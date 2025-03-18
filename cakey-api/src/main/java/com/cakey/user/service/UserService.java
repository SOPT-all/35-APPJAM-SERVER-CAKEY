package com.cakey.user.service;


import com.cakey.Constants;
import com.cakey.client.SocialType;
import com.cakey.client.kakao.api.KakaoSocialProvider;
import com.cakey.client.kakao.api.dto.KakaoUserDto;
import com.cakey.client.kakao.api.dto.UserCreateDto;
import com.cakey.exception.AuthExpiredJwtException;
import com.cakey.exception.AuthKakaoException;
import com.cakey.exception.AuthRTCacheException;
import com.cakey.exception.AuthWrongJwtException;
import com.cakey.feign.discord.DiscordFeignProvider;
import com.cakey.feign.discord.exception.CakeyFeignException;
import com.cakey.jwt.auth.JwtProvider;
import com.cakey.jwt.domain.Token;
import com.cakey.jwt.domain.UserRole;
import com.cakey.rescode.ErrorBaseCode;
import com.cakey.user.dto.JwtReissueRes;
import com.cakey.user.dto.LoginSuccessRes;
import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.user.dto.UserInfoDto;
import com.cakey.user.dto.UserInfoRes;
import com.cakey.user.exception.*;
import com.cakey.user.facade.UserFacade;
import com.cakey.user.facade.UserRetriever;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
    private final KakaoSocialProvider kakaoSocialProvider;
    private final JwtProvider jwtProvider;
    private final DiscordFeignProvider discordFeignProvider;

    @Transactional
    public LoginSuccessRes login(
            final String authorizationCode,
            final SocialType socialType,
            final String redirectUri,
            final HttpServletResponse response
    ) {
        ///카카오 유저정보
        final KakaoUserDto kakaoUserInfo;

        if (socialType.equals(SocialType.KAKAO)) {
            try {
                kakaoUserInfo = kakaoSocialProvider.getKakaoUserInfo(authorizationCode, redirectUri);
            } catch (AuthKakaoException e) {
                throw new UserKakaoException(UserErrorCode.KAKAO_LOGIN_FAILED);
            }
        } else {
            throw new UserBadRequestException(UserErrorCode.KAKAO_LOGIN_FAILED);
        }

        ///플랫폼 아이디
        final long platformId = kakaoUserInfo.id();

        ///이미 우리 유저인지 확인해서 userId 뽑기
        final Long userId = userFacade.findUserIdFromSocialTypeAndPlatformId(socialType, platformId);

        final LoginSuccessRes loginSuccessRes;
        if (userId == null) { ///유저 처음 가입
            ///유저생성
            final UserCreateDto userCreateDto = UserCreateDto.of(kakaoUserInfo.kakaoAccount().profile().nickname(),
                    UserRole.USER, socialType, kakaoUserInfo.id(), kakaoUserInfo.kakaoAccount().email());
            final long savedUserId = userFacade.createUser(userCreateDto);

            final Token newToken = jwtProvider.issueToken(savedUserId);

            ///쿠키설정
            setRefreshCookie(newToken.getRefreshToken(), response);

            loginSuccessRes = LoginSuccessRes.of(
                    savedUserId,
                    kakaoUserInfo.kakaoAccount().profile().nickname(),
                    newToken.getAccessToken());
        } else { ///전에 이미 우리 유저
            final Token newToken = jwtProvider.issueToken(userId);

            ///쿠키 설정
            setRefreshCookie(newToken.getRefreshToken(), response);

            loginSuccessRes = LoginSuccessRes.of(
                    userId,
                    kakaoUserInfo.kakaoAccount().profile().nickname(),
                    newToken.getAccessToken());
        }

        ///디스코드 웹훅
        ///todo: 추후에 비동기로 리팩
        final long userCount = userFacade.getUserCount();
        try {
            discordFeignProvider.sendSignUpInfo(loginSuccessRes.userName(), userCount);
        } catch (CakeyFeignException e) {
            throw new UserDiscordFeignException(UserErrorCode.DISCORD_FEIGN_FAILED);
        }
        return loginSuccessRes;
    }

    //jwt 재발급
    public JwtReissueRes jwtReissue(final long userId, final String refreshToken, final HttpServletResponse response) {

        final long userIdFromRT;
        final String newRefreshToken;

        /// 받아온 RT로 userId 뽑아서 검증 + 뽑을 때 RT도 검증
        try {
            userIdFromRT = jwtProvider.getUserIdFromSubject(refreshToken);
        } catch (AuthExpiredJwtException e) {
            throw new UserUnAuthorizedException(ErrorBaseCode.UNAUTHORIZED_RT_EXPIRED);
        } catch (AuthWrongJwtException e) {
            throw new UserUnAuthorizedException(ErrorBaseCode.UNAUTHORIZED_WRONG_RT);
        } catch (Exception e) {
            log.error("---------Jwt ReIssue Error ------- \n {} \n ----------------------------", e.getMessage(), e);
            throw new UserUnAuthorizedException(ErrorBaseCode.INTERNAL_SERVER_ERROR);
        }

        if (userId != userIdFromRT) {
            throw new UserUnAuthorizedException(ErrorBaseCode.UNAUTHORIZED_DIFF_USER_ID);
        }

        /// 받아온 RT와 기존의 RT 비교
        try {
            newRefreshToken = jwtProvider.findRTFromCache(userId);
        } catch (AuthRTCacheException e) {
            throw new UserUnAuthorizedException(UserErrorCode.USER_RT_CACHE_NOT_FOUNT);
        }
        if (!refreshToken.equals(newRefreshToken)) {
            throw new UserUnAuthorizedException(ErrorBaseCode.UNAUTHORIZED_WRONG_RT);
        }

        /// 새로운 AT, RT 생성
        final Token newToken = jwtProvider.issueToken(userId);
        setRefreshCookie(newToken.getRefreshToken(), response);

        return JwtReissueRes.of(newToken.getAccessToken());
    }

    //로그아웃
    public void logout(final long userId, HttpServletResponse response) {
        try {
            userFacade.isExistById(userId);
        } catch (NotFoundBaseException e) {
            throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND);
        }
        deleteRefreshCookie(response);
        jwtProvider.deleteRefreshToken(userId);
    }

    //refreshToken 쿠키 삭제
    public void deleteRefreshCookie(HttpServletResponse response) {
        ResponseCookie refreshCookie = ResponseCookie.from(Constants.REFRESH_TOKEN, "")
                .maxAge(0) /// 쿠키 즉시 삭제
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    //refreshToken 쿠키 세팅
    public void setRefreshCookie(final String refreshToken, final HttpServletResponse response) {
        ResponseCookie refreshCookie = ResponseCookie.from(Constants.REFRESH_TOKEN, refreshToken)
                .maxAge(14 * 24 * 60 * 60 * 1000) /// 리프레시 만료기간 (14일)
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