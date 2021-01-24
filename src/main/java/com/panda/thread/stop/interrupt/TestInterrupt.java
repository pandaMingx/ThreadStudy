package com.panda.thread.stop.interrupt;


import com.panda.thread.create.MyThread;

public class TestInterrupt {
    public static void main(String[] args) {
//        try {
            MyThread myThread = new MyThread();
            myThread.start();
//            Thread.sleep(1000);
            myThread.interrupt();
//            Thread.currentThread().interrupt();
            System.out.println("是否停止1："+myThread.isInterrupted());
            System.out.println("是否停止1："+myThread.isInterrupted());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
