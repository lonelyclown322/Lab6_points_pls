package smyts.lab6.server;

import java.util.Scanner;

public class ConsoleThread extends Thread {

    private static final Scanner scanner = new Scanner(System.in);
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            String command = scanner.nextLine();
            if ("exit".equalsIgnoreCase(command)) {
                shutDown();
                System.exit(0);
            }
        }
    }

    private void shutDown() {
        this.running = false;
    }
}
