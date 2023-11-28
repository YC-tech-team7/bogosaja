package com.YCtechAcademy.bogosaja.member.domain;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.YCtechAcademy.bogosaja.global.domain.BaseTimeEntity;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DynamicUpdate
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseTimeEntity implements UserDetails {

	@Id
	@Column(name ="member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="email", nullable = false, unique = true, columnDefinition = "varchar(50)")
	private String email;

	@Column(name = "password", nullable = false, unique = true, columnDefinition = "varchar(255)")
	private String password;

	@Column(name = "nickname", unique = true, columnDefinition = "varchar(50)")
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, columnDefinition = "varchar(40)")
	private Role role;

	@Column(name = "provider", columnDefinition = "varchar(40)")
	private String provider; //어떤 OAuth인지(google, naver 등)

	@Column(name = "provider_id", columnDefinition = "varchar(40)")
	private String providerId; // 해당 OAuth 의 key(id)

	// todo 다이나믹 업데이트  반영
	// public void update(String password, String nickname) {
	// 	this.password = password;
	// 	this.nickname = nickname;
	// }
	public void update(String nickname) {
		this.nickname = nickname;
	}
	public void resetPassword(String password) {
		this.password = password;
	}

	@Builder
	public Member(String email, String password, String nickname, Role role, String provider, String providerId) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> roles = new ArrayList<>();
		String authority = this.role.getAuthority();
		roles.add(new SimpleGrantedAuthority(authority));
		return roles;
	}

	@Override
	public String getUsername() {
		return email; //todo 추후검토
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static SignUpRequest toDTO(Member member) {
		return SignUpRequest.builder()
				.email(member.email)
				.password1(member.password)
				.password2(member.password)
				.nickname(member.nickname)
				.role(member.role)
				.build();
	}
}
