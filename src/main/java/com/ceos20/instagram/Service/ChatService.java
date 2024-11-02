package com.ceos20.instagram.Service;

import com.ceos20.instagram.Domain.ChatMessage;
import com.ceos20.instagram.Domain.Chatroom;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.ChatDto;
import com.ceos20.instagram.Repository.ChatRoomRepository;
import com.ceos20.instagram.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;

    // 채팅방 생성
    @Transactional
    public Chatroom createChatroom(String roomName, User sender, User receiver) {

        // 동일한 사용자 간의 채팅방이 있는지 확인 - 유효성 검사 여기서 할 거냐?
        Optional<Chatroom> existingChatroom = chatRoomRepository.findByUserIds(sender.getUserId(), receiver.getUserId());

        if (existingChatroom.isPresent()) {
            return existingChatroom.get(); // 존재하는 채팅방을 반환
        }

        // dto toEntity로 여기 바꿔라.
        // 서비스에는 기능만. 형변환이나 엔티티 변환이런 건 dto에
        Chatroom newChatroom = Chatroom.builder()
                .roomName(roomName)
                .sender(sender)
                .receiver(receiver)
                .build();

        return chatRoomRepository.save(newChatroom);
    }

    // 채팅방 목록 조회
    @Transactional(readOnly = true) // 맨 위에 transactional 붙이고, 읽기 작업만 이거 붙일 것. 
    public List<Chatroom> findChatroomList(User user) {
        return chatRoomRepository.findChatroomList(user, user); // sender와 receiver 모두 동일한 경우
        /*
        return chatrooms.stream()
                 .map(ChatroomRes::of)
                 .collect(Collectors.toList());
         */
    }

    // 메시지 보내기
    // save 만 한다면 transactional 붙일 필요가 있을까? - 레포지토리 구현체 보면 이미 붙여져 있음.
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
