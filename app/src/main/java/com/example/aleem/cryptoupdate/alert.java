package com.example.aleem.cryptoupdate;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class alert extends AppCompatActivity {

    private static final String Job_Tag = "my-unique-tag";
    private static  String Url ="";
    public String price;
    EditText searchTxt, aboveTxt, currentTxt, belowTxt;
    Button saveBtn, cancelBtn, searchBtn;
    RecyclerView recyclerView;
    MyAdapterTwo adapter;
    ArrayList<Coin> coins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerViewForAlerts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyAdapterTwo(this,coins);

        retrieve();

    }

    private void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_layout);

        searchTxt = dialog.findViewById(R.id.searchTxt);
        aboveTxt = dialog.findViewById(R.id.aboveTxt);
        currentTxt = dialog.findViewById(R.id.currentTxt);
        belowTxt = dialog.findViewById(R.id.belowTxt);

        searchBtn = dialog.findViewById(R.id.searchBtn);
        saveBtn = dialog.findViewById(R.id.saveBtn);
        cancelBtn = dialog.findViewById(R.id.cancelBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchTxt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }else if(aboveTxt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }else if(currentTxt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }else if(belowTxt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }else {
                    //save
                    save(searchTxt.getText().toString(), aboveTxt.getText().toString(),
                            belowTxt.getText().toString(), currentTxt.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Save
    private void save(String coinName, String coinPrice, String above, String below){
        DbAdapter db = new DbAdapter(this);
        db.openDb();
        long result = db.add(coinName, coinPrice, above,  below);
        if(result>0){
            searchTxt.setText("");
            aboveTxt.setText("");
            currentTxt.setText("");
            belowTxt.setText("");
        }else {
            Toast.makeText(getApplicationContext(),"Unable To Add", Toast.LENGTH_SHORT).show();
        }
        db.closeDb();

        retrieve();
    }

    //Retrieve
    public void retrieve(){
        DbAdapter db = new DbAdapter(this);
        db.openDb();
        coins.clear();

        int id;
        String coinName, above, below;
        Cursor c = db.getAllCoins();
        while (c.moveToNext()){
            id = c.getInt(0);
            coinName = c.getString(1);
            above = c.getString(2);
            below = c.getString(3);

            Coin coin = new Coin(coinName, above, below, id);
            coins.add(coin);
        }

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        if(!(coins.size()<1)){
            recyclerView.setAdapter(adapter);
            Job myJob = dispatcher.newJobBuilder()
                    .setService(MyJobService.class)
                    .setTag(Job_Tag)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setReplaceCurrent(false)
                    .setTrigger(Trigger.executionWindow(0, 0))
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();

            dispatcher.mustSchedule(myJob);

        }else{
            dispatcher.cancel(Job_Tag);
            Log.i("BackJob", "Msg From Bg Task Job cancel");
        }
    }

    public void Data(){
        if(searchTxt.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Coin Name", Toast.LENGTH_SHORT).show();
        }else {
            Url = "https://api.coinmarketcap.com/v1/ticker/" + searchTxt.getText().toString() + "/";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            price = "$" + object.getString("price_usd");
                            aboveTxt.setText(price);
                            currentTxt.setText(price);
                            belowTxt.setText(price);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Could Not Load Data! Make Sure Coin name is Correct. \nOr if coin name is more than one word put - between them.", Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
    }

    public void coinClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void alertListClicked(View view){
        Intent intent = new Intent(this, alert.class);
        startActivity(intent);
    }
}