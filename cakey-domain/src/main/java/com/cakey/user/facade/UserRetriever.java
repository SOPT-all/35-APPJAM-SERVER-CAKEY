package com.cakey.user.facade;

import com.cakey.client.SocialType;
import com.cakey.common.exception.NotFoundBaseException;
import com.cakey.user.domain.User;
import com.cakey.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRetriever {

    private final UserRepository userRepository;

    public User findById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        NotFoundBaseException::new);
    }

    public Long findUserIdFromSocialTypeAndPlatformId(final SocialType socialType, final long platformId) {
        return userRepository.findIdBySocialTypeAndSocialId(socialType, platformId).orElse(null);
    }

    //유저 있는지 확인
    public boolean isExistById(final long userId) {
        return userRepository.existsById(userId);
    }


}
