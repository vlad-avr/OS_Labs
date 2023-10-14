package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.example.control.Controller;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // Controller controller = new Controller();
        // controller.start();
        System.out.println("HELOO");
        // try {
        //     PipedOutputStream outStream = new PipedOutputStream(); 
        //     PipedInputStream inputStream = new PipedInputStream(outStream);
        //     ObjectOutputStream oos = new ObjectOutputStream(outStream);
        //     ObjectInputStream ois = new ObjectInputStream(inputStream);
        //     oos.writeObject("TEST");
        //     System.out.println((String)ois.readObject());
        //     ois.close();
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } catch (ClassNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
