package com.mobatia.naisapp.activities.bus_routes.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.sports.model.BusModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/** BusRouteSubListRecyclerViewAdapter
 * Created by gayatri on 5/4/17.
 */
public class BusRouteSubListRecyclerViewAdapter extends RecyclerView.Adapter<BusRouteSubListRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<BusModel> mPhotosModelArrayList;
    String photo_id="-1";
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView time,timeamorpm,routename,destination;
        public MyViewHolder(View view) {
            super(view);

            time= (TextView) view.findViewById(R.id.time);
            timeamorpm= (TextView) view.findViewById(R.id.timeamorpm);
            routename= (TextView) view.findViewById(R.id.routename);
            destination= (TextView) view.findViewById(R.id.destination);

        }
    }


    public BusRouteSubListRecyclerViewAdapter(Context mContext, ArrayList<BusModel> mPhotosList) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_busroute_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       holder.time.setText(" "+convertTime(mPhotosModelArrayList.get(position).getTime()).substring(0,5)+" ");
        String am=convertTime(mPhotosModelArrayList.get(position).getTime()).substring(6,7);
       holder.timeamorpm.setText(toUppercase(convertTime(mPhotosModelArrayList.get(position).getTime())));
        holder.routename.setText(mPhotosModelArrayList.get(position).getRoute_name());
        convertTime(mPhotosModelArrayList.get(position).getTime());
        if(position==0){
            holder.destination.setText("Start");
        }else if(position==mPhotosModelArrayList.size()-1){
            holder.destination.setText("Finish");

        }else{
            holder.destination.setVisibility(View.INVISIBLE);
        }

    }

    public String toUppercase(String word){
        String[] strArray = word.split(" ");
        StringBuilder builder = new StringBuilder();

            String cap = strArray[1].substring(0, 1).toUpperCase() + strArray[1].substring(1);
            builder.append(cap + " ");
       return builder.toString();

    }

    @Override
    public int getItemCount() {
        return mPhotosModelArrayList.size();
    }


    private String convertTime(String time){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = dateFormatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String displayValue = timeFormatter.format(date);
        if (displayValue.contains("a.m.")) {
            displayValue= AppUtils.replaceAmdot(displayValue);

        } else  if (displayValue.contains("p.m.")){
            displayValue=AppUtils.replacePmdot(displayValue);
        }
        return displayValue;
    }
}
