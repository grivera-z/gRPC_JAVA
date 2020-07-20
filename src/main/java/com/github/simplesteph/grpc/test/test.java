package com.github.simplesteph.grpc.test;

import java.time.Instant;
import testproto.Test;
import testproto.TimestampOuterClass;

public class test {
  public static void main(String[] args) {
    System.out.println(Instant.ofEpochMilli(1));
    System.out.println(Instant.ofEpochMilli(2));
    System.out.println(Instant.now());

    TimestampOuterClass.Timestamp timestamp = TimestampOuterClass.Timestamp.newBuilder().build();

    Test.TestMessage testMessage = Test.TestMessage.newBuilder()
        .setTimestamp(timestamp)
        .build();

    System.out.println(testMessage);
  }

}
