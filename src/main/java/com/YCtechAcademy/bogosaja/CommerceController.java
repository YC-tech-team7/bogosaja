package com.YCtechAcademy.bogosaja;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.repository.MemberRepository;
import com.YCtechAcademy.bogosaja.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommerceController {
    private final MemberRepository memberRepository;

    @GetMapping("/index") // "/" 가 겹쳐서 임시로 바꿈
    public String mainPage(Model model, @AuthenticationPrincipal Member member1){

        if (member1 != null){
            Member member = memberRepository.findByEmail(member1.getUsername()).orElseThrow();
            model.addAttribute("member", member.toDTO(member));
        }
	    return "index";
    }

}
