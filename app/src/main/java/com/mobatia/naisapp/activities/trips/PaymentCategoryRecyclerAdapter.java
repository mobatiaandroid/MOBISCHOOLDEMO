package com.mobatia.naisapp.activities.trips;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;

import java.util.ArrayList;

public class PaymentCategoryRecyclerAdapter extends RecyclerView.Adapter<PaymentCategoryRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CategoryModel> mStaffList;
    LayoutInflater inflater;





    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtTitle;
        TextView status;
        RelativeLayout statusLayout;

        public MyViewHolder(View view) {
            super(view);
            listTxtTitle = view.findViewById(R.id.listTxtTitle);
            status = view.findViewById(R.id.status);
            statusLayout = view.findViewById(R.id.statusLayout);




        }
    }


    public PaymentCategoryRecyclerAdapter(Context context,
                                          ArrayList<CategoryModel> arrList) {
        this.mContext = context;
        this.mStaffList = arrList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_recycler_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.listTxtTitle.setText(mStaffList.get(position).getCategory_name().trim());
        if(mStaffList.get(position).getStatus().equalsIgnoreCase("0"))
        {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_red);
            holder.status.setText("New");
        }
        else if(mStaffList.get(position).getStatus().equalsIgnoreCase("1")||mStaffList.get(position).getStatus().equalsIgnoreCase(""))
        {
            holder.statusLayout.setVisibility(View.GONE);

        }
        else if(mStaffList.get(position).getStatus().equalsIgnoreCase("2"))
        {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
            holder.status.setText("Updated");
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mStaffList.size();
    }

}
