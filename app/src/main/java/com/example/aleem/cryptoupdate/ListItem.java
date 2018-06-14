package com.example.aleem.cryptoupdate;



public class ListItem {

    private String name;
    private String symbol;
    private String price;
    private String changes;

    public ListItem(String name, String symbol, String price, String changes) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.changes = changes;
    }

    public String getNames() {
        return name;
    }

    public String getSymbol(){
        return symbol;
    }
    public String getPrice() {
        return price;
    }

    public String getChanges() {
        return changes;
    }
}
