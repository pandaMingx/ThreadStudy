package com.panda.thread.syntest.test6;

public class TestService6Sub extends TestService6{
    @Override
    public synchronized void methodA() {
        try {
            System.out.println("子类TestService6Sub运行方法methodA---begin! threadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("子类TestService6Sub运行方法methodA---end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.methodA();
    }
}
