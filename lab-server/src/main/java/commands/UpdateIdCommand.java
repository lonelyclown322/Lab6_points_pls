package commands;


import tools.ConnectionWorker;
import smyts.lab6.common.entities.Route;
import smyts.lab6.common.util.Request;
import smyts.lab6.common.util.Response;
import tools.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 *  class updating the element by id
 */


public class UpdateIdCommand extends Command {

    private RouteList routeList;
    private ConnectionWorker connectionWorker;

    public UpdateIdCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
        this.connectionWorker = new ConnectionWorker();
    }

    /**
     *
     *  method to execute the command
     */
    @Override
    public synchronized Response execute() {
        Response response = new Response();
        try {
            if (this.arguments.split(" ").length == 1) {
                response.setMessage("Команда update_id должна содержать id в качестве аргумента.");
                return response;
            }


            int id = Integer.parseInt(this.arguments.split(" ")[1]);

            Route route = routeList.getRouteById(id);
            if (route == null) {
                response.setMessage("Route с таким id не существует");
                return response;
            }
            if (!route.getCreatorLogin().equals(request.getUserData().getLogin())) {
                response.setMessage("Вы не обладаете правом обновлять значения не своего объекта.");
                return response;
            }

            if (request.getRoute() == null) {
                response.setObjectNeeded(true);
                return response;
            }


            routeList.getRll().remove(route);

            Route updatedRoute = request.getRoute();
            updatedRoute.setCreatorLogin(request.getUserData().getLogin());
            updatedRoute.setId(id);
            DataBaseManager dataBaseManager = routeList.getDataBaseManager();
            Connection connection;
            PreparedStatement preparedStatement;

            int i = 1;
            connection = dataBaseManager.getConnection();
            preparedStatement = connection.prepareStatement("update routes set name=?, coordinatex=?, coordinatey=?, fromx=?, fromy=?, fromz=?, fromname=?, tox=?, toy=?, toz=?, toname=?, distance=? where id = ?");
            preparedStatement.setString(1, request.getRoute().getName());
            preparedStatement.setDouble(2, request.getRoute().getCoordinates().getX());
            preparedStatement.setLong(3, request.getRoute().getCoordinates().getY());
            preparedStatement.setFloat(4, request.getRoute().getFrom().getX());
            preparedStatement.setLong(5, request.getRoute().getFrom().getY());
            preparedStatement.setDouble(6, request.getRoute().getFrom().getZ());
            preparedStatement.setString(7, request.getRoute().getFrom().getName());
            preparedStatement.setFloat(8, request.getRoute().getTo().getX());
            preparedStatement.setLong(9, request.getRoute().getTo().getY());
            preparedStatement.setDouble(10, request.getRoute().getTo().getZ());
            preparedStatement.setString(11, request.getRoute().getTo().getName());
            preparedStatement.setFloat(12, request.getRoute().getDistance());
            preparedStatement.setInt(13, id);
            preparedStatement.execute();
            routeList.getRll().add(updatedRoute);
        } catch (SQLException e) {
            response.setMessage("failure due to connection");
            return response;
        }
        response.setMessage("Значение элемента было успешно обновлено.");
        return response;
    }

//    private Boolean isExists(int id) {
//        DataBaseManager dataBaseManager = routeList.getDataBaseManager();
//        Connection connection;
//        PreparedStatement preparedStatement;
//        try {
//            connection = dataBaseManager.getConnection();
//            preparedStatement = connection.prepareStatement("select * from routes where id = ?");
//            preparedStatement.setInt(1, id);
//            return preparedStatement.executeQuery().next();
//        } catch (SQLException e) {
//            return null;
//        }
//
//    }

    @Override
    public String getDescription() {
        return ("Команда update_id обновляет значение элемента id которого равен заданному");
    }
}
