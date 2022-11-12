package ru.zakharovre.inside.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zakharovre.inside.dto.MessageRequestDto;
import ru.zakharovre.inside.service.MessageService;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public String postMessage(@RequestBody MessageRequestDto messageRequestDto) {
        return messageService.process(messageRequestDto);
    }
}
