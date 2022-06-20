package commands;


import smyts.lab6.common.entities.Route;
import tools.ConnectionWorker;
import smyts.lab6.common.util.Response;
import tools.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * command for adding element to the collection
 */

public class AddCommand extends Command {


    private RouteList routeList;
    private ConnectionWorker connectionWorker;

    /**
     * @param name name of the command
     * @param rl   wrapper of the list
     */
    public AddCommand(String name, RouteList rl) {
        super(name);
        this.routeList = rl;
        this.connectionWorker = new ConnectionWorker();
    }

    /**
     * method to execute the command
     */

    @Override
    public synchronized Response execute() {
        Response response = new Response();
        Route routeToAdd;
        routeToAdd = request.getRoute();
        if (routeToAdd == null) {
            response.setObjectNeeded(true);
            return response;
        }

        DataBaseManager dataBaseManager = routeList.getDataBaseManager();
        Connection connection;
        PreparedStatement preparedStatement;
        int newId;
        String newTime;
        try {
            int i = 1;
            connection = dataBaseManager.getConnection();
            preparedStatement = connection.prepareStatement("insert into routes (name, coordinatex, coordinatey, " +
                    "fromx, fromy, fromz, " +
                    "fromname, tox, toy, toz, toname, distance, creatorlogin) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning routes.id, routes.creationdate");
            preparedStatement.setString(i++, routeToAdd.getName());
            preparedStatement.setDouble(i++, routeToAdd.getCoordinates().getX());
            preparedStatement.setLong(i++, routeToAdd.getCoordinates().getY());
            preparedStatement.setFloat(i++, routeToAdd.getFrom().getX());
            preparedStatement.setLong(i++, routeToAdd.getFrom().getY());
            preparedStatement.setDouble(i++, routeToAdd.getFrom().getZ());
            preparedStatement.setString(i++, routeToAdd.getFrom().getName());
            preparedStatement.setFloat(i++, routeToAdd.getTo().getX());
            preparedStatement.setLong(i++, routeToAdd.getTo().getY());
            preparedStatement.setDouble(i++, routeToAdd.getTo().getZ());
            preparedStatement.setString(i++, routeToAdd.getTo().getName());
            preparedStatement.setFloat(i++, routeToAdd.getDistance());
            preparedStatement.setString(i, request.getUserData().getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            newId = resultSet.getInt("id");
            newTime = resultSet.getString("creationdate");
        } catch (SQLException e) {
            response.setMessage("Sorry, it's something wrong with the DB now");
            return response;
        }
        routeToAdd.setId(newId);
        routeToAdd.setCreationDate(newTime);
        routeToAdd.setCreatorLogin(request.getUserData().getLogin());
        routeList.getRll().add(routeToAdd);

        response.setMessage("Объект был успешно добавлен в коллекцию.");
        return response;
    }

    @Override
    public String getDescription() {
        return "Команда add *name* активирует форму для добавления в коллекцию нового элемента.";
    }
}
