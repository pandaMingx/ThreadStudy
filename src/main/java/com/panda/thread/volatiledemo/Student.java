package com.panda.thread.volatiledemo;

import java.util.concurrent.atomic.AtomicInteger;

public class Student {
     //volatile int num = 0;
    public AtomicInteger num = new AtomicInteger(0);


    public void addone(){
            num.getAndIncrement();
    }

    public void add(){

    }
    public AtomicInteger getNum() {
        return num;
    }

    public void setNum(AtomicInteger num) {
        this.num = num;
    }
}
