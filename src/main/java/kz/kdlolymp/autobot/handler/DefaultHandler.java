package kz.kdlolymp.autobot.handler;

import kz.kdlolymp.autobot.bot.Bot;
import kz.kdlolymp.autobot.command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Update;

public class DefaultHandler extends AbstractHandler{
    private static final Logger log = Logger.getLogger(DefaultHandler.class);

    public DefaultHandler(Bot bot){
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        return "";
    }
}
