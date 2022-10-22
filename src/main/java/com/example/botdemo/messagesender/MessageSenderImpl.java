package com.example.botdemo.messagesender;

import com.example.botdemo.BadooDemoBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {
    private BadooDemoBot demoBot;

    @Autowired
    public void setDemoBot(BadooDemoBot demoBot) {
        this.demoBot = demoBot;
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
            demoBot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPhoto(SendPhoto message) {
        try {
            demoBot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMediaGroup(SendMediaGroup mediaGroup) {
        try {
            demoBot.execute(mediaGroup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
