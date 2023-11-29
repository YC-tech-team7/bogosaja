package com.YCtechAcademy.bogosaja.member.service;

import java.util.Optional;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.YCtechAcademy.bogosaja.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<Member> member = memberRepository.findByEmail(email);
		if (member.isEmpty()) {
			throw new UsernameNotFoundException("not found user");
		}
		return member.get();
	}
}
