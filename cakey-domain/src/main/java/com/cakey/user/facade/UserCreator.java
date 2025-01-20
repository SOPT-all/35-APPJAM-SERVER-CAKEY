package com.cakey.user.facade;

import com.cakey.client.kakao.api.dto.UserCreateDto;
import com.cakey.user.domain.User;
import com.cakey.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserRepository userRepository;

    @Transactional
    public long createUser(final UserCreateDto userCreateDto) {
        final User user = User.createUser(userCreateDto.userName(), userCreateDto.userRole(),
                userCreateDto.socialType(), userCreateDto.socialId(), userCreateDto.socialEmail());
        System.out.println();
        final User savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
