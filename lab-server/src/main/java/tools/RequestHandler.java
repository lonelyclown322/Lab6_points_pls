package tools;

import smyts.lab6.common.util.Request;
import smyts.lab6.common.util.Response;

import java.net.Socket;
import java.util.concurrent.Executor;

public class RequestHandler implements Runnable {
    private CommandExecutor commandExecutor;
    private Executor senderManager;
    private Request request;
    private ConnectionWorker connectionWorker;
    private Socket socket;

    public RequestHandler(CommandExecutor commandExecutor, Executor senderManager,
                          Request request, ConnectionWorker connectionWorker, Socket socket) {
        this.commandExecutor = commandExecutor;
        this.senderManager = senderManager;
        this.request = request;
        this.connectionWorker = connectionWorker;
        this.socket = socket;
    }

    @Override
    public void run() {
        Response response = commandExecutor.execute(request);
        senderManager.execute(new Sender(connectionWorker, response, socket));
    }
}
