package com.YCtechAcademy.bogosaja.member.controller;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.YCtechAcademy.bogosaja.member.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.YCtechAcademy.bogosaja.auth.TokenInfo;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

   private final MemberService memberService;

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
        log.info("7");
        Cookie accessToken = generateCookie("accessToken", tokenInfo.accessToken());
        Cookie refreshToken = generateCookie("refreshToken", tokenInfo.refreshToken());

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        return "redirect:/";
    }

    private Cookie generateCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS를 사용하는 경우 true로 변경
        cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (초 단위)
        cookie.setPath("/");
        return cookie;
    }

    @PostMapping("/auth/signOut")
    public String signOut(HttpServletResponse response) {

        Cookie accessToken = generateCookie("accessToken", null);
        Cookie refreshToken = generateCookie("refreshToken", null);
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
        Cookie accessToken = generateCookie("accessToken", null);
        Cookie refreshToken = generateCookie("refreshToken", null);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        return "redirect:/";
    }

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

