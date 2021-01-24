package com.panda.thread.stop.interrupt;

public class InterruptThreadByReturn extends Thread {
    @Override
    public void run() {
        super.run();
        while (true){
            if(this.isInterrupted()){
                System.out.println("线程停止了!");
                return;
            }
            System.out.println("timer="+System.currentTimeMillis());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptThreadByReturn interruptThreadByReturn = new InterruptThreadByReturn();
        interruptThreadByReturn.start();
        Thread.sleep(2000);
        interruptThreadByReturn.interrupt();
    }
}
