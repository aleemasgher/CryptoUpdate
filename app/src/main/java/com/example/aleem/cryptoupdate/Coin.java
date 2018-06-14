package com.example.aleem.cryptoupdate;


public class Coin {
    private String coinNames, aboveElements, belowElements;
    private int id;

    public Coin(String coinNames, String aboveElements, String belowElements, int id) {
        this.coinNames = coinNames;
        this.aboveElements = aboveElements;
        this.belowElements = belowElements;
        this.id = id;
    }


    public String getCoinNames() {
        return coinNames;
    }

    public void setCoinNames(String coinNames) {
        this.coinNames = coinNames;
    }

    public String getAboveElements() {
        return aboveElements;
    }

    public void setAboveElements(String aboveElements) {
        this.aboveElements = aboveElements;
    }

    public String getBelowElements() {
        return belowElements;
    }

    public void setBelowElements(String belowElements) {
        this.belowElements = belowElements;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

