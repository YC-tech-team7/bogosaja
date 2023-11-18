package com.YCtechAcademy.bogosaja.member.dto;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

	private String email;

	private String password;

	private String nickname;

	private Role role = Role.ROLE_USER;

	public static Member toEntity(SignUpRequest signUpRequest) {
		return Member.builder()
			.email(signUpRequest.email)
			.password(signUpRequest.password)
			.nickname(signUpRequest.nickname)
			.role(signUpRequest.role)
			.build();
	}
}
