import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    static int countVar = 0;
    static int countNew = 16;
    public static void main(String[] args){
        final Path inputPath = Paths.get(args[0]);
        // Check if the input is a directory or a single file
        if (Files.isDirectory(inputPath)) {
            // Process all .asm files in the directory
            try (Stream<Path> paths = Files.list(inputPath)) {
                for (Path p : paths.toList()) {
                    if (p.toString().endsWith(".asm")) {
                        process(p.toFile());
                    }
                }
            } catch (IOException e) {
                System.err.println("An error occurred while listing the files: " + e.getMessage());
            }
        } else if (inputPath.toString().endsWith(".asm")) {
            // If the input is a single .asm file, process it
            process(inputPath.toFile());
        } else {
            System.out.println("The specified path is not a directory or an .asm file.");
        }
    }
    public static void process(File fileName) {
        try {
            String name = fileName.toPath().toString();
            Parser parser1 = new Parser(name);
            Parser parser2 = new Parser(name);
            SymbolTable symbolTable = new SymbolTable();
            init(symbolTable);
            // create new hack file.
            String path = name.replaceAll(".asm", ".hack");
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
            FileWriter newFile = new FileWriter(path);
            StringBuilder line = new StringBuilder();
            while (parser1.hasMoreLines()) {
                parser1.advance();
                if (parser1.instructionType().equals("L_INSTRUCTION")) {
                    String symbol = parser1.symbol();
                    if (!symbolTable.contains(symbol)) {
                        symbolTable.addEntry(symbol, countVar);
                        countVar--;
                    }
                }
                countVar++;
            }
            while (parser2.hasMoreLines()) {
                parser2.advance();
                line.delete(0, line.length());
                if (parser2.instructionType().equals("C_INSTRUCTION")) {
                    line.append("111");
                    line.append(Code.comp(parser2.comp()));
                    line.append(Code.dest(parser2.dest()));
                    line.append(Code.jump(parser2.jump()));
                    line.append("\n");
                    newFile.write(line.toString());
                } else if (parser2.instructionType().equals("A_INSTRUCTION")) {
                    String symbol = parser2.symbol();
                    int val = countNew;
                    if (convertStringToInteger(symbol) != null){
                        val = convertStringToInteger(symbol);
                        symbol = "R" + val;
                    }
                    if (!symbolTable.contains(symbol)) {
                        symbolTable.addEntry(symbol, val);
                        countNew++;
                    }
                    String bin;
                    val = symbolTable.getAddress(symbol);
                    bin = Integer.toBinaryString(0x10000 | val).substring(1);
                    newFile.write(bin + "\n");
                }
            }
            newFile.close();
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }
    public static void init(SymbolTable symbolTable) {
        for (int i = 0; i < 16; i++) {
            symbolTable.addEntry("R" + i, i);
        }
        symbolTable.addEntry("SCREEN", 16384);
        symbolTable.addEntry("KBD", 24576);
        symbolTable.addEntry("SP", 0);
        symbolTable.addEntry("LCL", 1);
        symbolTable.addEntry("ARG", 2);
        symbolTable.addEntry("THIS", 3);
        symbolTable.addEntry("THAT", 4);
    }

    public static Integer convertStringToInteger(String str) {
        try {
            // Attempt to parse the string as an integer
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            // If an exception is thrown, the string is not a valid integer
            return null;
        }
    }
}

