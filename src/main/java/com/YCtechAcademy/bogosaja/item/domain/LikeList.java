package com.YCtechAcademy.bogosaja.item.domain;

import com.YCtechAcademy.bogosaja.global.domain.BaseEntity;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "LikeList")
@Getter
public class LikeList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_list_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public LikeList(Member member, Item item) {
        this.member = member;
        this.item = item;
    }

    public LikeList() {
    }
}
