package com.YCtechAcademy.bogosaja.chatting.controller;


import com.YCtechAcademy.bogosaja.chatting.dto.ChatDTO;
import com.YCtechAcademy.bogosaja.chatting.dto.ChatRoom;
import com.YCtechAcademy.bogosaja.chatting.dto.RoomRequest;
import com.YCtechAcademy.bogosaja.chatting.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRepository chatRepository;

    @GetMapping("/chat")
    public ModelAndView chatHome() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("chat");
        return mav;
    }

    // 해당 채팅방 채팅내역 표시
    @PostMapping("/chat/chatlist")
    public ModelAndView goChatRoom(@RequestBody RoomRequest roomRequest, ModelAndView mav){
        //1. 조회
        List<ChatDTO> list = chatRepository.findRoomChatting(roomRequest.getRoomId());

        //return
        mav.addObject("list", list);
        mav.setViewName("jsonView");
        return mav;
    }

    // 채팅방 생성
    @PostMapping("/chat/createroom")
    public ModelAndView createRoom(@RequestBody RoomRequest roomRequest, ModelAndView mav) {

        //2. 채팅방 생성
        ChatRoom room = chatRepository.createChatRoom(roomRequest.getRoomName());
        log.info("CREATE Chat Room {}", room);

        //return
        mav.addObject("roomId", room.getRoomId());
        mav.setViewName("jsonView");
        return mav;
    }

    // 채팅에 참여한 유저 리스트 반환
    @PostMapping("/chat/userlist")
    public ModelAndView userList(@RequestBody RoomRequest roomRequest, ModelAndView mav) {
        //1. 조회
        ArrayList<String> userList = chatRepository.getUserList(roomRequest.getRoomId());

        //return
        mav.addObject("userList", userList);
        mav.setViewName("jsonView");
        return mav;
    }
}
