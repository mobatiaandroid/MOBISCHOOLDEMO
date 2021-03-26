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
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

public class ConfirmedOrderDateRecyclerAdapter extends RecyclerView.Adapter<ConfirmedOrderDateRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CartItemDateModel> mCartDateArrayList;
    String dept;
    CartDetailRecyclerAdapter mAdapter;
    String ordered_user_type;
    String student_id;
    String parent_id;
    String staff_id;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView itemDateTxt;

        RecyclerView cartItemRecycler;
        public MyViewHolder(View view) {
            super(view);


            cartItemRecycler=view.findViewById(R.id.cartItemRecycler);
            itemDateTxt=view.findViewById(R.id.itemDateTxt);

        }
    }


    public ConfirmedOrderDateRecyclerAdapter(Context mContext, ArrayList<CartItemDateModel> mCartDateArrayList,String ordered_user_type,String student_id,String parent_id,String staff_id) {
        this.mContext = mContext;
        this.mCartDateArrayList = mCartDateArrayList;
        this.ordered_user_type = ordered_user_type;
        this.student_id = student_id;
        this.parent_id = parent_id;
        this.staff_id = staff_id;

    }

    @Override
    public ConfirmedOrderDateRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cart_date_recycler, parent, false);

        return new ConfirmedOrderDateRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ConfirmedOrderDateRecyclerAdapter.MyViewHolder holder, int position) {
        holder.cartItemRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        holder.cartItemRecycler.addItemDecoration(itemDecoration);
        holder.cartItemRecycler.setLayoutManager(llm);
        holder.itemDateTxt.setText(mCartDateArrayList.get(position).getDelivery_date());
        mAdapter=new CartDetailRecyclerAdapter(mContext,mCartDateArrayList.get(position).getmCartItemDetailArrayList(),mCartDateArrayList.get(position).getDelivery_date(),ordered_user_type,student_id,parent_id,staff_id);
        holder.cartItemRecycler.setAdapter(mAdapter);
    }


    @Override
    public int getItemCount() {
        return mCartDateArrayList.size();
    }

}
