package com.YCtechAcademy.bogosaja.item.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.YCtechAcademy.bogosaja.item.domain.Item;
import com.YCtechAcademy.bogosaja.item.dto.ItemFormDto;
import com.YCtechAcademy.bogosaja.item.dto.ItemSearchDto;
import com.YCtechAcademy.bogosaja.item.dto.MainItemDto;
import com.YCtechAcademy.bogosaja.item.service.ItemService;
import com.YCtechAcademy.bogosaja.member.domain.Member;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //홈화면 - 상품 목록 보기, 페이징 기능 구현
    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,6);
        Page<MainItemDto> items=
                itemService.getMainItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "main";
    }

    //상품 등록 화면 조회(이동)
    @GetMapping(value = "/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    //상품 등록 기능
    @PostMapping(value = "/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다,");
            return "item/itemForm";
        }
        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    //어드민 - 상품 상세 조회(어드민 따로 구현 안해서 추후 구현 요망)
    // @GetMapping(value = "/admin/item/{itemId}")
    // public String adminItemDtl(@PathVariable("itemId") Long itemId, Model model) {
    //
    //     try {
    //         ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
    //         model.addAttribute("itemFormDto", itemFormDto);
    //     } catch (EntityNotFoundException e) {
    //         model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
    //         model.addAttribute("itemFormDto", new ItemFormDto());
    //         return "item/itemForm";
    //     }
    //     return "item/itemForm";
    // }

    // 상품 정보 수정 화면 조회(이동) 기능
    @GetMapping(value = "/item/{itemId}/form")
    public String itemUpdateForm(@PathVariable("itemId") Long itemId, Model model, @AuthenticationPrincipal Member member) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            if(!member.getEmail().equals(itemFormDto.getCreatedBy())){
                model.addAttribute("errorMessage", "해당 상품의 등록자만 수정할 수 있습니다.");
                model.addAttribute("itemFormDto", new ItemFormDto());
                return "item/itemForm";
            }
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    //상품 정보 수정 기능
    @PostMapping(value = "/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //어드민 - 상품 검색 조회 기능 (어드민 따로 구현 안해서 추후 구현 요망)
    @GetMapping(value = {"/items", "/items/{pages}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() :  0, 3);

        Page<Item> items =
                itemService.getAdminItemPage(itemSearchDto, pageable);
                model.addAttribute("items", items);
                model.addAttribute("itemSearchDto", itemSearchDto);
                model.addAttribute("maxPage", 5);
                return "item/itemMng";
    }

    //상품 상세 화면 조회(이동) 기능
    @GetMapping(value="/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId, @AuthenticationPrincipal Member member){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        itemFormDto.setIsCreatedByMember(Objects.equals(member.getEmail(), itemFormDto.getCreatedBy()));
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

    @PostMapping(value = "/item/like/{itemId}")
    public String like(@PathVariable("itemId") Long itemId){

        return "redirect:/item/"+itemId.toString();
    }
}

