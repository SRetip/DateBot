package com.example.botdemo.command;

import com.example.botdemo.domain.Duplet;
import com.example.botdemo.domain.Usser;
import com.example.botdemo.messagesender.MessageSender;
import com.example.botdemo.service.DupletService;
import com.example.botdemo.service.UsserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GoCommand implements Command {
    @Autowired
    private DupletService dupletService;

    @Autowired
    private UsserService usserService;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Message message) {
        Usser usser1 = null;
        try {
            usser1 = usserService.getSelf(message.getChatId());
        } catch (Exception e) {
            messageSender.sendMessage(SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Ви не зареєстровані, будьласка зареєструйтесь за допомогою /start")
                    .build());
            return;
        }
        Duplet duplet = dupletService.getDupletForUser(usser1);
        if (duplet == null) {
            messageSender.sendMessage(SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("На жаль, зараз нема цікавих для тебе анкет, спробуй пізніше за допомогою команди /go")
                    .build());
            return;
        }
        sendDuplet(duplet, message);
    }

    public void sendDuplet(Duplet duplet, Message message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = prepareKeyboard(duplet);
        if (duplet.getFirstUsser().getId().equals(message.getChatId())) {
            List<InputMedia> photos = duplet.getSecondUsser().getPhotos().stream().map(p -> InputMediaPhoto.builder().media(p.getPhotoId()).build()).collect(Collectors.toList());
            messageSender.sendMediaGroup(SendMediaGroup.builder()
                    .chatId(message.getChatId())
                    .medias(photos)
                    .build());
            messageSender.sendMessage(SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Age - " + duplet.getSecondUsser().getAge() + " name - " + duplet.getSecondUsser().getName() + " gender - " + duplet.getSecondUsser().getGender() + " about - " + duplet.getSecondUsser().getDetails())
                    .replyMarkup(inlineKeyboardMarkup).build());

        } else {
            List<InputMedia> photos = duplet.getFirstUsser().getPhotos().stream().map(p -> InputMediaPhoto.builder().media(p.getPhotoId()).build()).collect(Collectors.toList());
            messageSender.sendMediaGroup(SendMediaGroup.builder()
                    .chatId(message.getChatId())
                    .medias(photos)
                    .build());
            messageSender.sendMessage(SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Age - " + duplet.getSecondUsser().getAge() + " name - " + duplet.getSecondUsser().getName() + " gender - " + duplet.getSecondUsser().getGender() + " about - " + duplet.getSecondUsser().getDetails())
                    .replyMarkup(inlineKeyboardMarkup).build());
        }
    }

    private InlineKeyboardMarkup prepareKeyboard(Duplet duplet) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                Stream.of(
                        InlineKeyboardButton.builder()
                                .text("ТАК")
                                .callbackData("LIKED " + duplet.getId())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("НІ")
                                .callbackData("DISLIKED " + duplet.getId())
                                .build()).collect(Collectors.toList()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }
}
