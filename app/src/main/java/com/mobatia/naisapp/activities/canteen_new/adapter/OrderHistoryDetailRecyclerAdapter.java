package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.CartActivity;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrderHistoryDetailModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.home.adapter.ImagePagerDrawableAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OrderHistoryDetailRecyclerAdapter extends RecyclerView.Adapter<OrderHistoryDetailRecyclerAdapter.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<OrderHistoryDetailModel> mOrderHistoryDetailArrayList;
    String overallStatus="";
    ArrayList<String> homeBannerUrlImageArray;
    int currentPage = 0;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ViewPager bannerImagePager;
        TextView itemNameTxt,itemDescription,amountTxt,itemsCount,status;
        public MyViewHolder(View view) {
            super(view);
            itemNameTxt = view.findViewById(R.id.itemNameTxt);
            itemDescription = view.findViewById(R.id.itemDescription);
            amountTxt = view.findViewById(R.id.amountTxt);
            itemsCount = view.findViewById(R.id.itemsCount);
            status = view.findViewById(R.id.status);
            bannerImagePager = (ViewPager) view.findViewById(R.id.bannerImagePager);
        }
    }


    public OrderHistoryDetailRecyclerAdapter(Context mContext, ArrayList<OrderHistoryDetailModel> mOrderHistoryDetailArrayList,String overallStatus) {
        this.mContext = mContext;
        this.mOrderHistoryDetailArrayList = mOrderHistoryDetailArrayList;
        this.overallStatus=overallStatus;


    }

    @Override
    public OrderHistoryDetailRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_history_detail, parent, false);

        return new OrderHistoryDetailRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final OrderHistoryDetailRecyclerAdapter.MyViewHolder holder, int position) {
        holder.itemNameTxt.setText(mOrderHistoryDetailArrayList.get(position).getItem_name());
        holder.amountTxt.setText(mOrderHistoryDetailArrayList.get(position).getItem_total()+" AED");
        holder.itemDescription.setText(mOrderHistoryDetailArrayList.get(position).getItem_description());
        if (mOrderHistoryDetailArrayList.get(position).getQuantity().equalsIgnoreCase("1"))
        {
            holder.itemsCount.setText(mOrderHistoryDetailArrayList.get(position).getQuantity()+" item");
        }
        else
        {
            holder.itemsCount.setText(mOrderHistoryDetailArrayList.get(position).getQuantity()+" items");
        }

//
//        if (!mOrderHistoryDetailArrayList.get(position).getItem_image().equals("")) {
//
//            Picasso.with(mContext).load(AppUtils.replace(mOrderHistoryDetailArrayList.get(position).getItem_image().toString())).placeholder(R.drawable.canteendefaultitem).fit().into(holder.itemImage);
//        }
//        else
//        {
//
//            holder.itemImage.setImageResource(R.drawable.canteendefaultitem);
//        }
        if (overallStatus.equalsIgnoreCase("2"))
        {
            holder.status.setText("Delivered");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.canteen_red));

        }
        else
        {
            holder.status.setText("Cancelled");
            holder.status.setTextColor(mContext.getResources().getColor(R.color.red));
        }


        homeBannerUrlImageArray=mOrderHistoryDetailArrayList.get(position).getImageBanner();
        if (homeBannerUrlImageArray != null)
        {

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == homeBannerUrlImageArray.size()) {
                        currentPage = 0;
                        holder.bannerImagePager.setCurrentItem(currentPage,
                                false);
                    } else {

                        holder.bannerImagePager
                                .setCurrentItem(currentPage++, true);
                    }

                }
            };
            final Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 100, 3000);
            holder.bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray, mContext));
        }
        else
        {
            holder.bannerImagePager.setBackgroundResource(R.drawable.noitem);
        }

    }


    @Override
    public int getItemCount() {
        return mOrderHistoryDetailArrayList.size();
    }


}
