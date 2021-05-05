package com.panda.thread.locksupprt;

public class WaitAndNotifyDemo {
    static Object lock = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock){
                System.out.println(Thread.currentThread().getName()+"--进入");
                try {
                    lock.wait();//等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"--被唤醒");
            }
        }).start();

        new Thread(() -> {
            synchronized (lock){
               lock.notify();
                System.out.println(Thread.currentThread().getName()+"--通知");
            }
        }).start();
    }
}
