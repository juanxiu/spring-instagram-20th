package com.ceos20.instagram.Repository;

import com.ceos20.instagram.Domain.Chatroom;
import com.ceos20.instagram.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {
    List<Chatroom> findChatroomList(User sender, User receiver);
}
