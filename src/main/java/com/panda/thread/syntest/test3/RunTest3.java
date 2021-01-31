package com.panda.thread.syntest.test3;

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
