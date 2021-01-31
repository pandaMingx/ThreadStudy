package com.panda.thread.syntest.test3;

public class TestService3 {

    synchronized public static void methodA() {
        try {
            System.out.println("methodA: begin! threadName is " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodA: end! threadName is " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public void methodB() {
        try {
            System.out.println("methodB: begin! threadName is " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodB: end! threadName is " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
