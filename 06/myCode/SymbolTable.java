import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Integer> symbol;

    public SymbolTable() {
        this.symbol = new HashMap<>();
    }

    public void addEntry(String symbol, int address) {
        this.symbol.put(symbol, address);
    }

    public boolean contains(String symbol) {
        return this.symbol.containsKey(symbol);
    }

    public int getAddress(String symbol) {
        return this.symbol.get(symbol);
    }
}
