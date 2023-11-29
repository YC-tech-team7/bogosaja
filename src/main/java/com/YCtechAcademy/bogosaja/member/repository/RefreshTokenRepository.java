package com.YCtechAcademy.bogosaja.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YCtechAcademy.bogosaja.member.domain.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	Optional<RefreshToken> findByEmail(String email);
}
