package de.tobias.mcstat.server;

import de.tobias.mcstat.util.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class WebServer {

    Server server;
    ServletHandler handler;
    Integer port;

    public WebServer(Integer pPort) {
        port = pPort;
    }

    public void start() {
        Logger.info("Creating Server...");
        server = new Server(port);
        handler = new ServletHandler();
        server.setHandler(handler);

        Logger.info("Registering Server Routes...");
        handler.addServletWithMapping(ApiHandler.class, "/api/*");
        handler.addServletWithMapping(WebFileHandler.class, "/*");

        Logger.info("Starting Server...");

        try {
            server.start();
        } catch (Exception ex) {
            Logger.error("Failed to start Server:");
            ex.printStackTrace();
        }

        Logger.info("Server started with Port: " + port);
    }

    public void stop() {
        Logger.info("Stopping Server...");

        try {
            server.stop();
        } catch (Exception ex) {
            Logger.error("Failed to stop Server:");
            ex.printStackTrace();
        }

        Logger.info("Server stopped!");
    }
}
