package com.example.botdemo.service;

import com.example.botdemo.cache.Cache;
import com.example.botdemo.domain.UserState;
import com.example.botdemo.domain.Usser;
import com.example.botdemo.messagesender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    @Autowired
    private Cache<Usser> usserCache;
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private UsserService usserService;
    @Autowired
    private UserPhotoService userPhotoService;
    @Autowired
    private ValidationService validationService;

    private Map<Long, List<String>> usersPhoto = new HashMap<>();

    public boolean registerUser(Message message) {
        Usser usser = usserCache.getById(message.getChatId());
        if (usser != null) {
            switch (usser.getState()) {
                case REGISTRATION_AGE:
                    addAge(message, usser);
                    break;
                case REGISTRATION_GENDER:
                    addGender(message, usser);
                    break;
                case REGISTRATION_CITY:
                    addCity(message, usser);
                    break;
                case REGISTRATION_NAME:
                    addName(message, usser);
                    break;
                case REGISTRATION_DETAILS:
                    addDetails(message, usser);
                    break;
                case REGISTRATION_CONTACTS:
                    addContacts(message, usser);
                    break;
                case REGISTRATION_INTERESTED_AGE:
                    addInterestedAge(message, usser);
                    break;
                case REGISTRATION_INTERESTED_GENDER:
                    addInterestedGender(message, usser);
                    break;
                case REGISTRATION_ADD_PHOTO:
                    addPhotos(message, usser);
                    break;
                case REGISTRATION_SAVE:
                    saveUser(message, usser);
                    break;
            }
            return true;
        }
        return false;
    }

    private void saveUser(Message message, Usser usser) {
        if (message.hasText() && message.getText().equals("Готово")) {
            usserService.addUsser(usser);
            userPhotoService.updatePhotos(usersPhoto.get(usser.getId()), usser.getId());
            usserCache.removeById(usser.getId());
            List<InputMedia> list = usersPhoto.get(message.getChatId()).stream().map(p -> InputMediaPhoto.builder().media(p).build()).collect(Collectors.toList());
            usersPhoto.remove(usser.getId());
            list.get(0).setCaption("Твоя анкета:\n" + usser.getName() + ", " + usser.getAge() + " років, про себе:\n" + usser.getDetails() + "\n Якщо хочеш щось змінити введи команду \"/settings\", а для початку пошуку людей /go");
            messageSender.sendMediaGroup(SendMediaGroup
                    .builder()
                    .chatId(message.getChatId())
                    .medias(list)
                    .build());
        }
    }

    private void addPhotos(Message message, Usser usser) {
        if (message.hasPhoto()) {
            if (usersPhoto.get(message.getChatId()) == null) {
                List<String> photos = new ArrayList<>();
                try {
                    photos.add(message
                            .getPhoto()
                            .stream()
                            .max(Comparator
                                    .comparingInt(a -> a.getHeight() + a.getWidth()))
                            .orElseThrow(Exception::new).getFileId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                usersPhoto.put(message.getChatId(), photos);
            } else {
                try {
                    usersPhoto.get(message.getChatId()).add(message
                            .getPhoto()
                            .stream()
                            .max(Comparator
                                    .comparingInt(a -> a.getHeight() + a.getWidth()))
                            .orElseThrow(Exception::new).getFileId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        usser.setState(UserState.REGISTRATION_SAVE);
    }

    private void addInterestedGender(Message message, Usser usser) {
        usser.setPreferredGender(validationService.validateGender(message));
        usser.setState(UserState.REGISTRATION_ADD_PHOTO);
        usser.cleanPhotos();
        messageSender.sendMessage(SendMessage.builder()
                .text("І на останок, відправ 2 - 10 своїх фотографій і натисни на кнопку \"Готово\"")
                .replyMarkup(ReplyKeyboardMarkup
                        .builder()
                        .resizeKeyboard(true)
                        .oneTimeKeyboard(true)
                        .keyboardRow(new KeyboardRow() {
                            {
                                add(KeyboardButton
                                        .builder()
                                        .text("Готово")
                                        .build());
                            }
                        })
                        .build())
                .chatId(usser.getId())
                .build()
        );
    }

    private void addInterestedAge(Message message, Usser usser) {
        int[] ageLimit = validationService.validateAgeRegion(message);
        usser.setDesiredLowerAgeLimit(ageLimit[0]);
        usser.setDesiredUpperAgeLimit(ageLimit[1]);
        usser.setState(UserState.REGISTRATION_INTERESTED_GENDER);
        messageSender.sendMessage(SendMessage.builder()
                .text("Тепер введи стать людини яку ти шукаеш (ч/ж)")
                .chatId(usser.getId())
                .replyMarkup(ReplyKeyboardMarkup
                        .builder()
                        .resizeKeyboard(true)
                        .oneTimeKeyboard(true)
                        .keyboardRow(new KeyboardRow() {
                            {
                                add(KeyboardButton
                                        .builder()
                                        .text("ч")
                                        .build());
                                add(KeyboardButton
                                        .builder()
                                        .text("ж")
                                        .build());
                            }
                        })
                        .build()
                )
                .build()
        );
    }

    private void addContacts(Message message, Usser usser) {
        if (message.hasText()) {
            String telegUsername = message.getChat().getUserName();
            if (telegUsername == null) {
                messageSender.sendMessage(SendMessage
                        .builder()
                        .chatId(message.getChatId())
                        .text("Переконайтеся в тому що у вас є телеграм єзернейм(із собачкою) та натисніть \"Далі\"")
                        .replyMarkup(ReplyKeyboardMarkup
                                .builder()
                                .oneTimeKeyboard(true)
                                .resizeKeyboard(true)
                                .keyboardRow(new KeyboardRow() {
                                    {
                                        add(KeyboardButton
                                                .builder()
                                                .text("Далі")
                                                .build());
                                    }
                                })
                                .build())
                        .build());
                return;
            }
            usser.setUsernameTelegram("@" + telegUsername);
            usser.setState(UserState.REGISTRATION_INTERESTED_AGE);
            messageSender.sendMessage(SendMessage.builder()
                    .text("Тепер введи вік людини, яка тобі цікава у форматі \"число-чило\", наприклад 18-20")
                    .chatId(usser.getId())
                    .build()
            );
        }
    }

    private void addDetails(Message message, Usser usser) {
        if (message.hasText()) {
            usser.setDetails(message.getText());
            usser.setState(UserState.REGISTRATION_CONTACTS);
            messageSender.sendMessage(SendMessage.builder()
                    .text("Тепер щоб з тобою можна було зв'язатися, нам потрібен твій телеграм юзернейм(той що з собачкою), переконайся що він в тебе є і натисни \"Далі\"")
                    .chatId(usser.getId())
                    .replyMarkup(ReplyKeyboardMarkup
                            .builder()
                            .oneTimeKeyboard(true)
                            .resizeKeyboard(true)
                            .keyboardRow(new KeyboardRow() {
                                {
                                    add(KeyboardButton
                                            .builder()
                                            .text("Далі")
                                            .build());
                                }
                            })
                            .build())
                    .build()
            );
        }
    }

    public void startRegistration(Message message) {
        Usser usser = new Usser();
        usser.setId(message.getChatId());
        usser.setState(UserState.REGISTRATION_AGE);
        usser.setPhotos(new ArrayList<>());
        usserCache.add(usser);
        messageSender.sendMessage(SendMessage
                .builder()
                .chatId(usser.getId())
                .text("Для початку введіть свій вік(цифрою)")
                .build());
    }

    private void addAge(Message message, Usser usser) {
        usser.setAge(validationService.validateAge(message));
        usser.setState(UserState.REGISTRATION_GENDER);
        messageSender.sendMessage(SendMessage.builder()
                .text("Тепер введи свою стать(ч/ж)")
                .chatId(usser.getId())
                .replyMarkup(ReplyKeyboardMarkup
                        .builder()
                        .resizeKeyboard(true)
                        .oneTimeKeyboard(true)
                        .keyboardRow(new KeyboardRow() {
                            {
                                add(KeyboardButton
                                        .builder()
                                        .text("ч")
                                        .build());
                                add(KeyboardButton
                                        .builder()
                                        .text("ж")
                                        .build());
                            }
                        })
                        .build()
                )
                .build()
        );
    }

    private void addGender(Message message, Usser usser) {
        usser.setGender(validationService.validateGender(message));
        usser.setState(UserState.REGISTRATION_CITY);
        messageSender.sendMessage(SendMessage.builder()
                .text("Тепер обери місто(я доделаю клавиатерку, а пока просто вводите с большой букви по нормальному на украинском):")
                .chatId(usser.getId())
                .build()
        );
    }

    private void addCity(Message message, Usser usser) {
        usser.setCity(validationService.validateCity(message));
        usser.setState(UserState.REGISTRATION_NAME);
        messageSender.sendMessage(SendMessage.builder()
                .text("Тепер введи твій нікнейм")
                .chatId(usser.getId())
                .build()
        );
    }

    private void addName(Message message, Usser usser) {
        if (message.hasText()) {
            usser.setName(message.getText());
            usser.setState(UserState.REGISTRATION_DETAILS);
            messageSender.sendMessage(SendMessage.builder()
                    .text("Тепер введи якусь інформацію про себе (чим ти займаєшься, що тебе цікавить, тощо)")
                    .chatId(usser.getId())
                    .build()
            );
        }
    }
}
