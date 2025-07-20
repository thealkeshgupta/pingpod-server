package com.thealkeshgupta.PingPod.repository;

import com.thealkeshgupta.PingPod.model.ChatRoomMember;
import com.thealkeshgupta.PingPod.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByMember(User user);
}
