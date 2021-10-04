package com.mobatia.naisapp.fragments.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.bus_routes.adapter.BusRouteSubListRecyclerViewAdapter;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.fragments.home.module.SurveyQuestionsModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

public class SurveyPagerAdapter  extends PagerAdapter {
    Context mContext;
    ArrayList<SurveyQuestionsModel> mSurveyArrayList;
    private LayoutInflater mInflaters;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    ImageView nextQuestionBtn;
    SurveyChoiceAdapter busRouteSubListRecyclerViewAdapter;
    public SurveyPagerAdapter(Context context, ArrayList<SurveyQuestionsModel> mSurveyArrayList, ImageView nextQuestionBtn)
    {
        this.mContext=context;
        this.mSurveyArrayList=mSurveyArrayList;
        this.nextQuestionBtn=nextQuestionBtn;
    }

    @Override
    public int getCount() {
        return mSurveyArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View pageview = null;
        mInflaters = LayoutInflater.from(mContext);
        pageview = mInflaters.inflate(R.layout.adapter_survey_questions, null);
        TextView questionTxt = (TextView) pageview.findViewById(R.id.questionTxt);
        RecyclerView choiceRecycler = (RecyclerView) pageview.findViewById(R.id.choiceRecycler);
        questionTxt.setText(mSurveyArrayList.get(position).getQuestion());
        if (mSurveyArrayList.get(position).getAnswer_type().equalsIgnoreCase("1"))
        {
            choiceRecycler.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            choiceRecycler.setLayoutManager(llm);
        }
        else if (mSurveyArrayList.get(position).getAnswer_type().equalsIgnoreCase("2"))
        {
            choiceRecycler.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            choiceRecycler.setLayoutManager(llm);
        }
        else if (mSurveyArrayList.get(position).getAnswer_type().equalsIgnoreCase("3"))
        {
            choiceRecycler.setHasFixedSize(true);
            GridLayoutManager llm = new GridLayoutManager(mContext,1);
            llm.setOrientation(GridLayoutManager.HORIZONTAL);
            choiceRecycler.setLayoutManager(llm);
        }
        else if (mSurveyArrayList.get(position).getAnswer_type().equalsIgnoreCase("4"))
        {
            choiceRecycler.setHasFixedSize(true);
            GridLayoutManager llm = new GridLayoutManager(mContext,1);
            llm.setOrientation(GridLayoutManager.HORIZONTAL);
            choiceRecycler.setLayoutManager(llm);
        }
        Log.e("STAR CHECK",mSurveyArrayList.get(position).getAnswer());


         busRouteSubListRecyclerViewAdapter = new SurveyChoiceAdapter(mContext,mSurveyArrayList.get(position).getSurveyAnswersrrayList(),nextQuestionBtn,mSurveyArrayList.get(position).getAnswer_type());
        choiceRecycler.setAdapter(busRouteSubListRecyclerViewAdapter);
       // nextQuestionBtn.setClickable(false);
        choiceRecycler.addOnItemTouchListener(new RecyclerItemListener(mContext, choiceRecycler,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int pos) {
                        int clickedPosition=-1;
                        for (int i=0;i<mSurveyArrayList.get(position).getSurveyAnswersrrayList().size();i++)
                        {
                            if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(i).isClicked())
                            {
                                clickedPosition=i;
                            }
                        }
                        if (clickedPosition!=-1)
                        {
                            if (clickedPosition==pos)
                            {
                                AppController.answer_id="";
                                AppController.question_id=mSurveyArrayList.get(position).getId();

                                mSurveyArrayList.get(position).setAnswer("");
                                mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).setClicked(false);

                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==5)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==4)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);



                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==3)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);

                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==2)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);

                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==1)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(false);

                                }

//                                for (int j=0;j< mSurveyArrayList.get(position).getSurveyAnswersrrayList().size();j++)
//                                {
//                                    if (pos)
//                                }
                            }
                            else
                            {
                                AppController.answer_id=mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).getId();
                                AppController.question_id=mSurveyArrayList.get(position).getId();
                                mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(clickedPosition).setClicked(false);
                                mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).setClicked(true);
                                mSurveyArrayList.get(position).setAnswer( mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).getId());
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==5)
                                {

                                    if (pos==0)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                    }
                                    else if (pos==1)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                    }
                                    else if (pos==2)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                    }
                                    else if (pos==3)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                    }
                                    else if (pos==4)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(true);
                                    }
                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==4)
                                {

                                    if (pos==0)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                    }
                                    else if (pos==1)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                    }
                                    else if (pos==2)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                    }
                                    else if (pos==3)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(true);
                                    }

                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==3)
                                {

                                    if (pos==0)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    }
                                    else if (pos==1)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    }
                                    else if (pos==2)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                    }

                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==2)
                                {

                                    if (pos==0)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                    }
                                    else if (pos==1)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    }

                                }
                                if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==1)
                                {

                                    if (pos==0)
                                    {
                                        mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    }

                                }
                            }
                        }
                        else
                        {
                            AppController.answer_id=mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).getId();
                            AppController.question_id=mSurveyArrayList.get(position).getId();
                            mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).setClicked(true);
                            mSurveyArrayList.get(position).setAnswer( mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).getId());
                            if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==5)
                            {

                                if (pos==0)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                }
                                else if (pos==1)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                }
                                else if (pos==2)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                }
                                else if (pos==3)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(false);
                                }
                                else if (pos==4)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(4).setClicked0(true);
                                }
                            }
                            if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==4)
                            {

                                if (pos==0)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                }
                                else if (pos==1)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                }
                                else if (pos==2)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(false);
                                }
                                else if (pos==3)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(3).setClicked0(true);
                                }

                            }
                            if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==3)
                            {

                                if (pos==0)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                }
                                else if (pos==1)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(false);
                                }
                                else if (pos==2)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(2).setClicked0(true);
                                }

                            }
                            if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==2)
                            {

                                if (pos==0)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(false);
                                }
                                else if (pos==1)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(1).setClicked0(true);
                                }

                            }
                            if (mSurveyArrayList.get(position).getSurveyAnswersrrayList().size()==1)
                            {

                                if (pos==0)
                                {
                                    mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(0).setClicked0(true);
                                }

                            }
                        }
                        if (position==mSurveyArrayList.size()-1)
                        {
                            AppController.answer_id=mSurveyArrayList.get(position).getSurveyAnswersrrayList().get(pos).getId();
                            AppController.question_id=mSurveyArrayList.get(position).getId();

                        }

                        busRouteSubListRecyclerViewAdapter = new SurveyChoiceAdapter(mContext,mSurveyArrayList.get(position).getSurveyAnswersrrayList(),nextQuestionBtn,mSurveyArrayList.get(position).getAnswer_type());
                        choiceRecycler.setAdapter(busRouteSubListRecyclerViewAdapter);

                    }

                    public void onLongClickItem(View v, int pos) {

                    }
                }));
        ((ViewPager)container).addView(pageview, 0);

        return pageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
