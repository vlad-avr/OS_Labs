package com.example.func;

import java.security.SecureRandom;

public abstract class Function {
    protected final SecureRandom rnd = new SecureRandom();
    protected FunctionContainer container;
    protected int maxTimeCount = 15000;

    public void setContainer(FunctionContainer container){
        this.container = container;
    }

    public abstract Result compute(String input, int minorErrorAttempts);
}
