package com.YCtechAcademy.bogosaja.member.domain;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.YCtechAcademy.bogosaja.global.domain.BaseEntity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DynamicUpdate
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity implements UserDetails {

	@Id
	@GeneratedValue(generator = "uuid-hibernate-generator")
	@GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
	private UUID id;

	@Column(name="email", nullable = false, unique = true, columnDefinition = "varchar(50)")
	private String email;

	@Column(name = "password", nullable = false, unique = true, columnDefinition = "varchar(255)")
	private String password;

	@Column(name = "nickname", unique = true, columnDefinition = "varchar(50)")
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, columnDefinition = "varchar(40)")
	private Role role;


	public void update(String password, String nickname) {
		this.password = password;
		this.nickname = nickname;
	}

	@Builder
	public Member(UUID id, String email, String password, String nickname, Role role) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Role role = this.role;
		String authority = role.getAuthority();
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(authority)); // 권한을 simpleGrantedAuthority로 추상화하여 관리함
		return authorities;
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
		return false;
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
