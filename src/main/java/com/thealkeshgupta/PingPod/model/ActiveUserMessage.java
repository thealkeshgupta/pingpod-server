package com.thealkeshgupta.PingPod.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveUserMessage {
    private Long username;
    private Long roomId;

    public ActiveUserMessage() {
    }

    public ActiveUserMessage(Long username, Long roomId) {
        this.username = username;
        this.roomId = roomId;
    }

}
