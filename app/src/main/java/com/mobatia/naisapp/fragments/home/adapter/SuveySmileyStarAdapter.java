package com.mobatia.naisapp.fragments.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.home.module.SurveyAnswersModel;

import java.util.ArrayList;

public class SuveySmileyStarAdapter extends RecyclerView.Adapter<SuveySmileyStarAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SurveyAnswersModel> mSurveyArrayList;
    String dept;
    String answerType;
    RelativeLayout nextQuestionBtn;
    //    RoundedHorizontalProgressBar progress_bar;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView answerTxt,smileyTxt;
        RelativeLayout choiseRelative,smileyRelative,startRelative;
        ImageView smileyImg,starImg;
        public MyViewHolder(View view) {
            super(view);
            answerTxt = (TextView) view.findViewById(R.id.answerTxt);
            smileyTxt = (TextView) view.findViewById(R.id.smileyTxt);
            choiseRelative = (RelativeLayout) view.findViewById(R.id.choiseRelative);
            smileyRelative = (RelativeLayout) view.findViewById(R.id.smileyRelative);
            startRelative = (RelativeLayout) view.findViewById(R.id.startRelative);
            smileyImg = (ImageView) view.findViewById(R.id.smileyImg);
            starImg = (ImageView) view.findViewById(R.id.starImg);
//            progress_bar = (RoundedHorizontalProgressBar) view.findViewById(R.id.progress_bar);

        }
    }


    public SuveySmileyStarAdapter(Context mContext, ArrayList<SurveyAnswersModel> mSurveyArrayList,RelativeLayout nextQuestionBtn,String answerType) {
        this.mContext = mContext;
        this.mSurveyArrayList = mSurveyArrayList;
        this.nextQuestionBtn = nextQuestionBtn;
        this.answerType = answerType;

    }

    @Override
    public SuveySmileyStarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_survey_smiley_star, parent, false);

        return new SuveySmileyStarAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SuveySmileyStarAdapter.MyViewHolder holder, int position) {

        if (answerType.equalsIgnoreCase("1"))
        {
            holder.choiseRelative.setVisibility(View.VISIBLE);
            holder.smileyRelative.setVisibility(View.GONE);
            holder.startRelative.setVisibility(View.GONE);
            holder.answerTxt.setText(mSurveyArrayList.get(position).getAnswer());
            if (mSurveyArrayList.get(position).isClicked())
            {

                holder.answerTxt.setBackgroundColor(mContext.getResources().getColor(R.color.rel_one));
            }
            else
            {
                holder.answerTxt.setBackground(mContext.getResources().getDrawable(R.drawable.button_grey_white));
            }
        }
        else if (answerType.equalsIgnoreCase("2"))
        {
            holder.choiseRelative.setVisibility(View.GONE);
            holder.startRelative.setVisibility(View.GONE);
            holder.smileyRelative.setVisibility(View.VISIBLE);
            holder.smileyTxt.setText(mSurveyArrayList.get(position).getAnswer());
            if (mSurveyArrayList.get(position).isClicked())
            {
                holder.smileyImg.setImageResource(R.drawable.emoji3_active);

            }
            else
            {
                holder.smileyImg.setImageResource(R.drawable.emoji3);
            }

        }
        else if (answerType.equalsIgnoreCase("3"))
        {
            holder.choiseRelative.setVisibility(View.GONE);
            holder.smileyRelative.setVisibility(View.GONE);
            holder.startRelative.setVisibility(View.VISIBLE);
            if (mSurveyArrayList.get(position).isClicked())
            {
                holder.starImg.setImageResource(R.drawable.emoji2_active);

            }
            else
            {
                holder.smileyImg.setImageResource(R.drawable.emoji2);
            }

        }

    }


    @Override
    public int getItemCount() {
        return mSurveyArrayList.size();
    }

}
