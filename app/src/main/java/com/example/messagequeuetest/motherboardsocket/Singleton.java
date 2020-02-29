package com.example.messagequeuetest.motherboardsocket;

/**
 * Created by Administrator on 2019/9/10.
 */

public class Singleton {

    private static volatile int CODE = 0;

    public static int getCode() {
        return CODE;
    }

    public static void setCode(int code) {
        Singleton.CODE = code;
    }

    private volatile static Singleton singleton;

    private Singleton() {
    }

    public static Singleton getSingleton() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
