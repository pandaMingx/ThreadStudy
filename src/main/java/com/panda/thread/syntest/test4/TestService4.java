package com.panda.thread.syntest.test4;

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
