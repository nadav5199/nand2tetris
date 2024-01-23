import java.io.*;
import java.util.Scanner;

class Parser {
    Scanner file;
    String instruction;
    private final String A_INSTRUCTION = "A_INSTRUCTION";
    private final String C_INSTRUCTION = "C_INSTRUCTION";
    private final String L_INSTRUCTION = "L_INSTRUCTION";

    public Parser(String path) throws FileNotFoundException {
        File input = new File(path);
        file = new Scanner(input);
    }

    public boolean hasMoreLines() {
        return file.hasNextLine();
    }

    public void advance() {
        String next = null;
        if (hasMoreLines()) {
            next = file.nextLine();
            while (next.isEmpty() || next.startsWith("//")) {
                next = file.nextLine();
            }
        }
        instruction = next;
    }

    public String instructionType() {
        if (instruction.startsWith("@")) {
            return A_INSTRUCTION;
        } else if (instruction.startsWith("(")) {
            return L_INSTRUCTION;
        } else {
            return C_INSTRUCTION;
        }
    }

    public String symbol() {
        if (instructionType().equals(L_INSTRUCTION)) {
            return instruction.substring(1, instruction.length() - 1);
        } else {
            return instruction.substring(1);
        }
    }

    public String dest() {
        if (instructionType().equals(C_INSTRUCTION)) {
            int pos = instruction.indexOf("=");
            if (pos != -1) {
                return instruction.substring(0, pos);
            }
        }
        return null;
    }

    public String comp() {
        int pos1 = 0;
        if (instructionType().equals(C_INSTRUCTION)) {
            pos1 = instruction.indexOf("=") + 1;
            if (pos1 == -1) {
                pos1 = 0;
            }
            if (instruction.indexOf(";") == -1) {
                return instruction.substring(pos1);
            }
        }
        return instruction.substring(pos1, instruction.indexOf(";"));
    }

    public String jump() {
        int pos = 0;
        if (instructionType().equals(C_INSTRUCTION)) {
            pos = instruction.indexOf(";");
            if (pos == -1) {
                return null;
            }
        }
        return instruction.substring(pos + 1);
    }
}
