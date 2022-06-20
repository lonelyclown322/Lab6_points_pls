package commands;

import smyts.lab6.common.util.Response;
import tools.DataBaseManager;
import tools.RouteList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * class to clear the collection
 */

public class ClearCommand extends Command {

    private final RouteList routeList;

    public ClearCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
    }

    /**
     * method to execute the command
     */

    @Override
    public synchronized Response execute() {
        Response response = new Response();
        DataBaseManager dataBaseManager = routeList.getDataBaseManager();
        Connection connection;
        PreparedStatement preparedStatement;
        synchronized (this) {
            try {
                connection = dataBaseManager.getConnection();
                preparedStatement = connection.prepareStatement("delete from routes where creatorlogin=?");
                preparedStatement.setString(1, request.getUserData().getLogin());
                preparedStatement.execute();
                response.setMessage("Успешно удалено!");
            } catch (SQLException e) {
                response.setMessage("failure, something wrong with DB now");
                return response;
            }

            routeList.getRll().removeIf(route -> (route.getCreatorLogin()
                    .equals(request.getUserData().getLogin())));
        }
        return response;
//        ll.clear();
//        Response response = new Response();
//        response.setMessage("Коллекция была успешно очищена!");
//        return response;
    }

    @Override
    public String getDescription() {
        return ("Команда clear очищает коллекцию.");
    }


}
