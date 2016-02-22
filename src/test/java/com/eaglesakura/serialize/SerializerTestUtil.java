package com.eaglesakura.serialize;

import java.util.UUID;

public class SerializerTestUtil {
    public static boolean randBool() {
        return randInteger() % 2 == 0;
    }

    public static byte randInteger() {
        return (byte) ((Math.random() * 255) - 128);
    }

    public static float randFloat() {
        return (float) Math.random();
    }

    public static String randString() {
        return UUID.randomUUID().toString();
    }

    public static byte[] randBytes() {
        return new byte[]{
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
                randInteger(),
        };
    }
}
