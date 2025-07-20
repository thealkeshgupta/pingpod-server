package com.thealkeshgupta.PingPod.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long chatRoomId;
    private String content;
    private LocalDateTime timestamp;
}
