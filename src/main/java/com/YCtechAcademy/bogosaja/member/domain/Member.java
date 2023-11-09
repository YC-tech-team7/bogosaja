package com.YCtechAcademy.bogosaja.member.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.YCtechAcademy.bogosaja.global.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity implements UserDetails {

	@Id
	@GeneratedValue(generator = "uuid-hibernate-generator")
	@GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id;

	@Column(name="email", nullable = false, unique = true, columnDefinition = "varchar(50)")
	private String email;

	@Column(name = "password", nullable = false, unique = true, columnDefinition = "varchar(255)")
	private String password;

	@Column(name = "nickname", unique = true, columnDefinition = "varchar(50)")
	private String nickname;

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private Set<String> roles = new HashSet<>();

	@Builder
	public Member(UUID id, String email, String password, String nickname, Set<String> roles) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
		// return this.roles.stream()
		// 	.map(SimpleGrantedAuthority::new)
		// 	.collect(Collectors.toSet());
	}

	@Override
	public String getUsername() {
		return email; //todo 추후검토
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
}
