package com.YCtechAcademy.bogosaja.global.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

    // 게시물을 수정한 사용자 정보를 기록하기 위한 메서드
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if (authentication != null) {
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
    // public Optional<Member> getCurrentAuditor() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //
    //     if (authentication == null || !authentication.isAuthenticated()) {
    //         return Optional.empty();
    //     }
    //     return Optional.of((Member)authentication.getPrincipal());
    // }
}
