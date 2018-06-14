package com.example.aleem.cryptoupdate;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView coinName;
    TextView aboveElement;
    TextView belowElement;
    ItemClickListener itemClickListener;

    public MyHolder(View itemView) {
        super(itemView);

        coinName = itemView.findViewById(R.id.coinName);
        aboveElement = itemView.findViewById(R.id.aboveElement);
        belowElement = itemView.findViewById(R.id.belowElement);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v, getLayoutPosition());
    }


    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;
    }
}
