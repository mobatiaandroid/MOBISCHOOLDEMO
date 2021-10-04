package com.mobatia.naisapp.fragments.home.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.fragments.home.module.SurveyAnswersModel;
import com.mobatia.naisapp.fragments.home.module.SurveyModel;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import java.util.ArrayList;

public class SurveyChoiceAdapter extends RecyclerView.Adapter<SurveyChoiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SurveyAnswersModel> mSurveyArrayList;
    String dept;
    String answerType;
    ImageView nextQuestionBtn,choiseRelative;
    ImageView starImg;
//    RoundedHorizontalProgressBar progress_bar;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView answerTxt,smileyTxt,numberTxt;
        RelativeLayout choiseRelative,smileyRelative,startRelative,numberRelative;
        LinearLayout smileyLinear;
        ImageView smileyImg;
        public MyViewHolder(View view) {
            super(view);
            answerTxt = (TextView) view.findViewById(R.id.answerTxt);
            smileyTxt = (TextView) view.findViewById(R.id.smileyTxt);
            numberTxt = (TextView) view.findViewById(R.id.numberTxt);
            choiseRelative = (RelativeLayout) view.findViewById(R.id.choiseRelative);
            smileyRelative = (RelativeLayout) view.findViewById(R.id.smileyRelative);
            startRelative = (RelativeLayout) view.findViewById(R.id.startRelative);
            choiseRelative = (RelativeLayout) view.findViewById(R.id.choiseRelative);
            numberRelative = (RelativeLayout) view.findViewById(R.id.numberRelative);
            smileyImg = (ImageView) view.findViewById(R.id.smileyImg);
            starImg = (ImageView) view.findViewById(R.id.starImg);
            smileyLinear = (LinearLayout) view.findViewById(R.id.smileyLinear);
//            progress_bar = (RoundedHorizontalProgressBar) view.findViewById(R.id.progress_bar);

        }
    }


    public SurveyChoiceAdapter(Context mContext, ArrayList<SurveyAnswersModel> mSurveyArrayList,ImageView nextQuestionBtn,String answerType) {
        this.mContext = mContext;
        this.mSurveyArrayList = mSurveyArrayList;
        this.nextQuestionBtn = nextQuestionBtn;
        this.answerType = answerType;

    }

    @Override
    public SurveyChoiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_survey_choice, parent, false);

        return new SurveyChoiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Log.e("ANS_TYPE",answerType);
        if (answerType.equalsIgnoreCase("1"))
        {
            holder.choiseRelative.setVisibility(View.VISIBLE);
            holder.smileyRelative.setVisibility(View.GONE);
            holder.startRelative.setVisibility(View.GONE);
            holder.numberRelative.setVisibility(View.GONE);
            holder.answerTxt.setText(mSurveyArrayList.get(position).getAnswer());
            if (mSurveyArrayList.get(position).isClicked())
            {


                AppController.answer_id=mSurveyArrayList.get(position).getId();
                holder.answerTxt.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_green));
                holder.choiseRelative.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_green));
                nextQuestionBtn.setClickable(true);
            }
            else
            {
                AppController.answer_id="";
                holder.answerTxt.setBackgroundColor(mContext.getResources().getColor(R.color.rel_two));
                holder.choiseRelative.setBackgroundColor(mContext.getResources().getColor(R.color.rel_one));
                //holder.answerTxt.setBackground(mContext.getResources().getDrawable(R.drawable.button_grey_white));
            }
        }
        else if (answerType.equalsIgnoreCase("2"))
        {
            holder.choiseRelative.setVisibility(View.GONE);
            holder.startRelative.setVisibility(View.GONE);
            holder.numberRelative.setVisibility(View.GONE);
            holder.smileyRelative.setVisibility(View.VISIBLE);
            holder.smileyTxt.setText(mSurveyArrayList.get(position).getAnswer());
            if (mSurveyArrayList.get(position).isClicked())
            {
                holder.smileyTxt.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_green));
                holder.smileyRelative.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_green));
                if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Sad"))
                {
                    holder.smileyImg.setImageResource(R.drawable.sad);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Angry"))
                {
                    holder.smileyImg.setImageResource(R.drawable.angry);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Happy"))
                {
                    holder.smileyImg.setImageResource(R.drawable.happy);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Neutral"))
                {
                    holder.smileyImg.setImageResource(R.drawable.neutral);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Excited"))
                {
                    holder.smileyImg.setImageResource(R.drawable.excited);
                }

                AppController.answer_id=mSurveyArrayList.get(position).getId();

                nextQuestionBtn.setClickable(true);

            }

            else
            {
                holder.smileyTxt.setBackgroundColor(mContext.getResources().getColor(R.color.rel_two));
                holder.smileyLinear.setBackgroundColor(mContext.getResources().getColor(R.color.rel_two));
                holder.smileyRelative.setBackgroundColor(mContext.getResources().getColor(R.color.rel_one));
                AppController.answer_id="";
                if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Sad"))
                {
                    holder.smileyImg.setImageResource(R.drawable.sad_notselected);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Angry"))
                {
                    holder.smileyImg.setImageResource(R.drawable.angry_notselected);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Happy"))
                {
                    holder.smileyImg.setImageResource(R.drawable.happy_notselected);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Neutral"))
                {
                    holder.smileyImg.setImageResource(R.drawable.neutral_notselected);
                }
                else if (mSurveyArrayList.get(position).getAnswer().equalsIgnoreCase("Excited"))
                {
                    holder.smileyImg.setImageResource(R.drawable.excited_notselected);
                }

            }

        }
        else if (answerType.equalsIgnoreCase("3"))
        {
            holder.choiseRelative.setVisibility(View.GONE);
            holder.smileyRelative.setVisibility(View.GONE);
            holder.numberRelative.setVisibility(View.GONE);
            holder.startRelative.setVisibility(View.VISIBLE);
            if (mSurveyArrayList.get(position).isClicked0())
            {
                starImg.setImageResource(R.drawable.star_fill);
                AppController.answer_id=mSurveyArrayList.get(position).getId();
                nextQuestionBtn.setClickable(true);
            }
            else
            {
                AppController.answer_id="";
                holder.smileyImg.setImageResource(R.drawable.star);
            }



        }
        else if(answerType.equalsIgnoreCase("4"))
        {
            Log.e("ANS","TYPE");
            holder.choiseRelative.setVisibility(View.GONE);
            holder.smileyRelative.setVisibility(View.GONE);
            holder.startRelative.setVisibility(View.GONE);
            holder.numberRelative.setVisibility(View.VISIBLE);
            int value=position+1;
            String num=String.valueOf(value);
            holder.numberTxt.setText(mSurveyArrayList.get(position).getAnswer());
            if (mSurveyArrayList.get(position).isClicked())
            {


                AppController.answer_id=mSurveyArrayList.get(position).getId();
                holder.numberTxt.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_green));
                holder.numberRelative.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_green));
                nextQuestionBtn.setClickable(true);
            }
            else
            {
                AppController.answer_id="";
                holder.numberTxt.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                holder.numberRelative.setBackground(mContext.getResources().getDrawable(R.drawable.curve_number_survey));
                //holder.answerTxt.setBackground(mContext.getResources().getDrawable(R.drawable.button_grey_white));
            }
        }

    }


    @Override
    public int getItemCount() {
        return mSurveyArrayList.size();
    }

}