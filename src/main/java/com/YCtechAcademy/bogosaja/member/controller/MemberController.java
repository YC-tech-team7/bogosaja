package com.YCtechAcademy.bogosaja.member.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.YCtechAcademy.bogosaja.auth.JwtTokenProvider;
import com.YCtechAcademy.bogosaja.auth.TokenInfo;
import com.YCtechAcademy.bogosaja.item.dto.ItemSearchDto;
import com.YCtechAcademy.bogosaja.item.dto.MainItemDto;
import com.YCtechAcademy.bogosaja.item.service.ItemService;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.dto.DeleteRequest;
import com.YCtechAcademy.bogosaja.member.dto.MemberDto;
import com.YCtechAcademy.bogosaja.member.dto.ResetRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignInRequest;
import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import com.YCtechAcademy.bogosaja.member.dto.UpdateRequest;
import com.YCtechAcademy.bogosaja.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

   private final MemberService memberService;
   private final JwtTokenProvider jwtTokenProvider;
   private final ItemService itemService;

    @GetMapping("/auth/signUp")
    // 회원가입 화면
    public String signUpForm() {
        return "member/signUpForm";
    }

    @PostMapping("/auth/signUp")
    public String signUp(@ModelAttribute SignUpRequest signUpRequest, RedirectAttributes redirectAttributes) {
        try {
            memberService.signUp(signUpRequest);
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
            return "redirect:/members/auth/signUp";
        }
        // 회원가입 완료 페이지
        return "redirect:/";
    }

    @GetMapping("/auth/signIn")
    public String signInForm() {
        // 로그인 화면
        return "member/signInForm";
    }

    @PostMapping("/auth/signIn")
    public String signIn(@ModelAttribute SignInRequest signInRequest, HttpServletResponse response, RedirectAttributes redirectAttributes ) {
        log.info("email={}, password={}", signInRequest.email(), signInRequest.password());
        try {
            TokenInfo tokenInfo = memberService.signIn(signInRequest.email(), signInRequest.password());
            response.addCookie(jwtTokenProvider.generateCookie("refreshToken", tokenInfo.refreshToken()));
            response.addCookie(jwtTokenProvider.generateCookie("accessToken", tokenInfo.accessToken()));
            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
            return "redirect:/members/auth/signIn";
        }

    }

    @PostMapping("/auth/signOut")
    public String signOut(HttpServletResponse response) {
        memberService.signOut();

        Cookie accessToken = jwtTokenProvider.generateCookie("accessToken", null);
        Cookie refreshToken = jwtTokenProvider.generateCookie("refreshToken", null);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteForm(@AuthenticationPrincipal Member member, RedirectAttributes redirectAttributes) {
        if (Objects.equals(member.getProvider(), "google")) {
            redirectAttributes.addAttribute("errorMessage", "구글 회원은 회원탈퇴가 불가능합니다.");
            return "redirect:/mypage";
        }
        return "member/deleteForm";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute DeleteRequest deleteRequest, @AuthenticationPrincipal Member member,
            HttpServletResponse response) {
        memberService.delete(deleteRequest, member);
        Cookie accessToken = jwtTokenProvider.generateCookie("accessToken", null);
        Cookie refreshToken = jwtTokenProvider.generateCookie("refreshToken", null);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String updateForm() {
        return "member/updateUserInfoForm";
    }

    @GetMapping("/reset")
    public String resetForm(@AuthenticationPrincipal Member member, RedirectAttributes redirectAttributes) {
        if (Objects.equals(member.getProvider(), "google")) {
            redirectAttributes.addAttribute("errorMessage", "구글 회원은 비밀번호 수정이 불가능합니다.");
            return "redirect:/mypage";
        }
        return "member/resetPasswordForm";
    }



    @PostMapping("/update")
    public String update(@ModelAttribute UpdateRequest updateRequest, @AuthenticationPrincipal Member member) {
        memberService.update(updateRequest, member);
        return "redirect:/";
    }

    @PostMapping("/reset")
    public String update(@ModelAttribute ResetRequest resetRequest, @AuthenticationPrincipal Member member) {
        memberService.resetPw(resetRequest, member);
        return "redirect:/";
    }

    @GetMapping("/db")
    public String db(Model model) {
        List<MemberDto> memberList = memberService.findAll();
        model.addAttribute("memberList", memberList);
        return "member/members";
    }

    @GetMapping("/myItems")
    public String findMyItems(Optional<Integer> page, Model model, @AuthenticationPrincipal Member member){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,6);
        ItemSearchDto itemSearchDto = new ItemSearchDto();
        itemSearchDto.setCreatedBy(member.getEmail());
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);
        return "member/myItems";
    }
}
