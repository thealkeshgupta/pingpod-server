package com.thealkeshgupta.PingPod.service;

import com.thealkeshgupta.PingPod.exception.APIException;
import com.thealkeshgupta.PingPod.exception.ResourceNotFoundException;
import com.thealkeshgupta.PingPod.model.ChatRoom;
import com.thealkeshgupta.PingPod.model.ChatRoomMember;
import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.ChatRoomDTO;
import com.thealkeshgupta.PingPod.payload.ChatRoomMemberDTO;
import com.thealkeshgupta.PingPod.payload.UserDTO;
import com.thealkeshgupta.PingPod.repository.ChatRoomMemberRepository;
import com.thealkeshgupta.PingPod.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatRoomDTO createRoom(User owner, String name) {

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        ChatRoom chatRoom = new ChatRoom();

        chatRoom.setName(name);
        chatRoom.setOwner(owner);
        chatRoom.setCreatedAt(today);

        ChatRoomMember adminMember = new ChatRoomMember();
        adminMember.setChatRoom(chatRoom);
        adminMember.setAdmin(true);
        adminMember.setMember(owner);
        adminMember.setJoinedAt(today);

        chatRoom.getMembers().add(adminMember);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        Set<ChatRoomMemberDTO> members = savedChatRoom.getMembers().stream().map(member -> {
            ChatRoomMemberDTO dto = new ChatRoomMemberDTO();
            dto.setId(member.getMemberId());
            dto.setRoomId(member.getChatRoom().getRoomId());
            dto.setJoinedAt(member.getJoinedAt());
            dto.setAdmin(member.isAdmin());
            dto.setUser(modelMapper.map(member.getMember(), UserDTO.class));
            return dto;
        }).collect(Collectors.toSet());

        ChatRoomDTO savedChatRoomDTO = new ChatRoomDTO();
        savedChatRoomDTO.setRoomId(savedChatRoom.getRoomId());
        savedChatRoomDTO.setName(savedChatRoom.getName());
        savedChatRoomDTO.setCreatedAt(savedChatRoom.getCreatedAt());
        savedChatRoomDTO.setOwner(modelMapper.map(savedChatRoom.getOwner(), UserDTO.class));
        savedChatRoomDTO.setMembers(members);
        return savedChatRoomDTO;
    }

    @Override
    public List<ChatRoomDTO> getAllRooms() {
        List<ChatRoom> chatRoomsList = chatRoomRepository.findAll();

        List<ChatRoomDTO> chatRoomDTOs = chatRoomsList.stream().map(room -> {
            ChatRoomDTO dto = new ChatRoomDTO();
            dto.setRoomId(room.getRoomId());
            dto.setName(room.getName());
            dto.setCreatedAt(room.getCreatedAt());
            dto.setOwner(modelMapper.map(room.getOwner(), UserDTO.class));

            Set<ChatRoomMemberDTO> members = room.getMembers().stream().map(member -> {
                ChatRoomMemberDTO mDto = new ChatRoomMemberDTO();
                mDto.setId(member.getMemberId());
                mDto.setRoomId(member.getChatRoom().getRoomId());
                mDto.setJoinedAt(member.getJoinedAt());
                mDto.setAdmin(member.isAdmin());
                mDto.setUser(modelMapper.map(member.getMember(), UserDTO.class));
                return mDto;
            }).collect(Collectors.toSet());

            dto.setMembers(members);
            return dto;
        }).collect(Collectors.toList());
        return chatRoomDTOs;
    }

    @Override
    public ChatRoomDTO getChatRoomById(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));
        Set<ChatRoomMemberDTO> members = chatRoom.getMembers().stream().map(member -> {
            ChatRoomMemberDTO dto = new ChatRoomMemberDTO();
            dto.setId(member.getMemberId());
            dto.setRoomId(member.getChatRoom().getRoomId());
            dto.setJoinedAt(member.getJoinedAt());
            dto.setAdmin(member.isAdmin());
            dto.setUser(modelMapper.map(member.getMember(), UserDTO.class));
            return dto;
        }).collect(Collectors.toSet());

        ChatRoomDTO savedChatRoomDTO = new ChatRoomDTO();
        savedChatRoomDTO.setRoomId(chatRoom.getRoomId());
        savedChatRoomDTO.setName(chatRoom.getName());
        savedChatRoomDTO.setCreatedAt(chatRoom.getCreatedAt());
        savedChatRoomDTO.setOwner(modelMapper.map(chatRoom.getOwner(), UserDTO.class));
        savedChatRoomDTO.setMembers(members);
        return savedChatRoomDTO;
    }

    @Override
    public List<ChatRoomDTO> getChatRoomByUser(User user) {
        List<ChatRoomMember> memberships = chatRoomMemberRepository.findByMember(user);

        List<ChatRoom> chatRooms = memberships.stream().map(membership -> {
            return membership.getChatRoom();
        }).collect(Collectors.toList());

        List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream().map(room -> {
            ChatRoomDTO dto = new ChatRoomDTO();
            dto.setRoomId(room.getRoomId());
            dto.setName(room.getName());
            dto.setCreatedAt(room.getCreatedAt());
            dto.setOwner(modelMapper.map(room.getOwner(), UserDTO.class));

            Set<ChatRoomMemberDTO> members = room.getMembers().stream().map(member -> {
                ChatRoomMemberDTO mDto = new ChatRoomMemberDTO();
                mDto.setId(member.getMemberId());
                mDto.setRoomId(member.getChatRoom().getRoomId());
                mDto.setJoinedAt(member.getJoinedAt());
                mDto.setAdmin(member.isAdmin());
                mDto.setUser(modelMapper.map(member.getMember(), UserDTO.class));
                return mDto;
            }).collect(Collectors.toSet());

            dto.setMembers(members);
            return dto;
        }).collect(Collectors.toList());
        return chatRoomDTOs;
    }

    @Override
    public ChatRoomDTO joinChatRoom(Long chatRoomId, User user) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));


        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

        ChatRoomMember newMember = new ChatRoomMember();
        newMember.setChatRoom(chatRoom);
        newMember.setAdmin(false);
        newMember.setMember(user);
        newMember.setJoinedAt(today);

        chatRoom.getMembers().add(newMember);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        Set<ChatRoomMemberDTO> members = savedChatRoom.getMembers().stream().map(member -> {
            ChatRoomMemberDTO dto = new ChatRoomMemberDTO();
            dto.setId(member.getMemberId());
            dto.setRoomId(member.getChatRoom().getRoomId());
            dto.setJoinedAt(member.getJoinedAt());
            dto.setAdmin(member.isAdmin());
            dto.setUser(modelMapper.map(member.getMember(), UserDTO.class));
            return dto;
        }).collect(Collectors.toSet());

        ChatRoomDTO savedChatRoomDTO = new ChatRoomDTO();
        savedChatRoomDTO.setRoomId(savedChatRoom.getRoomId());
        savedChatRoomDTO.setName(savedChatRoom.getName());
        savedChatRoomDTO.setCreatedAt(savedChatRoom.getCreatedAt());
        savedChatRoomDTO.setOwner(modelMapper.map(savedChatRoom.getOwner(), UserDTO.class));
        savedChatRoomDTO.setMembers(members);
        return savedChatRoomDTO;
    }

    @Override
    @Transactional
    public void deleteChatRoom(User user, Long chatRoomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));

        if (!chatRoom.getOwner().getUserId().equals(user.getUserId())) {
            throw new APIException("Only the group owner can delete the room.");
        }

        chatRoomRepository.deleteById(chatRoomId);
    }

    @Override
    public boolean isMemberOfRoom(User user, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));

        boolean isMember = chatRoom.getMembers().stream()
                .anyMatch(member -> user.getUserId().equals(member.getMember().getUserId()));

        return isMember;
    }

}
