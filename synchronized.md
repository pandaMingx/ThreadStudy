## Java多线程（三）
本章主要讨论**synchronized**。
### 1.概述
“非线程安全”问题会在多个线程对同一个对象中的实例变量进行并发访问时发生，产生的后果就是“脏读”，也就是取到的数据其实是被更改过的。而“线程安全”就是以获得的实例变量的值是经过同步处理的，不会出现脏读的现象。  
**synchronzied**用来保证线程安全。

### 2.synchronized的用法
synchronized的用法共有如下四种：
- （1）synchronized同步方法
- （2）synchronized(this)同步代码块
- （3）synchronized(任意对象)同步代码块
- （4）synchronized同步静态方法

#### 2.1 synchronized同步方法 
synchronized同步方法的效果：  
1).同一时间只有一个线程可以执行synchronized同步方法中的代码。  
2).对其他synchronized同步方法或synchronized(this)同步代码块调用呈阻塞状态。  
```java
public class TestService1 {

    synchronized public void methodA(){
        try {
            System.out.println("methodA: begin! threadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodA: end! threadName is "+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
```java
public class RunTest1 {
    public static void main(String[] args) {
        TestService1 testService1 = new TestService1();
        Thread threadA = new Thread(testService1::methodA);
        Thread threadB = new Thread(testService1::methodA);
        threadA.setName("A");
        threadB.setName("B");
        threadA.start();
        threadB.start();
    }
}
```
运行结果：
```
methodA: begin! threadName is A
methodA: end! threadName is A
methodA: begin! threadName is B
methodA: end! threadName is B
```
上述代码及结果说明了第（1）点：同一时间只有一个线程可以执行synchronized同步方法中的代码。  
下面将上述代码稍作修改，以验证第（2）点：对其他synchronized同步方法或synchronized(this)同步代码块调用呈阻塞状态。
```java
public class TestService1 {

