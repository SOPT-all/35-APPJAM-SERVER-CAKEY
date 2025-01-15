package com.cakey.user.service;

//import com.cakey.client.dto.LoginReq;
//import com.cakey.client.dto.KakaoUserInfoRes;
//import com.cakey.client.kakao.api.KakaoSocialService;
//import com.cakey.common.auth.JwtTokenProvider;
//import com.cakey.common.auth.UserAuthentication;
//import com.cakey.jwt.domain.UserRole;
//import com.cakey.jwt.service.TokenService;
//import com.cakey.client.SocialType;
//import com.cakey.user.domain.User;
//import com.cakey.user.dto.AccessTokenGetSuccess;
//import com.cakey.user.dto.LoginSuccessRes;
import com.cakey.common.exception.NotFoundException;
import com.cakey.exception.ErrorCode;
import com.cakey.user.dto.UserInfoDto;
import com.cakey.user.dto.UserInfoRes;
import com.cakey.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final TokenService tokenService;
//    private final KakaoSocialService kakaoSocialService;
//
//    private final static String ACCESS_TOKEN = "accessToken";
//    private final static String REFRESH_TOKEN = "refreshToken";
//
//
//    public LoginSuccessRes create(
//            final String authorizationCode,
//            final LoginReq loginReq
//    ) {
//        return getTokenDto(kakaoSocialService.login(authorizationCode, loginReq));
//    }
//
//    public ResponseCookie accessCookie(LoginSuccessRes loginSuccessRes) {
//        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN, loginSuccessRes.accessToken())
//                .maxAge(60 * 60 * 7 * 24)
//                .path("/")
//                .secure(true)
//                .sameSite("None")
//                .httpOnly(true)
//                .build();
//        return accessCookie;
//    }
//
//    public ResponseCookie refreshCookie(LoginSuccessRes loginSuccessRes) {
//        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN,loginSuccessRes.refreshToken())
//                .maxAge(60 * 60 * 7 * 24)
//                .path("/")
//                .secure(true)
//                .sameSite("None")
//                .httpOnly(true)
//                .build();
//        return refreshCookie;
//    }
//
//    public KakaoUserInfoRes getUserInfo(final String authorizationCode,
//                                   final LoginReq loginReq
//    ) {
//        switch (loginReq.socialType()){
//            case KAKAO:
//                return kakaoSocialService.login(authorizationCode, loginReq);
//            default:
//                throw new RuntimeException("Social type not supported");
//        }
//    }
//
//    public Long createUser(final KakaoUserInfoRes userInfoRes) {
//        User user = User.createUser(
//                userInfoRes.name(),
//                UserRole.USER,
//                userInfoRes.socialType(),
//                userInfoRes.socialId()
//        );
//        return userRepository.save(user).getId();
//    }
//
//    public User getBySocialId(
//            final String socialId,
//            final SocialType socialType
//    )
//    {
//        User user = userRepository.findBySocialTypeAndSocialId(socialType, socialId)
//                .orElseThrow(()-> new RuntimeException("User not found"));
//        return user;
//    }
//
//    public AccessTokenGetSuccess refreshToken(final String refreshToken) {
//        Long userId = jwtTokenProvider.getUserFromJwt(refreshToken);
//        if(!userId.equals(tokenService.findIdByRefreshToken(refreshToken))){
//            throw new RuntimeException("Invalid refresh token");
//        }
//
//        UserAuthentication userAuthentication = new UserAuthentication(userId, null, null);
//        return AccessTokenGetSuccess.of(
//                jwtTokenProvider.issueAccessToken(userAuthentication)
//        );
//    }
//
//    public boolean isExistingUser(
//            final String socialId,
//            final SocialType socialType
//    ) {
//        return userRepository.findBySocialTypeAndSocialId(socialType, socialId).isPresent();
//    }
//
//    public LoginSuccessRes getTokenByUserId(
//            final Long id
//    ) {
//        UserAuthentication userAuthentication = new UserAuthentication(id, null, null);
//        String refreshToken = jwtTokenProvider.issueRefreshToken(userAuthentication);
//        tokenService.saveRefreshToken(id, refreshToken);
//        return LoginSuccessRes.of(
//                jwtTokenProvider.issueAccessToken(userAuthentication),
//                refreshToken
//        );
//    }
//    private LoginSuccessRes getTokenDto(final KakaoUserInfoRes userInfoRes) {
//        if(isExistingUser(userInfoRes.socialId(), userInfoRes.socialType())){
//            return getTokenByUserId(getBySocialId(userInfoRes.socialId(), userInfoRes.socialType()).getId());
//        }
//        else {
//            Long id = createUser(userInfoRes);
//            return getTokenByUserId(id);
//        }
//    }
    public UserInfoRes getUserInfo(final Long userId) {
        final UserInfoDto userInfoDto;
        try {
            userInfoDto = userFacade.findById(userId);
        } catch (NotFoundException e) {
            throw e;
        }
        return UserInfoRes.from(userInfoDto);

    }

}