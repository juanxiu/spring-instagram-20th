package com.ceos20.instagram.chatRoom.repository;

import com.ceos20.instagram.chatRoom.domain.Chatroom;
import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {
//    List<Chatroom> findChatroomList(User sender, User receiver);
    Optional<Chatroom> findBySenderAndReceiver(User sender, User receiver);
}
