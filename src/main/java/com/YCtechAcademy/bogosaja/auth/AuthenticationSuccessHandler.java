package com.YCtechAcademy.bogosaja.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.YCtechAcademy.bogosaja.member.domain.RefreshToken;
import com.YCtechAcademy.bogosaja.member.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		// OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져온다.
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		log.info("getAttributes : {}", oAuth2User.getAttributes());

		// 사용자 이메일을 가져온다.
		String email = oAuth2User.getAttribute("email");

		TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, email);

		// Refresh token 있는지 확인 업데이트 or 생성
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(email);

		if (refreshToken.isPresent()) {
			// 존재한다면
			refreshToken.get().setRefreshToken(tokenInfo.refreshToken());
		} else {
			refreshTokenRepository.save(new RefreshToken(tokenInfo.refreshToken(), email));
		}

		// 쿠키를 생성하여 응답한다.
		response.addCookie(jwtTokenProvider.generateCookie("refreshToken", tokenInfo.refreshToken()));
		response.addCookie(jwtTokenProvider.generateCookie("accessToken", tokenInfo.accessToken()));
		// 홈으로 리다이렉트 시킨다.
		getRedirectStrategy().sendRedirect(request, response, "/");
	}
}