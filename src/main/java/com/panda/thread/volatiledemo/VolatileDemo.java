package com.panda.thread.volatiledemo;

import java.util.concurrent.TimeUnit;

public class VolatileDemo {
    public static void main(String[] args) {
         Student student = new Student();

         new Thread(() -> {
             try {
                 TimeUnit.SECONDS.sleep(3);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

             student.add();

             System.out.println("线程："+Thread.currentThread().getName()+"将student中的num修改为："+student.getNum());
         }).start();

         // main线程，如果student.num一直等于0就一直在这循环
         while (student.getNum().get() == 0){

         }
        System.out.println("mian线程："+Thread.currentThread().getName()+"结束！");
    }
}