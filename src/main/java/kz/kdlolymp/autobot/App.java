package kz.kdlolymp.autobot;

import kz.kdlolymp.autobot.bot.Bot;
import kz.kdlolymp.autobot.service.MessageReceiver;
import kz.kdlolymp.autobot.service.MessageSender;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public class App {
    private static final Logger log = Logger.getLogger(App.class);
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIOPITY_FOR_RECEIVER = 3;
    private static final String BOT_ADMIN = "";

    public static void main(String[] args){
        ApiContextInitializer.init();
        Bot communications = new Bot("NSCommunicationBot", "2055995494:AAGTOu1xdDh7S3n7NymeSllUiNcKAthi8pU");

        MessageReceiver messageReceiver = new MessageReceiver(communications);
        MessageSender messageSender = new MessageSender(communications);

        communications.botConnect();

        Thread receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.setName("MsgReceiver");
        receiver.setPriority(PRIOPITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();

        sendStartReport(communications);
    }

    private static void sendStartReport(Bot communications) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(BOT_ADMIN);
        sendMessage.setText("Bot start up");
        communications.sendQueue.add(sendMessage);
    }
}
