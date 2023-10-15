package com.example.control;

import java.util.Scanner;

public class InputThread extends Thread {
    private Scanner scanner;
    private Manager manager;
    private boolean isDone = false;

    public InputThread(Scanner scanner, Manager manager) {
        this.scanner = scanner;
        this.manager = manager;
    }

    public void setDone() {
        this.isDone = true;
    }

    @Override
    public void run() {
        while (!isDone) {
            String input = getString(
                    "Enter runtime command (s -> cancel current computation; r -> get report on current computation): ");
            switch (input) {
                case "s":
                    manager.cancel();
                    return;
                case "r":
                    /* report */
                    break;
                case "":
                    isDone = true;
                    return;
                default:
                    System.out.println("Invalid command!");
            }
        }
    }

    private String getString(String prompt) {
        String res = "";
        System.out.println(prompt);
        while (!isDone && res == "") {
            res = scanner.nextLine();
        }
        return res;
    }

}
