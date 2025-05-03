package org.samostatnaPrace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:identifier.sqlite";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDatabase() {
        try (Connection conn = connect()) {
            var stmt = conn.createStatement();

            String sqlQuery = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    jmeno TEXT NOT NULL,
                    prijmeni TEXT NOT NULL,
                    rokNarozeni INTEGER NOT NULL,
                    typ TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS znamky (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id INTEGER NOT NULL,
                    znamka INTEGER NOT NULL,
                    FOREIGN KEY(student_id) REFERENCES students(id)
                );
            """;
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


