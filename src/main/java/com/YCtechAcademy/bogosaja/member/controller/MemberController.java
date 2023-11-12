package com.YCtechAcademy.bogosaja.member.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import com.YCtechAcademy.bogosaja.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

   private final MemberService memberService;

   @GetMapping("/signUp")
   public String signUpForm(){
      // 회원가입 화면
      return "member/signUpForm";
   }

   @PostMapping("/signUp")
   public String signUp(@RequestBody SignUpRequest signUpRequest) {
       // 회원가입 완료 페이지
       Member member = memberService.signUp(signUpRequest);
       System.out.println(member);
       return "redirect:/";
   }

   //
   // @PostMapping("/login")
   // public ResponseEntity<String> login(@RequestBody User user) {
   //     // 로그인 로직 구현
   // }
   //
   // @PostMapping("/logout")
   // public ResponseEntity<String> logout() {
   //     // 로그아웃 로직 구현
   // }



}

