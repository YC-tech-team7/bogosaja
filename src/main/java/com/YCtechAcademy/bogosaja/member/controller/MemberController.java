package com.YCtechAcademy.bogosaja.member.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.YCtechAcademy.bogosaja.auth.JwtTokenProvider;
import com.YCtechAcademy.bogosaja.auth.TokenInfo;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.dto.DeleteRequest;
import com.YCtechAcademy.bogosaja.member.dto.ResetRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignInRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import com.YCtechAcademy.bogosaja.member.dto.UpdateRequest;
import com.YCtechAcademy.bogosaja.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

   private final MemberService memberService;
   private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/auth/signUp")
    // 회원가입 화면
    public String signUpForm(){
        return "member/signUpForm";
    }

    @PostMapping("/auth/signUp")
    public String signUp(@ModelAttribute SignUpRequest signUpRequest) {
        memberService.signUp(signUpRequest);
        // 회원가입 완료 페이지
        return "redirect:/";
    }

    @GetMapping("/auth/signIn")
    public String signInForm(){
        // 로그인 화면
        return "member/signInForm";
    }

    @PostMapping("/auth/signIn")
    public String signIn(@ModelAttribute SignInRequest signInRequest, HttpServletResponse response) {
        log.info("email={}, password={}",signInRequest.email(), signInRequest.password());
        TokenInfo tokenInfo = memberService.signIn(signInRequest.email(), signInRequest.password());

        response.addCookie(jwtTokenProvider.generateCookie("refreshToken", tokenInfo.refreshToken()));
        response.addCookie(jwtTokenProvider.generateCookie("accessToken", tokenInfo.accessToken()));

		return "redirect:/";
	}

    @PostMapping("/auth/signOut")
    public String signOut(HttpServletResponse response) {
        memberService.signOut();

        Cookie accessToken = jwtTokenProvider.generateCookie("accessToken", null);
        Cookie refreshToken = jwtTokenProvider.generateCookie("refreshToken", null);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteForm() {
        return "member/deleteForm";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute DeleteRequest deleteRequest, @AuthenticationPrincipal Member member, HttpServletResponse response) {
        memberService.delete(deleteRequest, member);
        Cookie accessToken = jwtTokenProvider.generateCookie("accessToken", null);
        Cookie refreshToken = jwtTokenProvider.generateCookie("refreshToken", null);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        return "redirect:/";
    }

    //todo 구글 회원은 회원탈퇴 따로 , 업데이트 불가

    @GetMapping("/update")
    public String updateForm(){
       return "member/updateUserInfoForm";
    }

    @GetMapping("/reset")
    public String resetForm(){
        return "member/resetPasswordForm";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute UpdateRequest updateRequest, @AuthenticationPrincipal Member member) {
       memberService.update(updateRequest, member);
       return "redirect:/";
    }

    @PostMapping("/reset")
    public String update(@ModelAttribute ResetRequest resetRequest, @AuthenticationPrincipal Member member) {
        memberService.resetPw(resetRequest, member);
        return "redirect:/";
    }

    @GetMapping("/db")
    public String db(Model model){
        List<SignUpRequest> memberList = memberService.findAll();
        model.addAttribute("memberList", memberList);
        return "member/members";
    }

}

