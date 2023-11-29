package com.YCtechAcademy.bogosaja.item.dto;

import com.YCtechAcademy.bogosaja.item.domain.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;

    private String searchQuery = "";

}
