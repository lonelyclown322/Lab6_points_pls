package tools;

import database.DbConnector;
import smyts.lab6.common.entities.Route;
import tools.interfaces.ObjectMaker;
import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * class appending the objects of the collection to the file
 *
 */

public class DataBaseManager {

    //class responsible for appending database data to the list


    private final ObjectMaker<Route> objectMaker;
    private final DbConnector dbConnector = new DbConnector();

    public DataBaseManager(ObjectMaker<Route> objectMaker) throws SQLException {
        this.objectMaker = objectMaker;
        initialize();
    }

    public List<Route> start() throws SQLException {

        Connection connection = dbConnector.getNewConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from routes");
        List<Route> routes = Collections.synchronizedList(new LinkedList<>());
        while (resultSet.next()) {
            String[] routeData = new String[15];
            for (int i = 1; i <= 15; i++) {
                routeData[i-1] = resultSet.getString(i);
            }
            routes.add(objectMaker.makeObject(routeData));
        }
        connection.close();
        return routes;
    }

    public Connection getConnection() throws SQLException {
        return dbConnector.getNewConnection();
    }

    private boolean initialize() throws SQLException {
        Connection connection = dbConnector.getNewConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE SEQUENCE IF NOT EXISTS idgenerator START 1");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS routes (" +
                "id int PRIMARY KEY default nextval('idgenerator'::regclass)," +
                "name varchar(255) NOT NULL CHECK(name<>'')," +
                "coordinatex double precision," +
                "coordinatey bigint NOT NULL," +
                "creationDate timestamp default now()," +
                "fromx real," +
                "fromy bigint," +
                "fromz double precision," +
                "fromname varchar(76)," +
                "tox real," +
                "toy bigint," +
                "toz double precision," +
                "toname varchar(76), " +
                "distance real," +
                "creatorlogin varchar(76)" +
                ")");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS loginpassword (" +
                "login varchar(76) PRIMARY KEY," +
                "password varchar(41)" +
                ")");
        return true;
    }

}
