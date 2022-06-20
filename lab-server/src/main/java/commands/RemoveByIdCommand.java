package commands;


import smyts.lab6.common.entities.Route;
import smyts.lab6.common.util.Response;
import tools.DataBaseManager;
import tools.RouteList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;

/**
 * class removing objects of the collection by id
 */

public class RemoveByIdCommand extends Command {

    private final RouteList routeList;

    public RemoveByIdCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
    }

    /**
     * method to execute the command
     */

    @Override
    public Response execute() {
        Response response = new Response();
        int id;
        try {
            id = Integer.parseInt(request.getCommandNameAndArguments().split(" ")[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.setMessage("Команда remove_by_id должна содержать в качестве аргумента одно число - id элемента, который вы хотите удалить.");
            return response;
        }

        Route route = routeList.getRouteById(id);
        if (route == null) {
            response.setMessage("Route с таким id не существует");
            return response;
        }

        if (!route.getCreatorLogin().equals(request.getUserData().getLogin())) {
            response.setMessage("Вы не имеете прав на удаление объекта с таким id ");
            return response;
        }
        synchronized (this) {
            try {
                DataBaseManager dataBaseManager = routeList.getDataBaseManager();
                Connection connection = dataBaseManager.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("delete from routes where id=?");
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
            } catch (SQLException e) {
                response.setMessage("Sorry, something went wrong with the DB");
                return response;
            }
        }
        routeList.getRll().remove(routeList.getRouteById(id));
        response.setMessage("Элемент успешно удален.");
        return response;
    }

    @Override
    public String getDescription() {
        return ("Команда remove_by_id удаляет из коллекции элемент id которого равен заданному");
    }
}
