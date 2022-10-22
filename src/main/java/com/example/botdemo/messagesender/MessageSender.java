package com.example.botdemo.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface MessageSender {
    void sendMessage(SendMessage message);

    void sendPhoto(SendPhoto message);

    void sendMediaGroup(SendMediaGroup mediaGroup);
}
