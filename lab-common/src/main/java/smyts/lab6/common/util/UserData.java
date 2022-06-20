package smyts.lab6.common.util;

import java.io.Serializable;
import java.util.Scanner;

public class UserData implements Serializable {
    private final String login;
    private final String password;

    public UserData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static UserData getFromConsole() {
        String login;
        String password;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Пожалуйста, введите логин (цифры или буквы!): ");
            login = scanner.nextLine();
            System.out.println("Пожалуйста, введите пароль (цифры или буквы!): ");
            password = scanner.nextLine();
        } while (!validateLogin(login) || !validatePassword(password));

        return new UserData(login, password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    private static boolean validateLogin(String login) {
        return login.length() > 1;
    }

    private static boolean validatePassword(String password) {
        return password.length() > 1;
    }
}
