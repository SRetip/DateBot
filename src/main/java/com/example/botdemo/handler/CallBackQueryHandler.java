package com.example.botdemo.handler;

import com.example.botdemo.command.GoCommand;
import com.example.botdemo.domain.Duplet;
import com.example.botdemo.domain.UserReact;
import com.example.botdemo.domain.Usser;
import com.example.botdemo.messagesender.MessageSender;
import com.example.botdemo.service.DupletService;
import com.example.botdemo.service.UsserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CallBackQueryHandler implements Handler<CallbackQuery> {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private DupletService dupletService;
    @Autowired
    private UsserService usserService;
    @Autowired
    private GoCommand goCommand;


    @SneakyThrows
    @Override
    public void handle(CallbackQuery callbackQuery) {
        Duplet duplet = new Duplet();
        duplet.setId(Long.parseLong(callbackQuery.getData().split(" ")[1]));
        try {
            if (dupletService.evaluateDuplet(duplet, UserReact.valueOf(callbackQuery.getData().split(" ")[0]))) {
                Duplet duplet1 = dupletService.getDupletById(duplet.getId());
                messageSender.sendMessage(SendMessage
                        .builder()
                        .chatId(duplet1.getFirstUsser().getId())
                        .text("У вас є співпадіння з користувачем, ти можеш з ним зв'язатися! Його юзернейм - " + duplet1.getFirstUsser().getUsernameTelegram())
                        .build());
                messageSender.sendMessage(SendMessage
                        .builder()
                        .chatId(duplet1.getSecondUsser().getId())
                        .text("У вас є співпадіння з користувачем, ти можеш з ним зв'язатися! Його юзернейм - " + duplet1.getSecondUsser().getUsernameTelegram())
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message message = callbackQuery.getMessage();
        Usser usser = usserService.getSelf(message.getChatId());
        duplet = dupletService.getDupletForUser(usser);
        if (duplet == null) {
            messageSender.sendMessage(SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("На жаль, зараз нема цікавих для тебе анкет, спробуй пізніше за допомогою команди /go")
                    .build());
            return;
        }
        goCommand.sendDuplet(duplet, message);
    }

}
