package com.YCtechAcademy.bogosaja.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.YCtechAcademy.bogosaja.item.domain.Item;
import com.YCtechAcademy.bogosaja.item.dto.ItemSearchDto;
import com.YCtechAcademy.bogosaja.item.dto.MainItemDto;

import java.util.List;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);


}
