package com.marmu.handprint.sales_man.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marmu.handprint.R;
import com.marmu.handprint.sales_man.landing.activity.order.OrderBilling;
import com.marmu.handprint.sales_man.model.OrderList;

import java.io.Serializable;
import java.util.List;

/**
 * Created by azharuddin on 16/6/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<OrderList> mOrderLists;
    private Context mContext;

    public OrderAdapter(List<OrderList> orderLists, Context context) {
        this.mOrderLists = orderLists;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_sales_man_order_list,
                        parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OrderList orderList = mOrderLists.get(position);
        holder.partyName.setText(orderList.getPartyName());

        holder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderBilling = new Intent(mContext, OrderBilling.class);
                orderBilling.putExtra("orderBill", (Serializable) orderList);
                mContext.startActivity(orderBilling);
            }
        });

        holder.orderClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView partyName, orderView, orderClose;

        private MyViewHolder(View view) {
            super(view);
            partyName = (TextView) view.findViewById(R.id.party_name);
            orderView = (TextView) view.findViewById(R.id.order_view);
            orderClose = (TextView) view.findViewById(R.id.order_close);
        }
    }
}
