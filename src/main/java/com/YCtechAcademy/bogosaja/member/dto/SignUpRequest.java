package com.YCtechAcademy.bogosaja.member.dto;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.domain.Role;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

	private String email;

	private String password1;
	private String password2;

	private String nickname;

	private Role role = Role.USER;

	public static Member toEntity(SignUpRequest signUpRequest) {
		return Member.builder()
				.email(signUpRequest.email)
				.password(signUpRequest.password1)
				.nickname(signUpRequest.nickname)
				.role(signUpRequest.role)
				.build();
	}

}
