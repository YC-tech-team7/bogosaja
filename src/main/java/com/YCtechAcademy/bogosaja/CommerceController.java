package com.YCtechAcademy.bogosaja;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//User 타입과 product타입은 아직 만들지 않았습니다.
@RestController
public class CommerceController {

   @GetMapping("/")
   public String mainPage(){
	   return "index";
   }

}
