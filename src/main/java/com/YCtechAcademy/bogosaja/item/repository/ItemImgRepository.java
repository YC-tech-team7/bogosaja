package com.YCtechAcademy.bogosaja.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.YCtechAcademy.bogosaja.item.domain.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