    synchronized public void methodA(){
        try {
            System.out.println("methodA: begin! threadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodA: end! threadName is "+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public void methodB(){
        try {
            System.out.println("methodB: begin! threadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodB: end! threadName is "+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 同步代码块
   public void methodC(){
        try {
            synchronized (this){
                System.out.println("methodC: begin! threadName is "+Thread.currentThread().getName());
                Thread.sleep(5000);
                System.out.println("methodC: end! threadName is "+Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
```java
public class RunTest1 {
    public static void main(String[] args) {
        TestService1 testService1 = new TestService1();
        Thread threadA = new Thread(testService1::methodA);
        Thread threadB = new Thread(testService1::methodB);
        Thread threadC = new Thread(testService1::methodC);

        threadA.setName("A");
        threadB.setName("B");
        threadC.setName("C");

        threadA.start();
        threadB.start();
        threadC.start();
    }
}
```
运行结果：
```
methodA: begin! threadName is A
methodA: end! threadName is A
methodC: begin! threadName is C
methodC: end! threadName is C
methodB: begin! threadName is B
methodB: end! threadName is B
```
#### 2.2 synchronized(this)同步代码块  
synchronized(this)同步代码块的效果：  
1).对其他synchronized同步方法或synchronized(this)同步代码块调用呈阻塞状态。  
2).同一时间只有一个线程可以执行synchronized(this)同步代码块中的代码。
**synchronized同步方法或synchronized(this)持有的都是当前对象。**  
其实2.1节的案例就可以验证synchronized(this)同步代码块的效果，这里不再重复造轮子。

#### 2.3 synchronized(任意对象)同步代码块  
1).在多个线程持有“对象监视器”作为同一个对象的前提下，同一时间只有一个线程可以执行synchronized（非this对象x）同步代码块中的代码。  
2).当持有“对象监视器“为同一个对象的前提下，同一时间只有一个线程可以执行synchronized(非this对象x)同步代码块中的代码。  
下面通过代码验证：
```java
public class TestService2 {

    private String threadMonitor = new String();

     public void methodA(){
        try {
            synchronized (threadMonitor){
                System.out.println("methodA: begin! threadName is "+Thread.currentThread().getName());
                Thread.sleep(5000);
                System.out.println("methodA: end! threadName is "+Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
```java
public class RunTest2 {
    public static void main(String[] args) {
        TestService2 testService2 = new TestService2();
        Thread threadA = new Thread(testService2::methodA);
        Thread threadB = new Thread(testService2::methodA);

        threadA.setName("A");
        threadB.setName("B");
        
        threadA.start();
        threadB.start();

    }
}
```
运行结果：
```
methodA: begin! threadName is A
methodA: end! threadName is A
methodA: begin! threadName is B
methodA: end! threadName is B
```
可以看到threadA和threadB的对象监视器是同一个对象，所以同一时间只有一个线程被执行。  
将上述TestService2的代码稍作修改：
```java
public class TestService2 {

//    private String threadMonitor = new String();

     public void methodA(){
         String threadMonitor = new String();
        try {
            synchronized (threadMonitor){
                System.out.println("methodA: begin! threadName is "+Thread.currentThread().getName());
                Thread.sleep(5000);
                System.out.println("methodA: end! threadName is "+Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
main方法不变，运行main方法得到结果：
``` 
methodA: begin! threadName is A
methodA: begin! threadName is B
methodA: end! threadName is A
methodA: end! threadName is B
```
此时threadA和threadB的对象监视器不是同一个对象，故异步运行。

#### 2.4 synchronized同步静态方法
关键字synchronized如果应用在static静态方法上，就是对当前的.java文件对应的Class类进行持锁。而synchronized关键字加到非static静态方法上是给对象上锁。
```java
public class TestService3 {

    synchronized public static void methodA() {
        try {
            System.out.println("methodA: begin! threadName is " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodA: end! threadName is " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public void methodB() {
        try {
            System.out.println("methodB: begin! threadName is " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("methodB: end! threadName is " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
```java
public class RunTest3 {
    public static void main(String[] args) {
        TestService3 testService3 = new TestService3();
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                testService3.methodA();
            }
        });
        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                testService3.methodB();
            }
        });

        threadA.setName("A");
        threadB.setName("B");

        threadA.start();
        threadB.start();

    }
}
```
运行结果：
```
methodA: begin! threadName is A
methodB: begin! threadName is B
methodA: end! threadName is A
methodB: end! threadName is B 
```
运行结果是异步的，异步的原因是持有不同的锁，一个是对象锁，另外一个Class锁，Class锁可以对类的所有对象实例起作用。  

### 3.synchronized的特性
synchronized具有以下三个特性：
- （1）synchronized锁重入；
- （2）出现异常，锁自动释放；
- （3）同步不具有继承性。
#### 3.1 synchronized锁重入
关键字synchronized拥有锁重入功能，也就是在使用synchronized时，当一个线程得到一个对象锁后，再次请求次对象锁是可以再次得到该对象的锁的。
```java
public class TestService4 {
    synchronized public void methodA(){
        System.out.println("methodA!");
        methodB();
    }
    synchronized public void methodB(){
        System.out.println("methodB!");
        methodC();
    }
    synchronized public void methodC(){
        System.out.println("methodC!");
    }
}
```
```java
public class RunTest4 {
    public static void main(String[] args) {
        TestService4 testService4 = new TestService4();
        new Thread(testService4::methodA).start();
    }
}
```
运行结果：
```
methodA!
methodB!
methodC!
```
“可重入锁”的概念是：自己可以再次获取自己的内部锁，比如有个线程获得了某个对象的锁，此时这个对象锁还没有释放，当其再次想要获取这个对象的锁的时候还是可以获取的，如果不可锁重入的话，就会造成死锁。**可重入锁也支持在父子类继承的环境中**。
#### 3.2 出现异常，锁自动释放
```java
public class TestService5 {
    synchronized public void methodA(){
        if("A".equals(Thread.currentThread().getName())){
            System.out.println("methodA run! threadName is " +Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 让A线程运行到这里抛异常
            int a = 5/(2-2);
        }else{
            System.out.println("methodB run! threadName is " +Thread.currentThread().getName());
        }
    }
}
```

```java
public class RunTest5 {
    public static void main(String[] args) {
        TestService5 testService5 = new TestService5();
        Thread threadA = new Thread(testService5::methodA);
        Thread threadB = new Thread(testService5::methodA);

        threadA.setName("A");
        threadB.setName("B");
        
        threadA.start();
        threadB.start();
    }
}
```
运行结果：
``` 
methodA run! threadName is A
Exception in thread "A" java.lang.ArithmeticException: / by zero
	at com.panda.thread.syntest.test5.TestService5.methodA(TestService5.java:13)
	at java.lang.Thread.run(Thread.java:748)
methodB run! threadName is B
```
线程A出现异常并释放锁，线程B进入methedA方法正常打印，出现异常的锁被释放了。

#### 3.3 同步不具有继承性
```java
public class TestService6 {
    synchronized public void methodA(){
        try {
            System.out.println("父类TestService6运行方法methodA---begin! theadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("父类TestService6运行方法methodA---end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```
```java
public class TestService6Sub extends TestService6{
    @Override
    public void methodA() {
        try {
            System.out.println("子类TestService6Sub运行方法methodA---begin! threadName is "+Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("子类TestService6Sub运行方法methodA---end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.methodA();
    }
}
```
```java
public class RunTest6 {
    public static void main(String[] args) {
        TestService6Sub testService6Sub = new TestService6Sub();
        Thread threadA = new Thread(testService6Sub::methodA);
        Thread threadB = new Thread(testService6Sub::methodA);
        
        threadA.setName("A");
        threadB.setName("B");

        threadA.start();
        threadB.start();
    }
}
```
运行结果：
``` 
子类TestService6Sub运行方法methodA---begin! threadName is A
子类TestService6Sub运行方法methodA---begin! threadName is B
子类TestService6Sub运行方法methodA---end
子类TestService6Sub运行方法methodA---end
父类TestService6运行方法methodA---begin! theadName is A
父类TestService6运行方法methodA---end
父类TestService6运行方法methodA---begin! theadName is B
父类TestService6运行方法methodA---end
```
从运行结果看出，同步不能继承，所以还得在子类的方法中添加synchronized关键字。  
添加关键字synchronized后的运型结果：
``` 
子类TestService6Sub运行方法methodA---begin! threadName is A
子类TestService6Sub运行方法methodA---end
父类TestService6运行方法methodA---begin! theadName is A
父类TestService6运行方法methodA---end
子类TestService6Sub运行方法methodA---begin! threadName is B
子类TestService6Sub运行方法methodA---end
父类TestService6运行方法methodA---begin! theadName is B
父类TestService6运行方法methodA---end
```
### 4.synchronized和volatile比较
- （1）关键字volatile是线程同步的轻量级实现，所以volatile性能比synchronized要好，并且volatile只能修饰变量，而synchronized可以修饰方法、代码块。
- （2）多线程访问volatile不会发生阻塞，而synchronized会出现阻塞。
- （3）volatile能保证数据的可见性，但不能保证原子性；而synchronized可以保证原子性，也可间接保证可见性，因为他会将私有内存和公共内存中的数据做同步。
- （4）关键字volatile解决的是变量在多个线程之间的可见性；而synchronized关键字解决的是多个线程之间访问资源的同步性。  
关键字volatile主要使用场合是在多个线程中可以感知实例变量被更改了，并且可以获得最新的值使用，也就是用多线程读取共享变量时可以获得最新值使用。
