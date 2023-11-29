package com.YCtechAcademy.bogosaja.member.dto;

import com.YCtechAcademy.bogosaja.member.domain.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String email;
    private String nickname;
    private Role role;
    private Set<String> likedItemNames;

}
