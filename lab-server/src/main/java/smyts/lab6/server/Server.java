package smyts.lab6.server;

import config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Server {
    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        ServerConfig.logger.info("МИША");
        ServerConfig.logger.warn("ПОСТАВЬ БАЛЛЫ");
        ServerConfig.logger.debug("УМОЛЯЮ");
        Service.start();
    }
}

