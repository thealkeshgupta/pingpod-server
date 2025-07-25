package com.thealkeshgupta.PingPod.controller;

import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.MessageDTO;
import com.thealkeshgupta.PingPod.repository.MessageResponse;
import com.thealkeshgupta.PingPod.service.MessageService;
import com.thealkeshgupta.PingPod.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/send/{chatRoomId}")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageDTO messageDTO, @PathVariable Long chatRoomId) {
        User user = authUtil.loggedInUser();
        MessageDTO savedMessage = messageService.sendMessage(user, messageDTO, chatRoomId);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    @GetMapping("/{chatRoomId}/{pageNumber}")
    public ResponseEntity<?> getMessages(@PathVariable Long chatRoomId, @PathVariable Integer pageNumber) {
        User user = authUtil.loggedInUser();
        MessageResponse messages = messageService.getMessages(user, chatRoomId, pageNumber);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId) {
        User user = authUtil.loggedInUser();
        messageService.deleteMessage(user, messageId);
        return new ResponseEntity<>("Message deleted successfully", HttpStatus.OK);
    }

}
