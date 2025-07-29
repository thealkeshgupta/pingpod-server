package com.thealkeshgupta.PingPod.repository;

import com.thealkeshgupta.PingPod.model.ChatRoom;
import com.thealkeshgupta.PingPod.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatRoom(ChatRoom chatRoom, Pageable pageDetails);

    void deleteAllByIdIn(List<Long> toBeDeletedIDs);
}
