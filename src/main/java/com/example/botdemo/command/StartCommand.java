package com.example.botdemo.command;

import com.example.botdemo.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartCommand implements Command {

    @Autowired
    private RegistrationService registrationService;

    @Override
    public void execute(Message message) {
        registrationService.startRegistration(message);
    }
}
