package com.panda.thread.create;


/**
 * 线程的创建方式一：继承Thread类
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        super.run();
//        System.out.println("MyThread isAlive:"+this.isAlive());
        System.out.println("MyThread :"+this.getName()+" id:"+this.getId());
    }
}


