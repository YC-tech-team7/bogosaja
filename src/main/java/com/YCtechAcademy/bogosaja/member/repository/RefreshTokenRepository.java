package com.YCtechAcademy.bogosaja.member.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.YCtechAcademy.bogosaja.member.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID>{
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	Optional<RefreshToken> findByMember_Email(String email);
}
