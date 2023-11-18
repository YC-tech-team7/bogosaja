package com.YCtechAcademy.bogosaja.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.YCtechAcademy.bogosaja.auth.AuthenticationSuccessHandler;
import com.YCtechAcademy.bogosaja.auth.JwtAuthFilter;
import com.YCtechAcademy.bogosaja.member.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final CustomOAuth2UserService oAuth2UserService;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// 정적 자원에 대해서 Security를 적용하지 않음으로 설정
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic().disable()
				.csrf().disable()
				.formLogin().disable()
				.headers().frameOptions().sameOrigin()
				.and()
				.authorizeRequests()
				.antMatchers("/", "/members/auth/**","/mk",
						"/h2-console/**")
					.permitAll()
				.anyRequest().authenticated()

			.and()
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.oauth2Login()
			.loginPage("/members/auth/signIn")
			.defaultSuccessUrl("/")
			.successHandler(authenticationSuccessHandler)
			.userInfoEndpoint()
			.userService(oAuth2UserService); //구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함
		return http.build();

	}
}

