package com.YCtechAcademy.bogosaja.item.controller;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MemberRepository memberRepository;
    //마이페이지 기능(거래 이력 살펴보기) 추후 구현 예정 - 디렉토리 변경 가능성 있음
    @GetMapping("/mypage")
    public String goMy(Model model, @AuthenticationPrincipal Member member1){

        if (member1 != null){
            Member member = memberRepository.findByEmail(member1.getUsername()).orElseThrow();
            model.addAttribute("member", member.toDTO(member));
            return "mypage";
        }else{
            return "redirect:/member/auth/signin";
        }
    }
}
