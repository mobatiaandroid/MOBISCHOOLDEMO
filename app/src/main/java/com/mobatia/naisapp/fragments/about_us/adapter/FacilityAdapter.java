package com.mobatia.naisapp.fragments.about_us.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.about_us.model.AboutusModel;

import java.util.ArrayList;

/**
 * Created by gayatri on 10/5/17.
 */
public class FacilityAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<AboutusModel> aboutusModelArrayList;

    public FacilityAdapter(Context context,ArrayList<AboutusModel> aboutusModelArrayList) {
        mContext = context;
        this.aboutusModelArrayList=aboutusModelArrayList;
    }

    @Override
    public int getCount() {
        return aboutusModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Convert DP to PX
    // Source: http://stackoverflow.com/a/8490361
    /*public int dpToPx(int dps) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);

        return pixels;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        int imageID = 0;

        // Want the width/height of the items
        // to be 120dp
       /* int wPixel = dpToPx(120);
        int hPixel = dpToPx(120);*/

        // Move cursor to current position
        // Get the current value for the requested column

        if (convertView == null) {
            // If convertView is null then inflate the appropriate layout file
            convertView = LayoutInflater.from(mContext).inflate(R.layout.facility_grid_item, null);
        }
        else {

        }

        imageView = (ImageView) convertView.findViewById(R.id.imageView);

        if(aboutusModelArrayList.get(position).getItemPdfUrl().endsWith(".pdf")){
            imageView.setImageResource(R.drawable.pdfdownloadbutton);
        }else{
            imageView.setImageResource(R.drawable.webcontentviewbutton);

        }

        // Set height and width constraints for the image view
        /*imageView.setLayoutParams(new LinearLayout.LayoutParams(wPixel, hPixel));

        // Set the content of the image based on the provided URI


        // Image should be cropped towards the center
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Set Padding for images
        imageView.setPadding(8, 8, 8, 8);

        // Crop the image to fit within its padding
        imageView.setCropToPadding(true);*/

        return convertView;
    }
}