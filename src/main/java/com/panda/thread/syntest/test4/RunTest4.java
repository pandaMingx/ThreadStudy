package com.panda.thread.syntest.test4;

public class RunTest4 {
    public static void main(String[] args) {
        TestService4 testService4 = new TestService4();
        new Thread(testService4::methodA).start();
    }
}

