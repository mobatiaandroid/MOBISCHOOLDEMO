package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.model.CategoryModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRecyclerAdpter extends RecyclerView.Adapter<CategoryRecyclerAdpter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CategoryModel> mCategoryModelArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView categoryTitle;
        ImageView categoryImg,selectImg;
        LinearLayout bgLinear,mainLinear;

        public MyViewHolder(View view) {
            super(view);

            categoryImg = (ImageView) view.findViewById(R.id.categoryImg);
            selectImg = (ImageView) view.findViewById(R.id.selectImg);
            categoryTitle = (TextView) view.findViewById(R.id.categoryTitle);
            bgLinear = (LinearLayout) view.findViewById(R.id.bgLinear);
            mainLinear = (LinearLayout) view.findViewById(R.id.mainLinear);

        }
    }


    public CategoryRecyclerAdpter(Context mContext, ArrayList<CategoryModel> mCategoryModelArrayList) {
        this.mContext = mContext;
        this.mCategoryModelArrayList = mCategoryModelArrayList;

    }

    @Override
    public CategoryRecyclerAdpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_category_recycler, parent, false);

        return new CategoryRecyclerAdpter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryRecyclerAdpter.MyViewHolder holder, int position) {
        holder.categoryTitle.setText(mCategoryModelArrayList.get(position).getCategoryName());
        if (!mCategoryModelArrayList.get(position).getCategoryImage().equalsIgnoreCase(""))
        {
            Picasso.with(mContext).load(AppUtils.replace(mCategoryModelArrayList.get(position).getCategoryImage())).placeholder(R.drawable.default_cat).fit().into(holder.categoryImg);
        }
        else
        {
            holder.categoryImg.setBackgroundResource(R.drawable.default_cat);
        }
        if (mCategoryModelArrayList.get(position).isCategorySelected())
        {
            holder.selectImg.setVisibility(View.GONE);
     //       holder.mainLinear.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_date_orange));
            holder.bgLinear.setBackgroundResource(R.drawable.date_selected);
            holder.mainLinear.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        else
        {
            holder.selectImg.setVisibility(View.GONE);
            holder.mainLinear.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.bgLinear.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_item_bg));
        }


    }


    @Override
    public int getItemCount() {
        return mCategoryModelArrayList.size();
    }

}
