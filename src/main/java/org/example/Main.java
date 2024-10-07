package org.example;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Ok");

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot("6996741296:AAFtaKOst5LabTcUnrfVRXEwp299J5O2dTc"));
            System.out.println(1);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}