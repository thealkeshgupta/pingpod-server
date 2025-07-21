package com.thealkeshgupta.PingPod.controller;

import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.MessageDTO;
import com.thealkeshgupta.PingPod.service.MessageService;
import com.thealkeshgupta.PingPod.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class MessageWebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthUtil authUtil;


    @MessageMapping("/chat.send/{roomId}")
    public void sendMessage(@Payload MessageDTO messageDTO, @DestinationVariable Long roomId) {
        User user = authUtil.loggedInUser();

        System.out.println("Received message: " + messageDTO.getContent());

        MessageDTO savedMessage = messageService.sendMessage(user, messageDTO, roomId);

        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, savedMessage);
    }

}
