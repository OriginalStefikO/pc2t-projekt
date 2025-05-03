package org.samostatnaPrace;

import org.samostatnaPrace.studentGroups.StudentKyberbezpecnosti;
import org.samostatnaPrace.studentGroups.StudentTelekomunikaci;

import java.util.*;

public class Controller {
    private Scanner scanner = new Scanner(System.in);
    private StudentRepository repository;

    public Controller(StudentRepository repository) {
        this.repository = repository;
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = readInt("Zadejte číslo volby: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGradeToStudent();
                case 3 -> expelStudent();
                case 4 -> findStudentById();
                case 5 -> activateStudentSkill();
                case 6 -> printSortedStudentsInGroups();
                case 7 -> printAverageGradesByField();
                case 8 -> printStudentCountsInGroups();
//                case 9 -> saveStudentToFile();
//                case 10 -> loadStudentFromFile();
                case 0 -> {
                    System.out.println("Ukončuji program.");
                    return;
                }
                default -> System.out.println("Neplatná volba.");
            }
        }
    }

    private void printMenu() {
        System.out.println("""
            ===== MENU =====
            1. Přidat nového studenta
            2. Zadat studentovi známku
            3. Propustit studenta z univerzity
            4. Vyhledat studenta dle ID
            5. Spustit dovednost studenta
            6. Výpis studentů abecedně podle skupin
            7. Výpis průměrů podle oborů
            8. Počet studentů ve skupinách
            9. Uložit studenta do souboru
            10. Načíst studenta ze souboru
            0. Konec
            =================
            """);
    }

    private void addStudent() {
        System.out.print("Zadej název skupiny: ");
        String studentType = scanner.nextLine();

        System.out.print("Zadej jméno: ");
        String firstName = scanner.nextLine();

        System.out.print("Zadej příjmení: ");
        String lastName = scanner.nextLine();

        int year = readInt("Zadej rok narození: ");

        Student student;
        if (studentType.equalsIgnoreCase("telekomunikace")) {
            student = new StudentTelekomunikaci(firstName, lastName, year);
        } else if (studentType.equalsIgnoreCase("kyberbezpecnost")) {
            student = new StudentKyberbezpecnosti(firstName, lastName, year);
        } else {
            System.out.println("Neznámý typ studenta.");
            return;
        }

        repository.pridejStudenta(student);
    }

    private void addGradeToStudent() {
        int id = readInt("Zadej ID studenta: ");
        Student student = repository.nactiStudenta(id);
        if (student == null) {
            System.out.println("Student nenalezen.");
            return;
        }
        int grade = readInt("Zadej známku: ");
        repository.pridejZnamku(id, grade);
    }

    private void expelStudent() {
        int id = readInt("Zadej ID studenta: ");
        repository.smazatStudenta(id);
        System.out.println("Student byl úspěšně odstraněn.");
    }

    private void findStudentById() {
        int id = readInt("Zadej ID studenta: ");
        Student student = repository.nactiStudenta(id);
        if (student == null) {
            System.out.println("Student nenalezen.");
            return;
        }
        System.out.println(student);
    }

    private void activateStudentSkill() {
        int id = readInt("Zadej ID studenta: ");
        Student student = repository.nactiStudenta(id);
        if (student != null) {
            student.zpracujIdentitu();
        } else {
            System.out.println("Student nenalezen.");
        }
    }

    private void printSortedStudentsInGroups() {
        for (Student student : repository.nactiVsechnyStudenty()) {
            if (student == null) {
                System.out.println("Student nenalezen.");
                continue;
            }
            System.out.println(student);
        }
    }

    private void printAverageGradesByField() {
        System.out.println("Průměrné známky podle oborů:");
        System.out.println("Telekomunikace: " + repository.prumerneZnamkyPodleOboru("telekomunikace"));
        System.out.println("Kyberbezpečnost: " + repository.prumerneZnamkyPodleOboru("kyberbezpecnost"));
    }

    private void printStudentCountsInGroups() {
        System.out.println("Počet studentů ve skupinách:");
        System.out.println("Telekomunikace: " + repository.pocetStudentuVeSkupine("telekomunikace"));
        System.out.println("Kyberbezpečnost: " + repository.pocetStudentuVeSkupine("kyberbezpecnost"));
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Zadej číslo: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Zadej číslo: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}

