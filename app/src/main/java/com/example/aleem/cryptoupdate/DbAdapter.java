package com.example.aleem.cryptoupdate;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
    Context c;
    SQLiteDatabase db;
    DbHelper helper;

    public DbAdapter(Context ctx){
        this.c = ctx;
        helper = new DbHelper(c);
    }

    //Open Db
    public DbAdapter openDb(){
        try {
            db = helper.getWritableDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return this;
    }

    //Close Db
    public void closeDb(){
        try {
            helper.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Insert data to Db
    public long add(String coinName, String coinPrice, String above, String below){
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.COINNAME, coinName);
            cv.put(Constants.COINPRICE, coinPrice);
            cv.put(Constants.ABOVE, above);
            cv.put(Constants.BELOW, below);

            return db.insert(Constants.TB_NAME, Constants.ROW_ID, cv);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    //Retrieve
    public Cursor getAllCoins(){
        String[] columns = {Constants.ROW_ID, Constants.COINNAME, Constants.COINPRICE, Constants.ABOVE, Constants.BELOW};

        return db.query(Constants.TB_NAME, columns, null, null, null, null, null);
    }

    //Update
    /*public long update(int id, String coinName, String coinPrice, String above, String below){
        try {
            ContentValues cv = new ContentValues();
            cv.put(Constants.COINNAME, coinName);
            cv.put(Constants.COINPRICE, coinPrice);
            cv.put(Constants.ABOVE, above);
            cv.put(Constants.BELOW, below);

            return db.update(Constants.TB_NAME, cv, Constants.ROW_ID+" =?", new String[]{String.valueOf(id)});

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }*/

    //Delete
    public long delete(int id){
        try {
            return db.delete(Constants.TB_NAME, Constants.ROW_ID+" =?", new String[]{String.valueOf(id)});

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
