package org.samostatnaPrace;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        DatabaseManager.initDatabase();
        StudentRepository studentRepo = new StudentRepository();
        Controller app = new Controller(studentRepo);
        app.run();
    }
}