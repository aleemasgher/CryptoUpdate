package com.example.aleem.cryptoupdate;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJobService extends JobService{

    private static  String p = "";
    int id;
    String coinName = "", above= "", below= "";
    Thread t;
    Cursor c;
    BackgroundTask backgroundTask;

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onStartJob(final JobParameters job) {
        backgroundTask = new BackgroundTask(){

            @Override
            protected void onPostExecute(String s) {
                Log.i("BackJob","Msg From Bg Task");
                mainJob();
                jobFinished(job, false);
            }
        };

        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    public static class BackgroundTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return "Hello From Bg Job";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void mainJob() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    DbAdapter db = new DbAdapter(getApplicationContext());
                    db.openDb();
                    c = db.getAllCoins();
                    if(c != null && !c.isClosed()) {
                        if (c.getCount() >= 0) {
                            if (c.moveToFirst()) {
                                do {
                                    for(int i=0; i<c.getCount(); i++) {
                                        id = c.getInt(0);
                                        coinName = c.getString(1);
                                        above = c.getString(2);
                                        below = c.getString(3);
                                        String Url = "https://api.coinmarketcap.com/v1/ticker/" + coinName + "/";
                                        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject object = jsonArray.getJSONObject(i);
                                                        p = "$" + object.getString("price_usd");
                                                        Toast.makeText(getApplicationContext(), "P is " + p, Toast.LENGTH_SHORT).show();
                                                        checkConditions();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.i("Internet", "Could not load data internet is off");
                                            }
                                        });
                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest);
                                        try {
                                            Thread.sleep(7000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } while (c.moveToNext());
                            }else {
                                return;
                            }
                        }
                        c.getCount();
                        db.closeDb();
                    }
                }
            }
        });
        t.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void checkConditions() {
        int r1, r2;
        r1 = p.compareTo(above);
        r2 = p.compareTo(below);
        if (r1 >= 0) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), i, 0);
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Crypto Updates")
                    .setContentText(coinName + " is Above " + above)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            if (notificationManager != null) {
                notificationManager.notify(id, notification);
            }
        } else if (r2 <= 0) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), i, 0);
            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Crypto Updates")
                    .setContentText(coinName + " is Below " + below)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            if (notificationManager != null) {
                notificationManager.notify(id, notification);
            }
        }
    }
}
