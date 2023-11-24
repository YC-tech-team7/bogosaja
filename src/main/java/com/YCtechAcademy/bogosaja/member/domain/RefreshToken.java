package com.YCtechAcademy.bogosaja.member.domain;

import java.util.UUID;

import javax.persistence.*;

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

	@OneToOne
	@JoinColumn(name = "member_id", unique = true)
	private Member member;

	public RefreshToken(String refreshToken, Member member) {
		this.refreshToken = refreshToken;
		this.member = member;
	}

	public RefreshToken() {
	}
}
