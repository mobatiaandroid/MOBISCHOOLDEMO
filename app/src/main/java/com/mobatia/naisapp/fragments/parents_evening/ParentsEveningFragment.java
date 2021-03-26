/**
 *
 */
package com.mobatia.naisapp.fragments.parents_evening;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.parents_evening.ParentsEveningCalendarActivity;
import com.mobatia.naisapp.activities.parents_evening.ParentsEveninginfoActivity;
import com.mobatia.naisapp.activities.review_appointments.ReviewAppointmentsRecyclerViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.adapter.ParentsEveningStaffAdapter;
import com.mobatia.naisapp.fragments.parents_evening.adapter.ParentsEveningStudentAdapter;
import com.mobatia.naisapp.fragments.parents_evening.model.StaffModel;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Rijo K Jose
 */

public class ParentsEveningFragment extends Fragment implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {

    private View mRootView;
    private Context mContext;
    private ListView mAboutUsList;
    private String mTitle;
    private String mTabId;
    private String mStaffId="";
    private String mStudentId="";
    private String mStudentName="";
    private String mStaffName="";
    private String mClass="";
    private RelativeLayout staffRelative,studentRelative,relMain;
    private ImageView selectStaffImgView,selectStudentImgView,next,infoImg,reviewImageView;
    TextView mTitleTextView,studentNameTV,staffNameTV;
    //	private CustomAboutUsAdapter mAdapter;
//	private ArrayList<AboutUsModel> mAboutUsListArray;
    ArrayList<StudentModel> mListViewArray;
    ArrayList<StaffModel> mListViewStaffArray;

    public ParentsEveningFragment() {

    }

    public ParentsEveningFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     * android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_parent_evening_list, container,
                false);
