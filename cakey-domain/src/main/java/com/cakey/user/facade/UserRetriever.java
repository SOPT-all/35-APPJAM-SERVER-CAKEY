package com.cakey.user.facade;

import com.cakey.common.exception.CakeyDomainException;
import com.cakey.common.exception.NotFoundException;
import com.cakey.exception.ErrorCode;
import com.cakey.user.domain.User;
import com.cakey.user.dto.UserInfoDto;
import com.cakey.user.exception.UserDomainException;
import com.cakey.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRetriever {

    private final UserRepository userRepository;

    public User findById(final Long userId){
        return userRepository.findById(userId)
                .orElseThrow(
                        ()-> new NotFoundException());
    }
}
