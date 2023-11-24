package com.YCtechAcademy.bogosaja;

import com.YCtechAcademy.bogosaja.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CommerceController {

   @GetMapping("/")
   public String mainPage(Model model, @AuthenticationPrincipal Member member){

       log.info("member가 존재하나요? " + member);
       if (member != null){
           model.addAttribute("member", member);
       }
	   return "index";
   }

}
