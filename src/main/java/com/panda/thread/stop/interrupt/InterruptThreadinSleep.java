package com.panda.thread.stop.interrupt;

public class InterruptThreadinSleep extends Thread{
    @Override
    public void run() {
        super.run();
        try{
            System.out.println("begin");
            Thread.sleep(200000);
            System.out.println("end");
        }catch (InterruptedException e){
            System.out.println("进入catch--"+this.isInterrupted());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            InterruptThreadinSleep interruptThreadinSleep = new InterruptThreadinSleep();
            interruptThreadinSleep.start();
            Thread.sleep(200);
            interruptThreadinSleep.interrupt();
        }catch (InterruptedException e){
            System.out.println("main catch!");
            e.printStackTrace();
        }
    }
}
