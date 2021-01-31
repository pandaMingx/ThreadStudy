package com.panda.thread.syntest.test6;

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
