# java多线程（六）—— 线程池
## 1 概述
线程池做的主要工作是控制运行线程的数量，处理过程中将任务放入队列，然后再在线程创建后启动这些任务，如果线程数量超过了最大数量，超出数量的线程排队等候，等其他线程执行完毕，再从队列中取出任务来执行。  
线程池的主要特点为：线程复用，控制最大并发数，管理线程。  
线程池的好处：
- 降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗；
- 提高响应速度。当任务到达时，任务可以不需要等待线程创建就能立即执行。
- 方便管理线程。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统过得稳定性，使用线程池可以进行统一的分配，调优和监控。

## 2.三种常用线程池
- Executors.newFixedThreadPool(5):创建固定大小线程数的线程池
- Executors.newSingleThreadExecutor()：创建单个线程数的线程池
- Executors.newCachedThreadPool()：线程池根据任务数按需创建线程数

无论哪种线程池，底层都是通过ThreadPoolExecutor实现：
```java
/* @param nThreads the number of threads in the pool
     * @return the newly created thread pool
     * @throws IllegalArgumentException if {@code nThreads <= 0}
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
```
```java
public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
```
```java
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
```
## 3.线程池7大参数
```java
/**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters.
     *
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```
- corePoolSize:核心线程数，一直存在线程池中的线程数，即使这些线程是空闲的。
- maximumPoolSize：最大线程数，线程池中允许存在的最大线程数。
- keepAliveTime：空闲时间，非核心线程空闲时间，超过空闲时间这些线程将被回收。
- unit：空闲时间单位
- workQueue：阻塞队列，当有新任务提交给线程池执行时，线程池的核心线程没有空闲时，任务进入阻塞队列。一旦有核心线程空闲立即从阻塞队列中获取任务执行。
- threadFactory：线程工厂，用来创建生产线程。
- handler：拒绝策略，线程池线程数达到最大线程数且这些线程都没有空闲时且阻塞队列满时，提交的新任务将根据拒绝策略处理。

## 4.线程池核心工作原理
1.当线程数小于核心线程数时，创建线程。  
2.当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。  
3.当线程数大于等于核心线程数，且任务队列已满   
4.若线程数小于最大线程数，创建线程  
5.若线程数等于最大线程数，抛出异常，拒绝任务  
img

## 5.拒绝策略
jdk提供四种拒绝策略，默认为AbortPolicy  
img
> - AbortPolicy：默认，直接抛出RejectedExcutionException异常，阻止系统正常运行
> - DiscardPolicy：直接丢弃任务，不予任何处理也不抛出异常，如果运行任务丢失，这是一种好方案
> - CallerRunsPolicy：该策略既不会抛弃任务，也不会抛出异常，而是将此任务回退到调用者，由调用者所在线程执行此任务
> - DiscardOldestPolicy：抛弃队列中等待最久的任务，然后把当前任务加入队列中尝试再次提交当前任务

## 6.如何合理配置线程池参数
1.cpu密集型：核心线程数=cpu核数+1；  
2.io密集型：核心线程数=cpu核数*2；
