package com.example.botdemo.handler;

import com.example.botdemo.command.Command;
import com.example.botdemo.command.UnknownCommand;
import com.example.botdemo.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler implements Handler<Message> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RegistrationService registrationService;


    @Override
    public void handle(Message message) {
        if (!registrationService.registerUser(message)) {
            Command command = getCommand(message);
            command.execute(message);
        }
    }

    private Command getCommand(Message message) {
        try {
            return (Command) applicationContext.getBean(Class.forName(String.format(
                    "com.example.botdemo.command.%sCommand",
                    StringUtils.capitalize(message.getText().trim().substring(1)))));
        } catch (Exception e) {
            return new UnknownCommand();
        }
    }
}
