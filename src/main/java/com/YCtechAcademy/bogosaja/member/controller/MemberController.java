package com.YCtechAcademy.bogosaja.member.controller;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.YCtechAcademy.bogosaja.auth.TokenInfo;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.dto.DeleteRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignInRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import com.YCtechAcademy.bogosaja.member.dto.UpdateRequest;
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

    @PutMapping("/delete")
    public String delete(@ModelAttribute DeleteRequest deleteRequest, @AuthenticationPrincipal Member member) {
        memberService.delete(deleteRequest, member);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String updateForm(){
       return "member/updateForm";
    }

    @PutMapping("/update")
    public String update(@ModelAttribute UpdateRequest updateRequest, @AuthenticationPrincipal Member member) {
       memberService.update(updateRequest, member);
       return "redirect:/"; // todo 바꾼정보 반영된채로 updateform으로 가도록하기...
    }

    @GetMapping("/db")
    public String db(Model model){
        List<SignUpRequest> memberList = memberService.findAll();
        model.addAttribute("memberList", memberList);
        return "member/members";
    }

}

