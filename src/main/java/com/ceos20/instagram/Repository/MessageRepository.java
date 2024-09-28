package com.ceos20.instagram.Repository;

import com.ceos20.instagram.Domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository {
    List<ChatMessage> findChatMessages(Long roomid);
}
