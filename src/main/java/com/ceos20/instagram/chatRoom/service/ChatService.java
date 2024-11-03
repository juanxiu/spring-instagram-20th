package com.ceos20.instagram.chatRoom.service;

import com.ceos20.instagram.chat.dto.ChatDto;
import com.ceos20.instagram.chat.domain.ChatMessage;
import com.ceos20.instagram.chat.repository.ChatRepository;
import com.ceos20.instagram.chatRoom.repository.ChatRoomRepository;
import com.ceos20.instagram.chatRoom.domain.Chatroom;
import com.ceos20.instagram.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    private ChatRepository chatRepository;

    @Transactional
    public Chatroom createChatroom(String roomName, User sender, User receiver) {

        // 동일한 사용자 간의 채팅방이 있는지 확인
        Optional<Chatroom> existingChatroom = chatRoomRepository.findBySenderAndReceiver(sender, receiver);

        if (existingChatroom.isPresent()) {
            return existingChatroom.get(); // 존재하는 채팅방을 반환
        }

        Chatroom newChatroom = Chatroom.builder()
                .roomName(roomName)
                .sender(sender)
                .receiver(receiver)
                .build();

        return chatRoomRepository.save(newChatroom);
    }

//    // 채팅방 목록 조회
//    public List<Chatroom> findChatroomList(User user) {
//        return chatRoomRepository.findChatroomList(user, user); // sender와 receiver 모두 동일한 경우
//    }

    // 메시지 보내기
    @Transactional
    public void sendMessage(Long roomId, ChatDto chatDto) {
        Chatroom chatroom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다. roomId=" + roomId));

        ChatMessage newMessage = chatDto.toEntity(chatroom);
        chatRepository.save(newMessage);
    }

    // 이전 채팅 내역 조회.
    public List<ChatMessage> getChatMessages(Long roomId) {
        Chatroom chatroom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다. roomId=" + roomId));
        return chatRepository.findByRoom(chatroom);
    }





}
