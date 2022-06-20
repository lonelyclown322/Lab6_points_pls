package commands;

import smyts.lab6.common.util.Response;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * class printing all of the commands
 */

public class HelpCommand extends Command {

    HashMap<String, Command> commands;

    public HelpCommand(String name, HashMap<String, Command> commands) {
        super(name);
        this.commands = commands;
    }

    /**
     *
     *  method to execute the command
     */

    @Override
    public Response execute() {
        Response response = new Response();

        commands.forEach( (name, command) -> response.addStringToSend(command.getDescription()));
        return response;
    }

    @Override
    public String getDescription() {
        return ("Команда help выводит справку по доступным командам.");
    }
}
