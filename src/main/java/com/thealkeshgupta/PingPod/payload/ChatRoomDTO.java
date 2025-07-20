package com.thealkeshgupta.PingPod.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private Long roomId;
    private String name;
    private UserDTO owner;
    private String createdAt;
    private Set<ChatRoomMemberDTO> members;
}
