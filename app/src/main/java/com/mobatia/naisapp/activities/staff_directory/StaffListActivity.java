package com.mobatia.naisapp.activities.staff_directory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.staff_directory.adapter.CustomStaffDeptRecyclerAdapter;
import com.mobatia.naisapp.activities.staff_directory.adapter.StaffAdapterAdapterNew;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gayatri on 17/3/17.
 */
public class StaffListActivity extends Activity implements URLConstants, JSONConstants {
    private Context mContext;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    //ListView mStaffListView;
    private RecyclerView mStaffListView;
    LinearLayout linearSearch;
    EditText searchEditText;
    ImageView btnImgsearch;
    Bundle extras;
    String category_id, title;
    ArrayList<StaffModel> mStaffDeptfilteredList;
    ArrayList<StaffModel> mStaffDeptList;
    ArrayList<String> filtereddeptArrayList;
    ArrayList<String> deptArrayList;

    HashMap<String, ArrayList<StaffModel>> hashmap =
            new HashMap<String, ArrayList<StaffModel>>();
    HashMap<String, ArrayList<StaffModel>> filteredHashMap =
            new HashMap<String, ArrayList<StaffModel>>();
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            callStaffListAPI(URL_GETSTAFFLDEPT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            category_id = extras.getString("category_id");
            title = extras.getString("title");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        linearSearch = (LinearLayout) findViewById(R.id.linearSearch);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        btnImgsearch = (ImageView) findViewById(R.id.btnImgsearch);
        mStaffListView = (RecyclerView) findViewById(R.id.mStaffListView);
        mStaffListView.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mStaffListView.setLayoutManager(llm);
        headermanager = new HeaderManager(StaffListActivity.this, title);
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
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.isEditTextFocused(StaffListActivity.this)) {
                    AppUtils.hideKeyBoard(mContext);
                }
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        filtereddeptArrayList = new ArrayList<String>();
        mStaffDeptfilteredList = new ArrayList<>();
        mStaffDeptList = new ArrayList<>();
        deptArrayList = new ArrayList<String>();
        filteredHashMap =
                new HashMap<String, ArrayList<StaffModel>>();
        btnImgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deptArrayList.size() > 0) {
                    filtereddeptArrayList = new ArrayList<>();
                    filteredHashMap = new HashMap<>();

                    for (int i = 0; i < deptArrayList.size(); i++) {
                        mStaffDeptfilteredList = new ArrayList<>();

                        for (int j = 0; j < hashmap.get(deptArrayList.get(i)).size(); j++) {
                            if (deptArrayList.get(i)
                                    .toLowerCase().contains(searchEditText.getText().toString().toLowerCase()) || hashmap.get(deptArrayList.get(i)).get(j).getStaffName()
                                    .toLowerCase().contains(searchEditText.getText().toString().toLowerCase())) {
                                if (!(filtereddeptArrayList.contains(deptArrayList.get(i)))) {
                                    filtereddeptArrayList.add(deptArrayList.get(i));
//                                filteredHashMap.put(deptArrayList.get(i),hashmap.get(deptArrayList.get(i)));
                                }
                                if (!(mStaffDeptfilteredList.contains(hashmap.get(deptArrayList.get(i)).get(j)))) {
                                    mStaffDeptfilteredList.add(hashmap.get(deptArrayList.get(i)).get(j));
                                }
                                filteredHashMap.put(deptArrayList.get(i), mStaffDeptfilteredList);
                            }
                        }
                    }

                    StaffAdapterAdapterNew customStaffDeptAdapter = new StaffAdapterAdapterNew(mContext, filtereddeptArrayList, filteredHashMap);
                    mStaffListView.setAdapter(customStaffDeptAdapter);
                    if (searchEditText.getText().toString().equalsIgnoreCase("")) {

                        StaffAdapterAdapterNew customStaffDeptAdapters = new StaffAdapterAdapterNew(mContext, deptArrayList, hashmap);
                        mStaffListView.setAdapter(customStaffDeptAdapters);
                    }

                } else if (mStaffDeptList.size() > 0) {
                    mStaffDeptfilteredList = new ArrayList<>();
                    for (int i = 0; i < mStaffDeptList.size(); i++) {
                        if (mStaffDeptList.get(i).getStaffName()
                                .toLowerCase().contains(searchEditText.getText().toString().toLowerCase())) {
                            mStaffDeptfilteredList.add(mStaffDeptList.get(i));
                        }
                    }
                    CustomStaffDeptRecyclerAdapter customStaffDeptAdapter = new CustomStaffDeptRecyclerAdapter(mContext, mStaffDeptfilteredList, "");
                    mStaffListView.setAdapter(customStaffDeptAdapter);
                    if (searchEditText.getText().toString().equalsIgnoreCase("")) {

                        CustomStaffDeptRecyclerAdapter customStaffDeptAdapters = new CustomStaffDeptRecyclerAdapter(mContext, mStaffDeptList, "");
                        mStaffListView.setAdapter(customStaffDeptAdapters);
                    }

                }
                AppUtils.hideKeyBoard(mContext);

            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // TODO Auto-generated method stub

                if (deptArrayList.size() > 0) {
                    filtereddeptArrayList = new ArrayList<>();
                    filteredHashMap = new HashMap<>();

                    for (int i = 0; i < deptArrayList.size(); i++) {
                        mStaffDeptfilteredList = new ArrayList<>();

                        for (int j = 0; j < hashmap.get(deptArrayList.get(i)).size(); j++) {
                            if (deptArrayList.get(i).toLowerCase().contains(s.toString().toLowerCase()) ||
                                    hashmap.get(deptArrayList.get(i)).get(j).getStaffName().toLowerCase().contains(s.toString().toLowerCase())) {
                                if (!(filtereddeptArrayList.contains(deptArrayList.get(i)))) {
                                    filtereddeptArrayList.add(deptArrayList.get(i));
                                }
                                if (!(mStaffDeptfilteredList.contains(hashmap.get(deptArrayList.get(i)).get(j))))
                                {
                                    mStaffDeptfilteredList.add(hashmap.get(deptArrayList.get(i)).get(j));

                                }
                                filteredHashMap.put(deptArrayList.get(i), mStaffDeptfilteredList);
                              /*  filtereddeptArrayList.add(deptArrayList.get(i));
                                mStaffDeptfilteredList.add(hashmap.get(deptArrayList.get(i)).get(j));

                                filteredHashMap.put(deptArrayList.get(i), mStaffDeptfilteredList);*/
                            }
                        }
                    }
                    StaffAdapterAdapterNew customStaffDeptAdapter = new StaffAdapterAdapterNew(mContext, filtereddeptArrayList, filteredHashMap);
                    mStaffListView.setAdapter(customStaffDeptAdapter);
                    if (searchEditText.getText().toString().equalsIgnoreCase("")) {
                        StaffAdapterAdapterNew customStaffDeptAdapters = new StaffAdapterAdapterNew(mContext, deptArrayList, hashmap);
                        mStaffListView.setAdapter(customStaffDeptAdapters);
                    }

                } else if (mStaffDeptList.size() > 0) {
                    mStaffDeptfilteredList = new ArrayList<>();
                    for (int i = 0; i < mStaffDeptList.size(); i++) {
                        if (mStaffDeptList.get(i).getStaffName()
                                .toLowerCase().contains(s.toString().toLowerCase())) {
                            mStaffDeptfilteredList.add(mStaffDeptList.get(i));
                        }
                    }
                    CustomStaffDeptRecyclerAdapter customStaffDeptAdapter = new CustomStaffDeptRecyclerAdapter(mContext, mStaffDeptfilteredList, "");
                    mStaffListView.setAdapter(customStaffDeptAdapter);
                    if (searchEditText.getText().toString().equalsIgnoreCase("")) {

                        CustomStaffDeptRecyclerAdapter customStaffDeptAdapters = new CustomStaffDeptRecyclerAdapter(mContext, mStaffDeptList, "");
                        mStaffListView.setAdapter(customStaffDeptAdapters);
                    }

                }
