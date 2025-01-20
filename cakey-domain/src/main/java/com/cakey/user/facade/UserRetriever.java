package com.cakey.user.facade;

import com.cakey.client.SocialType;
import com.cakey.common.exception.NotFoundException;
import com.cakey.user.domain.User;
import com.cakey.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRetriever {

    private final UserRepository userRepository;

    public User findById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        ()-> new NotFoundBaseException());
    }

    public Long findUserIdFromSocialTypeAndPlatformId(final SocialType socialType, final long platformId) {
        return userRepository.findIdBySocialTypeAndSocialId(socialType, platformId).orElse(null);
    }


}
