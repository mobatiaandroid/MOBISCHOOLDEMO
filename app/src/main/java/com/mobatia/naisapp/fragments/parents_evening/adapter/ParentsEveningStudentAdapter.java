package com.mobatia.naisapp.fragments.parents_evening.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class ParentsEveningStudentAdapter extends  RecyclerView.Adapter<ParentsEveningStudentAdapter.MyViewHolder> {
    ArrayList<StudentModel> mSocialMediaModels;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView listTxtView,listTxtClass;

        public MyViewHolder(View view) {
            super(view);

          imgIcon= (ImageView) view.findViewById(R.id.imagicon);
            listTxtView= (TextView) view.findViewById(R.id.listTxtTitle);
            listTxtClass= (TextView) view.findViewById(R.id.listTxtClass);


        }
    }
    public ParentsEveningStudentAdapter(Context mContext, ArrayList<StudentModel> mSocialMediaModels) {
        this.mContext = mContext;
      this.mSocialMediaModels=mSocialMediaModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_student_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {



//   holder.imgIcon.setVisibility(View.GONE);
//        holder.imgIcon.setBackgroundResource(R.drawable.roundfb);
        if (!mSocialMediaModels.get(position).getmPhoto().equals("")) {

            Glide.with(mContext).load(AppUtils.replace(mSocialMediaModels.get(position).getmPhoto().toString())).asBitmap().placeholder(R.drawable.student).error(R.drawable.student).fitCenter().into(holder.imgIcon);
        }
        else

        {

            holder.imgIcon.setImageResource(R.drawable.student);
        }
    holder.listTxtView.setText(mSocialMediaModels.get(position).getmName());
    holder.listTxtClass.setText(mSocialMediaModels.get(position).getmClass());


    }


    @Override
    public int getItemCount() {

        return mSocialMediaModels.size();
    }
}
