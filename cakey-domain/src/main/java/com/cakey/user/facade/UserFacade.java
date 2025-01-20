package com.cakey.user.facade;

import com.cakey.client.SocialType;
import com.cakey.client.kakao.api.dto.UserCreateDto;
import com.cakey.user.domain.User;
import com.cakey.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserRetriever userRetriever;
    private final UserCreator userCreator;

    public UserInfoDto findById(final Long userId){
        User user = userRetriever.findById(userId);
        return new UserInfoDto(user.getName(), user.getSocialEmail());
    }

    public Long findUserIdFromSocialTypeAndPlatformId(final SocialType socialType, final long platformId) {
        return userRetriever.findUserIdFromSocialTypeAndPlatformId(socialType, platformId);
    }

    public long createUser(final UserCreateDto userCreateDto) {
        return userCreator.createUser(userCreateDto);
    }
}
