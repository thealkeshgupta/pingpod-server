package com.thealkeshgupta.PingPod.listener;


import com.thealkeshgupta.PingPod.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private PresenceService presenceService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Optional: Handle if you store sessionId -> username mapping manually
        System.out.println("User disconnected: " + event.getSessionId());
    }
}
