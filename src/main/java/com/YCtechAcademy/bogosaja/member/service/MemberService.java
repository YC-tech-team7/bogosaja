package com.YCtechAcademy.bogosaja.member.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YCtechAcademy.bogosaja.auth.JwtTokenProvider;
import com.YCtechAcademy.bogosaja.auth.TokenInfo;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.domain.RefreshToken;
import com.YCtechAcademy.bogosaja.member.dto.DeleteRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import com.YCtechAcademy.bogosaja.member.dto.UpdateRequest;
import com.YCtechAcademy.bogosaja.member.repository.MemberRepository;
import com.YCtechAcademy.bogosaja.member.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;

	private void validateDuplicateMember(SignUpRequest signUpRequest) {
		Optional<Member> member = memberRepository.findByEmail(signUpRequest.getEmail());
		if (!member.isEmpty()) {
			throw new IllegalStateException("이미 가입된 회원입니다!");
		}
		System.out.println("신규회원입니다.");
	}

	@Transactional
	public Member signUp(SignUpRequest signUpRequest) {
		this.validateDuplicateMember(signUpRequest);
		if(!signUpRequest.getPassword1().equals(signUpRequest.getPassword2())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		signUpRequest.setPassword1(passwordEncoder.encode(signUpRequest.getPassword1()));
		return memberRepository.save(SignUpRequest.toEntity(signUpRequest));
	}

	@Transactional
	public TokenInfo signIn(String email, String password) {

		// 1. 받은 email이 실제로 존재하는 회원인지 확인
		Optional<Member> member = memberRepository.findByEmail(email);
		System.out.println("나야ㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑㅑ");
		// 2. 사용자가 없는 경우 AccessDeniedException 발생
		if (member.isEmpty()) {
			throw new AccessDeniedException("존재하지 않는 회원입니다");
		}

		// 3. Login ID/PW 를 기반으로 Authentication 객체 생성
		// 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

		// 4. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
		// authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		// 5. 인증 정보를 기반으로 JWT 토큰 생성
		TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, email);

		// 6. refresh token 업데이트 or 생성
		refreshTokenRepository.findByMember_Email(member.get().getUsername())
			.ifPresentOrElse(
				refreshToken -> {
					refreshToken.setRefreshToken(tokenInfo.refreshToken());
					refreshTokenRepository.save(refreshToken);
				}, () -> refreshTokenRepository.save(new RefreshToken(tokenInfo.refreshToken(), member.get()))
			);

		return tokenInfo;
	}

	@Transactional
	public void delete(DeleteRequest deleteRequest, Member member) {
		if (!passwordEncoder.matches(deleteRequest.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		memberRepository.delete(member); // todo : 데이터삭제? 삭제필드 수정? 중 어떻게
	}
	@Transactional
	public void update(UpdateRequest updateRequest, Member member) {
		// 입력받은 현재 비밀번호와 저장된 비밀번호를 비교
		if (!updateRequest.password1().equals(updateRequest.password2())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		// 새 비밀번호로 업데이트
		member.update(passwordEncoder.encode(updateRequest.password1()), updateRequest.nickname());
	}

	public List<SignUpRequest> findAll(){
		List<Member> memberList = memberRepository.findAll();
		List<SignUpRequest> members = new ArrayList<>();
		for (Member member : memberList){
			members.add(member.toDTO(member));
		}
		return members;
	}
}
