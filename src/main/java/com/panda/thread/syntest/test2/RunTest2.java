package com.panda.thread.syntest.test2;

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
