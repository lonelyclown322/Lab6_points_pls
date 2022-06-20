package commands;

import smyts.lab6.common.util.Response;
import tools.DataBaseManager;
import tools.RouteList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * class removing all the elements greater than given
 */

public class RemoveGreaterCommand extends Command {

    private final RouteList routeList;


    public RemoveGreaterCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
    }

    /**
     * method to execute the command
     */

    @Override
    public synchronized Response execute() {
        Response response = new Response();
        float distance;
        try {
            distance = Float.parseFloat(this.arguments.split(" ")[1]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            response.setMessage("Команда remove_greater должна содержать один аргумента типа float.");
            return response;
        }
        if (distance <= 1) {
            response.setMessage("Введенное число должно быть больше 1 ");
            return response;
        }
        if (routeList.getRll().stream().noneMatch(route -> route.getCreatorLogin().equals(request.getUserData().getLogin()))) {
            response.setMessage("Нет объектов, которые вы могли бы удалить.");
            return response;
        }

        DataBaseManager dataBaseManager = routeList.getDataBaseManager();
        Connection connection;
        PreparedStatement preparedStatement;
        try {
            connection = dataBaseManager.getConnection();
            preparedStatement = connection.prepareStatement("delete from routes where creatorlogin=? and distance>?");
            preparedStatement.setString(1, request.getUserData().getLogin());
            preparedStatement.setFloat(2, distance);
            preparedStatement.execute();
        } catch (SQLException e) {
            response.setMessage("Sorry, something wrong with the DB now.");
            return response;
        }

        routeList.getRll().removeIf(route -> (route.getDistance() > distance && route.getCreatorLogin().equals(request.getUserData().getLogin())));

        response.setMessage("Удалено.");
        return response;

    }

    @Override
    public String getDescription() {
        return ("Команда remove_greater удаляет из коллекции элементы значение distance которых превыщает заданное");
    }
}
