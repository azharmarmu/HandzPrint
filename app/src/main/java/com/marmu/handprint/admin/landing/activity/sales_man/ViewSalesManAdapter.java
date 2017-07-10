package com.marmu.handprint.admin.landing.activity.sales_man;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marmu.handprint.R;

import java.util.List;

/**
 * Created by azharuddin on 30/6/17.
 */

class ViewSalesManAdapter extends RecyclerView.Adapter<ViewSalesManAdapter.MyViewHolder> {

    private List<SalesManModel> mSalesManList;
    private Context mContext;

    ViewSalesManAdapter(Context context, List<SalesManModel> mSalesManList) {
        this.mContext = context;
        this.mSalesManList = mSalesManList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sales_man,
                        parent, false);
        return new ViewSalesManAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SalesManModel salesManModel = mSalesManList.get(position);
        String details = salesManModel.getName() + " / " + salesManModel.getPhone();
        holder.salesManDetails.setText(details);

        holder.editSalesManDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editSalesMan = new Intent(mContext, AddSalesManActivity.class);
                editSalesMan.putExtra("key", salesManModel.getKey());
                editSalesMan.putExtra("name", salesManModel.getName());
                editSalesMan.putExtra("phone", salesManModel.getPhone());
                editSalesMan.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(editSalesMan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSalesManList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView salesManDetails, editSalesManDetails;

        MyViewHolder(View itemView) {
            super(itemView);
            salesManDetails = itemView.findViewById(R.id.sales_man_details);
            editSalesManDetails = itemView.findViewById(R.id.edit_sales_man);
        }
    }
}
