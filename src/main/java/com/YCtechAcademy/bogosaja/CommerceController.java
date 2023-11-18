package com.YCtechAcademy.bogosaja;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommerceController {

   @GetMapping("/")
   public String mainPage(){
	   return "index";
   }

}
