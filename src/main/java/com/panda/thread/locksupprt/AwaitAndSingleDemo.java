package com.panda.thread.locksupprt;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitAndSingleDemo {
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName()+"--进入");
                condition.await();
                System.out.println(Thread.currentThread().getName()+"--被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try{
                condition.signal();
                System.out.println(Thread.currentThread().getName()+"--通知");
            } finally {
                lock.unlock();
            }
        }).start();
    }
}
