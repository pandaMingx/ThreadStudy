package com.panda.thread.syntest.test5;

public class TestService5 {
    synchronized public void methodA(){
        if("A".equals(Thread.currentThread().getName())){
            System.out.println("methodA run! threadName is " +Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 让A线程运行到这里抛异常
            int a = 5/(2-2);
        }else{
            System.out.println("methodB run! threadName is " +Thread.currentThread().getName());
        }
    }
}
