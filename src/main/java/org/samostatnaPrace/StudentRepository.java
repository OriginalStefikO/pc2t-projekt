package org.samostatnaPrace;

import org.samostatnaPrace.studentGroups.StudentKyberbezpecnosti;
import org.samostatnaPrace.studentGroups.StudentTelekomunikaci;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public void pridejStudenta(Student student) {
        String sqlQuery = "INSERT INTO students(jmeno, prijmeni, rokNarozeni, typ) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, student.getJmeno());
            preparedStatement.setString(2, student.getPrijmeni());
            preparedStatement.setInt(3, student.getRokNarozeni());
            preparedStatement.setString(4, getTypStudenta(student));

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    student.setId(generatedId);
                }
            }

        } catch (SQLException e) {
            System.out.println("Chyba při ukládání studenta: " + e.getMessage());
        }
    }

    public Student nactiStudenta(int studentId) {
        String sqlQuery = "SELECT * FROM students WHERE id = ?";
        Student student = null;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String jmeno = resultSet.getString("jmeno");
                String prijmeni = resultSet.getString("prijmeni");
                int rokNarozeni = resultSet.getInt("rokNarozeni");
                String typ = resultSet.getString("typ");

                if (typ == null) {
                    System.out.println("Neznámý typ studenta: " + typ);
                    return null;
                }

                switch (typ) {
                    case "telekomunikace" -> student = new StudentTelekomunikaci(jmeno, prijmeni, rokNarozeni);
                    case "kyberbezpecnost" -> student = new StudentKyberbezpecnosti(jmeno, prijmeni, rokNarozeni);
                    default -> {
                        System.out.println("Neznámý typ studenta: " + typ);
                        return null;
                    }
                }

                student.setId(studentId);
                nactiZnamkyStudenta(student);
            }

        } catch (SQLException e) {
            System.out.println("Chyba při načítání studenta: " + e.getMessage());
        }

        return student;
    }

    public void smazatStudenta(int studentId) {
        String deleteGrades = "DELETE FROM znamky WHERE student_id = ?";
        String deleteStudent = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmtGrades = conn.prepareStatement(deleteGrades);
             PreparedStatement stmtStudent = conn.prepareStatement(deleteStudent)) {

            stmtGrades.setInt(1, studentId);
            stmtGrades.executeUpdate();

            stmtStudent.setInt(1, studentId);
            stmtStudent.executeUpdate();

            System.out.println("Student s ID " + studentId + " byl úspěšně odstraněn.");

        } catch (SQLException e) {
            System.out.println("Chyba při mazání studenta: " + e.getMessage());
        }
    }


    public void pridejZnamku(int studentId, int znamka) {
        String sqlQuery = "INSERT INTO znamky(student_id, znamka) VALUES(?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, znamka);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Chyba při ukládání známky: " + e.getMessage());
        }
    }

    private String getTypStudenta(Student student) {
        if (student instanceof StudentTelekomunikaci) {
            return "telekomunikace";
        } else if (student instanceof StudentKyberbezpecnosti) {
            return "kyberbezpecnost";
        } else {
            throw new IllegalArgumentException("Neznámý typ studenta");
        }
    }

    public List<Student> nactiVsechnyStudenty() {
        List<Student> studenti = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY Prijmeni, Jmeno";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String jmeno = rs.getString("jmeno");
                String prijmeni = rs.getString("prijmeni");
                int rokNarozeni = rs.getInt("rokNarozeni");
                String typ = rs.getString("typ");

                Student student;
                if (typ == null) {
                    System.out.println("Neznámý typ studenta: " + typ);
                    continue;
                }

                switch (typ) {
                    case "telekomunikace" -> student = new StudentTelekomunikaci(jmeno, prijmeni, rokNarozeni);
                    case "kyberbezpecnost" -> student = new StudentKyberbezpecnosti(jmeno, prijmeni, rokNarozeni);
                    default -> {
                        System.out.println("Neznámý typ studenta: " + typ);
                        continue;
                    }
                }

                student.setId(id);
                nactiZnamkyStudenta(student);
                studenti.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Chyba při načítání studentů: " + e.getMessage());
        }

        return studenti;
    }

    private void nactiZnamkyStudenta(Student student) {
        String sql = "SELECT znamka FROM znamky WHERE student_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, student.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                student.pridatZnamku(rs.getInt("znamka"));
            }

        } catch (SQLException e) {
            System.out.println("Chyba při načítání známek: " + e.getMessage());
        }
    }

    public String prumerneZnamkyPodleOboru(String typStudentu) {
        String sql = """
                SELECT AVG(z.znamka) AS prumer FROM students s
                JOIN znamky z ON s.id = z.student_id
                WHERE s.typ = ?
            """;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, typStudentu);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return String.valueOf(rs.getDouble("prumer"));
            }
        } catch (SQLException e) {
            System.out.println("Chyba při výpočtu průměrné známky: " + e.getMessage());
        }

        return null;
    }

    public String pocetStudentuVeSkupine(String typStudentu) {
        String sql = "SELECT COUNT(*) AS pocet FROM students WHERE typ = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, typStudentu);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return String.valueOf(rs.getInt("pocet"));
            }
        } catch (SQLException e) {
            System.out.println("Chyba při počítání studentů: " + e.getMessage());
        }

        return null;
    }
}
