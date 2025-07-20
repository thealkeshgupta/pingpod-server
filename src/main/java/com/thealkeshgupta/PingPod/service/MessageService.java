package com.thealkeshgupta.PingPod.service;

import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.MessageDTO;
import com.thealkeshgupta.PingPod.repository.MessageResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(User user, @Valid MessageDTO messageDTO, Long chatRoomId);

    MessageResponse getMessages(User user, Long chatRoomId, Integer pageNumber);

    void deleteMessage(User user, Long messageId);
}
