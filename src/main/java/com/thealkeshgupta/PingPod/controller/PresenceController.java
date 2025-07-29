package com.thealkeshgupta.PingPod.controller;

import com.thealkeshgupta.PingPod.model.ActiveUserMessage;
import com.thealkeshgupta.PingPod.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
public class PresenceController {

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/active/{roomId}")
    public void handleUserActive(@DestinationVariable Long roomId, @Payload ActiveUserMessage message) {
        presenceService.userJoined(roomId, message.getUsername());

        // Send current active users to the new user
        Set<Long> currentUsers = presenceService.getActiveUsers(roomId);
        messagingTemplate.convertAndSend("/topic/active/" + roomId, currentUsers);
    }

    @MessageMapping("/inactive/{roomId}")
    public void handleUserInactive(@DestinationVariable Long roomId, @Payload ActiveUserMessage message) {
        presenceService.userLeft(roomId, message.getUsername());

        Set<Long> currentUsers = presenceService.getActiveUsers(roomId);
        messagingTemplate.convertAndSend("/topic/active/" + roomId, currentUsers);
    }
}
