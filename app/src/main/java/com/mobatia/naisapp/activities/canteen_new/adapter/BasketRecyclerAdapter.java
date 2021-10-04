package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.model.BasketModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<BasketModel> mMyOrderArrayList;
    BasketDetailRecyclerAdapter mAdapter;
     String ordered_user_type="";
     String student_id="";
     String parent_id="";
     String staff_id="";
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView itemDateTxt,totalAmountTxt;
        ImageView closeImg;
        RecyclerView cartItemRecycler;
        public MyViewHolder(View view) {
            super(view);


            cartItemRecycler=view.findViewById(R.id.cartItemRecycler);
            itemDateTxt=view.findViewById(R.id.itemDateTxt);
            totalAmountTxt=view.findViewById(R.id.totalAmountTxt);
            closeImg=view.findViewById(R.id.imgClose);

        }
    }


    public BasketRecyclerAdapter(Context mContext, ArrayList<BasketModel> mMyOrderArrayList, String ordered_user_type, String student_id, String parent_id, String staff_id) {
        this.mContext = mContext;
        this.mMyOrderArrayList = mMyOrderArrayList;
        this.ordered_user_type=ordered_user_type;
        this.student_id=student_id;
        this.parent_id=parent_id;
        this.staff_id=staff_id;

    }

    @Override
    public BasketRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_basket_recycler, parent, false);

        return new BasketRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BasketRecyclerAdapter.MyViewHolder holder, int position) {
        holder.closeImg.setVisibility(View.GONE);
        holder.cartItemRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        holder.cartItemRecycler.addItemDecoration(itemDecoration);
        holder.cartItemRecycler.setLayoutManager(llm);
        holder.totalAmountTxt.setText("Total      "+mMyOrderArrayList.get(position).getTotal_amount()+"AED");
        holder.itemDateTxt.setText(AppUtils.dateConversionY(mMyOrderArrayList.get(position).getDelivery_date()));
        holder.closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAdapter=new BasketDetailRecyclerAdapter(mContext,mMyOrderArrayList.get(position).getmBasketDetailArrayList(),ordered_user_type,student_id,parent_id,staff_id,mMyOrderArrayList.get(position).getDelivery_date());
        holder.cartItemRecycler.setAdapter(mAdapter);

    }


    @Override
    public int getItemCount() {
        return mMyOrderArrayList.size();
    }

}
