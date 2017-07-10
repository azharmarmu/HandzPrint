package com.marmu.handprint.admin.landing.activity.taken;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.marmu.handprint.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by azharuddin on 30/6/17.
 */

class TakenAdapter extends RecyclerView.Adapter<TakenAdapter.MyViewHolder> {

    private List<TakenModel> mTakenList;
    private Context mContext;

    TakenAdapter(Context mContext, List<TakenModel> mTakenList) {
        this.mTakenList = mTakenList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sales_man,
                        parent, false);
        return new TakenAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TakenModel taken = mTakenList.get(position);
        final HashMap<String, Object> takenMap = taken.getTakenMap();
        holder.salesMan.setText(takenMap.get("sales_man_name").toString() + " / " + takenMap.get("sales_route").toString());
        if (takenMap.get("process").toString().equalsIgnoreCase("closed")) {
            holder.editSales.setText("Closed");
        }
        holder.editSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!takenMap.get("process").toString().equalsIgnoreCase("closed")) {
                    Intent editIntent = new Intent(mContext, SetTakenActivity.class);
                    editIntent.putExtra("key", taken.getKey());
                    editIntent.putExtra("takenMap", takenMap);
                    editIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(editIntent);
                } else {
                    Toast.makeText(mContext, "Taken is already closed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTakenList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView salesMan, editSales;

        MyViewHolder(View itemView) {
            super(itemView);
            salesMan = itemView.findViewById(R.id.sales_man_details);
            editSales = itemView.findViewById(R.id.edit_sales_man);
        }
    }
}
