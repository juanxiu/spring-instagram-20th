package com.ceos20.instagram;

import com.ceos20.instagram.Domain.ChatMessage;
import com.ceos20.instagram.Domain.Chatroom;
import com.ceos20.instagram.Domain.User;
import com.ceos20.instagram.Dto.ChatDto;
import com.ceos20.instagram.Repository.ChatRoomRepository;
import com.ceos20.instagram.Repository.MessageRepository;
import com.ceos20.instagram.Service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MessageRepository messageRepository;

    private User sender;
    private User receiver;
    private Chatroom chatroom;
    private ChatDto chatDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = User.builder().userId(1L).username("sender").build();
        receiver = User.builder().userId(2L).username("receiver").build();
        chatroom = Chatroom.builder().roomName("Test Room").sender(sender).receiver(receiver).build();
        chatDto = ChatDto.builder().message("Hello").build();
    }

    @Test
    void createChatroom_Success() {
        // given
        when(chatRoomRepository.findByUserIds(sender.getUserId(), receiver.getUserId())).thenReturn(Optional.empty());
        when(chatRoomRepository.save(any(Chatroom.class))).thenReturn(chatroom);

        // when
        Chatroom createdChatroom = chatService.createChatroom("Test Room", sender, receiver);

        // then
        assertNotNull(createdChatroom);
        assertEquals("Test Room", createdChatroom.getRoomName());
        verify(chatRoomRepository).findByUserIds(sender.getUserId(), receiver.getUserId());
        verify(chatRoomRepository).save(any(Chatroom.class));
    }

    @Test
    void createChatroom_ExistingChatroom() {
        // given
        when(chatRoomRepository.findByUserIds(sender.getUserId(), receiver.getUserId())).thenReturn(Optional.of(chatroom));

        // when
        Chatroom existingChatroom = chatService.createChatroom("Test Room", sender, receiver);

        // then
        assertNotNull(existingChatroom);
        assertEquals(chatroom, existingChatroom);
        verify(chatRoomRepository).findByUserIds(sender.getUserId(), receiver.getUserId());
        verify(chatRoomRepository, never()).save(any(Chatroom.class));
    }

    @Test
    void sendMessage_Success() {
        // given
        when(chatRoomRepository.findById(chatroom.getRoomId())).thenReturn(Optional.of(chatroom));

        ChatMessage newMessage = ChatMessage.builder().message(chatDto.getMessage()).roomId(chatroom).build();
        when(messageRepository.save(any(ChatMessage.class))).thenReturn(newMessage);

        // when
        chatService.sendMessage(chatroom.getRoomId(), chatDto);

        // then
        verify(chatRoomRepository).findById(chatroom.getRoomId());
        verify(messageRepository).save(any(ChatMessage.class));
    }

    @Test
    void sendMessage_ChatroomNotFound() {
        // given
        when(chatRoomRepository.findById(chatroom.getRoomId())).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            chatService.sendMessage(chatroom.getRoomId(), chatDto);
        });
        assertEquals("해당 채팅방이 존재하지 않습니다. roomId=" + chatroom.getRoomId(), thrown.getMessage());
    }

    @Test
    void findChatMessages_Success() {
        //given
        ChatMessage message = chatDto.toEntity(chatroom);

        when(messageRepository.findChatMessages(chatroom.getRoomId()))
                .thenReturn(Collections.singletonList(message));
        // when
        List<ChatMessage> messages = chatService.findChatMessages(chatroom.getRoomId());

        // then
        assertNotNull(messages);
        assertFalse(( messages).isEmpty());
        verify(messageRepository).findChatMessages(chatroom.getRoomId());
    }

    @Test
    void findChatroomList_Success() {
        // given - sender와 receiver가 동일
        when(chatRoomRepository.findChatroomList(sender, sender)).thenReturn(Collections.singletonList(chatroom));

        // when
        List<Chatroom> chatroomList = chatService.findChatroomList(sender);

        // then
        assertNotNull(chatroomList);
        assertFalse(chatroomList.isEmpty());
        verify(chatRoomRepository).findChatroomList(sender, sender);
    }
}
