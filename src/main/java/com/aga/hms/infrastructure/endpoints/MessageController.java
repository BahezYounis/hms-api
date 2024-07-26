package com.aga.hms.infrastructure.endpoints;

import com.aga.hms.infrastructure.Out.MessageSender;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {

    private final MessageSender messageSender;

    public MessageController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestBody String message) {
        messageSender.sendMessage(message);
    }
}