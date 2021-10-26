package kz.kdlolymp.autobot.service;

import kz.kdlolymp.autobot.bot.Bot;
import kz.kdlolymp.autobot.command.Command;
import kz.kdlolymp.autobot.command.ParsedCommand;
import kz.kdlolymp.autobot.command.Parser;
import kz.kdlolymp.autobot.handler.AbstractHandler;
import kz.kdlolymp.autobot.handler.DefaultHandler;
import kz.kdlolymp.autobot.handler.NotifyHandler;
import kz.kdlolymp.autobot.handler.SystemHandler;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

public class MessageReceiver implements Runnable{

    private static final Logger log = Logger.getLogger(MessageReceiver.class);
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    private Bot bot;
    private Parser parser;

    public MessageReceiver(Bot bot) {
        this.bot = bot;
        parser = new Parser(bot.getBotName());
    }

    @Override
    public void run() {
        log.info("[STARTED] MsgReceiver. Bot class: " + bot);
        while(true){
            for(Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()){
                log.debug("New object for analyze in queue " + object.toString());
                analyze(object);
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                log.error("Catch interrupt. Exit", e);
                return;
            }

        }
    }

    private void analyze(Object object) {
        if(object instanceof Update){
            Update update = (Update) object;
            log.debug("Update received: " + update.toString());
            analyzeForUpdateType(update);
        } else log.warn("Can't operate type of object: " + object.toString());
    }

    private void analyzeForUpdateType(Update update) {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        ParsedCommand parsedCommand = parser.getParsedCommand(inputText);
        AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());
        String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

        if(!"".equals(operationResult)){
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(operationResult);
            bot.sendQueue.add(message);
        }
    }

    private AbstractHandler getHandlerForCommand(Command command) {
        if(command == null){
            log.warn("Null command accepted. This is not good scenario.");
            return new DefaultHandler(bot);
        }
        switch (command){
            case START:
            case HELP:
            case ID:
                SystemHandler systemHandler = new SystemHandler(bot);
                log.info("Handler for command[" + command.toString() + "] is: " + systemHandler);
                return systemHandler;
            case NOTIFY:
                NotifyHandler notifyHandler = new NotifyHandler(bot);
                log.info("Handler for command[" + command.toString() + "] is: " + notifyHandler);
                return notifyHandler;
            default:
                log.info(("Handler for command[" + command.toString() + "] is not set. Return DefaultHandler"));
                return new DefaultHandler(bot);
        }
    }


}
