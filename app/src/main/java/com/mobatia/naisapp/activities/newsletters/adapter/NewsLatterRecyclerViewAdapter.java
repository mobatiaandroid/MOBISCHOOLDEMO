package com.mobatia.naisapp.activities.newsletters.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.newsletters.model.NewsLetterModel;

import java.util.ArrayList;

/**
 * Created by gayathri NR on 20/1/17.
 */
public class NewsLatterRecyclerViewAdapter extends RecyclerView.Adapter<NewsLatterRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NewsLetterModel> mnNewsLetterModelArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;


        public MyViewHolder(View view) {
            super(view);

            mTitleTxt = (TextView) view.findViewById(R.id.listTxtTitle);


        }
    }


    public NewsLatterRecyclerViewAdapter(Context mContext,ArrayList<NewsLetterModel> mnNewsLetterModelArrayList) {
        this.mContext = mContext;
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_aboutus_list_adapter_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mTitleTxt.setText(mnNewsLetterModelArrayList.get(position).getNewsLetterCatName());

    }


    @Override
    public int getItemCount() {
        return mnNewsLetterModelArrayList.size();
    }
}
