# Java多线程（一）
## 1.进程和线程
> 进程是操作系统结构的基础，是一次程序的执行；是一个程序及其数据在处理机上顺序执行所发生的活动；是程序在一个数据集合上运行的过程，它是操作系统进行资源分配和调度的一个独立单位。

> 线程是在进程中独立运行的子系统

## 2.线程的创建方式
### 2.1 继承Thread类
 ```
 /**
  * 线程的创建方式一：继承Thread类
  */
 public class MyThread extends Thread {
     @Override
     public void run() {
         super.run();
         System.out.println("MyThread....");
     }
 }
 ```
```
public class RunThread{
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("运行结束！ ");
    }
}
```
如果欲创建的线程类已经有一个父类了，这时就不能再继承自Thread类了，因为**Java不支持多继承**，所以就需要实现Runnable接口来应对这种情况。
### 2.2 实现Runnable接口
```
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("My Runnable ...");
    }
}
```
```
public class RunRunable {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
        System.out.println("运行结束！ ");
    }
}
```
很多业务场景下我们需要获取线程执行后的结果，上述两种方法就不适用了，可以实现Callable接口通过FutureTask获取返回值。
### 2.3 实现Callable接口
```
public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("My Callable...");
        return "result";
    }
}
```
```
public class RunCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyCallable());
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println("futureTask返回值"+futureTask.get());
        System.out.println("运行结束！ ");

    }
}
```
### 2.4 区别
> 1）.继承Thread的类无法继承其他类，实现Runnable或Callable接口可以继承其他类；  
> 2）.Callable接口里定义的方法有返回值，可以声明抛出异常；

## 3.Thread的基础API
 **3.1 start()方法**  
 start()方法的作用及时通知“线程规划器”此线程已经准备就绪，等待调用线程对象的run()方法。这个过程其实就是让系统安排一个时间来调用Thread中的run()方法。  
 **3.2 currentThread()方法**  
 currentThread()方法可返回代码段正在被那个线程调用的信息。  
 **3.3 isAlive()方法**
 方法isAlive的功能是判断当前的线程是否处于**活动状态**。
 > 活动状态就是线程已经启动尚未终止。线程处于正在运行或准备开始运行的状态，就认为线程是“存活”的。
 ```
public class MyThread extends Thread {
    @Override
    public void run() {
        super.run();
        System.out.println("MyThread isAlive:"+this.isAlive());
    }
}
public class RunThread{
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        System.out.println("begin:"+myThread.isAlive());
        myThread.start();
        System.out.println("end:"+myThread.isAlive());
    }
}
```
运行结果：
```
begin:false
end:true
MyThread isAlive:true
```
**3.4 sleep()方法**  
方法sleep()的作用是在指定的毫秒数内让当前“正在执行的线程”休眠（暂停执行）。  
**3.5 getId()方法**  
getId()方法的作用是取得线程的唯一标识。
```
public class MyThread extends Thread {
    @Override
    public void run() {
        super.run();
        System.out.println("MyThread :"+this.getName()+" id:"+this.getId());
    }
}
```
运行结果：
```
MyThread :Thread-0 id:11
```
