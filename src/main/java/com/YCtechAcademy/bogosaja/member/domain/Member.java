package com.YCtechAcademy.bogosaja.member.domain;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.YCtechAcademy.bogosaja.global.domain.BaseEntity2;
import com.YCtechAcademy.bogosaja.item.domain.Item;
import com.YCtechAcademy.bogosaja.item.domain.LikeList;
import com.YCtechAcademy.bogosaja.item.repository.LikeListRepository;
import com.YCtechAcademy.bogosaja.member.dto.MemberDto;
import lombok.Getter;
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
@Getter
@Table(name = "member")
public class Member extends BaseEntity2 implements UserDetails {

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

	@Column(name = "provider", columnDefinition = "varchar(40)")
	private String provider; //어떤 OAuth인지(google, naver 등)

	@Column(name = "provider_id", columnDefinition = "varchar(40)")
	private String providerId; // 해당 OAuth 의 key(id)

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

	public MemberDto toDto(Member member, LikeListRepository likeListRepository) {

		Set<String> likedItemNames = likeListRepository.findByMember(member).stream()
				.map(likeList -> likeList.getItem().getItemNm())
				.collect(Collectors.toSet());

		return MemberDto.builder()
				.email(member.email)
				.nickname(member.nickname)
				.role(member.role)
				.likedItemNames(likedItemNames)
				.build();
	}
}
