package com.maybank.liangquan.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maybank.liangquan.demo.object.Token;

// Handler for Login
@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

	Optional<Token> findByToken(String token);

	Optional<Token> findByRefreshToken(String refreshToken);
}
