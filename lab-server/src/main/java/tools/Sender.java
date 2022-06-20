package tools;

import config.ServerConfig;
import smyts.lab6.common.util.Response;

import java.io.IOException;
import java.net.Socket;

public class Sender implements Runnable {
    private final ConnectionWorker connectionWorker;
    private final Response response;
    private final Socket socket;

    public Sender(ConnectionWorker connectionWorker, Response response, Socket socket) {
        this.connectionWorker = connectionWorker;
        this.response = response;
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            connectionWorker.sendResponse(socket, response);
            ServerConfig.logger.info("Ответ пользователю " + socket.getInetAddress() + " отправлен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
