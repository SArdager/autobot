package kz.kdlolymp.autobot.handler;

import kz.kdlolymp.autobot.bot.Bot;
import kz.kdlolymp.autobot.command.ParsedCommand;
import org.telegram.telegrambots.api.objects.Update;

public abstract class AbstractHandler {
    Bot bot;

    public AbstractHandler(Bot bot) {
        this.bot = bot;
    }

    public abstract String operate(String chatId, ParsedCommand parsedCommand, Update update);
}
