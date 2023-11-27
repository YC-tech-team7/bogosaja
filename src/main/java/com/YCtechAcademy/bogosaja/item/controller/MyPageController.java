package com.YCtechAcademy.bogosaja.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    //마이페이지 기능(거래 이력 살펴보기) 추후 구현 예정 - 디렉토리 변경 가능성 있음
    @GetMapping("/mypage")
    public String goMy(){
        return "mypage";
    }
}
