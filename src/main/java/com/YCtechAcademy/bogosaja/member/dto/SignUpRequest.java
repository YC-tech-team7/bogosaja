package com.YCtechAcademy.bogosaja.member.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.domain.Roles;

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

	private Roles roles = Roles.ROLE_USER;

	public static Member toEntity(SignUpRequest signUpRequest) {
		return Member.builder()
			.email(signUpRequest.email)
			.password(signUpRequest.password)
			.nickname(signUpRequest.nickname)
			.roles(signUpRequest.roles)
			.build();
	}
}
