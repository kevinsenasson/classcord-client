package fr.classcord.app;

import fr.classcord.model.Message;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Message message = new Message("private", null, "Kevinou", "Kloklo", "Hello World!", null);
        System.out.println(message);
    }
}
