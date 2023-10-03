package com.example.control;

import java.util.Scanner;

public class Controller {
    
    public void start(){

    }

    private void main_loop(){
        String input;
        while(true){
            input = get_string("\nCommand: ");
            switch(input){
                case "c" : /*calculate */break;
                case "s" : /*cancel */break;
                case "r" : /*report */break;
                case "h" : help(); break;
                case "e" : return;
                default : System.out.println("\nUnknown command!");
            }
        }
    }

    private void help(){
        System.out.println("\n c -> start calculation;\n s -> cancel current computation;\n r -> get a report on program status;\n h -> print help;\n e - exit program");
    }

    private String get_string(String prompt){
        Scanner scan = new Scanner(System.in);
        String res;
        do{
            System.out.println(prompt);
            res = scan.nextLine();
        }while(res == null);
        return res;
    }

}
