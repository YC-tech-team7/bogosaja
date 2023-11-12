package com.YCtechAcademy.bogosaja.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.YCtechAcademy.bogosaja.auth.AuthenticationSuccessHandler;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	//
	// private final JwtAuthFilter jwtAuthFilter;
	// private final CustomOAuth2UserService oAuth2UserService;
	// private final AuthenticationSuccessHandler authenticationSuccessHandler;


	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// 정적 자원에 대해서 Security를 적용하지 않음으로 설정
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	// @Bean
	// public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	// 	http.httpBasic().disable()
	// 			.csrf().disable()
	// 			.formLogin().disable()
	// 			.headers().frameOptions().sameOrigin()
	// 			.and()
	// 			.authorizeRequests()
	// 			.antMatchers("/", "members/login", "members/signUp", "/members/auth/**",
	// 					"/h2-console/**")
	// 				.permitAll()
	// 			.anyRequest().authenticated()
	// 		.and()
	// 		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	// 		.oauth2Login()
	// 		.loginPage("/members/login")
	// 		.defaultSuccessUrl("/")
	// 		.userInfoEndpoint()
	// 		.userService(oAuth2UserService);
	// 	return http.build();
	//
	// }
}

