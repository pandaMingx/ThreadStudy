package com.panda.thread.waitnotify;

public class MyThread1 extends Thread{
    private Object lock;
    public MyThread1(Object lock) {
        super();
        this.lock = lock;
    }

    public void run(){
        try{
            synchronized (lock){
                System.out.println(System.currentTimeMillis()+"开始等待..");
                lock.wait();
                System.out.println(System.currentTimeMillis()+"结束等待..");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
