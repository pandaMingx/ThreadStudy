## Java多线程（二）
上一篇“Java多线程（一）”主要讨论的是线程的创建，本章主要讨论**停止线程**。 
### 1.概述
停止一个线程意味着在线程处理完任务之前停掉正在做的操作，也就是放弃当前的操作。虽然这看起来很简单，但是必须做好防范措施，以便达到预期的效果。  
在java中有以下3中方法可以终止正在运行的线程：
- （1）使用退出标志，使线程正常退出，也就是当run方法完成后线程终止。 
- （2）使用stop方法强行终止线程，但是不推荐使用这个方法，因为此方法已经过时。  
- （3）使用interrupt方法中断线程。
### 2.使用stop()停止线程
使用stop()停止线程是非常暴力的，但不推荐用它，虽然它确实可以停止一个正在运行的线程，但这个方法是**不安全**的，而且已经是被弃用作废的，在将来的java版本中，这个方法将不可用或不被支持。
```
public class MyThread extends Thread {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 5000; i++) {
            System.out.println("i=" + (i + 1));
        }
    }

    public static void main(String[] args) {
        try {
            MyThread myThread = new MyThread();
            myThread.start();
            Thread.sleep(10);
            myThread.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
JDK源码中将stop()标识为过时（Deprecated）：
```
 @Deprecated
    public final void stop() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            checkAccess();
            if (this != Thread.currentThread()) {
                security.checkPermission(SecurityConstants.STOP_THREAD_PERMISSION);
            }
        }
...
```
**stop()方法的不良后果**
> 1)调用 stop() 方法会立刻停止 run() 方法中剩余的全部工作，包括在 catch 或 finally 语句中的，并抛出ThreadDeath异常(通常情况下此异常不需要显示的捕获)，因此可能会导致一些清理性的工作的得不到完成，如文件，数据库等的关闭。  
> 2)调用 stop() 方法会立即释放该线程所持有的所有的锁，导致数据得不到同步，出现数据不一致的问题。
>
### 3.使用interrupt方法中断线程
***tips:使用interrupt()方法仅仅是在当前线程中打了一个停止的标记，并不是真正的停止线程。***  
所以使用interrupt()方法中断线程还需要结合判断线程是不是停止的，如果判断线程是中断状态再停止线程。Java的SDK提供了两种方法来判断线程是否停止：
- 1）this.interrupted():测试当**前线程**是否已经是中断状态，执行后具有将状态标志清除为false的功能。
- 2）this.isInterrupted():测试**线程Thread对象**是否已经是中断状态，但不清除状态标志。
#### 3.1 两种判断线程停止状态方式的区别
```
public static void main(String[] args) {
        try {
            MyThread myThread = new MyThread();
            myThread.start();
            Thread.sleep(100);
            myThread.interrupt();
            System.out.println("是否停止1："+myThread.interrupted());
            System.out.println("是否停止1："+myThread.interrupted());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```
输出结果：
```
是否停止1：false
是否停止1：false
```
代码执行myThread.interrupt()给myThread线程打上停止标记。然后用interrupted()来判断线程状态。但从输出结果来看，线程并未停止，这也就印证了interrupted()的解释：**测试当前线程是否已经中断**，当前线程是main线程，从未中断过。  
将上面的代码稍作修改：
```
public class TestInterrupt {
    public static void main(String[] args) {
        try {
            MyThread myThread = new MyThread();
            myThread.start();
            Thread.sleep(100);
//            myThread.interrupt();
            Thread.currentThread().interrupt();
            System.out.println("是否停止1："+Thread.interrupted());
            System.out.println("是否停止1："+Thread.interrupted());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
输出结果：
```
是否停止1：true
是否停止1：false
```
修改后的代码使用Thread.currentThread().interrupt();中断了main线程，并且第一次执行interrupted()的结果为true，第二次执行的结果为false，这也印证了interrupted()具有**将状态标志清除的功能**。  
再来看看isInterrupted()方法：
```
    public static void main(String[] args) {
          MyThread myThread = new MyThread();
          myThread.start();
          myThread.interrupt();
          System.out.println("是否停止1："+myThread.isInterrupted());
          System.out.println("是否停止1："+myThread.isInterrupted());
    }
```
输出结果：
```
是否停止1：true
是否停止1：true
```
从输出结果可以看到，方法isInterrupted()并未清除状态标志，所以打印了两个true。
#### 3.2 中断线程的方法——异常法
```
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
```
输出结果：
```
线程是停止状态了。。。。
线程停止，跳出for循环，进入catch
java.lang.InterruptedException
	at com.panda.thread.stop.interrupt.InterruptMyThread.run(InterruptMyThread.java:11)
```
### 3.3 中断线程的方法——return
 将方法interrupt()与return结合使用也能实现停止线程的效果。
 ```
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
```
输出结果：
```
...
timer=1611474998138
timer=1611474998138
timer=1611474998138
timer=1611474998138
线程停止了!
```
不过还是建议使用“异常法”来实现线程的停止，因为可以在catch块中可以对异常的信息进行相关的处理，而且使用异常流能更好、更方便地控制程序的运行流程。
#### 3.4 在沉睡中停止
如果在sleep状态下停止某一线程，会抛出InterruptedException异常，并清除停止状态值。
```
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
```
输出结果：
```
begin
进入catch--false
java.lang.InterruptedException: sleep interrupted
	at java.lang.Thread.sleep(Native Method)
	at com.panda.thread.stop.interrupt.InterruptThreadinSleep.run(InterruptThreadinSleep.java:9)
```
