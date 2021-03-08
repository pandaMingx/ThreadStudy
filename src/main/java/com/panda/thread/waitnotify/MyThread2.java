package com.panda.thread.waitnotify;

public class MyThread2 extends Thread {
    private Object lock;

    public MyThread2(Object lock) {
        super();
        this.lock = lock;
    }

    public void run() {

        synchronized (lock) {
            System.out.println(System.currentTimeMillis() + "开始通知..");
            lock.notify();
            System.out.println(System.currentTimeMillis() + "结束通知..");
        }

    }
}
