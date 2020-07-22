package com.github.simplesteph.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Hello I`m a gRPC client");

        GreetingClient main = new GreetingClient();
        main.run();
    }

    private void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051).usePlaintext().build();

/*        doUnaryCall(channel);
            System.out.println();
        doServerStreamingCall(channel);
            System.out.println();*/
        doClientStreamingCall(channel);

        //do something
        System.out.println("Shutting down Channel");
        channel.shutdown();
    }

    private void doUnaryCall(ManagedChannel channel){
        // Created a greet service client
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        //Unary
        // created a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
            .setFirstName("Gabriel")
            .setLastName("Rivera")
            .build();

        // created a protocol buffer for a GreetRequest
        GreetRequest greetRequest = GreetRequest.newBuilder()
        .setGreeting(greeting)
        .build();

        // call the RPC and get back a GreetResponse (protocol buffers)
        GreetResponse greetResponse =  greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());
    }

    private void doServerStreamingCall(ManagedChannel channel){
        // Created a greet service client
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        // Server Streaming
        // we prepare the request
        GreetManyTimesRequest greetManyTimesRequest =
                GreetManyTimesRequest.newBuilder()
                        .setGreeting(
                                Greeting.newBuilder()
                                        .setFirstName("Gabriel ;)")
                        ).build();

        //we stream the responses (in a blocking manner)
        greetClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });
    }
    private void doClientStreamingCall(ManagedChannel channel){
        // Create and asynchronous client
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongGreatRequest> requestObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                // we get a response from the server
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
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
        requestObserver.onNext(LongGreatRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Pepe")
                        .setLastName("Love")
                        .build())
                .build());

        // Steaming message #2
        System.out.println("Sending message 2");
        requestObserver.onNext(LongGreatRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Tamarindo")
                        .setLastName("Gerald")
                        .build())
                .build());

        // Steaming message #3
        System.out.println("Sending message 3");
        requestObserver.onNext(LongGreatRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Marc")
                        .setLastName("Habbit")
                        .build())
                .build());

        // we tell the server that the client is donde sending data
        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }


}
