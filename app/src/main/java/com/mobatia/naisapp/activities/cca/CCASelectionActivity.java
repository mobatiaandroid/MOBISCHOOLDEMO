package com.mobatia.naisapp.activities.cca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.adapter.CCAsActivityAdapter;
import com.mobatia.naisapp.activities.cca.adapter.CCAsWeekListAdapter;
import com.mobatia.naisapp.activities.cca.model.CCADetailModel;
import com.mobatia.naisapp.activities.cca.model.WeekListModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

public class CCASelectionActivity extends Activity {
    Context mContext;
    public static ArrayList<CCADetailModel> CCADetailModelArrayList;
    //    ArrayList<String> weekList;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
   public static RelativeLayout msgRelative;
    ImageView back;
    ImageView home;
    String tab_type = "CCAs";
    Bundle extras;
    //    ArrayList<String> mCcaArrayList;
    RecyclerView recycler_review;
    RecyclerView weekRecyclerList;
    GridLayoutManager recyclerViewLayoutManager;
    GridLayoutManager recyclerweekViewLayoutManager;
    int pos = 0;
    public static int ccaDetailpos = 0;
    public static Button submitBtn;
    public static Button nextBtn;
    public static boolean filled = false;
    boolean weekSelected = false;
    int weekPosition = 0;
    public  static int ccaedit = 0;
    CCAsWeekListAdapter mCCAsWeekListAdapter;
    TextView TVselectedForWeek;
    TextView textViewCCAaSelect;
    TextView textViewStudName;
    TextView messageTxt;
    CCAsActivityAdapter mCCAsActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cca_selection);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            ccaedit = extras.getInt("ccaedit",0);
//            pos = extras.getInt("pos");
            CCADetailModelArrayList = (ArrayList<CCADetailModel>) extras.getSerializable("CCA_Detail");
        }

        AppController.weekList = new ArrayList<>();
        AppController.weekListWithData = new ArrayList<>();
