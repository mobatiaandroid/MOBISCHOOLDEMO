package com.mobatia.naisapp.fragments.social_media.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.social_media.model.SocialMediaModel;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class SocialMediaAdapter extends  RecyclerView.Adapter<SocialMediaAdapter.MyViewHolder> {
    ArrayList<SocialMediaModel> mSocialMediaModels;
    Context mContext;
    Drawable iconImage;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView listTxtView;

        public MyViewHolder(View view) {
            super(view);

          imgIcon= (ImageView) view.findViewById(R.id.imagicon);
            listTxtView= (TextView) view.findViewById(R.id.listTxtTitle);


        }
    }
    public SocialMediaAdapter(Context mContext,ArrayList<SocialMediaModel> mSocialMediaModels) {
        this.mContext = mContext;
      this.mSocialMediaModels=mSocialMediaModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_social_media_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

if(mSocialMediaModels.get(position).getTab_type().startsWith("Facebook")) {
    holder.imgIcon.setImageResource(R.drawable.facebookiconmedia);
    final int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        holder.imgIcon.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.roundfb) );
    } else {
        holder.imgIcon.setBackground( mContext.getResources().getDrawable(R.drawable.roundfb));
    }
    holder.listTxtView.setText(mSocialMediaModels.get(position).getTab_type().replace("Facebook:"," "));

    //holder.imgIcon.setBackgroundDrawable(R.drawable.roundfb);
}else if(mSocialMediaModels.get(position).getTab_type().startsWith("Twitter")){
    holder.imgIcon.setImageResource(R.drawable.twittericon);
    final int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        holder.imgIcon.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.roundtw) );
    } else {
        holder.imgIcon.setBackground( mContext.getResources().getDrawable(R.drawable.roundtw));
    }
    //holder.imgIcon.setBackground(mContext.getDrawable(R.drawable.roundtw));
    holder.listTxtView.setText(mSocialMediaModels.get(position).getTab_type().replace("Twitter:"," "));

}
else if(mSocialMediaModels.get(position).getTab_type().startsWith("Instagram")){
    holder.imgIcon.setImageResource(R.drawable.instagramicon);
    final int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        holder.imgIcon.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.roundins) );
    } else {
        holder.imgIcon.setBackground( mContext.getResources().getDrawable(R.drawable.roundins));
    }
    holder.listTxtView.setText(mSocialMediaModels.get(position).getTab_type().replace("Instagram:"," "));

}

    }


    @Override
    public int getItemCount() {
       System.out.println("Adapter---size" + mSocialMediaModels.size());

        return mSocialMediaModels.size();
    }
}
