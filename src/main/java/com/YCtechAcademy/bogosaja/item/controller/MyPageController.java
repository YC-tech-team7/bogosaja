package com.YCtechAcademy.bogosaja.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    @GetMapping("/mypage")
    public String goMy(){
        return "mypage";
    }
}
