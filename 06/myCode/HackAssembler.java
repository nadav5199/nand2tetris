import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class HackAssembler {
    public static void main(String[] args) {
        try {
            Parser parser1 = new Parser(args[0]);
            Parser parser2 = new Parser(args[0]);
            SymbolTable symbolTable = new SymbolTable();
            init(symbolTable);
            int countLine = 0;
            int countVar = 16;
            // create new hack file.
            String path = args[0].replaceAll(".asm", ".hack");
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists");
            }
            FileWriter newFile = new FileWriter(path);
            StringBuilder line = new StringBuilder();
            while (parser1.hasMoreLines()) {
                parser1.advance();
                if (parser1.instructionType().equals("L_INSTRUCTION")) {
                    String symbol = parser1.symbol();
                    if (!symbolTable.contains(symbol)) {
                        symbolTable.addEntry(symbol, countVar);
                        countVar++;
                    }
                }
            }
            while (parser2.hasMoreLines()) {
                parser2.advance();
                line.delete(0, line.length());
                if (parser2.instructionType().equals("C_INSTRUCTION")) {
                    line.append("111");
                    line.append(Code.comp(parser2.comp()));
                    line.append(Code.dest(parser2.dest()));
                    line.append(Code.jump(parser2.jump()));
                    newFile.write(line.toString() + "\n");
                } else {
                    String symbol = parser2.symbol();
                    int val;
                    String bin;
                    if (!symbolTable.contains(symbol)) {
                        symbolTable.addEntry(symbol, countVar);
                        countVar++;
                    }
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
}

