package com.panda.thread.stop.interrupt;

public class InterruptMyThread extends Thread {
    @Override
    public void run() {
        super.run();
        try{
            for(int i=0;i<50000;i++){
                if(this.interrupted()){
                    System.out.println("线程是停止状态了。。。。");
                    throw new InterruptedException();
                }
                System.out.println(i);
            }
        }catch (InterruptedException e){
            System.out.println("线程停止，跳出for循环，进入catch");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            InterruptMyThread interruptMyThread = new InterruptMyThread();
            interruptMyThread.start();
            Thread.sleep(1000);
            interruptMyThread.interrupt();
        } catch (InterruptedException e) {
            System.out.println("main catch~");
            e.printStackTrace();
        }
    }
}
