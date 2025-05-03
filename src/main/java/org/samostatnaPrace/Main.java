package org.samostatnaPrace;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initDatabase();
        StudentRepository studentRepo = new StudentRepository();
        Controller app = new Controller(studentRepo);
        app.run();
    }
}