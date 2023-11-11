package com.YCtechAcademy.bogosaja.member.domain;

public enum Roles {
	ROLE_USER("ROLE_USER"),
	ROLE_ANONYMOUS("ROLE_ANONYMOUS"),
	ROLE_ADMIN("ROLE_ADMIN");

	String roles;

	Roles(String roles) {
		this.roles = roles;
	}

	public String value() {
		return roles;
	}
}

