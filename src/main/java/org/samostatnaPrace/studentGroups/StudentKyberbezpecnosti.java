package org.samostatnaPrace.studentGroups;

import org.samostatnaPrace.Student;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StudentKyberbezpecnosti extends Student {
    public StudentKyberbezpecnosti(String jmeno, String prijmeni, int rokNarozeni) {
        super(jmeno, prijmeni, rokNarozeni);
    }

    private String hashName(String vstup) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(vstup.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Chyba pri hashovani: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String zpracujSkill() {
        return hashName(jmeno + prijmeni);
    }
}

