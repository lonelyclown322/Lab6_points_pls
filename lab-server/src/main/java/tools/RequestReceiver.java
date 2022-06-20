package tools;

import config.ServerConfig;
import smyts.lab6.common.util.Request;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;

public class RequestReceiver implements Runnable {
    private ConnectionWorker connectionWorker;
    private CommandExecutor commandExecutor;
    private Socket socket;
    private Executor senderManager;

    public RequestReceiver(ConnectionWorker connectionWorker, CommandExecutor commandExecutor, Socket socket, Executor sender) {
        this.connectionWorker = connectionWorker;
        this.commandExecutor = commandExecutor;
        this.socket = socket;
        this.senderManager = sender;
    }

    @Override
    public void run() {
        boolean alive = true;

        while (alive) {
            Thread t;
            try {
                Request request = connectionWorker.receiveCommandNameAndArguments(socket);
                ServerConfig.logger.info("Получен запрос от " + socket.getInetAddress() +
                        " на выполнение команды " + request.getCommandNameAndArguments().split(" ")[0]);
                t = new Thread(new RequestHandler(commandExecutor, senderManager, request, connectionWorker, socket));
                t.start();
            } catch (IOException | ClassNotFoundException e) {
                alive = false;
            }
        }
    }
}
