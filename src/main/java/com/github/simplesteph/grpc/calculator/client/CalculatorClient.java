package com.github.simplesteph.grpc.calculator.client;

import com.proto.sum.CalculatorServiceGrpc;
import com.proto.sum.PrimeNumberDecompositionRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
  public static void main(String[] args) {

    System.out.println("Hello I`m a gRPC client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051).usePlaintext().build();

    System.out.println("Creating stub");

    // Created a greet service client
    CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);
    //unitary
    /*
        // created a protocol buffer for a SumRequest
        SumRequest sumRequest = SumRequest.newBuilder()
            .setFirstValue(20)
            .setSecondValue(15)
            .build();

        // call the RPC and get back a SumResponse (protocol buffers)
        SumResponse sumResponse = calculatorClient.sum(sumRequest);

        // print result
        System.out.println(sumRequest.getFirstValue() + " + " + sumRequest.getSecondValue() + ": " + sumResponse.getSumResult());
    */

    PrimeNumberDecompositionRequest primeNumberDecompositionRequest = PrimeNumberDecompositionRequest.newBuilder()
            .setNumber(120)
            .build();

    calculatorClient.primeNumberDecomposition(primeNumberDecompositionRequest).forEachRemaining(primeNumberDecompositionResponse -> {
      System.out.println(primeNumberDecompositionResponse.getPrimeFactor());
    });

    //do something
    System.out.println("Shutting down Channel");
    channel.shutdown();

  }
}
