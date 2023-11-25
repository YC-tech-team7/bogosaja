package com.YCtechAcademy.bogosaja.member.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshToken {

	@Id
	@GeneratedValue(generator = "uuid-hibernate-generator")
	@GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
	private UUID id;

	private String refreshToken;

	@Column(nullable = false, unique = true)
	private String email;

	public RefreshToken(String refreshToken, String email) {
		this.refreshToken = refreshToken;
		this.email = email;
	}

	public RefreshToken() {
	}
}
