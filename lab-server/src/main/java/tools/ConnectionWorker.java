package tools;

import smyts.lab6.common.util.Request;
import smyts.lab6.common.util.Response;
import smyts.lab6.common.util.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ConnectionWorker {

    public Request receiveCommandNameAndArguments(Socket socket) throws IOException,
            ClassNotFoundException {
        byte[] bytes = new byte[4097];
        InputStream inputStream = socket.getInputStream();
        inputStream.read(bytes);

        Request request = Serializer.deSerializeRequest(bytes);
        return request;

    }

    public void sendResponse(Socket socket, Response response) throws IOException {
        socket.getOutputStream().write(Serializer.serializeResponse(response).array());
    }

}
