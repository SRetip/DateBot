package com.example.botdemo.command;

import com.example.botdemo.messagesender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class UnknownCommand implements Command {
    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Message message) {
        messageSender.sendMessage(SendMessage
                .builder()
                .chatId(message.getChatId())
                .text("Невідома команда")
                .build());
    }
}
