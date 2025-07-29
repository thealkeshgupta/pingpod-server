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
import com.thealkeshgupta.PingPod.repository.UserRepository;
import com.thealkeshgupta.PingPod.util.GenerateRoomId;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomMemberRepository chatRoomMemberRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GenerateRoomId generateRoomId;

    @Override
    public ChatRoomDTO createRoom(User owner, String name) {


        ChatRoom chatRoom = new ChatRoom();

        Long uniqueRoomId = generateRoomId.generateUniqueRoomId();
        chatRoom.setRoomId(uniqueRoomId);

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

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

        boolean isMember = chatRoom.getMembers().stream()
                .anyMatch(member -> user.getUserId().equals(member.getMember().getUserId()));

        if (isMember) {
            throw new APIException("Already a member of this room");
        }
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

    @Override
    public ChatRoomDTO toggleAdmin(User loggedInUser, Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));

        ChatRoomMember loggedInMember = chatRoomMemberRepository.findByChatRoomRoomIdAndMemberUserId(chatRoomId, loggedInUser.getUserId())
                .orElseThrow(() -> new APIException("User is not part of this room"));

        if (!loggedInMember.isAdmin()) {
            throw new APIException("Only Room Owner/Admins can perform this action");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        ChatRoomMember targetUser = chatRoomMemberRepository.findByChatRoomRoomIdAndMemberUserId(chatRoomId, userId)
                .orElseThrow(() -> new APIException("User is not a member of this room"));

        targetUser.setAdmin(!targetUser.isAdmin());

        chatRoomMemberRepository.save(targetUser);

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
    public void exitChatRoom(Long chatRoomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));

        boolean isMember = chatRoom.getMembers().stream()
                .anyMatch(member -> user.getUserId().equals(member.getMember().getUserId()));

        boolean isOwner = chatRoom.getOwner().getUserId().equals(user.getUserId());

        if (isOwner) {
            throw new APIException("Room Owners cannot exit their owned room");
        }

        if (!isMember) {
            throw new APIException("Not a member of this room");
        }

        chatRoom.getMembers().removeIf(member -> member.getMember().getUserId().equals(user.getUserId()));

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

    }

    @Override
    @Transactional
    public ChatRoomDTO removeUser(User loggedInUser, Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));

        ChatRoomMember loggedInMember = chatRoomMemberRepository.findByChatRoomRoomIdAndMemberUserId(chatRoomId, loggedInUser.getUserId())
                .orElseThrow(() -> new APIException("User is not part of this room"));

        if (!loggedInMember.isAdmin()) {
            throw new APIException("Only Room Owner/Admins can perform this action");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        if (chatRoom.getOwner().getUserId().equals(userId)) {
            throw new APIException("Room Owner cannot be removed");
        }

        ChatRoomMember targetUser = chatRoomMemberRepository.findByChatRoomRoomIdAndMemberUserId(chatRoomId, userId)
                .orElseThrow(() -> new APIException("User is not a member of this room"));


        chatRoom.getMembers().remove(targetUser);

        System.out.println("deleting... " + targetUser.getMemberId());
        chatRoomMemberRepository.deleteByMemberId(targetUser.getMemberId());

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

}
