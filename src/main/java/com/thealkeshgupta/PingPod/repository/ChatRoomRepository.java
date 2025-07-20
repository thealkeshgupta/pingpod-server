package com.thealkeshgupta.PingPod.repository;

import com.thealkeshgupta.PingPod.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
