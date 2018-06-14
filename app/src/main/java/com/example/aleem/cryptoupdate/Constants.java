package com.example.aleem.cryptoupdate;


public class Constants {
    static final String ROW_ID = "id";
    static final String COINNAME = "coinName";
    static final String COINPRICE = "coinPrice";
    static final String ABOVE = "above";
    static final String BELOW = "below";

    static final String DB_NAME = "b_DB";
    static final String TB_NAME = "b_TB";
    static final int DB_VERSION = '1';

    static final String CREATE_TB = "CREATE TABLE b_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "coinName TEXT NOT NULL, coinPrice TEXT NOT NULL, above TEXT NOT NULL, below TEXT NOT NULL);";
}
