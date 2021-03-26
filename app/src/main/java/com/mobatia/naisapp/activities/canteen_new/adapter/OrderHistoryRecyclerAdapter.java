package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrderHistoryDateModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

public class OrderHistoryRecyclerAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<OrderHistoryDateModel> mOrderHistoryArrayList;
    OrderHistoryDetailRecyclerAdapter mAdapter;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView itemDateTxt;

        RecyclerView cartItemRecycler;
        public MyViewHolder(View view) {
            super(view);


            cartItemRecycler=view.findViewById(R.id.cartItemRecycler);
            itemDateTxt=view.findViewById(R.id.itemDateTxt);

        }
    }


    public OrderHistoryRecyclerAdapter(Context mContext, ArrayList<OrderHistoryDateModel> mOrderHistoryArrayList) {
        this.mContext = mContext;
        this.mOrderHistoryArrayList = mOrderHistoryArrayList;


    }

    @Override
    public OrderHistoryRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cart_date_recycler, parent, false);

        return new OrderHistoryRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderHistoryRecyclerAdapter.MyViewHolder holder, int position) {
        holder.cartItemRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        holder.cartItemRecycler.addItemDecoration(itemDecoration);
        holder.cartItemRecycler.setLayoutManager(llm);
        holder.itemDateTxt.setText(AppUtils.dateConversionY(mOrderHistoryArrayList.get(position).getDelivery_date()));
        mAdapter=new OrderHistoryDetailRecyclerAdapter(mContext,mOrderHistoryArrayList.get(position).getOrderHistoryDetailArrayList(),mOrderHistoryArrayList.get(position).getStatus());
        holder.cartItemRecycler.setAdapter(mAdapter);
    }


    @Override
    public int getItemCount() {
        return mOrderHistoryArrayList.size();
    }

}
