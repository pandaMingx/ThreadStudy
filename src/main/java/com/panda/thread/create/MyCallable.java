package com.panda.thread.create;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("My Callable...");
        return "result";
    }
}
