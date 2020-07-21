package com.github.simplesteph.grpc.calculator.server;


import com.proto.sum.*;
import io.grpc.stub.StreamObserver;

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


}
