package com.YCtechAcademy.bogosaja.item.repository;

import com.YCtechAcademy.bogosaja.item.domain.Item;
import com.YCtechAcademy.bogosaja.item.domain.LikeList;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LikeListRepository extends JpaRepository<LikeList, Long> {
    Optional<LikeList> findByMemberAndItem(Member member, Item item);

    Collection <LikeList> findByMember(Member member);
}