package com.thealkeshgupta.PingPod.repository;

import com.thealkeshgupta.PingPod.model.ChatRoomMember;
import com.thealkeshgupta.PingPod.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByMember(User user);

    Optional<ChatRoomMember> findByChatRoomRoomIdAndMemberUserId(Long chatRoomId, Long userId);

    @Modifying
    @Query("DELETE FROM ChatRoomMember m WHERE m.memberId = :memberId")
    void deleteByMemberId(Long memberId);
}
