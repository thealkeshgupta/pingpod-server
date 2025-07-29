package com.thealkeshgupta.PingPod.service;

import com.thealkeshgupta.PingPod.exception.APIException;
import com.thealkeshgupta.PingPod.exception.ResourceNotFoundException;
import com.thealkeshgupta.PingPod.model.ChatRoom;
import com.thealkeshgupta.PingPod.model.Message;
import com.thealkeshgupta.PingPod.model.User;
import com.thealkeshgupta.PingPod.payload.MessageDTO;
import com.thealkeshgupta.PingPod.repository.ChatRoomRepository;
import com.thealkeshgupta.PingPod.repository.MessageRepository;
import com.thealkeshgupta.PingPod.repository.MessageResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MessageDTO sendMessage(User user, MessageDTO messageDTO, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));


        if (!chatRoomService.isMemberOfRoom(user, chatRoomId)) {
            throw new APIException("Access not allowed to this room");
        }

        Message message = new Message();

        message.setSender(user);
        message.setContent(messageDTO.getContent());
        message.setChatRoom(chatRoom);

        Message savedMessage = messageRepository.save(message);

        return modelMapper.map(savedMessage, MessageDTO.class);
    }

    @Override
    public MessageResponse getMessages(User user, Long chatRoomId, Integer pageNumber) {
        if (!chatRoomService.isMemberOfRoom(user, chatRoomId)) {
            throw new APIException("Access not allowed to this room");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom", "roomId", chatRoomId));

        Sort sortByAndOrder = Sort.by("timestamp").descending();

        Pageable pageDetails = PageRequest.of(pageNumber, 20, sortByAndOrder);

        Page<Message> messagesPage = messageRepository.findByChatRoom(chatRoom, pageDetails);

        if (messagesPage.isEmpty()) {
            throw new APIException("No messages yet");
        }

        List<MessageDTO> messageDTOs = messagesPage.stream().map((message -> {
            return modelMapper.map(message, MessageDTO.class);
        })).collect(Collectors.toList());

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setContent(messageDTOs);
        messageResponse.setPageNumber(pageNumber);
        messageResponse.setLastPage(messagesPage.isLast());

        return messageResponse;
    }

    @Override
    public void deleteMessage(User user, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "messageId", messageId));


        if (!message.getSender().getUserId().equals(user.getUserId()) && !message.getChatRoom().getOwner().getUserId().equals(user.getUserId())) {

            throw new APIException("Only sender/owner can delete the message");
        }

        messageRepository.deleteById(messageId);
    }

    @Override
    @Transactional
    public void deleteBulkMessages(List<Long> toBeDeletedIDs) {
        messageRepository.deleteAllByIdIn(toBeDeletedIDs);
    }

}