//                for (int k=0;k<hashmap.size();k++)
//                {
//                if (hashmap.get(deptArrayList.get(k)).size() > 0) {
//                    filtered = new ArrayList<StaffModel>();
//                    for (int i = 0; i < deptArrayList.size(); i++) {
//                        if (deptArrayList.get(i).getSports_name()
//                                .toLowerCase().contains(s.toString().toLowerCase())) {
//                            filtered.add(deptArrayList.get(i));
//                        }
//                    }
//                }
//                    StaffAdapterAdapterNew sportsEventListAdapters = new StaffAdapterAdapterNew(mContext, filtered);
//                    mSportListView.setAdapter(sportsEventListAdapters);
//                    if (searchEditText.getText().toString()
//                            .equalsIgnoreCase("")) {
//                        StaffAdapterAdapterNew sportsEventListAdapter=new StaffAdapterAdapterNew(mContext,mSportsModelArrayList);
//                        mSportListView.setAdapter(sportsEventListAdapter);
//                    }
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void callStaffListAPI(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "staffcategory_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), category_id};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            deptArrayList = new ArrayList<String>();
                            JSONObject dataObject = secobj.getJSONObject("data");
                            JSONArray staffArray = dataObject.getJSONArray("staffs");
                            JSONArray depArray = dataObject.getJSONArray("departments");
                            if (depArray.length() > 0) {
                                String depObject = "";

                                for (int i = 0; i < depArray.length(); i++) {

                                    depObject = depArray.getString(i);
                                    deptArrayList.add(depObject);

                                }

                                for (int j = 0; j < deptArrayList.size(); j++) {


                                    JSONObject staffObject = staffArray.getJSONObject(j);

                                    JSONArray keyArray = staffObject.getJSONArray(deptArrayList.get(j));
                                    System.out.println("staffdept----name--1--" + j + "---" + keyArray);
                                    mStaffDeptList = new ArrayList<>();
                                    if (keyArray.length() > 0) {
                                        for (int l = 0; l < keyArray.length(); l++) {

                                            JSONObject keyObj = keyArray.getJSONObject(l);
                                            mStaffDeptList.add(addStaffDeptDetails(keyObj));
                                        }

                                    }/*else{
                                        deptArrayList.remove(j);
                                    }*/
                                    //if(mStaffDeptList.size()>0) {
                                    hashmap.put(deptArrayList.get(j), mStaffDeptList);
                                    // }

                                }

                                /*StaffAdapterAdapterNew customStaffDeptAdapter = new StaffAdapterAdapterNew(mContext, deptArrayList,mStaffDeptList);
                                mStaffListView.setAdapter(customStaffDeptAdapter);*/
                                if (hashmap.size() == 1 && mStaffDeptList.size() == 0) {
                                    AppUtils.showDialogAlertFinish((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.no_details_available), R.drawable.exclamationicon, R.drawable.round);

                                } else {
                                    StaffAdapterAdapterNew customStaffDeptAdapter = new StaffAdapterAdapterNew(mContext, deptArrayList, hashmap);
                                    mStaffListView.setAdapter(customStaffDeptAdapter);
                                }
                            } else {
                                mStaffDeptList = new ArrayList<>();
                                for (int j = 0; j < staffArray.length(); j++) {
                                    JSONObject staffObject = staffArray.getJSONObject(j);

                                    mStaffDeptList.add(addStaffDeptDetails(staffObject));


                                }

                                CustomStaffDeptRecyclerAdapter customStaffDeptAdapter = new CustomStaffDeptRecyclerAdapter(mContext, mStaffDeptList, "");
                                mStaffListView.setAdapter(customStaffDeptAdapter);

                            }

                               /*CustomStaffDirectoryAdapter customStaffDirectoryAdapter = new CustomStaffDirectoryAdapter(mContext, mStaffDirectoryListArray);
                                mStaffDirectoryList.setAdapter(customStaffDirectoryAdapter);*/
                           /* } else {
                                Toast.makeText(StaffListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffListAPI(URL_GETSTAFFLDEPT_LIST);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffListAPI(URL_GETSTAFFLDEPT_LIST);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffListAPI(URL_GETSTAFFLDEPT_LIST);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                                , getResources().getString(R.string.ok));
                        dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
               /* CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                        , getResources().getString(R.string.ok));
                dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    /*  private StaffModel addStaffDeptDetails(JSONObject obj,String dep) {
          StaffModel model = new StaffModel();
          try {
              model.setStaffId(obj.optString("id"));
              model.setStaffName(obj.optString("name"));
              model.setStaffDepartment(dep);
              model.setStaffPhoneNo(obj.optString("phone"));
              model.setStaffAbout(obj.getString("about"));
              model.setStaffEmail(obj.optString("email"));
              model.setStaffContactNo(obj.optString("contact"));
          } catch (Exception ex) {
              System.out.println("Exception is" + ex);
          }

          return model;
      }
  */
    private StaffModel addStaffDeptDetails(JSONObject obj) {
        StaffModel model = new StaffModel();
        try {
            model.setStaffId(obj.optString("id"));
            model.setStaffName(obj.optString("name"));
            model.setStaffPhoneNo(obj.optString("phone"));
            model.setStaffAbout(obj.getString("about"));
            model.setStaffEmail(obj.optString("email"));
            model.setRole(obj.optString("role"));
            model.setStaffContactNo(obj.optString("contact"));
            model.setStaffImage(obj.optString("staff_photo"));
        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }
}
