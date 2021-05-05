# Java多线程——LockSupport
## 概述
java提供了三种线程的等待通知方式：
- wait/notify机制
- await/single机制
- park/unpart机制

## 1.LockSupport核心方法
> LockSupport类的核心方法其实就两个：park()和unpark()，其中park()方法用来阻塞当前调用线程，unpark()方法用于唤醒指定线程。
  这其实和Object类的wait()和signal()方法有些类似，但是LockSupport的这两种方法从语意上讲比Object类的方法更清晰，而且可以针对指定线程进行阻塞和唤醒。

## 2.LockSupport原理简介
>LockSupport类使用了一种名为Permit（许可）的概念来做到阻塞和唤醒线程的功能，可以把许可看成是一种(0,1)信号量（Semaphore），但与 Semaphore 不同的是，许可的累加上限是1。
 初始时，permit为0，当调用unpark()方法时，线程的permit加1，当调用park()方法时，如果permit为0，则调用线程进入阻塞状态。

## 3.使用示例
```java
public class LockSupportDemo {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("进入线程："+Thread.currentThread().getName());
            LockSupport.park();//阻塞线程
            System.out.println("线程被唤醒："+Thread.currentThread().getName());
        });
        thread1.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread2 = new Thread(() -> {
            LockSupport.unpark(thread1);//唤醒线程thread1
            System.out.println("通知唤醒："+Thread.currentThread().getName());
        });
        thread2.start();

    }
}
```
运行结果：
```
进入线程：Thread-0
通知唤醒：Thread-1
线程被唤醒：Thread-0
```
## 4.wait/notify机制
```java
public class WaitAndNotifyDemo {
    static Object lock = new Object();
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock){
                System.out.println(Thread.currentThread().getName()+"--进入");
                try {
                    lock.wait();//等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"--被唤醒");
            }
        }).start();

        new Thread(() -> {
            synchronized (lock){
               lock.notify();
                System.out.println(Thread.currentThread().getName()+"--通知");
            }
        }).start();
    }
}
```
运行结果：
```
Thread-0--进入
Thread-1--通知
Thread-0--被唤醒
```

## 5.await/single机制
```java
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
```
运行结果：
```
Thread-0--进入
Thread-1--通知
Thread-0--被唤醒
```

## 6.区别
>1）wait和notify,await和single调用这前必须先获得锁对象，但是park不需要获取某个对象的锁就可以锁住线程。  
>2）notify或single只能随机选择一个线程唤醒，无法唤醒指定的线程，unpark却可以唤醒一个指定的线程。

