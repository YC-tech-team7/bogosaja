package com.YCtechAcademy.bogosaja.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MainItemDto {

    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    // Querydsl로 결과 조회 시 MainItemDto 객체로 바로 받아오도록 활용
    @QueryProjection
    public MainItemDto(long id, String itemNm, String itemDetail, String imgUrl, int price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
