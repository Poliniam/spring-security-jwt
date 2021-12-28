package com.javamaster.springsecurityjwt.log;

import org.aspectj.lang.annotation.*;

public class Logging {
    @Pointcut("execution(* net.proselyte.aop.*.*(..))")
    public void selectAllMethodsAvaliable() {

    }

    @Before("selectAllMethodsAvaliable()")
    public void beforeAdvice() {
        System.out.println("Now we are going to initiate profile.");
    }

    @After("selectAllMethodsAvaliable()")
    public void afterAdvice() {
        System.out.println("Profile has been initiated.");
    }

    @AfterReturning(pointcut = "selectAllMethodsAvaliable()", returning = "someValue")
    public void afterReturningAdvice(Object someValue) {
        System.out.println("Value: " + someValue.toString());
    }

    @AfterThrowing(pointcut = "selectAllMethodsAvaliable()", throwing = "e")
    public void inCaseOfExceptionThrowAdvice(ClassCastException e) {
        System.out.println("An exception: " + e.toString());
    }

}
