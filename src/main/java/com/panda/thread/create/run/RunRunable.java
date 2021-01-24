package com.panda.thread.create.run;

import com.panda.thread.create.MyRunnable;

public class RunRunable {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
        System.out.println("运行结束！ ");
    }
}
