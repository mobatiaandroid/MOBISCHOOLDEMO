package com.mobatia.naisapp.activities.sports.filter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilterStudentListRecyclerAdapter extends RecyclerView.Adapter<FilterStudentListRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    public ArrayList<FilterModel> mFilterModelList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView typeTitle;
        CheckBox checkBoxFilter;
        //        AvatarView avatarImageView;
        CircularImageView avatarImageView;


        // AvatarView mAvatarImageView;
        public MyViewHolder(View view) {
            super(view);

            typeTitle = view.findViewById(R.id.typeTitle);
            checkBoxFilter = view.findViewById(R.id.checkBoxFilter);
            avatarImageView = view.findViewById(R.id.avatarImageView);

        }
    }


    public FilterStudentListRecyclerAdapter(Context mContext, ArrayList<FilterModel> mObcMainModelArrayList) {
        this.mContext = mContext;
        this.mFilterModelList = mObcMainModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_filter_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        if (position==0) {
//            holder.avatarImageView.setFillColor(mContext.getResources().getColor(R.color.rel_one));
//        } else if (position==1) {
//            holder.avatarImageView.setFillColor(mContext.getResources().getColor(R.color.rel_two));
//        } else if (position==2) {
//            holder.avatarImageView.setFillColor(mContext.getResources().getColor(R.color.rel_five));
//        } else if (position==3) {
//            holder.avatarImageView.setFillColor(mContext.getResources().getColor(R.color.rel_six));
//        } else{
//            holder.avatarImageView.setFillColor(mContext.getResources().getColor(R.color.grey));
//        }
//        Glide.with(mContext).load(AppUtils.replace( mFilterModelList.get(position).getmPhoto())).placeholder(R.drawable.student).error(R.drawable.student).centerCrop().into(holder.avatarImageView);
        if (!mFilterModelList.get(position).getmPhoto().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mFilterModelList.get(position).getmPhoto().toString())).placeholder(R.drawable.student).fit().into(holder.avatarImageView);
        }
        else

        {

            holder.avatarImageView.setImageResource(R.drawable.student);
        }
        holder.checkBoxFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilterModelList.get(position).setCheckedStudent(isChecked);

            }
        });

        if (mFilterModelList.get(position).getCheckedStudent()) {
            holder.checkBoxFilter.setChecked(true);

        }
        else
        {
            holder.checkBoxFilter.setChecked(false);

        }
        holder.typeTitle.setText(mFilterModelList.get(position).getmName());

    }


    @Override
    public int getItemCount() {
        return mFilterModelList.size();
    }
}

