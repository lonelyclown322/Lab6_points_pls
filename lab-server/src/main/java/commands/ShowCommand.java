package commands;

import smyts.lab6.common.entities.Route;
import smyts.lab6.common.util.Response;
import tools.RouteList;
import tools.RoutesMergeSorting;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * class printing the collection
 */

public class ShowCommand extends Command {
    RouteList routeList;

    public ShowCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
    }

    /**
     * method to execute the command
     */

    @Override
    public Response execute() {
        Response response = new Response();

        routeList.getRll().sort(Comparator.comparing(Route::getName));

        if (routeList.getRll().size() == 0) {
            response.setMessage("Коллекция пуста!");
            return response;
        }

        //Sorting routes before sending

        RoutesMergeSorting.sort(routeList.getRll());

        response.setRoutes(routeList.getRll());

        return response;

    }

    @Override
    public String getDescription() {
        return ("Команда show выводит в стандартный поток вывода все элементы коллекции в строковом представлении");
    }
}
