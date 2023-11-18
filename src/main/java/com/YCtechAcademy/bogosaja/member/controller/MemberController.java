package com.YCtechAcademy.bogosaja.member.controller;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.YCtechAcademy.bogosaja.auth.TokenInfo;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.dto.DeleteRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignInRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import com.YCtechAcademy.bogosaja.member.dto.UpdateRequest;
import com.YCtechAcademy.bogosaja.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
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
   public String signUp(@RequestBody SignUpRequest signUpRequest) {
       memberService.signUp(signUpRequest);
       // 회원가입 완료 페이지
       return "member/signInForm";
   }

    @GetMapping("/auth/signIn")
    // 로그인 화면
    public String signInForm(){
        return "member/signInForm";
    }

    @PostMapping("/auth/signIn")
    public String signIn(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        TokenInfo tokenInfo = memberService.signIn(signInRequest.email(), signInRequest.password());

        Cookie accessToken = generateCookie("accessToken", tokenInfo.accessToken());
        Cookie refreshToken = generateCookie("refreshToken", tokenInfo.refreshToken());

        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        return "member/signUpForm";

    }

    private Cookie generateCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS를 사용하는 경우 true로 변경
        cookie.setMaxAge(3600); // 쿠키 유효 시간 설정 (초 단위)
        cookie.setPath("/");
        return cookie;
    }

    @PutMapping("/delete")
    public String delete(@RequestBody DeleteRequest deleteRequest, @AuthenticationPrincipal Member member) {
       memberService.delete(deleteRequest, member);
        return "redirect:/";
    }
    @GetMapping("/update")
    public String updateForm(){
       // 회원정보 수정 화면
       return "member/updateForm";
    }
    @PutMapping("/update")
    public String update(@RequestBody UpdateRequest updateRequest, @AuthenticationPrincipal Member member) {
       memberService.update(updateRequest, member);
        return "redirect:/"; // todo 바꾼정보 반영된채로 updateform으로 가도록하기...
    }



}

