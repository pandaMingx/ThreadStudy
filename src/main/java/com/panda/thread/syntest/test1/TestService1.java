package com.panda.thread.syntest.test1;

public class TestService1 {
    synchronized public void methodA(){
        try {
            System.out.println("methodA: begin! threadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodA: end! threadName is "+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public void methodB(){
        try {
            System.out.println("methodB: begin! threadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodB: end! threadName is "+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 同步代码块
    public void methodC(){
        try {
            synchronized (this){
                System.out.println("methodC: begin! threadName is "+Thread.currentThread().getName());
                Thread.sleep(5000);
                System.out.println("methodC: end! threadName is "+Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
