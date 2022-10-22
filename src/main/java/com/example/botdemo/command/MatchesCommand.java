package com.example.botdemo.command;

import com.example.botdemo.domain.Usser;
import com.example.botdemo.messagesender.MessageSender;
import com.example.botdemo.service.DupletService;
import com.example.botdemo.service.UsserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MatchesCommand implements Command {
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
        StringBuilder result = new StringBuilder();
        dupletService.getMatchedPairForUser(usser1).forEach(u -> {
            result.append(u.getName()).append(" - ").append(u.getUsernameTelegram()).append(",\n");
        });
        result.trimToSize();
        messageSender.sendMessage(SendMessage
                .builder()
                .chatId(usser1.getId())
                .text(result.toString())
                .build());
    }
}
