package com.YCtechAcademy.bogosaja.item.service;

import com.YCtechAcademy.bogosaja.item.domain.LikeList;
import com.YCtechAcademy.bogosaja.item.repository.LikeListRepository;
import com.YCtechAcademy.bogosaja.member.domain.Member;
import com.YCtechAcademy.bogosaja.member.repository.MemberRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.YCtechAcademy.bogosaja.item.domain.Item;
import com.YCtechAcademy.bogosaja.item.domain.ItemImg;
import com.YCtechAcademy.bogosaja.item.dto.ItemFormDto;
import com.YCtechAcademy.bogosaja.item.dto.ItemImgDto;
import com.YCtechAcademy.bogosaja.item.dto.ItemSearchDto;
import com.YCtechAcademy.bogosaja.item.dto.MainItemDto;
import com.YCtechAcademy.bogosaja.item.repository.ItemImgRepository;
import com.YCtechAcademy.bogosaja.item.repository.ItemRepository;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final LikeListRepository likeListRepository;
    private final MemberRepository memberRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);


        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional
    public boolean likes(String email, Long itemId) throws NotFoundException {
        boolean isLiked = false;
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Member not found with email: " + email));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        Optional<LikeList> likeList = likeListRepository.findByMemberAndItem(member, item);

        // 이미 찜한 상태이면 찜하기 취소
        if (likeList.isPresent()) {
            likeListRepository.delete(likeList.get());
            log.info("isLiked");
        }
        // 찜하지 않은 상태이면 찜하기 수행
        else {
            isLiked = true;
            LikeList newLikeList = new LikeList(member, item);
            likeListRepository.save(newLikeList);
        }
        return isLiked;
    }

    public boolean is_likes(String email, Long itemId) throws NotFoundException {
        boolean isLiked = false;
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Member not found with email: " + email));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        Optional<LikeList> likeList = likeListRepository.findByMemberAndItem(member, item);

        if (likeList.isPresent()) {
            isLiked = true;
        }
        return isLiked;
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }


    //상품 삭제
    @Transactional
    public void deleteByItemId(Long id) {
        itemRepository.deleteById(id);
    }

}