//        weekList.add("Sunday");
//        weekList.add("Monday");
//        weekList.add("Tuesday");
//        weekList.add("Wednesday");
//        weekList.add("Thursday");
//        weekList.add("Friday");
//        weekList.add("Saturday");
        for (int i = 0; i < 7; i++) {
            WeekListModel mWeekListModel = new WeekListModel();
            mWeekListModel.setWeekDay(getWeekday(i));
            mWeekListModel.setWeekDayMMM(getWeekdayMMM(i));
            if (ccaedit==0) {
                mWeekListModel.setChoiceStatus("0");
                mWeekListModel.setChoiceStatus1("0");
            }else{
                mWeekListModel.setChoiceStatus("1");
                mWeekListModel.setChoiceStatus1("1");

            }

            AppController.weekList.add(mWeekListModel);
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        msgRelative = (RelativeLayout) findViewById(R.id.msgRelative);
        recycler_review = (RecyclerView) findViewById(R.id.recycler_view_cca);
        weekRecyclerList = (RecyclerView) findViewById(R.id.weekRecyclerList);
        TVselectedForWeek = (TextView) findViewById(R.id.TVselectedForWeek);
        textViewCCAaSelect = (TextView) findViewById(R.id.textViewCCAaSelect);
        textViewStudName = (TextView) findViewById(R.id.textViewStudName);
        messageTxt = (TextView) findViewById(R.id.messageTxt);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        nextBtn.getBackground().setAlpha(255);
        submitBtn.getBackground().setAlpha(150);
        headermanager = new HeaderManager(CCASelectionActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
        messageTxt.startAnimation(startAnimation);
        if (PreferenceManager.getStudClassForCCA(mContext).equalsIgnoreCase("")) {
            textViewStudName.setText(PreferenceManager.getStudNameForCCA(mContext));
        } else {
            textViewStudName.setText(Html.fromHtml(PreferenceManager.getStudNameForCCA(mContext) + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(mContext)));

        }
        if (ccaedit==0)
        {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Info", "Please select a CCA or None for each choice and each day", R.drawable.exclamationicon, R.drawable.round);
            submitBtn.getBackground().setAlpha(150);
            submitBtn.setVisibility(View.INVISIBLE);
        }
        else
        {
            submitBtn.getBackground().setAlpha(255);
            submitBtn.setVisibility(View.VISIBLE);
            nextBtn.getBackground().setAlpha(255);
            nextBtn.setVisibility(View.GONE);

        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              for (int i=0;i<CCADetailModelArrayList.size();i++)
//              {
//                  System.out.println("Choice1 "+CCADetailModelArrayList.get(i).getDay()+":"+CCADetailModelArrayList.get(i).getChoice1());
//                  System.out.println("Choice2 "+CCADetailModelArrayList.get(i).getDay()+":"+CCADetailModelArrayList.get(i).getChoice2());
//              }
                if (filled) {

                    Intent mInent = new Intent(CCASelectionActivity.this, CCAsReviewActivity.class);
                    startActivity(mInent);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Select choice for all available days", R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        recycler_review.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        recycler_review.addItemDecoration(itemDecoration);
        recycler_review.setLayoutManager(recyclerViewLayoutManager);
//        for (int i = 0; i < CCADetailModelArrayList.size(); i++)
//            if (CCADetailModelArrayList.get(i).getDay().equalsIgnoreCase("Sunday")) {
//                {
//                    ccaDetailpos=i;
//                    CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(i).getCcaChoiceModel(), CCADetailModelArrayList.get(i).getCcaChoiceModel2(),0,AppController.weekList);
//                    recycler_review.setAdapter(mCCAsActivityAdapter);
//                    break;
//                }
//            }

        TVselectedForWeek.setText("Sunday");
//        for (int j = 0; j < AppController.weekList.size(); j++) {
//            for (int i = 0; i < CCADetailModelArrayList.size(); i++) {
//                if (!AppController.weekList.get(j).getWeekDay().equalsIgnoreCase(CCADetailModelArrayList.get(i).getDay())) {
//                    AppController.weekList.get(j).setChoiceStatus("2");
//                    AppController.weekList.get(j).setChoiceStatus1("2");
//                }
//                else
//                {
//                    AppController.weekList.get(j).setChoiceStatus("0");
//                    AppController.weekList.get(j).setChoiceStatus1("0");
//                }
//            }
//        }

        for (int i = 0; i < AppController.weekList.size(); i++) {
            AppController.weekList.get(i).setChoiceStatus("2");
            AppController.weekList.get(i).setChoiceStatus1("2");
            AppController.weekList.get(i).setDataInWeek("0");

        }


        for (int i = 0; i < AppController.weekList.size(); i++) {
            for (int j = 0; j < CCADetailModelArrayList.size(); j++) {
                if (AppController.weekList.get(i).getWeekDay().equalsIgnoreCase(CCADetailModelArrayList.get(j).getDay())) {
                    if (ccaedit==0) {
                        AppController.weekList.get(i).setChoiceStatus("0");
                        AppController.weekList.get(i).setChoiceStatus1("0");
                    }else{
                        AppController.weekList.get(i).setChoiceStatus("1");
                        AppController.weekList.get(i).setChoiceStatus1("1");
                    }
                    AppController.weekList.get(i).setDataInWeek("1");
                    AppController.weekListWithData.add(i);
                }

            }
        }
        for (int i = 0; i < CCADetailModelArrayList.size(); i++) {
            if (CCADetailModelArrayList.get(i).getDay().equalsIgnoreCase("Sunday")) {
                ccaDetailpos = i;
                textViewCCAaSelect.setVisibility(View.VISIBLE);
                TVselectedForWeek.setVisibility(View.VISIBLE);
                mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(i).getCcaChoiceModel(), CCADetailModelArrayList.get(i).getCcaChoiceModel2(), 0, AppController.weekList, weekRecyclerList);
                recycler_review.setAdapter(mCCAsActivityAdapter);

                break;
            } else if (i == CCADetailModelArrayList.size() - 1) {
                if (!CCADetailModelArrayList.get(i).getDay().equalsIgnoreCase("Sunday")) {
                    mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, 0);
                    recycler_review.setAdapter(mCCAsActivityAdapter);
                    textViewCCAaSelect.setVisibility(View.GONE);
                    TVselectedForWeek.setVisibility(View.GONE);
                    AppController.weekList.get(0).setChoiceStatus("2");
                    AppController.weekList.get(0).setChoiceStatus1("2");
//                    Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show();
                }
            }

        }


//        CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(0).getCcaChoiceModel(), CCADetailModelArrayList.get(0).getCcaChoiceModel2());
//        recycler_review.setAdapter(mCCAsActivityAdapter);

        weekRecyclerList.setHasFixedSize(true);
//        recyclerweekViewLayoutManager = new GridLayoutManager(mContext, 7);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        weekRecyclerList.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
//        weekRecyclerList.addItemDecoration(itemDecoration);
        weekRecyclerList.setLayoutManager(llm);
//        weekRecyclerList.setLayoutManager(recyclerweekViewLayoutManager);
        mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition);
        weekRecyclerList.setAdapter(mCCAsWeekListAdapter);
        weekRecyclerList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), weekRecyclerList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {

                        for (int i = 0; i < CCADetailModelArrayList.size(); i++) {
                            if (AppController.weekList.get(position).getWeekDay().equalsIgnoreCase(CCADetailModelArrayList.get(i).getDay())) {
                                pos = i;
                                ccaDetailpos = i;

                                weekSelected = true;
                                break;
                            } else {
                                weekSelected = false;

                            }
                            if (weekSelected) {

                                break;
                            }
                        }
                        if (!weekSelected) {
                            textViewCCAaSelect.setVisibility(View.GONE);
                            TVselectedForWeek.setVisibility(View.GONE);
                            msgRelative.setVisibility(View.GONE);
                            CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, 0);
                            recycler_review.setAdapter(mCCAsActivityAdapter);
                            mCCAsActivityAdapter.notifyDataSetChanged();
                            AppController.weekList.get(position).setChoiceStatus("2");
                            AppController.weekList.get(position).setChoiceStatus1("2");
                            Toast.makeText(mContext, "CCA choice not available", Toast.LENGTH_SHORT).show();

                        } else {
                            textViewCCAaSelect.setVisibility(View.VISIBLE);
                            TVselectedForWeek.setVisibility(View.VISIBLE);
                            msgRelative.setVisibility(View.VISIBLE);
                            CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(pos).getCcaChoiceModel(), CCADetailModelArrayList.get(pos).getCcaChoiceModel2(), position, AppController.weekList, weekRecyclerList);
                            recycler_review.setAdapter(mCCAsActivityAdapter);
                            mCCAsActivityAdapter.notifyDataSetChanged();
                        }
                        for (int j = 0; j < AppController.weekList.size(); j++) {
                            if (AppController.weekList.get(j).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(j).getChoiceStatus1().equalsIgnoreCase("0")) {
                                filled = false;
                                break;
                            } else {
                                filled = true;
                            }
                            if (!filled) {
                                break;
                            }

                        }
                        if (filled) {
                            submitBtn.getBackground().setAlpha(255);
                            submitBtn.setVisibility(View.VISIBLE);
                            nextBtn.getBackground().setAlpha(255);
                            nextBtn.setVisibility(View.GONE);


                        } else {
                            submitBtn.getBackground().setAlpha(150);
                            submitBtn.setVisibility(View.INVISIBLE);
                            nextBtn.getBackground().setAlpha(255);
                            nextBtn.setVisibility(View.VISIBLE);

                        }
                        weekPosition = position;
                        mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition);
                        weekRecyclerList.setAdapter(mCCAsWeekListAdapter);
                        TVselectedForWeek.setText(AppController.weekList.get(position).getWeekDay());
