package com.cakey.user.facade;

import com.cakey.user.domain.User;
import com.cakey.user.dto.UserInfoDto;
import jakarta.persistence.Column;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserRetriever userRetriever;

    public UserInfoDto findById(final Long userId){
        User user = userRetriever.findById(userId);
        return new UserInfoDto(user.getName(), user.getSocialEmail());
    }
}
