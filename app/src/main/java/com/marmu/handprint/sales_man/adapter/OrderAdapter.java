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

import java.util.List;

/**
 * Created by azharuddin on 16/6/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<OrderList> mOrderLists;
    private Context mContext;
    private String salesKey;
    private String salesMan;
    private String salesRoute;

    public OrderAdapter(String salesKey, String salesMan, String salesRoute, List<OrderList> orderLists, Context context) {
        this.mOrderLists = orderLists;
        this.mContext = context;
        this.salesKey = salesKey;
        this.salesMan = salesMan;
        this.salesRoute = salesRoute;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_order,
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
                orderBilling.putExtra("orderBill", orderList);
                orderBilling.putExtra("sales_key", salesKey);
                orderBilling.putExtra("sales_man_name", salesMan);
                orderBilling.putExtra("sales_route", salesRoute);
                orderBilling.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(orderBilling);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderLists.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView partyName, orderView;

        private MyViewHolder(View view) {
            super(view);
            partyName = view.findViewById(R.id.party_name);
            orderView = view.findViewById(R.id.order_view);
        }
    }
}
