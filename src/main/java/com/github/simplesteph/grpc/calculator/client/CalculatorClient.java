package com.github.simplesteph.grpc.calculator.client;

import com.proto.sum.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

  public static void main(String[] args) {
    System.out.println("Hello I`m a gRPC client");
    CalculatorClient main = new CalculatorClient();
    main.run();
  }

  private void run(){
      ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051).usePlaintext().build();
      //doUnaryCall(channel,10,22);
      //doServerStreamingCall(channel, 120);
      doClientStreamingCall(channel);

      //do something
      System.out.println("Shutting down Channel");
      channel.shutdown();
  }
  private void doUnaryCall(ManagedChannel channel,int setFirstValue,int setSecondValue) {
      CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);

      //unitary
      // created a protocol buffer for a SumRequest
      SumRequest sumRequest = SumRequest.newBuilder()
          .setFirstValue(setFirstValue)
          .setSecondValue(setSecondValue)
          .build();

      // call the RPC and get back a SumResponse (protocol buffers)
      SumResponse sumResponse = calculatorClient.sum(sumRequest);

      // print result
      System.out.println(sumRequest.getFirstValue() + " + " + sumRequest.getSecondValue() + ": " + sumResponse.getSumResult());
  }

  private void doServerStreamingCall(ManagedChannel channel,int number) {
      CalculatorServiceGrpc.CalculatorServiceBlockingStub calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel);
      PrimeNumberDecompositionRequest primeNumberDecompositionRequest = PrimeNumberDecompositionRequest.newBuilder()
              .setNumber(number)
              .build();

      calculatorClient.primeNumberDecomposition(primeNumberDecompositionRequest).forEachRemaining(primeNumberDecompositionResponse -> {
        System.out.println(primeNumberDecompositionResponse.getPrimeFactor());
      });
  }

  private void doClientStreamingCall(ManagedChannel channel) {
    CalculatorServiceGrpc.CalculatorServiceStub asyncClient = CalculatorServiceGrpc.newStub(channel);

    CountDownLatch latch = new CountDownLatch(1);

    StreamObserver <ComputeAverageRequest> requestObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {

      @Override
      public void onNext(ComputeAverageResponse value) {
        // we get a response from the server
        System.out.println("Received a response from the server");
        System.out.println(value.getAverage());
        // onNext will be called only once
      }

      @Override
      public void onError(Throwable t) {
        // we get an error from the server
      }

      @Override
      public void onCompleted() {
        // the server is done sending us data
        System.out.println("Server has completed sending us something");
        latch.countDown();
        // onCompleted will be called right after onNext
      }
    });

    // Steaming message #1
    System.out.println("Sending message 1");
    requestObserver.onNext(ComputeAverageRequest.newBuilder()
          .setNumber(4)
          .build());

    // Steaming message #2
    System.out.println("Sending message 2");
    requestObserver.onNext(ComputeAverageRequest.newBuilder()
          .setNumber(3)
          .build());

    // Steaming message #3
    System.out.println("Sending message 3");
    requestObserver.onNext(ComputeAverageRequest.newBuilder()
          .setNumber(2)
          .build());

    // Steaming message #4
    System.out.println("Sending message 4");
    requestObserver.onNext(ComputeAverageRequest.newBuilder()
          .setNumber(1)
          .build());

    // we tell the server that the client is done sending data
    requestObserver.onCompleted();

    try {
      latch.await(3L, TimeUnit.SECONDS);
    } catch (InterruptedException e){
      e.printStackTrace();
    }

  }
}
