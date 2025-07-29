package com.thealkeshgupta.PingPod.controller;

import com.thealkeshgupta.PingPod.model.TypingStatus;
import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.MessageDTO;
import com.thealkeshgupta.PingPod.repository.UserRepository;
import com.thealkeshgupta.PingPod.service.MessageService;
import com.thealkeshgupta.PingPod.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class MessageWebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private UserRepository userRepository;


    @MessageMapping("/chat.send/{roomId}")
    public void sendMessage(@Payload MessageDTO messageDTO, @DestinationVariable Long roomId) {
        User user = userRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Received message: " + messageDTO.getContent());

        MessageDTO savedMessage = messageService.sendMessage(user, messageDTO, roomId);

        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, savedMessage);
    }


    @MessageMapping("/typing/{roomId}")
    @SendTo("/topic/typing/{roomId}")
    public TypingStatus handleTyping(@DestinationVariable String roomId, TypingStatus typingStatus) {
        return typingStatus;
    }

}
