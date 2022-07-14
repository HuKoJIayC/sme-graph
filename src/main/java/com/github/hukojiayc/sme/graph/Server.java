package com.github.hukojiayc.sme.graph;

import com.github.hukojiayc.sme.graph.handler.GraphHandler;
import com.github.hukojiayc.sme.graph.handler.LoginHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {

  private static Server server = null;

  private final HttpServer httpServer;
  private final int hostPort;

  private static final int threadPoolCount = 20;

  private Server(int port) {
    log.info("Init server on port {}", port);
    hostPort = port;
    ThreadPoolExecutor threadPoolExecutor =
        (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolCount);
    try {
      httpServer = HttpServer.create(new InetSocketAddress(hostPort), 0);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Critical error starting server on port" + port);
    }
    httpServer.setExecutor(threadPoolExecutor);
    GraphHandler graphHandler = new GraphHandler();
    httpServer.createContext(graphHandler.getPath(), graphHandler);
  }

  public static synchronized Server getInstance() {
    if (server == null) {
      server = new Server(5050);
      log.info("Server started on port {}", 5050);
      server.httpServer.start();
    }
    return server;
  }
}
