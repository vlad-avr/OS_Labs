package com.example.control;

import java.util.Scanner;

public class Controller{

    private Manager manager = new Manager(this);
    private Scanner scan = new Scanner(System.in);

    public void start() {
        mainLoop();
    }

    public Scanner getScanner(){
        return this.scan;
    }

    private void mainLoop() {
        String input;

        help();
        while (true) {
            input = get_string("\nCommand: ");
            switch (input) {
                case "c":
                    input = get_string("\n Enter number: ");
                    manager.calculate(input);
                    break;
                // case "s":
                //     manager.cancel();
                //     break;
                // case "r":
                //     /* report */break;
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
