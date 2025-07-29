package com.thealkeshgupta.PingPod.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypingStatus {
    private long senderId;
    private boolean typing;
}
