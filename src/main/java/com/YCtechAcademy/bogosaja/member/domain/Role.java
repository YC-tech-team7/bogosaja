package com.YCtechAcademy.bogosaja.member.domain;

import lombok.Getter;

public enum Role {
	ROLE_USER("ROLE_USER"),
	ROLE_ANONYMOUS("ROLE_ANONYMOUS"),
	ROLE_ADMIN("ROLE_ADMIN");

	@Getter String role;

	Role(String role) {
		this.role = role;
	}
}

