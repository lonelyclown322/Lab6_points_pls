package tools;

import smyts.lab6.common.entities.Coordinates;
import smyts.lab6.common.entities.Location;
import smyts.lab6.common.entities.Route;
import tools.interfaces.ObjectMaker;

/**
 * class for making the routes from the file
 */

public class RouteMaker implements ObjectMaker<Route> {

    @Override
    public Route makeObject(String[] splitString) {

        Route route = new Route();

        int i = 0;
        int id = Integer.parseInt(splitString[i++]);


        route.setId(id);
        route.setName(splitString[i++]);

        Coordinates coordinates = new Coordinates();
        coordinates.setX(Double.parseDouble(splitString[i++]));
        coordinates.setY(Long.parseLong(splitString[i++]));
        route.setCoordinates(coordinates);


        route.setCreationDate(splitString[i++]);
        for (int j = 0; j < 2; j++) {
            Location location = new Location();
            location.setX(Float.parseFloat(splitString[i++]));
            location.setY(Long.parseLong(splitString[i++]));
            location.setZ(Double.parseDouble(splitString[i++]));
            location.setName(splitString[i++]);
            if (j == 0) route.setFrom(location);
            else route.setTo(location);
        }

        route.setDistance(Float.parseFloat(splitString[i++]));
        route.setCreatorLogin(splitString[i]);


        return route;
    }
}
