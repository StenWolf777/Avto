package org.example;

import org.example.repository.UserRepository;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.awt.SystemColor.*;

public class MyBot extends TelegramLongPollingBot {
    private final UserRepository repository = new UserRepository();

    public MyBot(String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasContact()) {
            if (repository.getUserById(update.getMessage().getChatId()).getState() == BotState.PHONE_NUMBER) {
                repository.setPhoneNumber(update.getMessage().getChatId(), update.getMessage().getContact().getPhoneNumber());
                SendMessage message = new SendMessage();
                if (repository.getUserById(update.getMessage().getChatId()).getLanguage().equals("uzb")) {
                    message.setText("Telefon Raqmingiz muvafaqiyatli ro'yxatdan o'tkazildi\n\n" +
                            "Sizga murojat qilishimiz uchun ismingizni kiriting");
                } else {
                    message.setText("Ваш номер телефона успешно зарегистрирован\n" +
                            "Введите свое имя, чтобы мы могли связаться с вами");
                }
                message.setChatId(update.getMessage().getChatId());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (text.equals("/start")) {

                if (repository.getUserById(update.getMessage().getChatId()) == null) {
                    repository.createUser(update.getMessage().getChatId(), BotState.START);
                }
                User user = repository.getUserById(update.getMessage().getChatId());

                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> list = new ArrayList<>();
                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("O'zbek\uD83C\uDDFA\uD83C\uDDFF");
                button.setCallbackData("uzb");
                row.add(button);
                List<InlineKeyboardButton> row1 = new ArrayList<>();
                InlineKeyboardButton button1 = new InlineKeyboardButton();
                button1.setText("Русский\uD83C\uDDF7\uD83C\uDDFA");
                button1.setCallbackData("rus");
                row1.add(button1);
                list.add(row1);
                list.add(row);
                markup.setKeyboard(list);
                SendMessage message = new SendMessage();
                message.setText("Tilni tanlang\uD83D\uDC47\nВыберите язык\uD83D\uDC47\nТилни танланг\uD83D\uDC47");
                message.setChatId(chatId);
                message.setReplyMarkup(markup);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (text.equals("/location")) {
                SendLocation sendLocation = new SendLocation();
                sendLocation.setLatitude(41.2318117);
                sendLocation.setLongitude(69.2066512);
                sendLocation.setChatId(chatId);
                try {
                    execute(sendLocation);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                String phoneNumber = repository.getUserById(chatId).getPhoneNumber();
                SendMessage message1 = new SendMessage();
                message1.setText("Yangi kontakt qoldirildi:\n\nTelefon raqam:" + phoneNumber + " \nIsmi:" + update.getMessage().getFrom().getFirstName());
                message1.setChatId("-4013704389");
                try {
                    execute(message1);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                ForwardMessage forwardMessage = new ForwardMessage();
                forwardMessage.setChatId("-4013704389");
                forwardMessage.setMessageId(update.getMessage().getMessageId());
                forwardMessage.setFromChatId(chatId);
                try {
                    execute(forwardMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

                SendMessage message = new SendMessage();
                message.setText("Sizning xabaringiz muvaffaqiyatli yuborildi");
                message.setChatId(chatId);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            switch (data) {
                case "uzb" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    repository.setUpdateLanguage(chatId, "uzb");
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    SendMessage message = new SendMessage();
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("Mashinalar");
                    button.setCallbackData("mash");
                    row.add(button);

                    List<InlineKeyboardButton> row1 = new ArrayList<>();
                    InlineKeyboardButton button34 = new InlineKeyboardButton();
                    button34.setText("Aloqa bo'limi");
                    button34.setCallbackData("alqblm");
                    row1.add(button34);

                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button33 = new InlineKeyboardButton();
                    button33.setText("Avto servis bo'limi");
                    button33.setCallbackData("avts");
                    row4.add(button33);
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("Onlayn shartnoma tuzish");
                    button1.setCallbackData("Onlnshrt");
                    row.add(button1);
//                    List<InlineKeyboardButton> row6 = new ArrayList<>();
                    list.add(row);
//                    list.add(row6);
                    list.add(row1);
                    list.add(row4);
                    markup.setKeyboard(list);

                    message.setText("Quyidagailardan birini tanlang.");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "rus" -> {

                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    repository.setUpdateLanguage(chatId, "rus");
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    SendMessage message = new SendMessage();
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("Машины");
                    button.setCallbackData("mash-ru");
                    row.add(button);
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("Онлаин переговоры");
                    button1.setCallbackData("Onlnshrtru");
                    row.add(button1);
                    List<InlineKeyboardButton> row1 = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button5 = new InlineKeyboardButton();
                    button5.setText("Авто сервис");
                    button5.setCallbackData("avsru");
                    row2.add(button5);
                    InlineKeyboardButton button34 = new InlineKeyboardButton();
                    button34.setText("Отдел связи");
                    button34.setCallbackData("otdsru");
                    row1.add(button34);
                    list.add(row);
                    list.add(row1);
                    list.add(row2);
                    markup.setKeyboard(list);

                    message.setText("Выберите Один из Них.");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "mash" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("BESTUNE");
                    button.setCallbackData("best");
                    row.add(button);
                    list.add(row);
                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button4 = new InlineKeyboardButton();
                    button4.setText("JETOUR");
                    button4.setCallbackData("jet");
                    row4.add(button4);
                    list.add(row4);
                    List<InlineKeyboardButton> row1 = new ArrayList<>();
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("HONGQI");
                    button1.setCallbackData("hon");
                    row.add(button1);
                    List<InlineKeyboardButton> row45 = new ArrayList<>();
                    InlineKeyboardButton button45 = new InlineKeyboardButton();
                    button45.setText("AIQAR");
                    button45.setCallbackData("aiq");
                    row45.add(button45);
                    list.add(row45);
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("DONGFENG");
                    button2.setCallbackData("don");
                    row.add(button2);
                    list.add(row2);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    SendMessage message = new SendMessage();
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message1 = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Qaysi avtomobil haqida ma'lumotga ega bo'lmoqchisiz?");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "mash-ru" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("BESTUNE");
                    button.setCallbackData("bestru");
                    row.add(button);
                    list.add(row);
                    List<InlineKeyboardButton> row7 = new ArrayList<>();
                    List<InlineKeyboardButton> row6 = new ArrayList<>();
                    InlineKeyboardButton button9 = new InlineKeyboardButton();
                    button9.setText("JETOUR");
                    button9.setCallbackData("jetru");
                    row6.add(button9);
                    list.add(row6);
                    List<InlineKeyboardButton> row1 = new ArrayList<>();
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("HONGQI");
                    button1.setCallbackData("honru");
                    row.add(button1);
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("DONGFENG");
                    button2.setCallbackData("donru");
                    row.add(button2);
                    list.add(row2);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    InlineKeyboardButton button23 = new InlineKeyboardButton();
                    button23.setText("AIQAR");
                    button23.setCallbackData("aiq-ru");
                    row3.add(button23);
                    list.add(row3);
                    SendMessage message = new SendMessage();
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    markup.setKeyboard(list);
                    message.setReplyMarkup(markup);
                    message.setText("О какой машине вы хотели бы узнать?");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "best" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("BESTUNE T55");
                    button1.setCallbackData("bestuneT55");
                    row.add(button1);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("BESTUNE B70");
                    button2.setCallbackData("bestuneB70");
                    row2.add(button2);
                    List<InlineKeyboardButton> row5 = new ArrayList<>();
                    InlineKeyboardButton button4 = new InlineKeyboardButton();
                    button4.setText("BESTUNE T33");
                    button4.setCallbackData("bestune33");
                    row3.add(button4);
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("BESTUNE T99");
                    button.setCallbackData("bestuneT99");
                    InlineKeyboardButton button3 = new InlineKeyboardButton();
                    button3.setText("BESTUNE B70S");
                    button3.setCallbackData("bestune B70S");
                    row.add(button3);
                    row.add(button);
                    list.add(row);
                    list.add(row2);
                    list.add(row3);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Avtomobillardan birini tanlang.");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "bestru" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("BESTUNE T55");
                    button1.setCallbackData("bestuneruT55");
                    row.add(button1);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("BESTUNE B70");
                    button2.setCallbackData("bestuneruB70");
                    row2.add(button2);
                    List<InlineKeyboardButton> row5 = new ArrayList<>();
                    InlineKeyboardButton button4 = new InlineKeyboardButton();
                    button4.setText("BESTUNE T33");
                    button4.setCallbackData("bestuneru33");
                    row3.add(button4);
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("BESTUNE T99");
                    button.setCallbackData("bestuneruT99");
                    InlineKeyboardButton button3 = new InlineKeyboardButton();
                    button3.setText("BESTUNE B70S");
                    button3.setCallbackData("bestuneru B70S");
                    row.add(button3);
                    row.add(button);
                    list.add(row);
                    list.add(row2);
                    list.add(row3);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Выберите Щдин из Них");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "bestuneT99" -> {
                    List<InputMedia> list = new ArrayList<>();

                    InputMedia file1 = new InputMediaPhoto("https://carsdo.ru/job/CarsDo/photo-gallery/faw/bestune-t99-1-13.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/2/23/Bestune_T99_facelift_IMG003.jpg");
                    InputMedia file4 = new InputMediaPhoto("https://www.major-faw.ru/files/resources/3_in.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file4);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);
                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage massage = new SendMessage();
                    massage.setText("Narxi 512 000.000.00so'm");
                    massage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(massage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(40);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "bestuneB70" -> {
                    List<InputMedia> list = new ArrayList<>();

                    InputMedia file1 = new InputMediaPhoto("https://bestunekw.com/wp-content/uploads/2022/08/Bestune-B70-Cover_01-1-1-1536x864.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://s.auto.drom.ru/i24280/c/photos/fullsize/faw/besturn_b70/faw_besturn_b70_1128332.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s.auto.drom.ru/i24280/c/photos/fullsize/faw/besturn_b70/faw_besturn_b70_1128333.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage massage = new SendMessage();
                    massage.setText("Uzunligi 4800mm\nEni 1820mm\n  Bo'yi 1472mm\nBak Hajmi 58Litr\nYukhona Hajmi 522Litr" +
                            "Eng Yuqori Tezlik 220kms\n100km ga Yoqilq' Sarfi 7.5-8Litr\nDvigatel 1.5 turbo 169 Ot Kuchi\n Old Padveskaga Ega \nAvtomatik Tormoz\n" +
                            "Adaptirovcanniy Kruiz Kontrol\nZavotdan Tanirovkabilan Chiqadi\nYUqori Sifatli charmda Ishlangan O'rindiqlar\n6 Bosqichli Elektro O'rindiqlar\n2 zonali Avto Sovugich\n" +
                            "Orqa Oynaning Elektron Isitgichi\nAvto Yonadigan Faralar\nKalitsiz Ochish Tizimi\n360 Gradusli Kamera\n422 400 000.00so'm");
                    massage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(massage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(44);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "bestuneT55" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file7 = new InputMediaPhoto("https://autoreview.ru/images/Article/1718/Article_171854_860_575.jpg");
                    InputMedia file8 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/March/17/faw-t55-4.jpg");
                    InputMedia file = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2023/April/12/FAW-Bestune-T55-5.jpg");
                    list.add(file);
                    list.add(file7);
                    list.add(file8);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);
                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    SendMessage message = new SendMessage();
                    message.setText("Narxi 318 720 000.00so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(42);

                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "bestune33" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file8 = new InputMediaPhoto("https://i.infocar.ua/i/2/6010/114626/1920x.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://avtoaziya.ru/images/FOTO/16/FAW-Bestune-T33-2019-2020-8.jpg");
                    InputMedia file10 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Bestune_T33_img04.jpg/305px-Bestune_T33_img04.jpg");
                    list.add(file8);
                    list.add(file9);
                    list.add(file10);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);
                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(96);

                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "hon" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("HONGQI HS5");
                    button1.setCallbackData("HongqiHS5");
                    row.add(button1);
                    InlineKeyboardButton button10 = new InlineKeyboardButton();
                    button10.setText("HONGQI H9");
                    button10.setCallbackData("Hongqi H9");
                    row.add(button10);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("HONGQI H5");
                    button2.setCallbackData("HongqiH5");
                    row2.add(button2);
                    List<InlineKeyboardButton> row5 = new ArrayList<>();
                    InlineKeyboardButton button4 = new InlineKeyboardButton();
                    button4.setText("HONGQI HS7");
                    button4.setCallbackData("HongqiHS7");
                    row3.add(button4);
                    InlineKeyboardButton button3 = new InlineKeyboardButton();
                    button3.setText("HONGQI EHS9");
                    button3.setCallbackData("HongqiEHS9");
                    row3.add(button3);
                    list.add(row);
                    list.add(row2);
                    list.add(row3);
                    list.add(row5);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Avtomobillardan birini tanlang.");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }


                case "HongqiH5" -> {
                    List<InputMedia> list = new ArrayList<>();

                    InputMedia file1 = new InputMediaPhoto("https://rg.ru/uploads/images/2023/03/31/111-2_3c6.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2022/06/hongqi_h5_new_5_1000.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://static.78.ru/images/uploads/1691761917718.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4945mm\nEni 1845mm\nBalandligi 1470\nOq'irligi 1615kg\nGGibrid ham Benzinda ham Tokda Harakatlanadi\n" + "\n" +
                            "Yulxona Hajmi 540Litr\nEng Yuqori Tezlik 220kms\n100kmga Yoqilg'i Sarfi 6 Litr\nDvigatel 1.8 turbo 197 Ot Kuchi\nOld Padveska Mavjud\n" + "\n" +
                            "Old Ikta g'ildiraklari Tortadi\nAvtamatik Avcvaria Tormozi Mavjud]\nKruiz Boshqaruv Tizimi ham Mavjud\nAvto Parkovka Tizimiham Mavjud\n" + " \n" +
                            "64 Rangli Yoritish Tizimi\n O'rindiqlarni Obduv va Padagrev qilish sistemasi\n6 Bosqichli Elektra O'rindiqlar\nIkki Iqlimliy Harorat Kantrolleri\nSlepoy Zona Ushlash\n" + "\n" +
                            "Super Havo Filtiri\n Faralarni Avto Boshqaruvi\n360 grdusli Kamera\n Uzoqni Yorituvchi Yorig'lik Tizimi\n Sifatliy Dinamiklar\n Narxi 30.000$");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1387);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "HongqiHS5" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://motor.ru/imgs/2022/12/26/08/5729758/f4753a2c0e3af2b3a4f93d1b6826a3b5f5be6642.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2019/May/24/hongqi-hs5-4.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2019/May/24/hongqi-hs5-6.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);

                    }

                    SendMessage message = new SendMessage();
                    message.setText("Narxi 46.000$\nDvigatel 2 turbo 224 Ot Kuch\nEng Yuqori Tezlik 240kms\nUzunligi 4760mm\nEni 1907mm\nBalandligi 1700\n" +
                            "Yukxona Hajmi 342Litr\n 100kmga Yoqilq'i Sarfi 8.2-8.7Litr\n 100kmga Tezlikka 8.2 sikuntda Chiqadi \n" +
                            "Kruiz Kantrol\n Avto Tormoz \n O'rindiqlarni Isitish va Sovutish Tizimi\nIkta Katta Display\nBlutuz\nYuqori Sifatliy Charmdan Qilinga O'ruindiqlar" +
                            "Panarama Tom\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(38);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "HongqiHS7" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Hongqi_HS7_001.jpg/1200px-Hongqi_HS7_001.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2019/July/09/hongqi-hs7-4.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s.auto.drom.ru/i24275/c/photos/fullsize/hongqi/hs7/hongqi_hs7_1102797.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(34);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException ex) {
                        throw new RuntimeException(ex);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Dvigatel 3L Compressor Atmos 326 Ot Kuchi\nUzunligi5035mm\nEni1989mm\nBalandligi1756mm\nEng Yuqori Tezlik 240kms\n" +
                            "7O'rindiqliy \n 100 km ga Yoqilq'i Sarfi 9.5Litr\n100km ga 7.5 sekuntda chiqadi\npo;niy Privod\nAvto Tormoz\nPnevmatik Padveska\n" +
                            "O'rindiqlarni Isitish va so'vutish Tizimi\nO'rindiqlar Alikantarodan Ishlangn\n14 dinamik\nDisplay12.3 dyum\nRazetk 220 B\n" +
                            "Slepoy zona Ushlash\n360 gradusli Kamera\nNarxi 60.000$");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "Hongqi H9" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2020/January/09/hongqi-h9-add.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://www.ixbt.com/img/n1/news/2023/7/0/1488x0_1_autohomecar__ChxkmWSK8lSAGiPWACNlI6Fxl5k330_large.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/4/4b/Hongqi_H9_013.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 5137mm\nEni 1904mm\nBalandligi 1493mm\n4 O'rindiqliy\nYoqilq'i Sarfi 100kmga 9Litr\nKrutyashiy Moment 400\nDvigatel 283 Ot Kuchi\nDvigatel Hajmi 3T 6v\nBak Hajmi 62Litr\n100km ga 7.1 sekuntda chiqadi\n " +
                            "\"Pnevmatik Padveska \n Pnarama Tom\nSveta Diod Faralar\n O'zi Avtyamatik Yoqiladigan Ertalabki Chiroqlar\\nKo'p Qavatli Oyna\\nOrqa Oynalar Hotirasi bor\\nRul Isitgichi\\nO'rindiqlarning Charmi Nappa va Alikantaro\\nOld Sidenianing Kalitlari\\nOld Sidenialarni Isitish va Sovutish Tizimi\\n4 Iqlimliy Avto Sovutgich\\n\" +\n" +
                            "\"4 O'rindiq Elektro Boshqaruvga ega \nPribor Xotirasi Displaydan\n12 Dinamiklar\nMusiqaliy 253 Rang\\nAvto Parkovka\\nKalitdsiz Avtomabilni Ochish Imkonirati\\nMabil Telefondan Avtomabilni Ochish Imkoniyati\\nAvto Vidio Indikator\\nWi-Fi Boshqaru Tizimi\\nSinsiz Quvatlash Tizimi\\nAvto Tormoz\\nTezlik Haqida Beriladiga Avtomatik Malumot\\n\" +\n" +
                            "\"Slepoy Zonalarni Korish Funksiasi\nHaydovchi Holatini Tekshirish\nAdaptirovanniy Kruiz Kontrol\\n360 Gradusli Kamera\\nNarxi 100.000$");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1367);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "HongqiEHS9" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://hongqi-avilon.ru/upload/iblock/71c/qlga3dv50z5m2fwdhznut7rvts92qvm7.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://hongqi-avilon.ru/upload/iblock/915/1mm9jw3degjgpc5wezwd1o6okjedaxiu.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s0.rbk.ru/v6_top_pics/media/img/3/48/756642878091483.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 5209mm\nEni 2010mm\nBalandligi 1713mm\nG'ildirak Bazasi 3110mm\nOg'irligi 2620kg\n" +
                            " O'rindiq Joylashhuvi 2+2+2\nAkumulyator Hajmi 99 kilovat\n Krutyashiy Moment 300+450\nDvigatel 405kilovat\nElektro Mobil\n" +
                            "Elektr Zahirasi 600 km\nABS Sistemasi\nAvto Tormoz +\nOrqa padveska +\n100kms ga 4.9 sekund\n3 qatorda 2 o'rindiq mavjud\nIqlim nazorati +\nSalon ichidagi chiroqlar\nAvtomat faralar\n360 gradusli kamera\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(32);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "don" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("DONGFENG  T5 EVO");
                    button2.setCallbackData("DONGFENG T5 EVO");
                    row2.add(button2);
                    List<InlineKeyboardButton> row13 = new ArrayList<>();
                    InlineKeyboardButton button34 = new InlineKeyboardButton();
                    button34.setText("AEOLUS SHINE GS ATMOS");
                    button34.setCallbackData("AEOLUS SHINE GS ATMOS");
                    row13.add(button34);
                    List<InlineKeyboardButton> row23 = new ArrayList<>();
                    InlineKeyboardButton button21 = new InlineKeyboardButton();
                    button21.setText("AEOLUS SHINE GS TURBO");
                    button21.setCallbackData("AEOLUS SHINE GS TURBO");
                    row23.add(button21);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    InlineKeyboardButton button11 = new InlineKeyboardButton();
                    button11.setText("AEOLUS HUGE");
                    button11.setCallbackData("AEOLUS HUGE");
                    row3.add(button11);
                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button12 = new InlineKeyboardButton();
                    button12.setText("DONGFENG AEOLUS YIXUAN MAX");
                    button12.setCallbackData("DONGFENG AEOLUS YIXUAN MAX");
                    row4.add(button12);
                    List<InlineKeyboardButton> row5 = new ArrayList<>();
                    InlineKeyboardButton button13 = new InlineKeyboardButton();
                    button13.setText("FENGON 600");
                    button13.setCallbackData("FENGON 600");
                    row5.add(button13);
                    List<InlineKeyboardButton> row6 = new ArrayList<>();
                    InlineKeyboardButton button14 = new InlineKeyboardButton();
                    button14.setText("GLORY 330S");
                    button14.setCallbackData("GLORY 330S");
                    row6.add(button14);
                    List<InlineKeyboardButton> row7 = new ArrayList<>();
                    InlineKeyboardButton button15 = new InlineKeyboardButton();
                    button15.setText("TUNLAND G9");
                    button15.setCallbackData("TUNLAND G9");
                    row7.add(button15);
                    List<InlineKeyboardButton> row10 = new ArrayList<>();
                    InlineKeyboardButton button17 = new InlineKeyboardButton();
                    button17.setText("DONGFENG FENGON S508 EVR");
                    button17.setCallbackData("DONGFENG FENGON S508 EVR");
                    row10.add(button17);
                    List<InlineKeyboardButton> row9 = new ArrayList<>();
                    InlineKeyboardButton button7 = new InlineKeyboardButton();
                    button7.setText("AEOLUS SHINE");
                    button7.setCallbackData("AEOLUS SHINE");
                    row9.add(button7);
                    list.add(row2);
                    list.add(row3);
                    list.add(row4);
                    list.add(row5);
                    list.add(row9);
                    list.add(row6);
                    list.add(row7);
                    list.add(row10);
                    list.add(row23);
                    list.add(row13);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Avtomabillardan birini tanlang");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "Dongfeng T5 EVO" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://center-av.ru/resources/bodies/ae88b8c43033cfe8f87cd45f917d12918b32c831.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://sc03.alicdn.com/kf/Hf7d5a1d6dcd24716ad94220c686bf318h.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://img.tinbanxe.vn/webp/images/dongfeng/dong-feng-t5-evo/noi-that/danh-gia-dong-feng-t5-evo-noi-that-ghe-ngoi-TINBANXE.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4565mm\nEni 1860mm\nBalandligi 1690\nOq'irligi 1925kg\n Bak Hajmi 55Litr\nYukhona Hajmi 380Litr\nEng Yuqori Tezlik 220kms\n" +
                            "Yoqilq'i Sarfi 100kmga 8,9-5,6,-6.6Litr\nDvigatel 1.5 Turbo 197 Ot Kuchi\nQuyiladigan Yoqilq'i Sifati AI95\nOldi Q'ildiraklari Tortadi\nKruiz Nazorat\nWIFI+\n6Bosqichli Elektro O'rindiqlar\nWertual Asistent\n" +
                            " Kruiz Kontrol\n100kms ga 9.5 sekuntda chiqadi\nOvozli Yordamchi\nAvto boshqaruvga Ega Faralar\nOrqa Kamera\nPanarama Tom\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(52);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "AEOLUS HUGE" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://avcdn.av.by/cargeneration/0002/8067/3335.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://content.onliner.by/news/1100x5616/1c39a3e40420c4f0a5c5eb807b3c3460.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s.auto.drom.ru/i24277/c/photos/fullsize/dongfeng/aeolus_haoji/dongfeng_aeolus_haoji_1112922.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4720mm\nEni 1910\nBalandligi\n1710mm\nUmumiy Oq'irligi 1697kg\nBak Hajmi 52Litr\nDvigatel 1.5 Turbo 170 Ot kuchi\n" +
                            "Eng Yuqori Tezlik 180kms\n100km ga Yoqilq'i Sarfi 5Litr\nQuyiladigan Yoqilq'i Sifati Ai 95\nPadveska Makferson Brendidan\nOldi Tortadi\nKruiz Kontrol\nIchki Chiroqlar\n" +
                            "100km ga 8 sekuntda Chiqadi\n360 gradusli kamera\n Panarama Tom\nAvtamatik Avaria Tormozi\nIsitish va Sovutish Tizimiga Ega Haydovchi O'rindiq'i\n6 Bosqichli Elektro O'rindiqlar\n" +
                            "Avto Boshqaruvga Ega Chiroqlar\nSuper Havo Tozalagich");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(54);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "DONGFENG AEOLUS YIXUAN MAX" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/b/b3/Aeolus_Yixuan_Max_003.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/July/20/dongfeng-aeolus-max2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/July/20/dongfeng-aeolus-max4.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://www.ml-vehicle.com/uploads/202338258/dongfeng-aeolus-yixuan-max363566af-866e-4923-a262-955fe5a79b3a.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4797mm\nEnini 1870mm\nBalandligi 1475mm\nVazni 1486kg\nBak Hajmi 52\nDvigatel 1.5-190 Ot Kuchi\nEng Yuqori Tezlik 210kms\n" +
                            "100km ga 8Litr\nQuyiladigan Yoqilq'i Sifati Ai95-92\nOldi Tortadi\nMustaqil Padveska\n360 gradusli Kamera\nAvto Avaria Tormozi\nKruiz Kontrol\nYuqori Sifatli Charmdan Qilinga n Orindiqlar\n" +
                            "6 Bosqichli Elektro O'rimdiqlar\nAvtomatik Yonuvchi Faralar\nUzoqni Yorituvchi Faralar\nAuto Oyna Tozaluvch\nAuto So'vutgich\nSuper Filtr\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(58);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "FENGON 600" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://i.ytimg.com/vi/BnfaRLCcRiY/maxresdefault.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://images.khmer24.co/24-02-22/dfsk-fengon-600-1-5-turbocharged-miller-cycle-engine--549535170858451480612253-d.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://assets-global.website-files.com/636e3329f2c48fa4e963929e/63ce936cd805d541f3156894_features.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://i.ytimg.com/vi/cuOnExDrca4/maxresdefault.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4720mm\nEni 1865mm\nBalandligi 1710mm\nQuyiladigan Yoqilq'i Sifati Ai92\n100 km ga Yoqil'i Sarfi 7Litr\n" +
                            "Yoqilq'i Baki Hajmi 58Litr\nDvigatel 1.5Turbo\n 184 Ot Kuchi\nO'rindiqlar Joilashuvi 2+2+2\nPanarama Tom\nEuro Standart\nOld Padveska MakPherson\nKruiz Kontrol\n360 gradusli Kamera\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(60);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "GLORY 330S" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://mosautoshina.ru/i/auto/dongfeng_330s_2020.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://sc04.alicdn.com/kf/H4fc6e63cbb594cfd87b4f5646dc1035bZ/255107010/H4fc6e63cbb594cfd87b4f5646dc1035bZ.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://image.made-in-china.com/202f0j00FbgcKfTdLwor/Dfsk-Dongfeng-Glory-330s-Chinese-Gasoline-Hybrid-Used-Smart-MPV-Minivan-Small-Car-Engine-5mt-Cheap-7-Seats-Passenger-Best-Mini-Bus-MPV-Van-Vehicle-for-Business.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://image.made-in-china.com/202f0j00jozkKqaFkmcr/Dfsk-Dongfeng-Glory-330s-Chinese-Gasoline-Hybrid-Used-Smart-MPV-Minivan-Small-Car-Engine-5mt-Cheap-7-Seats-Passenger-Best-Mini-Bus-MPV-Van-Vehicle-for-Business.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4365mm\nEni 1720mm\nBalandligi 1790\nQuyiladigan Benzin Sifati Ai92\n100km ga Yoqilq'i Sarfi 6.5Litr\n" +
                            "Dvigatel 1.5 ATM\n116 Ot Kuchi\nO'rindiqlar Joilashuvi 2+3+2\nMehanika\nOld Padveska Makpherson\nOrqa Privod\nTumanga Qarshi Faralar");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(62);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "TUNLAND G9" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://abiznews.net/wp-content/uploads/2023/09/Foton-Tunland-G9.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2020/September/28/foton-tunland-g9-5.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://carsdb.ru/auto/foton/tunland-g9/photo/15.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://carsdb.ru/auto/foton/tunland-g9/photo/8.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 5630mm\nEni 1980mm\nBalandligi 1905mm\nDvigatel 2Turbo\n238 Ot Kuchi\n Eng Yuqori Tezlik 220kms\nYoqilg'i Baki Hajmi 60Litr\nABS Sistemasi+\nKruiz Kontrol\nYomq'ir Dachchiklari\n" +
                            "Orqa Bort Hjmi 1520/1580mm\n360Gradusli Kamera\nPanarama Tom\nOrqa Bort 100kg gacha Yuk Ko'tara Oladi\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(64);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "bestune B70S" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/d/dd/Bestune_B70S_img01.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/November/23/faw-b70s-5.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://carexpert.ru/img/int1200/fawbb7i20-07.jpg");
                    InputMedia file7 = new InputMediaPhoto("https://carnewschina.com/wp-content/uploads/2022/07/bestune-b70s-knight-2a.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file7);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4810mm\nEni 1840mm\nBalandligi1415mm\nOg'irligi 1540kg\nYoqilq'i Baki Hajmi 50Litr\nEng yuqori Tezligi 230kms\n100km ga Yoqilq'i Sarfi 7Litr\nDvigatel 2turbo\n224 Ot Kuchi\nQuyiladigan Yoqilq'i Sifati Ai92-Ai95\nOldi Tortadi\nSport va Norm Tezlikda Yuradi\nYuqori Sifatli Charmdan Ishlangan O'rindiqlar\nO'rindiqlarni Elektro Boshqaruvi\nRriuz Kontrol\n" +
                            "O'rindiqlarni isitish imkoniyati\nO'rindiq Xotirasi\nOrqa Oynani Elektron Tarzda Isitish\nAvariya Xolatida Ogohlantirish Imkoniyati\nPalasa Ushlash Imkoniyati\n360 Gradusli Kamera\nPanarama Tom\nYomq'ir Dchchiklari\n ");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(76);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "DONGFENG FENGON S508 EVR" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://autoreview.ru/images/Article/1750/Article_175081_860_575.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2022/December/28/dongfeng-e5-4.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2022/December/28/dongfeng-e5-5.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2022/December/28/dongfeng-e5-2.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4760mm\nEni 1865mm\nBalandligi 1710mm\nElektro Dvigatel 177 Ot Kuchi\nBenza Dvigatel 1.5 ATMOS 110 Ot Kuchi\nOg'irligi 2325kg\nAkumulyator Hajmi 19.4Wh\nTarmoz Elektrik va Benzin\nAvtomabil Gibrid\nGildirak Bazasi 1785mm\nElektr Orqali 110km\nBenzin + Elektr 1200km\n100km ga Yoqilg'i Sarfi 2-5 Litr\n220V da Zaryadlanish Vaqt Muddati 4-5 Soat\nYoqilg'i Baki Hajmi 60Litr\n 2 Zonnniy Klimat Kontrol\nKruiz Kontrol Sistemasi\nCharmdan Ishlangan O'rindiqlar\n12.8 Dyumli Aqilliy Display\nUzoqni Yorituvchi LED Faralar\nOld Privotda Harakatlanadi\n360 gradusli Kamera\nABS Sistemasi+\nSimsiz Quvvatlash Tizimi\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(78);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "AEOLUS SHINE" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2023/07/dongfeng_aeolus_shine_max_1_1000-1280x720.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://wroom.ru/i/cars2/dongfeng_aeolus-shine-max_1_2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2023/08/dongfeng_aeolus_shine_max_3_1000.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://turbo.azstatic.com/uploads/full/2024%2F02%2F05%2F17%2F59%2F08%2F479bdc3d-e674-4204-89a0-8517fccac9a1%2F3782_r1qMcGFRQblORIGanm7Szw.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4670\nEni 1812\nBalandligi 1410\nOg'irligi 1232kg\nEng Yuqori Tezligi 200kms\n100 km ga Yoqilq'i Sarfi 5.4Litr\nQuyi;adiga Benzin Sifati Ai92-Ai95\nOld Privod\nPadveska Macpherson\n100kms ga 9 sekuntda Chiqadi\nEletron Tarzda Ochilo'vchi Yukxona\n360 gradusli Kamera\nAvto Parkovka\nJarmdan  Ishlangan O'rindiqlar\nAvto Tormoz\nSimsiz Quvvatlash Tizimi\n6 Bosqichli Elektro O'rindiqlar\nLyuk\nAvto Yonib O'chuvchi Faralar\nSport Rejim\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(80);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "aiq-ru" -> {
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row21 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("AIQAR ARRIZO E");
                    button2.setCallbackData("AIQAR ARRIZO Eru");
                    row21.add(button2);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    InlineKeyboardButton button11 = new InlineKeyboardButton();
                    button11.setText("AIQAR  EQ7");
                    button11.setCallbackData("AIQAR  EQ7ru");
                    row3.add(button11);
                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button12 = new InlineKeyboardButton();
                    button12.setText("AIQAR EQ3");
                    button12.setCallbackData("eqru");
                    row4.add(button12);
                    list.add(row21);
                    list.add(row3);
                    list.add(row4);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setReplyMarkup(markup);
                    message.setText("Выберите Один из Них");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "AEOLUS SHINE GS TURBO" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://dongfengpanama.com.pa/assets/img/carros/AeolusGS/Portada2.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://dev.alizing.by/storage/cars/2024/01/27/acdfd911fc07d669d97be5860abfa5ada0e896e2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2023/08/dongfeng_aeolus_shine_max_3_1000.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://www.domkrat.by/upload/img_catalog/shine-gs/aeolus_shine_gs_13.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4630mm\nEni 1830mm\nBalandligi 1610mm\nOg'irligi 1330kg\nBak Hajmmi 50Litr\n Dvigatel 1.5Turbo\n190 Ot Kuchi\nEng Yuqori Tezlik 190kms\n100km ga Yoqilq'i Sarfi 7.4Litr\nOld Privod\nQuyiladigan Benzin Sifati Ai92-Ai95\nPadveska Macpherson\nYukxona Hajmi 474-1109Litr\nKruiz Kontrol\nOchiq Lyuk\nSimsiz Quvvatlash Tizimi\nUzoqni Yorituvchi Led Faralar\nECO va SPORT Rejim\nAvto Sovutgich\n6ta Dinamik\n540 gradusli Kamera\nAuto Holt\nABS+EBD\n7 dyumli Ekran\n ");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(86);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "AEOLUS SHINE GS ATMOS" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://dongfenguzbekistan.uz/assets/photos/Portada2.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://content.onliner.by/news/1100x5616/138cf6db47b787e22b81f4f46a587338.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://avcdn.av.by/salonoffermedium/0002/8071/8684.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://content.onliner.by/news/amp/aee6c1b47a2e0cf8c1fad67d3703e44b.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Uzunligi 4610\nBalandligi 1810\nEni 1610\nOg'izligi 1314kg\nBak Hajmi 50Litr\nEng Yuqori Tezlik 165kms\nQuyiladigan Benzin Sifati Ai92-Ai95\nOd Privod\nPadveska MacphersonYukxona Hajmi 474-1109Litr\nKruiz Kontrol\nYuqori Sifatli Charmdan Ishlangan O'rindiqlar\n4 Bosqichli Elektro O'rindiqlar\n13 dyumli Elektro o;rindiqlar\nSinsiz Quvvatlash Tizimi\nPanarama Tom\nAuto Hold\n");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(88);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "Onlnshrt" -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setText("Onlayn shartnoma  olish uchun telefon raqamingizni qoldiring");
                    repository.updateBotState(update.getCallbackQuery().getMessage().getChatId(), BotState.PHONE_NUMBER);
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    List<KeyboardRow> rowList = new ArrayList<>();
                    KeyboardRow row = new KeyboardRow();
                    KeyboardButton button = new KeyboardButton();
                    button.setText("Telefon raqamni ulashish");
                    button.setRequestContact(true);
                    row.add(button);
                    rowList.add(row);
                    replyKeyboardMarkup.setKeyboard(rowList);
                    message.setReplyMarkup(replyKeyboardMarkup);
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    replyKeyboardMarkup.getOneTimeKeyboard();

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "Onlnshrtru" -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setText("Оставьте свой номер телефона, чтобы получить онлайн-договор");
                    repository.updateBotState(update.getCallbackQuery().getMessage().getChatId(), BotState.PHONE_NUMBER);
                    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    List<KeyboardRow> rowList = new ArrayList<>();
                    KeyboardRow row = new KeyboardRow();
                    KeyboardButton button = new KeyboardButton();
                    button.setText("Поделитесь номером телефона");
                    button.setRequestContact(true);
                    row.add(button);
                    rowList.add(row);
                    replyKeyboardMarkup.setKeyboard(rowList);
                    message.setReplyMarkup(replyKeyboardMarkup);
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    replyKeyboardMarkup.getOneTimeKeyboard();

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "bestuneruT99" -> {
                    List<InputMedia> list = new ArrayList<>();

                    InputMedia file1 = new InputMediaPhoto("https://carsdo.ru/job/CarsDo/photo-gallery/faw/bestune-t99-1-13.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/2/23/Bestune_T99_facelift_IMG003.jpg");
                    InputMedia file4 = new InputMediaPhoto("https://www.major-faw.ru/files/resources/3_in.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file4);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);
                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage massage = new SendMessage();
                    massage.setText("Цена 512.000.000.00 сум");
                    massage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(massage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(40);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "bestuneruB70" -> {
                    List<InputMedia> list = new ArrayList<>();

                    InputMedia file1 = new InputMediaPhoto("https://bestunekw.com/wp-content/uploads/2022/08/Bestune-B70-Cover_01-1-1-1536x864.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://s.auto.drom.ru/i24280/c/photos/fullsize/faw/besturn_b70/faw_besturn_b70_1128332.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s.auto.drom.ru/i24280/c/photos/fullsize/faw/besturn_b70/faw_besturn_b70_1128333.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage massage = new SendMessage();
                    massage.setText("Цена 422 400 000.00 сум");
                    massage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(massage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(44);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "bestuneruT55" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file7 = new InputMediaPhoto("https://autoreview.ru/images/Article/1718/Article_171854_860_575.jpg");
                    InputMedia file8 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/March/17/faw-t55-4.jpg");
                    InputMedia file = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2023/April/12/FAW-Bestune-T55-5.jpg");
                    list.add(file);
                    list.add(file7);
                    list.add(file8);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);
                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    SendMessage message = new SendMessage();
                    message.setText("Цена 318.720 000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(42);

                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "bestuneru33" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file8 = new InputMediaPhoto("https://i.infocar.ua/i/2/6010/114626/1920x.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://avtoaziya.ru/images/FOTO/16/FAW-Bestune-T33-2019-2020-8.jpg");
                    InputMedia file10 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Bestune_T33_img04.jpg/305px-Bestune_T33_img04.jpg");
                    list.add(file8);
                    list.add(file9);
                    list.add(file10);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);
                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(520);

                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "bestuneru B70S" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/d/dd/Bestune_B70S_img01.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/November/23/faw-b70s-5.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://carexpert.ru/img/int1200/fawbb7i20-07.jpg");
                    InputMedia file7 = new InputMediaPhoto("https://carnewschina.com/wp-content/uploads/2022/07/bestune-b70s-knight-2a.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file7);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 435 200 000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(76);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }


                case "AIQAR e Q7ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://carnewschina.com/wp-content/uploads/2023/04/1-4.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://carnewschina.com/wp-content/uploads/2023/04/4-4.jpg");
                    ;
                    InputMedia file7 = new InputMediaPhoto("https://images.khmer24.co/24-02-12/aiqar-eq7-981845170770194028817274-d.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file7);
                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("370.560.000.00 сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(764);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "eqru" -> {
                    System.out.println(1);
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://i.ytimg.com/vi/rnub1HR3igc/maxresdefault.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://i.ytimg.com/vi/nU3z4ySP6Jc/maxresdefault.jpg");
                    InputMedia file7 = new InputMediaPhoto("https://images.khmer24.co/24-02-12/aiqar-eq3-981845170770178691982277-d.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file7);
                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("369.920.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1024);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "donru" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("DONGFENG T5 EVO");
                    button2.setCallbackData("DONGFENG T5 EVOru");
                    row2.add(button2);
                    List<InlineKeyboardButton> row13 = new ArrayList<>();
                    InlineKeyboardButton button34 = new InlineKeyboardButton();
                    button34.setText("AEOLUS SHINE GS ATMOS");
                    button34.setCallbackData("AEOLUS SHINE GS ATMOSru");
                    row13.add(button34);
                    List<InlineKeyboardButton> row23 = new ArrayList<>();
                    InlineKeyboardButton button21 = new InlineKeyboardButton();
                    button21.setText("AEOLUS SHINE GS TURBO");
                    button21.setCallbackData("AEOLUS SHINE GS TURBOru");
                    row23.add(button21);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    InlineKeyboardButton button11 = new InlineKeyboardButton();
                    button11.setText("AEOLUS HUGE");
                    button11.setCallbackData("AEOLUS HUGEru");
                    row3.add(button11);
                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button12 = new InlineKeyboardButton();
                    button12.setText("DONGFENG AEOLUS YIXUAN MAX");
                    button12.setCallbackData("DONGFENG AEOLUS YIXUAN MAXru");
                    row4.add(button12);
                    List<InlineKeyboardButton> row5 = new ArrayList<>();
                    InlineKeyboardButton button13 = new InlineKeyboardButton();
                    button13.setText("FENGON 600");
                    button13.setCallbackData("FENGON 600ru");
                    row5.add(button13);
                    List<InlineKeyboardButton> row6 = new ArrayList<>();
                    InlineKeyboardButton button14 = new InlineKeyboardButton();
                    button14.setText("GLORY 330S");
                    button14.setCallbackData("GLORY 330Sru");
                    row6.add(button14);
                    List<InlineKeyboardButton> row7 = new ArrayList<>();
                    InlineKeyboardButton button15 = new InlineKeyboardButton();
                    button15.setText("TUNLAND G9");
                    button15.setCallbackData("TUNLAND G9ru");
                    row7.add(button15);
                    List<InlineKeyboardButton> row10 = new ArrayList<>();
                    InlineKeyboardButton button17 = new InlineKeyboardButton();
                    button17.setText("DONGFENG FENGON S508 EVR");
                    button17.setCallbackData("DONGFENG FENGON S508 EVRru");
                    row10.add(button17);
                    List<InlineKeyboardButton> row9 = new ArrayList<>();
                    InlineKeyboardButton button7 = new InlineKeyboardButton();
                    button7.setText("AEOLUS SHINE");
                    button7.setCallbackData("AEOLUS SHINEru");
                    row9.add(button7);
                    list.add(row2);
                    list.add(row3);
                    list.add(row4);
                    list.add(row5);
                    list.add(row9);
                    list.add(row6);
                    list.add(row7);
                    list.add(row10);
                    list.add(row23);
                    list.add(row13);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Выберите щдин из них");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "Dongfeng T5 EVOru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://center-av.ru/resources/bodies/ae88b8c43033cfe8f87cd45f917d12918b32c831.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://sc03.alicdn.com/kf/Hf7d5a1d6dcd24716ad94220c686bf318h.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://img.tinbanxe.vn/webp/images/dongfeng/dong-feng-t5-evo/noi-that/danh-gia-dong-feng-t5-evo-noi-that-ghe-ngoi-TINBANXE.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 389.120.000.00");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(52);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "AEOLUS SHINE GS ATMOSru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://dongfenguzbekistan.uz/assets/photos/Portada2.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://content.onliner.by/news/1100x5616/138cf6db47b787e22b81f4f46a587338.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://avcdn.av.by/salonoffermedium/0002/8071/8684.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://content.onliner.by/news/amp/aee6c1b47a2e0cf8c1fad67d3703e44b.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 384.000.000.00 сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(88);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "AEOLUS SHINE GS TURBOru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://dongfengpanama.com.pa/assets/img/carros/AeolusGS/Portada2.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://dev.alizing.by/storage/cars/2024/01/27/acdfd911fc07d669d97be5860abfa5ada0e896e2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2023/08/dongfeng_aeolus_shine_max_3_1000.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://www.domkrat.by/upload/img_catalog/shine-gs/aeolus_shine_gs_13.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 281.600.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(86);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "AEOLUS HUGEru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://avcdn.av.by/cargeneration/0002/8067/3335.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://content.onliner.by/news/1100x5616/1c39a3e40420c4f0a5c5eb807b3c3460.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s.auto.drom.ru/i24277/c/photos/fullsize/dongfeng/aeolus_haoji/dongfeng_aeolus_haoji_1112922.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 384.000.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(54);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "DONGFENG AEOLUS YIXUAN MAXru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/b/b3/Aeolus_Yixuan_Max_003.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/July/20/dongfeng-aeolus-max2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2021/July/20/dongfeng-aeolus-max4.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://www.ml-vehicle.com/uploads/202338258/dongfeng-aeolus-yixuan-max363566af-866e-4923-a262-955fe5a79b3a.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 384.000.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(58);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "FENGON 600ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://i.ytimg.com/vi/BnfaRLCcRiY/maxresdefault.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://images.khmer24.co/24-02-22/dfsk-fengon-600-1-5-turbocharged-miller-cycle-engine--549535170858451480612253-d.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://assets-global.website-files.com/636e3329f2c48fa4e963929e/63ce936cd805d541f3156894_features.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://i.ytimg.com/vi/cuOnExDrca4/maxresdefault.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 358.400.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(60);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "GLORY 330Sru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://mosautoshina.ru/i/auto/dongfeng_330s_2020.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://sc04.alicdn.com/kf/H4fc6e63cbb594cfd87b4f5646dc1035bZ/255107010/H4fc6e63cbb594cfd87b4f5646dc1035bZ.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://image.made-in-china.com/202f0j00FbgcKfTdLwor/Dfsk-Dongfeng-Glory-330s-Chinese-Gasoline-Hybrid-Used-Smart-MPV-Minivan-Small-Car-Engine-5mt-Cheap-7-Seats-Passenger-Best-Mini-Bus-MPV-Van-Vehicle-for-Business.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://image.made-in-china.com/202f0j00jozkKqaFkmcr/Dfsk-Dongfeng-Glory-330s-Chinese-Gasoline-Hybrid-Used-Smart-MPV-Minivan-Small-Car-Engine-5mt-Cheap-7-Seats-Passenger-Best-Mini-Bus-MPV-Van-Vehicle-for-Business.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("243.200.000.00");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(62);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "TUNLAND G9ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://abiznews.net/wp-content/uploads/2023/09/Foton-Tunland-G9.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2020/September/28/foton-tunland-g9-5.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://carsdb.ru/auto/foton/tunland-g9/photo/15.jpg");
                    InputMedia file9 = new InputMediaPhoto("https://carsdb.ru/auto/foton/tunland-g9/photo/8.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file9);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 588.800.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(64);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "DONGFENG FENGON S508 EVRru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://autoreview.ru/images/Article/1750/Article_175081_860_575.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2022/December/28/dongfeng-e5-4.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2022/December/28/dongfeng-e5-5.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2022/December/28/dongfeng-e5-2.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 390.400.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(78);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "AEOLUS SHINEru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2023/07/dongfeng_aeolus_shine_max_1_1000-1280x720.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://wroom.ru/i/cars2/dongfeng_aeolus-shine-max_1_2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2023/08/dongfeng_aeolus_shine_max_3_1000.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://turbo.azstatic.com/uploads/full/2024%2F02%2F05%2F17%2F59%2F08%2F479bdc3d-e674-4204-89a0-8517fccac9a1%2F3782_r1qMcGFRQblORIGanm7Szw.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 294.400.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(80);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jet" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("JETOUR X70 PLUS");
                    button2.setCallbackData("jetx70");
                    row2.add(button2);
                    List<InlineKeyboardButton> row13 = new ArrayList<>();
                    InlineKeyboardButton button34 = new InlineKeyboardButton();
                    button34.setText("JETOUR X70");
                    button34.setCallbackData("jetx70+");
                    row13.add(button34);
                    List<InlineKeyboardButton> row23 = new ArrayList<>();
                    InlineKeyboardButton button21 = new InlineKeyboardButton();
                    button21.setText("JETOUR T2");
                    button21.setCallbackData("jet2");
                    row23.add(button21);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    InlineKeyboardButton button11 = new InlineKeyboardButton();
                    button11.setText("JETOUR DASHING");
                    button11.setCallbackData("jetdash");
                    row3.add(button11);
                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button12 = new InlineKeyboardButton();
                    button12.setText("JETOUR X90 PLUS");
                    button12.setCallbackData("jet90");
                    row4.add(button12);
                    list.add(row2);
                    list.add(row3);
                    list.add(row4);
                    list.add(row23);
                    list.add(row13);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Avtomobillardan birini tanlang");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "jetx70" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Jetour_X70_Plus_003.jpg/1280px-Jetour_X70_Plus_003.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://i.ytimg.com/vi/h5Zq_Vgg_XQ/maxresdefault.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.ixbt.com/img/n1/news/2023/2/0/5-11_large.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://focus.ua/static/storage/thumbs/1088x/4/58/67269c7e-ce56a1d65b03c65fb0c8f502e67d3584.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Narxi 409.600.000.00 so'm ");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1138);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jetx70+" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://carsdo.ru/job/CarsDo/photo-gallery/jetour/x70-1.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2020/September/11/jetour-x70-3.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://carsdo.ru/job/CarsDo/photo-gallery/jetour/x70-16.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Jetour_X70_002.jpg/2560px-Jetour_X70_002.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("331.520.000.00so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1042);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jet2" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Jetour_T2.jpg/1280px-Jetour_T2.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://134706.selcdn.ru/v1/SEL_39171/site-production-public/system/image/file/10027702/vehicle_slider_4%402x.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/e/e9/Jetour_T2_interior.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://134706.selcdn.ru/v1/SEL_39171/site-production-public/system/image/file/9408237/vehicle_slider_7%402x.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Narxi 486.400.000.00so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1026);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jetdash" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/1/17/Jetour_Dashing_006.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/f/f8/Jetour_Dashing_photo2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.gazeta.uz/media/img/2023/03/C6Qzwv16788786926614_b.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://134706.selcdn.ru/v1/SEL_39171/site-production-public/system/image/file/7508001/vehicle_slider_7__1_%402x.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Narxi 384.000.000.00so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1036);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jet90" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://data.favorit-motors.ru/upload/iblock/e89/e8941cca456515394fe44a6ce279d53e.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://a.d-cd.net/FCMjl60zb5VSHHuBlszaYjNdFKI-1920.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.souzcentr.ru/image/catalog/jetour/x90plus/features/x90plus_feature01.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://alizing.by/storage/cars/2023/09/07/a7d7fe46b1708781e17277d13020e8a7290fd5dc.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Narxi 478.720.000.00 so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1098);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jetru" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("JETOUR X70 PLUS");
                    button2.setCallbackData("jetx70ru");
                    row2.add(button2);
                    List<InlineKeyboardButton> row13 = new ArrayList<>();
                    InlineKeyboardButton button34 = new InlineKeyboardButton();
                    button34.setText("JETOUR X70");
                    button34.setCallbackData("jetx70+ru");
                    row13.add(button34);
                    List<InlineKeyboardButton> row23 = new ArrayList<>();
                    InlineKeyboardButton button21 = new InlineKeyboardButton();
                    button21.setText("JETOUR T2");
                    button21.setCallbackData("jet2ru");
                    row23.add(button21);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    InlineKeyboardButton button11 = new InlineKeyboardButton();
                    button11.setText("JETOUR DASHING");
                    button11.setCallbackData("jetdashru");
                    row3.add(button11);
                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button12 = new InlineKeyboardButton();
                    button12.setText("JETOUR X90 PLUS");
                    button12.setCallbackData("jet90ru");
                    row4.add(button12);
                    list.add(row2);
                    list.add(row3);
                    list.add(row4);
                    list.add(row23);
                    list.add(row13);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Выберите один из них");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "jetx70ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Jetour_X70_Plus_003.jpg/1280px-Jetour_X70_Plus_003.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://i.ytimg.com/vi/h5Zq_Vgg_XQ/maxresdefault.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.ixbt.com/img/n1/news/2023/2/0/5-11_large.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://focus.ua/static/storage/thumbs/1088x/4/58/67269c7e-ce56a1d65b03c65fb0c8f502e67d3584.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 409.600.000.00 so'm ");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1138);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jetx70+ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://carsdo.ru/job/CarsDo/photo-gallery/jetour/x70-1.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2020/September/11/jetour-x70-3.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://carsdo.ru/job/CarsDo/photo-gallery/jetour/x70-16.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Jetour_X70_002.jpg/2560px-Jetour_X70_002.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 331.520.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1042);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jet2ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Jetour_T2.jpg/1280px-Jetour_T2.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://134706.selcdn.ru/v1/SEL_39171/site-production-public/system/image/file/10027702/vehicle_slider_4%402x.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/e/e9/Jetour_T2_interior.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://134706.selcdn.ru/v1/SEL_39171/site-production-public/system/image/file/9408237/vehicle_slider_7%402x.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 486.400.000.00so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1026);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jetdashru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/1/17/Jetour_Dashing_006.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/f/f8/Jetour_Dashing_photo2.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.gazeta.uz/media/img/2023/03/C6Qzwv16788786926614_b.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://134706.selcdn.ru/v1/SEL_39171/site-production-public/system/image/file/7508001/vehicle_slider_7__1_%402x.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 384.000.000.00so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1036);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "jet90ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://data.favorit-motors.ru/upload/iblock/e89/e8941cca456515394fe44a6ce279d53e.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://a.d-cd.net/FCMjl60zb5VSHHuBlszaYjNdFKI-1920.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.souzcentr.ru/image/catalog/jetour/x90plus/features/x90plus_feature01.jpg");
                    InputMedia file6 = new InputMediaPhoto("https://alizing.by/storage/cars/2023/09/07/a7d7fe46b1708781e17277d13020e8a7290fd5dc.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    list.add(file6);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 478.720.000.00 so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1098);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "honru" -> {
                    long chatId = update.getCallbackQuery().getMessage().getChatId();
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button1 = new InlineKeyboardButton();
                    button1.setText("HONGQI HS5");
                    button1.setCallbackData("HONGQI HS5ru");
                    row.add(button1);
                    InlineKeyboardButton button10 = new InlineKeyboardButton();
                    button10.setText("HONGQI H9");
                    button10.setCallbackData("Hongqi H9ru");
                    row.add(button10);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    List<InlineKeyboardButton> row2 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("HONGQI H5");
                    button2.setCallbackData("HongqiH5ru");
                    row2.add(button2);
                    List<InlineKeyboardButton> row5 = new ArrayList<>();
                    InlineKeyboardButton button4 = new InlineKeyboardButton();
                    button4.setText("HONGQI HS7");
                    button4.setCallbackData("HongqiHS7ru");
                    row3.add(button4);
                    InlineKeyboardButton button3 = new InlineKeyboardButton();
                    button3.setText("HONGQI EHS9");
                    button3.setCallbackData("HongqiEHS9ru");
                    row3.add(button3);
                    list.add(row);
                    list.add(row2);
                    list.add(row3);
                    list.add(row5);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setChatId(chatId);
                    message.setReplyMarkup(markup);
                    message.setText("Выберите один из автомабилей");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "HongqiHS5ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://motor.ru/imgs/2022/12/26/08/5729758/f4753a2c0e3af2b3a4f93d1b6826a3b5f5be6642.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2019/May/24/hongqi-hs5-4.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2019/May/24/hongqi-hs5-6.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);

                    }

                    SendMessage message = new SendMessage();
                    message.setText("Цена 497.420.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(38);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "Hongqi H9ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2020/January/09/hongqi-h9-add.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://www.ixbt.com/img/n1/news/2023/7/0/1488x0_1_autohomecar__ChxkmWSK8lSAGiPWACNlI6Fxl5k330_large.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/4/4b/Hongqi_H9_013.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText(" Цена 1.177.600.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1367);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "HongqiH5ru" -> {
                    List<InputMedia> list = new ArrayList<>();

                    InputMedia file1 = new InputMediaPhoto("https://rg.ru/uploads/images/2023/03/31/111-2_3c6.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://xn----7sbbeeptbfadjdvm5ab9bqj.xn--p1ai/wp-content/uploads/2022/06/hongqi_h5_new_5_1000.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://static.78.ru/images/uploads/1691761917718.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 390.400.000.00сум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1387);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "HongqiEHS9ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://hongqi-avilon.ru/upload/iblock/71c/qlga3dv50z5m2fwdhznut7rvts92qvm7.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://hongqi-avilon.ru/upload/iblock/915/1mm9jw3degjgpc5wezwd1o6okjedaxiu.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s0.rbk.ru/v6_top_pics/media/img/3/48/756642878091483.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 1.305.600.000.00cум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(32);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "AIQAR ARRIZO Eru" -> {

                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://www.chinapev.com/wp-content/uploads/2020/05/2020-Arrizo-e.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://storage.kun.uz/source/9/PSQM3dWnWaMX7VFHt0pu2QKqhEeCAQWN.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.chinamobil.ru/photo/ChArrizoE/full/chery-arrizo-e-04.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 242.540.000.00 so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1030);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "HongqiHS7ru" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Hongqi_HS7_001.jpg/1200px-Hongqi_HS7_001.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://autoreview.ru/images/gallery/%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8/2019/July/09/hongqi-hs7-4.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://s.auto.drom.ru/i24275/c/photos/fullsize/hongqi/hs7/hongqi_hs7_1102797.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);
                    SendMediaGroup group = new SendMediaGroup();
                    group.setMedias(list);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    mediaGroup.setMedias(list);
                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Цена 783.360.000.00cум");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(34);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                case "aiq" -> {
                    List<List<InlineKeyboardButton>> list = new ArrayList<>();
                    List<InlineKeyboardButton> row21 = new ArrayList<>();
                    InlineKeyboardButton button2 = new InlineKeyboardButton();
                    button2.setText("AIQAR ARRIZO E");
                    button2.setCallbackData("AIQAR ARRIZO E");
                    row21.add(button2);
                    List<InlineKeyboardButton> row3 = new ArrayList<>();
                    InlineKeyboardButton button11 = new InlineKeyboardButton();
                    button11.setText("AIQAR EQ7");
                    button11.setCallbackData("AIQAR  EQ7");
                    row3.add(button11);
                    List<InlineKeyboardButton> row4 = new ArrayList<>();
                    InlineKeyboardButton button12 = new InlineKeyboardButton();
                    button12.setText("AIQAR EQ3");
                    button12.setCallbackData("eqtest");
                    row4.add(button12);
                    list.add(row21);
                    list.add(row3);
                    list.add(row4);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    SendMessage message = new SendMessage();
                    markup.setKeyboard(list);
                    message.setReplyMarkup(markup);
                    message.setText("Avtomobillardan birini tanlang");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    message.setReplyMarkup(markup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "AIQAR e Q7" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://carnewschina.com/wp-content/uploads/2023/04/1-4.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://carnewschina.com/wp-content/uploads/2023/04/4-4.jpg");
                    InputMedia file7 = new InputMediaPhoto("https://images.khmer24.co/24-02-12/aiqar-eq7-981845170770194028817274-d.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file7);
                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("370.560.000.00 so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(764);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "eqtest" -> {
                    System.out.println(1);
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://i.ytimg.com/vi/rnub1HR3igc/maxresdefault.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://i.ytimg.com/vi/nU3z4ySP6Jc/maxresdefault.jpg");
                    InputMedia file7 = new InputMediaPhoto("https://images.khmer24.co/24-02-12/aiqar-eq3-981845170770178691982277-d.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file7);
                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("369.920.000.00so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1024);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "AIQAR ARRIZO E" -> {
                    List<InputMedia> list = new ArrayList<>();
                    InputMedia file1 = new InputMediaPhoto("https://www.chinapev.com/wp-content/uploads/2020/05/2020-Arrizo-e.jpg");
                    InputMedia file3 = new InputMediaPhoto("https://storage.kun.uz/source/9/PSQM3dWnWaMX7VFHt0pu2QKqhEeCAQWN.jpg");
                    InputMedia file5 = new InputMediaPhoto("https://www.chinamobil.ru/photo/ChArrizoE/full/chery-arrizo-e-04.jpg");
                    list.add(file1);
                    list.add(file3);
                    list.add(file5);

                    SendMediaGroup mediaGroup = new SendMediaGroup();
                    mediaGroup.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    mediaGroup.setMedias(list);


                    try {
                        execute(mediaGroup);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    SendMessage message = new SendMessage();
                    message.setText("Narxi 242.540.000.00 so'm");
                    message.setChatId(update.getCallbackQuery().getMessage().getChatId());

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                    CopyMessage copyMessage = new CopyMessage();
                    copyMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    copyMessage.setFromChatId("5750963176");
                    copyMessage.setMessageId(1030);
                    try {
                        execute(copyMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "alqblm" -> {
                    InputFile file1 = new InputFile("https://t4.ftcdn.net/jpg/03/65/60/33/360_F_365603381_jl2eSsk2nsz7hFbGpfZSWwfXLxO1Unp4.jpg");
                    SendPhoto photo = new SendPhoto();
                    photo.setCaption(" ☎\uFE0F +998781131378");
                    photo.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    photo.setPhoto(file1);
                    try {
                        execute(photo);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "avts" -> {
                    InputFile file2 = new InputFile("https://cooperlakeautomotive.com/img/service-AutoService.jpg");
                    SendPhoto photo = new SendPhoto();
                    photo.setCaption("☎\uFE0F +998998041008 Orifjon");
                    photo.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    photo.setPhoto(file2);
                    try {
                        execute(photo);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "avsru" -> {
                    InputFile file2 = new InputFile("https://cooperlakeautomotive.com/img/service-AutoService.jpg");
                    SendPhoto photo = new SendPhoto();
                    photo.setCaption("☎\uFE0F +998998041008 Орифжон");
                    photo.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    photo.setPhoto(file2);
                    try {
                        execute(photo);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }


                }
                case "otdsru" -> {
                    InputFile file1 = new InputFile("https://t4.ftcdn.net/jpg/03/65/60/33/360_F_365603381_jl2eSsk2nsz7hFbGpfZSWwfXLxO1Unp4.jpg");
                    SendPhoto photo = new SendPhoto();
                    photo.setCaption(" ☎\uFE0F +998781131378");
                    photo.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    photo.setPhoto(file1);
                    try {
                        execute(photo);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        } else if (update.getMessage().hasDocument()) {
            SendMessage message = new SendMessage();
            message.setText(String.valueOf(update.getMessage().getMessageId()));
            message.setChatId(update.getMessage().getChatId());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "https://t.me/bestune_uz_bot";
    }
}