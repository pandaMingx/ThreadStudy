package com.panda.thread.create.run;

import com.panda.thread.create.MyCallable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RunCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println("futureTask返回值"+futureTask.get());
        System.out.println("运行结束！ ");

    }
}
