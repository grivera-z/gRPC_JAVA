package com.github.simplesteph.grpc.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class CalculatorServer {

  public static void main(String[] args) throws InterruptedException, IOException {
    System.out.println("Hola de gRPC");

    Server server = ServerBuilder.forPort(50051)
        .addService(new CalculatorServiceImpl())
        .build();

    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread(()-> {
      System.out.println("Received shutdown Request");
      server.shutdown();
      System.out.println("Successfully stopped the server");
    }));
    server.awaitTermination();

  }
}
