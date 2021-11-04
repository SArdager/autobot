package kz.kdlolymp.autobot.ability;

import kz.kdlolymp.autobot.bot.Bot;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;

public class Notify implements Runnable {

    private static final Logger log = Logger.getLogger(Notify.class);
    private static final int MILLISEC_IN_SEC = 1000;

    Bot bot;
    long delayInMillisec;
    String chatId;

    public Notify(Bot bot, String chatId, long delayInMillisec) {
        this.bot = bot;
        this.delayInMillisec = delayInMillisec;
        this.chatId = chatId;
        log.debug("CREATE. " + toString());
    }

    @Override
    public void run() {
        log.info("RUN. " + toString());
        bot.sendQueue.add(getFirstMessage());
        try {
            Thread.sleep(delayInMillisec);
            bot.sendQueue.add(getSecondSticker());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("FINISH. " + toString());
    }

    private SendMessage getFirstMessage(){
        return new SendMessage(chatId, "I will send notify after " + delayInMillisec / MILLISEC_IN_SEC + "sec");
    }

    private SendSticker getSecondSticker() {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker("AAGTOu1xdDh7S3n7NymeSllUiNcKAthi8pU");
        sendSticker.setChatId(chatId);
        return sendSticker;
    }
    private SendMessage getSecondMessage(){
        return new SendMessage(chatId, "This is notify message. Thanks for using.");
    }


}
