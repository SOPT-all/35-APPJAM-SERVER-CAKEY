package com.cakey.user.repository;

import com.cakey.client.SocialType;
import com.cakey.user.domain.User;
import com.cakey.user.dto.UserInfoDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

//    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findById(final Long userId);
}
