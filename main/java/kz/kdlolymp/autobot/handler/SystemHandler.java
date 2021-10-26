package kz.kdlolymp.autobot.handler;

import kz.kdlolymp.autobot.bot.Bot;
import kz.kdlolymp.autobot.command.Command;
import kz.kdlolymp.autobot.command.ParsedCommand;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class SystemHandler extends AbstractHandler{
    private static final Logger log = Logger.getLogger(SystemHandler.class);
    private final String END_LINE = "\n";

    public SystemHandler(Bot bot){
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();

        switch (command){
            case START:
                bot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(chatId));
                break;
            case ID:
                return "Your telegramID: " + update.getMessage().getFrom().getId();

        }
        return "";
    }

    private SendMessage getMessageHelp(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("*This is help message*").append(END_LINE).append(END_LINE);
        text.append("[/start](/start) - show start message").append(END_LINE);
        text.append("[/help](/help) - show help message").append(END_LINE);
        text.append("[/id](/id) - know your ID in telegram ").append(END_LINE);
        text.append("/*notify* _time-in-sec_  - receive notification from me after the specified time").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private SendMessage getMessageStart(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("Қош келдіңіздер! Мен КДЛ Олимп желісіне кеңес беремін.").append(END_LINE);
        text.append("Тілді таңдаңыз.").append(END_LINE).append(END_LINE);
        text.append("Привет! Я консультант сети КДЛ Олимп.").append(END_LINE);
        text.append("Выберите язык.").append(END_LINE);
        sendMessage.setText(text.toString());
        return sendMessage;
    }
}