//        setHasOptionsMenu(true);
        mContext = getActivity();
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI();
        if(AppUtils.isNetworkConnected(mContext)) {
            getStudentList();
        }else{
            AppUtils.showDialogAlertDismiss(getActivity(),"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
        return mRootView;
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        studentNameTV = (TextView) mRootView.findViewById(R.id.studentNameTV);
        staffNameTV = (TextView) mRootView.findViewById(R.id.staffNameTV);
        selectStaffImgView = (ImageView) mRootView.findViewById(R.id.selectStaffImgView);
        next = (ImageView) mRootView.findViewById(R.id.next);
        selectStudentImgView = (ImageView) mRootView.findViewById(R.id.selectStudentImgView);
        studentRelative = (RelativeLayout) mRootView.findViewById(R.id.studentRelative);
        staffRelative = (RelativeLayout) mRootView.findViewById(R.id.staffRelative);
        mTitleTextView.setText(PARENT_EVENING);
        relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
        reviewImageView = (ImageView)  mRootView.findViewById(R.id.reviewImageView);
        infoImg = (ImageView) mRootView.findViewById(R.id.infoImg);
        infoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, ParentsEveninginfoActivity.class);

                mContext.startActivity(mIntent);
            }
        });
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(PreferenceManager.getIsFirstTimeInPE(mContext))
        {
            PreferenceManager.setIsFirstTimeInPE(mContext,false);
            Intent mintent = new Intent(mContext, ParentsEveninginfoActivity.class);
            mintent.putExtra("type", 1);
            mContext.startActivity(mintent);
        }
        selectStaffImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListViewStaffArray.size()>0) {

                    showStaffList();
                }else
                {
                    Toast.makeText(mContext, "No Staff Found.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        selectStudentImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListViewArray.size()>0) {
                    showStudentList();
                }
                else
                {
                    Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent mIntent =new Intent(getActivity(), ParentsEveningCalendarActivity.class);
                mIntent.putExtra(JTAG_STAFF_ID,mStaffId);
                mIntent.putExtra(JTAG_STUDENT_ID,mStudentId);
                mIntent.putExtra("studentName",mStudentName);
                mIntent.putExtra("staffName",mStaffName);
                mIntent.putExtra("studentClass",mClass);
                mContext.startActivity(mIntent);
            }
        });
        reviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent mIntent =new Intent(getActivity(), ReviewAppointmentsRecyclerViewActivity.class);

                mContext.startActivity(mIntent);
            }
        });

    }

    public void getStudentList() {

        try {
            mListViewArray = new ArrayList<StudentModel>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_STUDENT_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN,JTAG_USERS_ID};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mListViewArray.add(getSearchValues(dataObject));
                                            }

                                        } else {
                                            //CustomStatusDialog();
                                            Toast.makeText(mContext, "No Student Found.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getStudentList();

                                }
                            } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void getStaffList(String student_id) {

        try {
            mListViewStaffArray = new ArrayList<StaffModel>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_STAFF_LIST_ACCORDING_TO_STUDENT);
            String[] name = new String[]{JTAG_ACCESSTOKEN,JTAG_STUDENT_ID};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),student_id};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mListViewStaffArray.add(getStaffValues(dataObject));
                                            }
                                            staffRelative.setVisibility(View.VISIBLE);
                                        } else {
                                            //CustomStatusDialog();
                                            staffRelative.setVisibility(View.INVISIBLE);

                                            Toast.makeText(mContext, "No Staff Found", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getStudentList();

                                }else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else  {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private StudentModel getSearchValues(JSONObject Object)
            throws JSONException {
        StudentModel mStudentModel = new StudentModel();
        mStudentModel.setmId(Object.getString(JTAG_ID));
		mStudentModel.setmClass(Object.getString(JTAG_TAB_CLASS));
        mStudentModel.setmSection(Object.getString(JTAG_TAB_SECTION));
        mStudentModel.setmName(Object.getString(JTAG_TAB_NAME));
        mStudentModel.setmPhoto(Object.getString(JTAG_TAB_PHOTO).replaceAll(" ","%20"));
        return mStudentModel;
    }

    private StaffModel getStaffValues(JSONObject Object)
            throws JSONException {
        StaffModel mStaffModel = new StaffModel();
        mStaffModel.setmId(Object.getString(JTAG_ID));
        mStaffModel.setmName(Object.getString(JTAG_TAB_NAME));
        mStaffModel.setmPhoto(Object.getString(JTAG_TAB_STAFF_PHOTO).replaceAll(" ","%20"));

        return mStaffModel;
    }

    public void showStudentList() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
        RecyclerView socialMediaList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())

        iconImageView.setImageResource(R.drawable.boy);


        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            iconImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.boy));
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button));

        } else {
            iconImageView.setBackground(mContext.getResources().getDrawable(R.drawable.boy));
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button));
        }

        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider_teal)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        ParentsEveningStudentAdapter socialMediaAdapter = new ParentsEveningStudentAdapter(mContext, mListViewArray);
        socialMediaList.setAdapter(socialMediaAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        socialMediaList.addOnItemTouchListener(new RecyclerItemListener(mContext, socialMediaList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        selectStudentImgView.setImageResource(R.drawable.student);
                        studentNameTV.setText(mListViewArray.get(position).getmName());
                        mStudentId= mListViewArray.get(position).getmId();
                        mStudentName= mListViewArray.get(position).getmName();
                        mClass= mListViewArray.get(position).getmClass();
                        staffRelative.setVisibility(View.INVISIBLE);
                        selectStaffImgView.setImageResource(R.drawable.addiconinparentsevng);
                        staffNameTV.setText("Staff Name:-");
                        next.setVisibility(View.GONE);
                        if (!mListViewArray.get(position).getmPhoto().equals("")) {

                            Glide.with(mContext).load(AppUtils.replace(mListViewArray.get(position).getmPhoto().toString())).asBitmap().placeholder(R.drawable.student).error(R.drawable.student).fitCenter().into(selectStudentImgView);
                        }
                        else

                        {

                            selectStudentImgView.setImageResource(R.drawable.student);
                        }
                        if(AppUtils.isNetworkConnected(mContext)) {
                            getStaffList(mListViewArray.get(position).getmId());
                        }else{
                            AppUtils.showDialogAlertDismiss(getActivity(),"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

                        }
                        dialog.dismiss();

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }
    public void showStaffList() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
        RecyclerView socialMediaList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())

        iconImageView.setImageResource(R.drawable.girl);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            iconImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.girl));
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button));

        } else {
            iconImageView.setBackground(mContext.getResources().getDrawable(R.drawable.girl));
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button));
        }

        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider_teal)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        ParentsEveningStaffAdapter socialMediaAdapter = new ParentsEveningStaffAdapter(mContext, mListViewStaffArray);
        socialMediaList.setAdapter(socialMediaAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        socialMediaList.addOnItemTouchListener(new RecyclerItemListener(mContext, socialMediaList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        selectStaffImgView.setImageResource(R.drawable.staff);
                        staffNameTV.setText(mListViewStaffArray.get(position).getmName());
                        mStaffId= mListViewStaffArray.get(position).getmId();
                        mStaffName= mListViewStaffArray.get(position).getmName();
                        if (!mListViewStaffArray.get(position).getmPhoto().equals("")) {

                            Glide.with(mContext).load(AppUtils.replace(mListViewStaffArray.get(position).getmPhoto().toString())).asBitmap().placeholder(R.drawable.staff).error(R.drawable.staff).fitCenter().into(selectStaffImgView);
                        }
                        else

                        {

                            selectStaffImgView.setImageResource(R.drawable.staff);
                        }
                        next.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

}
