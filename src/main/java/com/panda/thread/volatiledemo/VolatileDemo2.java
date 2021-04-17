package com.panda.thread.volatiledemo;

public class VolatileDemo2 {
    public static void main(String[] args) {
         Student student = new Student();
         // 20个线程,每个线程执行加一操作1000次，如果volatile保证原子性，那么最终的值一个是2000。
         for(int i=1;i<=20;i++){
             new Thread(() -> {
                 for(int j=1;j<=1000;j++){
                     student.addone();
                 }
             }).start();
         }

         // 等待20个线程都执行完成，main线程输出num的值
        while (Thread.activeCount()>2){
            Thread.yield();
        }

        System.out.println(student.getNum());
    }
}
