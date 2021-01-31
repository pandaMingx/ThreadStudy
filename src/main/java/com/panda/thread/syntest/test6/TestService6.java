package com.panda.thread.syntest.test6;

public class TestService6 {
    synchronized public void methodA(){
        try {
            System.out.println("父类TestService6运行方法methodA---begin! theadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("父类TestService6运行方法methodA---end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
