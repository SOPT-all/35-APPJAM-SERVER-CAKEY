package com.cakey.user.repository;

import com.cakey.client.SocialType;
import com.cakey.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {


    Optional<User> findById(final Long userId);

    @Query("SELECT u.id FROM User u WHERE u.socialType = :socialType AND u.socialId = :socialId")
    Optional<Long> findIdBySocialTypeAndSocialId(SocialType socialType, long socialId);
}


