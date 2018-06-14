package com.example.aleem.cryptoupdate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyAdapterTwo extends RecyclerView.Adapter<MyHolder>{

    Context c;
    ArrayList<Coin> coins;

    public MyAdapterTwo(Context ctx, ArrayList<Coin> coins){
        this.c = ctx;
        this.coins = coins;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model, null);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.coinName.setText(coins.get(position).getCoinNames());
        holder.aboveElement.setText(coins.get(position).getAboveElements());
        holder.belowElement.setText(coins.get(position).getBelowElements());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, final int pos) {
                AlertDialog.Builder alert = new AlertDialog.Builder(c);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure to Delete you will not get Alerts for this coin.");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(coins.get(pos).getId());
                        coins.remove(pos);
                        notifyItemRemoved(pos);
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });
    }

    private void delete(int id) {
        DbAdapter db = new DbAdapter(c);
        db.openDb();

        //delete
        long result = db.delete(id);
        if (result > 0) {
            Log.d(TAG, "Msg: " +result);
        } else {
            Toast.makeText(c, "Unable to Delete", Toast.LENGTH_SHORT).show();
        }
        db.closeDb();
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }
}
