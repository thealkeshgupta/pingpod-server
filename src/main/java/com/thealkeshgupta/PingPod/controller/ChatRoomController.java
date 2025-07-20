package com.thealkeshgupta.PingPod.controller;

import com.thealkeshgupta.PingPod.model.ChatRoom;
import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.ChatRoomDTO;
import com.thealkeshgupta.PingPod.service.ChatRoomService;
import com.thealkeshgupta.PingPod.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    AuthUtil authUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createChatRoom(@Valid @RequestBody ChatRoomDTO chatRoomDTO) {
        User user = authUtil.loggedInUser();
        ChatRoomDTO savedChatRoomDTO = chatRoomService.createRoom(user, chatRoomDTO.getName());
        return new ResponseEntity<>(savedChatRoomDTO, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllChatRooms() {
        List<ChatRoomDTO> rooms = chatRoomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChatRoomById(@PathVariable Long chatRoomId) {
        ChatRoomDTO chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getChatRoomByUserId() {
        User user = authUtil.loggedInUser();
        List<ChatRoomDTO> rooms = chatRoomService.getChatRoomByUser(user);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @PostMapping("/join/{chatRoomId}")
    public ResponseEntity<?> joinChatRoom(@PathVariable Long chatRoomId) {
        User user = authUtil.loggedInUser();
        ChatRoomDTO room = chatRoomService.joinChatRoom(chatRoomId, user);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Long chatRoomId) {
        User user = authUtil.loggedInUser();

        chatRoomService.deleteChatRoom(user, chatRoomId);
        return new ResponseEntity<>("Room deleted successfully", HttpStatus.OK);
    }
}
