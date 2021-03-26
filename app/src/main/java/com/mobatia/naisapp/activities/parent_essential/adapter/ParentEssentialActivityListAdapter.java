package com.mobatia.naisapp.activities.parent_essential.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.parent_essentials.model.ParentEssentialsModel;

import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class ParentEssentialActivityListAdapter extends RecyclerView.Adapter<ParentEssentialActivityListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ParentEssentialsModel> mnNewsLetterModelArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView submenu;


        public MyViewHolder(View view) {
            super(view);

            submenu = (TextView) view.findViewById(R.id.listTxtTitle);


        }
    }


    public ParentEssentialActivityListAdapter(Context mContext, ArrayList<ParentEssentialsModel> mnNewsLetterModelArrayList) {
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
        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());

    }


    @Override
    public int getItemCount() {
        return mnNewsLetterModelArrayList.size();
    }

}
