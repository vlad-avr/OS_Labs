package com.example.control;


public class Manager {

    public int GCD(int n2, int n1) {
       // System.out.println("\n\t " + n2 + "\t " + n1);
        if (n2 == 0) {
            return n1;
        }
        return GCD(n2, n1 % n2);
    }

}
