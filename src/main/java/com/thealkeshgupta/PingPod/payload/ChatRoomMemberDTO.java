package com.thealkeshgupta.PingPod.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomMemberDTO {
    private Long id;
    private Long roomId;
    private boolean isAdmin;
    private String joinedAt;
    private UserDTO user;
}
