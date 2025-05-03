package org.samostatnaPrace;

import org.samostatnaPrace.studentGroups.StudentKyberbezpecnosti;
import org.samostatnaPrace.studentGroups.StudentTelekomunikaci;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Controller {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentRepository repository;

    public Controller(StudentRepository repository) {
        this.repository = repository;
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = readInt("Zadejte cislo volby: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGradeToStudent();
                case 3 -> expelStudent();
                case 4 -> findStudentById();
                case 5 -> activateStudentSkill();
                case 6 -> printSortedStudentsByLastName();
                case 7 -> printAverageGradesOfField();
                case 8 -> printStudentCountsInGroups();
                case 9 -> saveStudentToFile();
                case 10 -> loadStudentFromFile();
                case 0 -> {
                    System.out.println("Ukoncuji program.");
                    return;
                }
                default -> System.out.println("Neplatna volba.");
            }
        }
    }

    private void loadStudentFromFile() {
        scanner.nextLine();
        System.out.print("Zadejte nazev souboru pro nacteni studenta: ");
        String nazevSouboru = scanner.nextLine();

        int studentId = -1;
        String jmeno = null;
        String prijmeni = null;
        int rokNarozeni = -1;
        List<Integer> znamky = new ArrayList<>();
        String typ = null;

        try {
            FileReader fileReader = new FileReader(nazevSouboru);
            Scanner fileScanner = new Scanner(fileReader);

            while (fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(": ");

                switch (line[0]) {
                    case "ID" -> studentId = Integer.parseInt(line[1]);
                    case "Jmeno" -> jmeno = line[1];
                    case "Prijmeni" -> prijmeni = line[1];
                    case "Rok narozeni" -> rokNarozeni = Integer.parseInt(line[1]);
                    case "Znamky" -> {
                        line[1] = line[1].replace("[", "").replace("]", "");
                        String[] grades = line[1].split(", ");
                        for (String grade : grades) {
                            znamky.add(Integer.parseInt(grade));
                        }
                    }
                    case "Typ" -> typ = line[1];
                }
            }

            if (jmeno == null || prijmeni == null || rokNarozeni == -1 || typ == null) {
                System.out.println("Chyba: Neco z udaju neni spravne.");
                return;
            }

            if (studentId != -1) {
                repository.upravStudenta(studentId, jmeno, prijmeni, rokNarozeni);
                repository.upravZnamkyStudenta(studentId, znamky);
                return;
            }

            if (typ.equals("telekomunikace")) {
                Student student = new StudentTelekomunikaci(jmeno, prijmeni, rokNarozeni);
                student.setId(studentId);
                student.setZnamky(znamky);
                int addedStudentId = repository.pridejStudenta(student);
                repository.upravZnamkyStudenta(addedStudentId, znamky);
            } else if (typ.equals("kyberbezpecnost")) {
                Student student = new StudentKyberbezpecnosti(jmeno, prijmeni, rokNarozeni);
                student.setId(studentId);
                student.setZnamky(znamky);
                int addedStudentId = repository.pridejStudenta(student);
                repository.upravZnamkyStudenta(addedStudentId, znamky);
            } else {
                System.out.println("Neznamy typ studenta.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Soubor nenalezen: " + e.getMessage());
        }
    }

    private void saveStudentToFile() {
        int id = readInt("Zadejte ID studenta k ulozeni do souboru: ");
        Student student = repository.nactiStudenta(id);
        if (student != null) {
            System.out.println("Ukladam studenta do souboru: " + student);

            try {
                FileWriter fileWriter = new FileWriter("student_" + id + ".txt");
                fileWriter.write("ID: " + student.getId() + "\n");
                fileWriter.write("Jmeno: " + student.getJmeno() + "\n");
                fileWriter.write("Prijmeni: " + student.getPrijmeni() + "\n");
                fileWriter.write("Rok narozeni: " + student.getRokNarozeni() + "\n");
                fileWriter.write("Znamky: " + student.getZnamky() + "\n");
                fileWriter.write("Studijni prumer: " + student.getStudijniPrumer() + "\n");
                fileWriter.write("Typ: " + (student instanceof StudentTelekomunikaci ? "telekomunikace" : "kyberbezpecnost") + "\n");
                fileWriter.write("Skill: " + student.zpracujSkill() + "\n");
                fileWriter.close();
                System.out.println("Student byl uspesne ulozen do souboru: " + "student_" + id + ".txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("Student nenalezen.");
        }
    }

    private void printMenu() {
        System.out.println("""
            ===== MENU =====
            1. Pridat noveho studenta\t\t\t\t6. Vypis studentu abecedne podle prijmeni
            2. Zadat studentovi znamku\t\t\t\t7. Vypis prumeru podle oboru
            3. Propustit studenta z univerzity\t\t8. Pocet studentu ve skupinach
            4. Vyhledat studenta dle ID\t\t\t\t9. Ulozit studenta do souboru
            5. Spustit dovednost studenta\t\t\t10. Nacist studenta ze souboru
            0. Konec
            =================
            """);
    }

    private void addStudent() {
        scanner.nextLine();
        System.out.print("Zadej nazev skupiny: ");
        String studentType = scanner.nextLine();

        System.out.print("Zadej jmeno: ");
        String firstName = scanner.nextLine();

        System.out.print("Zadej prijmeni: ");
        String lastName = scanner.nextLine();

        int year = readInt("Zadej rok narozeni: ");

        Student student;
        if (studentType.equalsIgnoreCase("telekomunikace")) {
            student = new StudentTelekomunikaci(firstName, lastName, year);
        } else if (studentType.equalsIgnoreCase("kyberbezpecnost")) {
            student = new StudentKyberbezpecnosti(firstName, lastName, year);
        } else {
            System.out.println("Neznamy typ studenta.");
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
        int grade = readInt("Zadej znamku: ");
        repository.pridejZnamku(id, grade);
    }

    private void expelStudent() {
        int id = readInt("Zadej ID studenta: ");
        repository.smazatStudenta(id);
        System.out.println("Student byl uspesne odstranen.");
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
            System.out.println("Vybran student: " + student);
            System.out.println("Skill studenta: " + student.zpracujSkill());
        } else {
            System.out.println("Student nenalezen.");
        }
    }

    private void printSortedStudentsByLastName() {
        for (Student student : repository.nactiVsechnyStudenty()) {
            if (student == null) {
                System.out.println("Student nenalezen.");
                continue;
            }
            System.out.println(student);
        }
    }

    private void printAverageGradesOfField() {
        System.out.println("Prumerne znamky podle oboru:");
        System.out.println("Telekomunikace: " + repository.prumerneZnamkyPodleOboru("telekomunikace"));
        System.out.println("Kyberbezpecnost: " + repository.prumerneZnamkyPodleOboru("kyberbezpecnost"));
    }

    private void printStudentCountsInGroups() {
        System.out.println("Pocet studentu ve skupinach:");
        System.out.println("Telekomunikace: " + repository.pocetStudentuVeSkupine("telekomunikace"));
        System.out.println("Kyberbezpecnost: " + repository.pocetStudentuVeSkupine("kyberbezpecnost"));
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Zadej cislo: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}

