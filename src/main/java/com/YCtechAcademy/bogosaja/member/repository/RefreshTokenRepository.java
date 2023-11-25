package com.YCtechAcademy.bogosaja.member.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.YCtechAcademy.bogosaja.member.domain.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID>{
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	Optional<RefreshToken> findByEmail(String email);
}
