package org.samostatnaPrace.studentGroups;

import org.samostatnaPrace.Student;

import java.util.HashMap;
import java.util.Map;

public class StudentTelekomunikaci extends Student {
    private static final Map<Character, String> MORSE = new HashMap<>();
    static {
        MORSE.put('A', ".-"); MORSE.put('B', "-..."); MORSE.put('C', "-.-.");
        MORSE.put('D', "-.."); MORSE.put('E', "."); MORSE.put('F', "..-.");
        MORSE.put('G', "--."); MORSE.put('H', "...."); MORSE.put('I', "..");
        MORSE.put('J', ".---"); MORSE.put('K', "-.-"); MORSE.put('L', ".-..");
        MORSE.put('M', "--"); MORSE.put('N', "-."); MORSE.put('O', "---");
        MORSE.put('P', ".--."); MORSE.put('Q', "--.-"); MORSE.put('R', ".-.");
        MORSE.put('S', "..."); MORSE.put('T', "-"); MORSE.put('U', "..-");
        MORSE.put('V', "...-"); MORSE.put('W', ".--"); MORSE.put('X', "-..-");
        MORSE.put('Y', "-.--"); MORSE.put('Z', "--..");

        MORSE.put('0', "-----"); MORSE.put('1', ".----"); MORSE.put('2', "..---");
        MORSE.put('3', "...--"); MORSE.put('4', "....-"); MORSE.put('5', ".....");
        MORSE.put('6', "-...."); MORSE.put('7', "--..."); MORSE.put('8', "---..");
        MORSE.put('9', "----.");
    }

    public StudentTelekomunikaci(String jmeno, String prijmeni, int rokNarozeni) {
        super(jmeno, prijmeni, rokNarozeni);
    }

    private String toMorse(String text) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toUpperCase().toCharArray()) {
            if (MORSE.containsKey(ch)) {
                sb.append(MORSE.get(ch)).append(" ");
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String zpracujIdentitu() {
        return toMorse(jmeno + " " + prijmeni);
    }
}
