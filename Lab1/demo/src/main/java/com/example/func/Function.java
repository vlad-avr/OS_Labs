package com.example.func;

import java.security.SecureRandom;

public abstract class Function {
    protected final SecureRandom rnd = new SecureRandom();


    public abstract Result compute(String input, int minorErrorAttempts);
}
