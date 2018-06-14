package com.example.aleem.cryptoupdate;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<ListItem> listItems;
    private Context context;

    public MyAdapter(ArrayList<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.txtViewName.setText(listItem.getNames());
        holder.symbolElement.setText(listItem.getSymbol());
        holder.txtViewpice.setText(listItem.getPrice());
        holder.txtViewchange.setText(listItem.getChanges());

        if(holder.txtViewchange.getText().toString().contains("-")){
            holder.txtViewchange.setBackgroundColor(Color.parseColor("#ef9a9a"));
        }else {
            holder.txtViewchange.setBackgroundColor(Color.parseColor("#C5E1A5"));
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtViewName;
        public TextView symbolElement;
        public TextView txtViewpice;
        public TextView txtViewchange;


        public ViewHolder(View itemView) {
            super(itemView);

            txtViewName = itemView.findViewById(R.id.fstElement);
            symbolElement = itemView.findViewById(R.id.symbolElement);
            txtViewpice = itemView.findViewById(R.id.scdElement);
            txtViewchange = itemView.findViewById(R.id.thirdElement);
        }

    }

    public void setFilter(ArrayList<ListItem> newList){
        listItems = new ArrayList<>();
        listItems.addAll(newList);
        notifyDataSetChanged();
    }

}
