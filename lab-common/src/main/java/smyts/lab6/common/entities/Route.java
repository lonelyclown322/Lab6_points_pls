package smyts.lab6.common.entities;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Route implements Comparable<Route>, Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле может быть null
    private Location to; //Поле может быть null
    private float distance; //Значение поля должно быть больше 1
    private String creatorLogin;

    @Override
    public int compareTo(Route o) {
        return (int) (distance - o.distance);
    }

    public Route() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) throws DateTimeParseException {
        this.creationDate = LocalDateTime.parse(creationDate.replace(" ", "T"));
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public String getCreatorLogin() {
        return this.creatorLogin;
    }

    public void setCreatorLogin(String creatorLogin) {
        this.creatorLogin = creatorLogin;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        if (distance <= 1) {
            throw new IllegalArgumentException();
        } else {
            this.distance = distance;
        }
    }

    @Override
    public String toString() {
        return "id = " + id + "\n" +
                "name = " + name +  "\n" +
                "coordinates = { " + coordinates.toString() + "}"  + "\n" +
                "creationDate = " + creationDate +  "\n" +
                "location from = { " + from.toString() + "}" +  "\n" +
                "location to = { " + to.toString() + "}" +  "\n" +
                "distance = " + distance +  "\n" +
                "creatorLogin = " + creatorLogin + "\n";
    }
}
