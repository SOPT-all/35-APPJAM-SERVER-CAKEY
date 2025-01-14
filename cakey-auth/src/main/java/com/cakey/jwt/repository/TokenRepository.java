//package com.cakey.jwt.repository;
//
//import com.cakey.jwt.domain.Token;
//import java.util.Optional;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface TokenRepository extends CrudRepository<Token, Long> {
//    Optional<Token> findByRefreshToken(final String refreshToken);
//    Optional<Token> findById(final Long id);
//}
