package com.panda.thread.locksupprt;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("进入线程："+Thread.currentThread().getName());
            LockSupport.park();//阻塞线程
            System.out.println("线程被唤醒："+Thread.currentThread().getName());
        });
        thread1.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread2 = new Thread(() -> {
            LockSupport.unpark(thread1);//唤醒线程thread1
            System.out.println("通知唤醒："+Thread.currentThread().getName());
        });
        thread2.start();

    }
}
