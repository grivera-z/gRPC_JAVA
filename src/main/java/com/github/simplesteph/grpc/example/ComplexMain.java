package com.github.simplesteph.grpc.example;

import example.complex.Complex.*;

public class ComplexMain {
  public static void main(String[] args) {

    System.out.println("hola");

    DummyMessage dummyMessage = DummyMessage.newBuilder()
        .setId(1)
        .setName("hola")
        .build();


    System.out.println(dummyMessage);
  }
}
