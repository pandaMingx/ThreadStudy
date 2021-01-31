package com.panda.thread.syntest.test1;

import com.panda.thread.syntest.test1.TestService1;

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
