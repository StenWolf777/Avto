package org.example;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws TelegramApiException{
            System.out.println("Bot muvoffaqiyatli ishga tushdi");
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot("7869070844:AAGNk7-alcLu1QUOmodDaTmPcEQURMQgu2s"));
            System.out.println("successful");
    }
}