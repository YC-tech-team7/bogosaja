package com.YCtechAcademy.bogosaja.member.service;

import java.util.Arrays;
import java.util.Optional;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.YCtechAcademy.bogosaja.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<Member> member = memberRepository.findByEmail(email);
		if(!member.isPresent()){
			throw new UsernameNotFoundException(email + " is not found.");
		}

		return org.springframework.security.core.userdetails.User.builder()
				.username(member.get().getUsername())
				.password(member.get().getPassword())
				.roles(member.get().getAuthorities().toString())
				.build();
	}
}
