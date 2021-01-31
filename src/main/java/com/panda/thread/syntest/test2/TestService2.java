package com.panda.thread.syntest.test2;

public class TestService2 {

//    private String threadMonitor = new String();

     public void methodA(){
         String threadMonitor = new String();
        try {
            synchronized (threadMonitor){
                System.out.println("methodA: begin! threadName is "+Thread.currentThread().getName());
                Thread.sleep(5000);
                System.out.println("methodA: end! threadName is "+Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
