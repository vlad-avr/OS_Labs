package com.example.control;

import java.util.Scanner;

public class Controller {

    private Manager manager = new Manager();
    private Scanner scan = new Scanner(System.in);
    private String input;

    public void start() {
        mainLoop();
    }

    public Scanner getScanner() {
        return this.scan;
    }

    private void mainLoop() {
        Thread managerThread;
        help();
        while (true) {
            input = get_string("\nCommand: ");
            switch (input) {
                case "c":
                    if (!manager.isComputing) {
                        input = get_string("\n Enter number: ");
                        managerThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                manager.calculate(input);
                            }
                        });
                        managerThread.start();
                    } else {
                        System.out.println(
                                "\nThere is an ongoing computation present right now. You can not start another one - cancel first!\n");
                    }
                    break;
                case "s":
                    if (manager.isComputing) {
                        manager.cancel();
                    }else{
                        System.out.println(
                                "\nUnable to cancel ongoing computation because there is no ongoing computation\n");
                    }
                    break;
                case "r":
                    /* report */break;
                case "h":
                    help();
                    break;
                case "e":
                    if(manager.isComputing){
                        manager.cancel();
                    }
                    manager.Done();
                    return;
                default:
                    System.out.println("\nUnknown command!");
            }
        }
    }

    private void help() {
        System.out.println(
                "\n c -> start calculation;\n s -> cancel current computation;\n r -> get report on current computation;\n h -> print help;\n e -> exit program");
    }

    private String get_string(String prompt) {
        String res;
        do {
            System.out.println(prompt);
            res = scan.nextLine();
        } while (res == null);
        return res;
    }

}
