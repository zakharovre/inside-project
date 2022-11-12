package ru.zakharovre.inside.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.zakharovre.inside.dto.MessageRequestDto;
import ru.zakharovre.inside.dto.MessageResponseDto;
import ru.zakharovre.inside.repository.MessageRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService.process(new MessageRequestDto("testUser1", "testMessage1"));
        messageService.process(new MessageRequestDto("testUser2", "testMessage2"));
        messageService.process(new MessageRequestDto("testUser3", "testMessage3"));
    }

    @Test
    void addMessageTest() throws Exception {
        MessageRequestDto addMessage = new MessageRequestDto("addedUser", "addedMessage");
        String savedUsername = objectMapper
                .readValue(messageService.process(addMessage), MessageResponseDto.class)
                .getUsername();
        assertEquals(addMessage.getUsername(), savedUsername);
        assertEquals(4, messageService.findAll().size());
    }

    @Test
    void showHistoryTest() throws Exception {
        MessageRequestDto fourthMessage = new MessageRequestDto("fourth", "test");
        MessageRequestDto lastMessage = new MessageRequestDto("last", "test");
        messageService.process(fourthMessage);
        messageService.process(lastMessage);
        MessageRequestDto showMessage = new MessageRequestDto("showUser", "history 2");
        List<MessageResponseDto> response = objectMapper
                .readValue(messageService.process(showMessage), new TypeReference<>() {

                });
        assertEquals(5, messageService.findAll().size());
        assertEquals(2, response.size());
        assertEquals(lastMessage.getUsername(), response.get(0).getUsername());
        assertEquals(fourthMessage.getUsername(), response.get(1).getUsername());
    }

    @AfterEach
    void tearDown() {
        messageService.deleteAll();
    }
}