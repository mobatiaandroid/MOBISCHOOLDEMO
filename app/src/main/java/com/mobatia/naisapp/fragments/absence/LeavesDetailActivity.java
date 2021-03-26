package com.mobatia.naisapp.fragments.absence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.fragments.absence.model.LeavesModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 5/5/17.
 */
public class LeavesDetailActivity extends Activity implements NaisClassNameConstants {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Context mContext = this;
    LinearLayout fromlayout,reasonlayout;
    TextView stnameValue,leaveDateFromValue,leaveDateToValue,reasonValue,studClassValue;
    Bundle extras;
    int position;
    String studentNameStr="";
    String studentClassStr="";
    String fromDate="";
    String toDate="";
    String reasonForAbsence="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_detailpage);
        initUI();
        setValues();
    }
    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            studentNameStr=extras.getString("studentName");
            studentClassStr=extras.getString("studentClass");
            fromDate=extras.getString("fromDate");
            toDate=extras.getString("toDate");
            reasonForAbsence=extras.getString("reasonForAbsence");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        fromlayout= (LinearLayout) findViewById(R.id.fromlayout);
        reasonlayout= (LinearLayout) findViewById(R.id.reasonlayout);
        stnameValue= (TextView) findViewById(R.id.stnameValue);
        studClassValue= (TextView) findViewById(R.id.studClassValue);
        leaveDateFromValue= (TextView) findViewById(R.id.leaveDateFromValue);
        leaveDateToValue= (TextView) findViewById(R.id.leaveDateToValue);
        reasonValue= (TextView) findViewById(R.id.reasonValue);

        headermanager = new HeaderManager(LeavesDetailActivity.this, ABSENCE);
        headermanager.getHeader(relativeHeader, 0);

        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    private void setValues(){
        stnameValue.setText(studentNameStr);
        studClassValue.setText(studentClassStr);
        leaveDateFromValue.setText(AppUtils.dateConversionY(fromDate));
        leaveDateToValue.setText(AppUtils.dateConversionY(toDate));
        reasonValue.setText(reasonForAbsence);
    }

}
