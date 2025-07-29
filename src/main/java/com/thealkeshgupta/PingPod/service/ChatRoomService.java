package com.thealkeshgupta.PingPod.service;

import com.thealkeshgupta.PingPod.model.ChatRoom;
import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.ChatRoomDTO;

import java.util.List;

public interface ChatRoomService {

    ChatRoomDTO createRoom(User owner, String name);

    List<ChatRoomDTO> getAllRooms();

    ChatRoomDTO getChatRoomById(Long chatRoomId);

    List<ChatRoomDTO> getChatRoomByUser(User user);

    ChatRoomDTO joinChatRoom(Long chatRoomId, User user);

    void deleteChatRoom(User user, Long chatRoomId);

    boolean isMemberOfRoom(User user, Long chatRoomId);

    ChatRoomDTO toggleAdmin(User loggedInUser, Long chatRoomId, Long userId);

    void exitChatRoom(Long chatRoomId, User user);

    ChatRoomDTO removeUser(User loggedInUser, Long chatRoomId, Long userId);
}
