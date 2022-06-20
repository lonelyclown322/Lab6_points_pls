package tools;

import smyts.lab6.common.entities.Route;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class RouteList {
    private final List<Route> rll;
    private final ZonedDateTime creationDatetime;
    private final LinkedList<String> commandsHistory;
    private final DataBaseManager dataBaseManager;

    public RouteList(DataBaseManager dataBaseManager) throws SQLException {
        this.dataBaseManager = dataBaseManager;
        this.rll = dataBaseManager.start();
        this.creationDatetime = ZonedDateTime.now();
        this.commandsHistory = new LinkedList<>();
    }

    public List<Route> getRll() {
        return rll;
    }

    public Route getRouteById (int id) {
        if (id < 0) return null;

        for (Route cur : rll) {
            if (cur.getId() == id) return cur;
        }

        return null;
    }

    public void addCommandName(String commandName) {
        if (commandsHistory.size() == 6) {
            commandsHistory.removeFirst();
        }
        commandsHistory.add(commandName);
    }

    public ZonedDateTime getCreationDatetime() {
        return creationDatetime;
    }

    public LinkedList<String> getCommandsHistory() {
        return commandsHistory;
    }

    public DataBaseManager getDataBaseManager() {
        return dataBaseManager;
    }

}
