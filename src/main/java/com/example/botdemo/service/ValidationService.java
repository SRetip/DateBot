package com.example.botdemo.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

@Component
public class ValidationService {

    @SneakyThrows
    public int validateAge(Message message) {
        if (message.hasText() && message.getText().matches("\\d+")) {
            return Integer.parseInt(message.getText());
        } else throw new Exception();//TODO exception
    }

    @SneakyThrows
    public int[] validateAgeRegion(Message message) {
        if (message.hasText() && message.getText().matches("\\d+-\\d+")) {
            String[] numbers = message.getText().split("-");
            int[] arr = new int[2];
            arr[0] = Integer.parseInt(numbers[0]);
            arr[1] = Integer.parseInt(numbers[1]);
            if ((arr[0] > 16 & arr[1] > 16))
                return Arrays.stream(arr).sorted().toArray();
            else throw new Exception();//TODO exception
        } else throw new Exception();//TODO exception
    }

    @SneakyThrows
    public String validateCity(Message message) {
        if (message.hasText()) {
            return message.getText();
        } else throw new Exception();//TODO exception
    }

    @SneakyThrows
    public String validateGender(Message message) {
        if (message.hasText() && (message.getText().equals("ч") || message.getText().equals("ж")))
            return message.getText();
        throw new Exception();//TODO exception
    }
}
