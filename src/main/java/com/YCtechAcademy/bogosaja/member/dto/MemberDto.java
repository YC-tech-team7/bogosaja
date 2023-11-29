package com.YCtechAcademy.bogosaja.member.dto;

import com.YCtechAcademy.bogosaja.item.domain.LikeList;
import com.YCtechAcademy.bogosaja.item.repository.LikeListRepository;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.domain.Role;
import com.YCtechAcademy.bogosaja.item.domain.Item;
import com.YCtechAcademy.bogosaja.item.repository.ItemRepository;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

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
