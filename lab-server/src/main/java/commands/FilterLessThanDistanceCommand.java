package commands;
import smyts.lab6.common.entities.Route;
import smyts.lab6.common.util.Response;
import tools.RouteList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * class filtering the collection by distance
 */

public class FilterLessThanDistanceCommand extends Command {

    private final RouteList routeList;

    public FilterLessThanDistanceCommand(String name, RouteList routeList) {
        super(name);
        this.routeList = routeList;
    }

    /**
     *
     *  method to execute the command
     */

    @Override
    public Response execute() {
        Response response = new Response();
        try {
            double distance = Double.parseDouble(this.arguments.split(" ")[1]);
            response.setRoutes(new LinkedList<>(routeList.getRll().stream().
                    filter(route -> route.getDistance() < distance).collect(Collectors.toList())));
            return response;
        } catch (NumberFormatException  e) {
            response.setMessage("Неверный формат ввода данных.");
            return response;
        }

    }

    @Override
    public String getDescription() {
        return ("Команда filter_less_than_distance выводит элементы коллекции поле distance которых меньше заданного");
    }
}
