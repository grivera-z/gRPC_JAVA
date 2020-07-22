package com.github.simplesteph.grpc.calculator.server;


import com.proto.sum.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

  @Override
  public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
    // Extract the fields we need
    int firstValue = request.getFirstValue();
    int secondValue = request.getSecondValue();

    // Create the response
    int sum_result = firstValue + secondValue;
    SumResponse sumResponse = SumResponse.newBuilder()
        .setSumResult(sum_result)
        .build();

    // Send the response
    responseObserver.onNext(sumResponse);

    // Complete the RPC call
    responseObserver.onCompleted();
  }

  @Override
  public void primeNumberDecomposition(PrimeNumberDecompositionRequest request, StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
    Integer number = request.getNumber();
    Integer divisor = 2;

      while (number > 1) {
        if (number % divisor == 0){
          number = number/divisor;
          PrimeNumberDecompositionResponse sumManyTimesResponse = PrimeNumberDecompositionResponse.newBuilder()
                  .setPrimeFactor(divisor)
                  .build();

          // Send the response
          responseObserver.onNext(sumManyTimesResponse);
        }
        else{
          divisor=divisor+1;
        }
      }

      // Complete the RPC call
      responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
    StreamObserver<ComputeAverageRequest> requestObserver = new StreamObserver<ComputeAverageRequest>() {

      // Running sum and running count
      int count = 0;
      int sum = 0;


      @Override
      public void onNext(ComputeAverageRequest value) {
        //Client sends a message
        sum += value.getNumber();
        count++;
      }

      @Override
      public void onError(Throwable t) {

      }

      @Override
      public void onCompleted() {
        double average = (double) sum/count;

        responseObserver.onNext(
                ComputeAverageResponse
                        .newBuilder()
                        .setAverage(average)
                        .build()
        );

        // this is when we want to return a response (responseObserver)
        responseObserver.onCompleted();
      }
    };
    return requestObserver;  }

}
