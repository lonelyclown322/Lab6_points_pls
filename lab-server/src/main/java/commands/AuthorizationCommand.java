package commands;

import smyts.lab6.common.util.Response;
import tools.DataBaseManager;
import tools.Encryptor;
import tools.RouteList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorizationCommand extends Command{

    private RouteList routeList;
    public AuthorizationCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
    }

    @Override
    public Response execute() {
        Response response = new Response();
        DataBaseManager dataBaseManager = routeList.getDataBaseManager();
        Connection connection;
        PreparedStatement preparedStatement;
        boolean isRegistring = this.arguments.split(" ")[1].equals("r");
        if (isRegistring) {
            try {
                connection = dataBaseManager.getConnection();
                preparedStatement = connection.prepareStatement("insert into loginpassword(login, password) values (?, ?)");
                preparedStatement.setString(1, request.getUserData().getLogin());
                preparedStatement.setString(2, Encryptor.encryptBySHA1(request.getUserData().getPassword()));
                preparedStatement.execute();
            } catch (SQLException e) {
                response.setMessage("Увы, пользователь с таким логином уже существует, попробуйте выбрать себе другой логин!");
                return response;
            }
            response.setMessage("success");
            return response;
        }
        try {
            connection = dataBaseManager.getConnection();
            preparedStatement = connection.prepareStatement("select * from loginpassword where login=? and password=?");
            preparedStatement.setString(1, request.getUserData().getLogin());
            preparedStatement.setString(2, Encryptor.encryptBySHA1(request.getUserData().getPassword()));
            ResultSet resultSet = preparedStatement.executeQuery();
            response.setMessage(resultSet.next() ? "success" : "неверный логин или пароль");
            return response;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.setMessage("failure");
            return response;
        }
    }

    @Override
    public String getDescription() {
        return "Команда authorization используется для авторизации новых пользователей регистрации для новых пользователей";
    }
}
