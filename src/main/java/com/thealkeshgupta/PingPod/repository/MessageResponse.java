package com.thealkeshgupta.PingPod.repository;

import com.thealkeshgupta.PingPod.payload.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private List<MessageDTO> content;
    private Integer pageNumber;
    private boolean lastPage;
}
