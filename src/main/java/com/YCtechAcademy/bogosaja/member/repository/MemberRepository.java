package com.YCtechAcademy.bogosaja.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.YCtechAcademy.bogosaja.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
