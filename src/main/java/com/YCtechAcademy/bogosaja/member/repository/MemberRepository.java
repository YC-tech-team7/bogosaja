package com.YCtechAcademy.bogosaja.member.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.YCtechAcademy.bogosaja.member.dto.SignUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
	Optional<Member> findByEmail(String email);
}
