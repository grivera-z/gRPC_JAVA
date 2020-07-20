package com.github.simplesteph.grpc.calculator.server;


import com.proto.sum.CalculatorServiceGrpc;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
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
}
