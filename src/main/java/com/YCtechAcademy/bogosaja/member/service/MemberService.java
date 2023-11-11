package com.YCtechAcademy.bogosaja.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import com.YCtechAcademy.bogosaja.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;


	public Member signUp(SignUpRequest signUpRequest) {
		signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		return memberRepository.save(SignUpRequest.toEntity(signUpRequest));
	}

}
