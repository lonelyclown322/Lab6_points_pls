package commands;

import smyts.lab6.common.entities.Route;
import smyts.lab6.common.util.Response;
import tools.DataBaseManager;
import tools.RouteList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * class removing the last element of the collection
 */

public class RemoveLastCommand extends Command {

    private RouteList routeList;

    public RemoveLastCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
    }

    /**
     * method to execute the command
     */

    @Override
    public synchronized Response execute() {
        Response response = new Response();
        if (this.routeList.getRll().size() == 0) {
            response.setMessage("коллекция уже пустая!");
            return response;
        }

        Route routeToDelete = routeList.getRll().stream().filter(route -> Objects.equals(route.getCreatorLogin(), request.getUserData().getLogin()))
                .max(Comparator.comparing(Route::getCreationDate)).get();
        if (routeToDelete == null) {
            response.setMessage("Нет элементов, которые вы можете удалять.");
            return response;
        }

        DataBaseManager dataBaseManager = routeList.getDataBaseManager();
        Connection connection;
        PreparedStatement preparedStatement;
        try {
            connection = dataBaseManager.getConnection();
            preparedStatement = connection.prepareStatement("delete from routes where id=?");
            preparedStatement.setInt(1, routeToDelete.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            response.setMessage("Something went wrong with the DB now, try again later.");
            return response;
        }
        routeList.getRll().remove(routeToDelete);
        response.setMessage("удалено.");
        return response;

    }

    @Override
    public String getDescription() {
        return ("Команда remove_last удаляет последний элемент из коллекции");
    }
}
