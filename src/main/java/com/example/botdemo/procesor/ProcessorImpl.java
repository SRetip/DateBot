package com.example.botdemo.procesor;

import com.example.botdemo.handler.CallBackQueryHandler;
import com.example.botdemo.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ProcessorImpl implements Processor {
    private CallBackQueryHandler callbackQueryHandler;
    private MessageHandler messageHandler;

    @Autowired
    public void setCallbackQueryHandler(CallBackQueryHandler callbackQueryHandler) {
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Autowired
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void executeMessage(Message message) {
        messageHandler.handle(message);
    }

    @Override
    public void executeCallBackQuery(CallbackQuery callbackQuery) {
        callbackQueryHandler.handle(callbackQuery);
    }
}