//                        horizontalScrollView
                        if (weekPosition==6)
                           weekRecyclerList.scrollToPosition(6);
                        else
                            weekRecyclerList.scrollToPosition(0);

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));

        for (int j = 0; j < AppController.weekList.size(); j++) {
            if (AppController.weekList.get(j).getDataInWeek().equalsIgnoreCase("1")) {
                for (int i = 0; i < CCADetailModelArrayList.size(); i++) {
                    if (AppController.weekList.get(j).getWeekDay().equalsIgnoreCase(CCADetailModelArrayList.get(i).getDay())) {
                        pos = i;
                        ccaDetailpos = i;

                        weekSelected = true;
                        break;
                    } else {
                        weekSelected = false;

                    }
                    if (weekSelected) {

                        break;
                    }
                }
                if (!weekSelected) {
                    textViewCCAaSelect.setVisibility(View.GONE);
                    TVselectedForWeek.setVisibility(View.GONE);
                    msgRelative.setVisibility(View.VISIBLE);
                    CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, 0);
                    recycler_review.setAdapter(mCCAsActivityAdapter);
                    mCCAsActivityAdapter.notifyDataSetChanged();
                    AppController.weekList.get(j).setChoiceStatus("2");
                    AppController.weekList.get(j).setChoiceStatus1("2");
                    Toast.makeText(mContext, "CCA choice not available", Toast.LENGTH_SHORT).show();

                } else {
                    textViewCCAaSelect.setVisibility(View.VISIBLE);
                    TVselectedForWeek.setVisibility(View.VISIBLE);
                    msgRelative.setVisibility(View.VISIBLE);
                    CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(pos).getCcaChoiceModel(), CCADetailModelArrayList.get(pos).getCcaChoiceModel2(), j, AppController.weekList, weekRecyclerList);
                    recycler_review.setAdapter(mCCAsActivityAdapter);
                    mCCAsActivityAdapter.notifyDataSetChanged();
                }
                for (int k = 0; k < AppController.weekList.size(); k++) {
                    if (AppController.weekList.get(k).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(k).getChoiceStatus1().equalsIgnoreCase("0")) {
                        filled = false;
                        msgRelative.setVisibility(View.VISIBLE);

                        break;
                    } else {
                        filled = true;
                    }
                    if (!filled) {
                        break;
                    }

                }
                if (filled) {
                    submitBtn.getBackground().setAlpha(255);
                    submitBtn.setVisibility(View.VISIBLE);
                    nextBtn.getBackground().setAlpha(255);
                    nextBtn.setVisibility(View.GONE);
                    msgRelative.setVisibility(View.GONE);

//                    msgRelative.setVisibility(View.GONE);


                } else {
                    msgRelative.setVisibility(View.VISIBLE);
                    submitBtn.getBackground().setAlpha(150);
                    submitBtn.setVisibility(View.INVISIBLE);
                    nextBtn.getBackground().setAlpha(255);
                    nextBtn.setVisibility(View.VISIBLE);

                }
                weekPosition = j;
                mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition);
                weekRecyclerList.setAdapter(mCCAsWeekListAdapter);
                TVselectedForWeek.setText(AppController.weekList.get(j).getWeekDay());
                break;
            }
        }

        if (AppController.weekListWithData.size()>0)
        {
            nextBtn.getBackground().setAlpha(255);
            nextBtn.setVisibility(View.VISIBLE);
        }
        else {
            nextBtn.getBackground().setAlpha(255);
            nextBtn.setVisibility(View.GONE);

        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weekPosition=weekPosition+1;

                if (AppController.weekListWithData.contains(weekPosition)) {
                    for (int a=0;a<AppController.weekListWithData.size();a++)
                    {
                        if (AppController.weekListWithData.get(a)==weekPosition) {
                        //weekPosition = a;
                            weekPosition = AppController.weekListWithData.get(a);
                            break;
                        }
                    }

         /*           for (int a=0;a<AppController.weekListWithData.size();a++)
                    {
                        if (weekPosition==AppController.weekListWithData.get(a)) {
                            weekPosition = AppController.weekListWithData.get(a);
                        }
                    }
                    weekPosition = AppController.weekListWithData.get(weekPosition);*/
                }
                else
                {
                    if (weekPosition>=(AppController.weekList.size()-1))
                    {
                        weekPosition=0;
                    }
                    if (AppController.weekListWithData.contains(weekPosition))
                    {
//                        weekPosition = AppController.weekListWithData.get(weekPosition);
                        for (int a=0;a<AppController.weekListWithData.size();a++)
                        {
//                            if (AppController.weekListWithData.contains(weekPosition)) {
                              if (AppController.weekListWithData.get(a)==weekPosition) {
//                                weekPosition = a;
                                weekPosition = AppController.weekListWithData.get(a);

                                break;
                            }
                        }
                    }
                    else
                    {
                        for (int m=weekPosition;m<AppController.weekList.size();m++)
                        {
                            if (AppController.weekListWithData.contains(m)) {
                                weekPosition =m;
                                System.out.println("weekposition:m:"+weekPosition);
                                break;
                            }
                        }
                        if (!(AppController.weekListWithData.contains(weekPosition)))
                        {
                            weekPosition = 0;
                        }
                    }
                }

                for (int j = weekPosition; j < AppController.weekList.size(); j++)
                {
                    if (AppController.weekList.get(j).getDataInWeek().equalsIgnoreCase("1")) {
                        for (int i = 0; i < CCADetailModelArrayList.size(); i++) {
                            if (AppController.weekList.get(j).getWeekDay().equalsIgnoreCase(CCADetailModelArrayList.get(i).getDay())) {
                                pos = i;
                                ccaDetailpos = i;

                                weekSelected = true;
                                break;
                            } else {
                                weekSelected = false;

                            }
                            if (weekSelected) {

                                break;
                            }
                        }
                        if (!weekSelected) {
                            textViewCCAaSelect.setVisibility(View.GONE);
                            TVselectedForWeek.setVisibility(View.GONE);
                            CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, 0);
                            recycler_review.setAdapter(mCCAsActivityAdapter);
                            mCCAsActivityAdapter.notifyDataSetChanged();
                            AppController.weekList.get(j).setChoiceStatus("2");
                            AppController.weekList.get(j).setChoiceStatus1("2");
//                            Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show();

                        } else {
                            textViewCCAaSelect.setVisibility(View.VISIBLE);
                            TVselectedForWeek.setVisibility(View.VISIBLE);

                            CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList.get(pos).getCcaChoiceModel(), CCADetailModelArrayList.get(pos).getCcaChoiceModel2(), j, AppController.weekList, weekRecyclerList);
                            recycler_review.setAdapter(mCCAsActivityAdapter);
                            mCCAsActivityAdapter.notifyDataSetChanged();
                        }
                        for (int k = 0; k < AppController.weekList.size(); k++) {
                            if (AppController.weekList.get(k).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(k).getChoiceStatus1().equalsIgnoreCase("0")) {
                                filled = false;
                                msgRelative.setVisibility(View.VISIBLE);

                                break;
                            } else {
                                filled = true;
                            }
                            if (!filled) {
                                break;
                            }

                        }
                        if (filled) {
                            submitBtn.getBackground().setAlpha(255);
                            submitBtn.setVisibility(View.VISIBLE);
                            nextBtn.getBackground().setAlpha(255);
                            nextBtn.setVisibility(View.GONE);
                            msgRelative.setVisibility(View.GONE);

                            nextBtn.setVisibility(View.GONE);

                        } else {
                            msgRelative.setVisibility(View.VISIBLE);

                            submitBtn.getBackground().setAlpha(150);
                            submitBtn.setVisibility(View.INVISIBLE);
                            nextBtn.getBackground().setAlpha(255);
                            nextBtn.setVisibility(View.VISIBLE);


                        }
                        weekPosition = j;
                        mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, weekPosition);
                        weekRecyclerList.setAdapter(mCCAsWeekListAdapter);
                        TVselectedForWeek.setText(AppController.weekList.get(j).getWeekDay());
                        break;
                    }
                }
                if (weekPosition==6) {
                    weekRecyclerList.getLayoutManager().scrollToPosition(weekPosition);
                }
                else
                {
                    weekRecyclerList.getLayoutManager().scrollToPosition(0);

                }

            }
        });
    }

    String getWeekday(int weekDay) {
        String day = "";
        switch (weekDay) {
            case 0:
                day = "Sunday";
                break;
            case 1:
                day = "Monday";
                break;
            case 2:
                day = "Tuesday";
                break;
            case 3:
                day = "Wednesday";
                break;
            case 4:
                day = "Thursday";
                break;
            case 5:
                day = "Friday";
                break;
            case 6:
                day = "Saturday";
                break;
        }
        return day;
    }

    String getWeekdayMMM(int weekDay) {
        String day = "";
        switch (weekDay) {
            case 0:
                day = "SUN";
                break;
            case 1:
                day = "MON";
                break;
            case 2:
                day = "TUE";
                break;
            case 3:
                day = "WED";
                break;
            case 4:
                day = "THU";
                break;
            case 5:
                day = "FRI";
                break;
            case 6:
                day = "SAT";
                break;
        }
        return day;
    }


}
