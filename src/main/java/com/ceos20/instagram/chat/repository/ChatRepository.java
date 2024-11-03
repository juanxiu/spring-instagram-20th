package com.ceos20.instagram.chat.repository;

import com.ceos20.instagram.chat.domain.ChatMessage;
import com.ceos20.instagram.chatRoom.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long>{
    List<ChatMessage> findByRoom(Chatroom room);
}
