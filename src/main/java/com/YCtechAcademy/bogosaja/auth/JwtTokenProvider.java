package com.YCtechAcademy.bogosaja.auth;

import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import com.YCtechAcademy.bogosaja.member.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.YCtechAcademy.bogosaja.auth.domain.JwtCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Slf4j
@Component
public class JwtTokenProvider {
	private final Key key;
	private final UserDetailsService UserDetailsService;
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UserDetailsService UserDetailsService) {
		byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes());
		this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
		this.UserDetailsService = UserDetailsService;
	}

	public TokenInfo generateToken(Authentication authentication, String email) {
		String authorities = authentication.getAuthorities().stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority())
				.collect(Collectors.joining(","));

		LocalDateTime now = LocalDateTime.now();

		String accessToken = Jwts.builder()
			.setSubject(authentication.getName())
			.claim("auth", authorities)
			.claim("email", email)
			.setExpiration(Date.from(now
				.plusMinutes(30)
				.atZone(ZoneId.systemDefault()).toInstant()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		String refreshToken = Jwts.builder()
			.setExpiration(Date.from(now
				.plusDays(14)
				.atZone(ZoneId.systemDefault()).toInstant()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		return new TokenInfo(accessToken, refreshToken);
	}

	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public Authentication getAuthentication(String accessToken) {
		log.info("현재 누가 로그인 되어있나 확인 중");
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) {
			throw new AccessDeniedException("권한 정보가 없는 토큰입니다.");
		}

		UserDetails userDetails = UserDetailsService.loadUserByUsername(claims.getSubject());
		log.info("get:" +userDetails.getAuthorities());

		return new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
	}

	// 토큰 복호화 메서드
	public Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}


	// 토큰 검증
	public JwtCode validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return JwtCode.ACCESS;
		} catch (ExpiredJwtException e) {
			// 기한 만료
			return JwtCode.EXPIRED;
		} catch (Exception e) {
			// parsing 에러
			return JwtCode.DENIED;
		}
	}

	// 토큰을 쿠키로 변환하는 기능
	public javax.servlet.http.Cookie generateCookie(String from, String token) {
		javax.servlet.http.Cookie cookie = new Cookie(from, token);

		cookie.setPath("/");
		cookie.setHttpOnly(true); // XSS 공격을 막기 위한 설정
		cookie.setSecure(false);

		return cookie;
	}

}
