package ru.zakharovre.inside.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zakharovre.inside.dto.MessageRequestDto;
import ru.zakharovre.inside.entity.Message;
import ru.zakharovre.inside.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private static final String HISTORY_REGEX = "history\\s[0-9]+";
    private final MessageRepository messageRepository;
    private final ObjectMapper mapper;

    public MessageService(MessageRepository messageRepository, ObjectMapper mapper) {
        this.messageRepository = messageRepository;
        this.mapper = mapper;
    }

    public String process(MessageRequestDto messageRequestDto) {
        if (messageRequestDto.getMessage().matches(HISTORY_REGEX)) {
            int amount = Integer.parseInt(messageRequestDto.getMessage().substring(8));
            return getMessages(amount);
        }
        Message message = new Message();
        message.setUsername(messageRequestDto.getUsername());
        message.setMessage(messageRequestDto.getMessage());
        message.setCreatedAt(LocalDateTime.now());
        message = messageRepository.save(message);
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse object");
        }
    }

    private String getMessages(int amount) {
        Page<Message> page = messageRepository.findAll(PageRequest.of(0, amount, Sort.by(Sort.Order.desc("id"))));
        List<Message> messages = page.getContent();
        try {
            return mapper.writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse object");
        }
    }

    public void deleteAll() {
        messageRepository.deleteAll();
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
