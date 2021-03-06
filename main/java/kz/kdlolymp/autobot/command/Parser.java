package kz.kdlolymp.autobot.command;

import javafx.util.Pair;
import org.apache.log4j.Logger;

public class Parser {
    private static final Logger log = Logger.getLogger(Parser.class);
    private final String PREFIX_FOR_COMMAND = "/";
    private final String DELIMITED_COMMAND_BOTNAME = "@";
    private String botName;

    public Parser(String botName){
        this.botName = botName;
    }

    public ParsedCommand getParsedCommand(String text){
        String trimText = "";
        if(text != null) trimText = text.trim();
        ParsedCommand result = new ParsedCommand(Command.NONE, trimText);

        if("".equals(trimText)) return result;
        Pair<String, String> commandAndText = getDelimitedCommandFromText(trimText);
        if(isCommand(commandAndText.getKey())){
            if(isCommandForMe(commandAndText.getKey())){
                String commandForParse = cutCommandFromFullText(commandAndText.getKey());
                Command commandFromText = getCommandFromText(commandForParse);
                result.setText(commandAndText.getValue());
                result.setCommand(commandFromText);
            } else {
                result.setCommand(Command.NOTFORME);
                result.setText(commandAndText.getValue());
            }
        }
        return result;
    }

    private Command getCommandFromText(String text) {
        String upperCaseText = text.toUpperCase().trim();
        Command command = Command.NONE;
        try {
            command = Command.valueOf(upperCaseText);
        } catch (IllegalArgumentException e){
            log.debug("Can't parse command: " + text);
        }
        return command;
    }

    private String cutCommandFromFullText(String text) {
        return text.contains(DELIMITED_COMMAND_BOTNAME) ?
                text.substring(1,text.indexOf(DELIMITED_COMMAND_BOTNAME)) :
                text.substring(1);
    }

    private Pair<String, String> getDelimitedCommandFromText(String trimText) {
        Pair<String, String> commandText;

        if(trimText.contains(" ")){
            int indexOfSpace = trimText.indexOf(" ");
            commandText = new Pair<>(trimText.substring(0, indexOfSpace), trimText.substring(indexOfSpace+1));
        } else commandText = new Pair<>(trimText, "");
        return commandText;
    }

    private boolean isCommandForMe(String command){
        if(command.contains(DELIMITED_COMMAND_BOTNAME)){
            String botNameForEqual = command.substring(command.indexOf(DELIMITED_COMMAND_BOTNAME)+1);
            return botName.equals(botNameForEqual);
        }
        return true;
    }

    private boolean isCommand(String text){
        return text.startsWith(PREFIX_FOR_COMMAND);
    }

}
