package com.panda.thread.syntest.test5;

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
