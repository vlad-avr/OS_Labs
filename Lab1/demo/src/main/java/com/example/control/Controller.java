package com.example.control;

import java.util.Scanner;

public class Controller {

    Manager manager = new Manager();

    public void start() {
        mainLoop();
    }

    private void mainLoop() {
        String input;
        while (true) {
            input = get_string("\nCommand: ");
            switch (input) {
                case "c":
                    input = get_string("\n Enter number: ");
                    manager.calculate(input);
                    break;
                case "s":
                    /* cancel */break;
                case "r":
                    /* report */break;
                case "h":
                    help();
                    break;
                case "e":
                    manager.Done();
                    return;
                default:
                    System.out.println("\nUnknown command!");
            }
        }
    }

    private void help() {
        System.out.println(
                "\n c -> start calculation;\n s -> cancel current computation;\n r -> get a report on program status;\n h -> print help;\n e - exit program");
    }

    private String get_string(String prompt) {
        Scanner scan = new Scanner(System.in);
        String res;
        do {
            System.out.println(prompt);
            res = scan.nextLine();
        } while (res == null);
        return res;
    }

}
