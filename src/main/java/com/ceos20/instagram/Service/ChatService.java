package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.ChatMessage;
import com.ceos20.instagram.Domain.Chatroom;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.ChatDto;
import com.ceos20.instagram.Repository.ChatRoomRepository;
import com.ceos20.instagram.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;

    // 채팅방 생성
    @Transactional
    public Chatroom createChatroom(String roomName, User sender, User receiver) {
        Chatroom chatroom = Chatroom.builder()
                .roomName(roomName)
                .sender(sender)
                .receiver(receiver)
                .build();

        return chatRoomRepository.save(chatroom);
    }

    // 채팅방 목록 조회
    public List<Chatroom> findChatroomList(User user) {
        return chatRoomRepository.findChatroomList(user, user);
    }

    // 메시지 보내기
    @Transactional
    public void sendMessage(Long roomId, ChatDto chatDto) {
        Chatroom chatroom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다. roomId=" + roomId));

        ChatMessage newMessage = chatDto.toEntity(chatroom);
        messageRepository.save(newMessage);
    }

    // 이전 채팅 내역 조회.
    public List<ChatMessage> findChatMessages(Long roomId){
        return messageRepository.findChatMessages(roomId);
    }



}
