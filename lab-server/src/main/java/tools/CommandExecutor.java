package tools;

import commands.*;
import smyts.lab6.common.util.Request;
import smyts.lab6.common.util.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * invoker class
 */

public class CommandExecutor {

    HashMap<String, Command> clientCommands = new HashMap<>();
    HashMap<String, Command> noAuthorizationCommands = new HashMap<>();
    private final RouteList routeList;
    public CommandExecutor(RouteList routeList) {
        this.routeList = routeList;
        initCommands();

    }

    /**
     * @param request received request
     */

    public Response execute(Request request) {
        String commandName;
        String commandNameAndArguments = request.getCommandNameAndArguments();
        Response response = new Response();

        commandName = commandNameAndArguments.split(" ")[0];

        Command serverCommand = noAuthorizationCommands.get(commandName);
        if (serverCommand != null) {
            return executeServerCommand(request);
        }

        Command command = clientCommands.get(commandName);
        if (!isAuthorized(request)) {
            response.setMessage("failure, authorize first");
            return response;
        }

        if (command == null) {
            response.setMessage("no such command");
            return response;
        }

        prepareCommandToExecute(command, request, commandNameAndArguments);

        response = command.execute();
        return response;
    }


    private void initCommands() {
        noAuthorizationCommands.put("authorization", new AuthorizationCommand("authorization", routeList));
        clientCommands.put("clear", new ClearCommand("clear", routeList));
        clientCommands.put("help", new HelpCommand("help", clientCommands));
        clientCommands.put("info", new InfoCommand("info", routeList));
        clientCommands.put("show", new ShowCommand("show", routeList));
        clientCommands.put("add", new AddCommand("add", routeList));
        clientCommands.put("update_id", new UpdateIdCommand("update_id", routeList));
        clientCommands.put("remove_by_id", new RemoveByIdCommand("remove_by_id", routeList));
        clientCommands.put("remove_last", new RemoveLastCommand("remove_last", routeList));
        clientCommands.put("remove_greater", new RemoveGreaterCommand("remove_greater", routeList));
        clientCommands.put("history", new HistoryCommand("history", routeList));
        clientCommands.put("average_of_distance", new AverageOfDistanceCommand("average_of_distance", routeList));
        clientCommands.put("filter_less_than_distance", new FilterLessThanDistanceCommand("filter_less_than_distance", routeList));
        clientCommands.put("print_unique_distance", new PrintUniqueDistanceCommand("print_unique_distance", routeList));
    }

    private boolean isAuthorized(Request request) {
        DataBaseManager dataBaseManager = routeList.getDataBaseManager();
        Connection connection;
        PreparedStatement preparedStatement;
        try {
            connection = dataBaseManager.getConnection();
            preparedStatement = connection.prepareStatement("select * from loginpassword where login=? and password=?");
            preparedStatement.setString(1, request.getUserData().getLogin());
            preparedStatement.setString(2, Encryptor.encryptBySHA1(request.getUserData().getPassword()));
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public Response executeServerCommand(Request request) {
        Command serverCommand = noAuthorizationCommands.get(request.getCommandNameAndArguments().split(" ")[0]);
        serverCommand.setArguments(request.getCommandNameAndArguments());
        serverCommand.setRequest(request);
        return serverCommand.execute();
    }

    private void prepareCommandToExecute(Command command, Request request, String arguments) {
        command.setRequest(request);
        command.setArguments(arguments);
        routeList.addCommandName(command.getName());
    }

}
