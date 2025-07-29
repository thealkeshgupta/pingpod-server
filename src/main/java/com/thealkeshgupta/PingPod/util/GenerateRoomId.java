package com.thealkeshgupta.PingPod.util;

import com.thealkeshgupta.PingPod.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateRoomId {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    public Long generateUniqueRoomId() {
        Long roomId;
        do {
            roomId = 100000L + new Random().nextInt(900000);
        } while (chatRoomRepository.existsById(roomId));
        return roomId;
    }
}
