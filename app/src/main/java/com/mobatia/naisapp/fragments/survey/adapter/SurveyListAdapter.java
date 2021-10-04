package com.mobatia.naisapp.fragments.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.notifications.adapter.PushNotificationRecyclerAdapter;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.fragments.survey.model.SurveyListModel;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;

import java.util.ArrayList;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SurveyListModel> surveyListArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.listTxtTitle);


        }
    }

    public SurveyListAdapter(Context mContext, ArrayList<SurveyListModel> surveyListArrayList) {
        this.mContext = mContext;
        this.surveyListArrayList = surveyListArrayList;

    }

    @Override
    public SurveyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_settings_list_adapter, parent, false);

        return new SurveyListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SurveyListAdapter.MyViewHolder holder, int position) {


        holder.title.setText(surveyListArrayList.get(position).getSurvey_name());
    }


    @Override
    public int getItemCount() {
        return surveyListArrayList.size();
    }

}
