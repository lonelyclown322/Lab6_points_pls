package smyts.lab6.server;

import config.ServerConfig;
import tools.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service {

    public static void start() {

        RouteList routeList;
        try {
            DataBaseManager dataBaseManager = new DataBaseManager(new RouteMaker());
            routeList = new RouteList(dataBaseManager);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
            return;
        }

        ServerSocket server;
        try {
            server = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        CommandExecutor commandExecutor = new CommandExecutor(routeList);
        ConnectionWorker connectionWorker = new ConnectionWorker();

        ExecutorService receiveHandler = Executors.newCachedThreadPool();
        ExecutorService senderManager = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 3);
        Thread thread = new Thread(new ConsoleThread());
        thread.start();

        while (true) {
            Socket socket;
            try {
                socket = server.accept();
                ServerConfig.logger.info("Установлено соединение с " + socket.getInetAddress());
                receiveHandler.execute(new RequestReceiver(connectionWorker, commandExecutor, socket, senderManager));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

    }


//        try {
//            while (true) {
//                Request request = connectionWorker
//                        .receiveCommandNameAndArguments(socket);
//                Response response = commandExecutor.execute(request);
//                connectionWorker.sendResponse(socket, response);
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            Request request = new Request();
//            request.setCommandNameAndArguments("save");
//            commandExecutor.execute(request);
//            System.out.println("сервер завершил свою работу.");
//        }

}


// IDEA
//String path = "C:\\Users\\smyts\\Desktop\\jokes.txt";
//        if (args.length == 1) {
//            path = args[0];
//        }
//
//        RouteList routeList = new RouteList();
//        DataReader reader = new DataReader(path);
//        FIleManager fIleManager = new FIleManager(reader, routeList.getRll(), new RouteMaker());
//        fIleManager.start();
//        Route.setIdGenerator(fIleManager.getMaxId());
//
//        Thread thread = new ConsoleThread(routeList, path);
//        thread.start();