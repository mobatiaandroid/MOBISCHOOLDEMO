package com.mobatia.naisapp.activities.payment_history;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentStaffHistoryNew extends Activity implements URLConstants, JSONConstants, NaisClassNameConstants {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Context mContext;
    ImageView studImg;
    String stud_img="";
    Bundle extras;
    String studentNameStr = "";
    String studentClassStr = "";
    String studentIdStr = "";
    TextView studentName;
    LinearLayout mStudentSpinner;
    ArrayList<PaymentWalletHistoryModel> newsLetterModelArrayList = new ArrayList<>();
    RecyclerView mNewsLetterListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_staff_history);
        mContext = this;
        initUI();

    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            studentNameStr = extras.getString("studentName");
            studentIdStr = extras.getString("studentId");
            stud_img=extras.getString("studentImage");

        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
        studentName = (TextView) findViewById(R.id.studentName);
        studImg = (ImageView)findViewById(R.id.imagicon);
        mNewsLetterListView = (RecyclerView) findViewById(R.id.mListView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mNewsLetterListView.setLayoutManager(llm);
        mNewsLetterListView.setHasFixedSize(true);
        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        headermanager = new HeaderManager(PaymentStaffHistoryNew.this, "Payment History");
        headermanager.getHeader(relativeHeader, 0);

        back = headermanager.getLeftButton();

        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        if (AppUtils.isNetworkConnected(mContext)) {
            getEventsListApi(URL_GET_STAFF_WALLET_HISTORY,studentIdStr);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        studentName.setText(studentNameStr);
        if (!(stud_img.equals(""))) {

            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
        }
        else

        {

            studImg.setImageResource(R.drawable.boy);
        }
        mNewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(mContext, mNewsLetterListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(mContext, PaymentPrintActivity.class);
                        intent.putExtra("title", "Canteen Top-Up");
                        intent.putExtra("orderId", newsLetterModelArrayList.get(position).getKeycode());
                        intent.putExtra("invoice", newsLetterModelArrayList.get(position).getInvoice());
                        intent.putExtra("amount", newsLetterModelArrayList.get(position).getAmount());
                        intent.putExtra("paidby", newsLetterModelArrayList.get(position).getName());
                        intent.putExtra("paidDate", newsLetterModelArrayList.get(position).getDate_time());
                        intent.putExtra("tr_no", newsLetterModelArrayList.get(position).getTrn_no());
                        intent.putExtra("payment_type", newsLetterModelArrayList.get(position).getPayment_type());
                        if (newsLetterModelArrayList.get(position).getBill_no().equalsIgnoreCase(""))
                        {
                            intent.putExtra("billingCode", "--");

                        }
                        else {
                            intent.putExtra("billingCode", newsLetterModelArrayList.get(position).getBill_no());

                        }
                        mContext.startActivity(intent);

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        }


    private void getEventsListApi(String urlGetNewsletters, final String stud_id) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(urlGetNewsletters);
        String[] name={"access_token","staff_id", "users_id"};
        String[] value={PreferenceManager.getAccessToken(mContext),stud_id, PreferenceManager.getUserId(mContext)};
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
                            JSONArray data = secobj.getJSONArray("data");

                            if (data.length() > 0) {
                                newsLetterModelArrayList.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.optJSONObject(i);

                                    newsLetterModelArrayList.add(addEventsDetails(dataObject));
                                }
                                mNewsLetterListView.setVisibility(View.VISIBLE);
                                PaymentHistoryRecyclerAdapter newsLetterAdapter = new PaymentHistoryRecyclerAdapter(mContext, newsLetterModelArrayList);
                                mNewsLetterListView.setAdapter(newsLetterAdapter);
                            } else {
                                newsLetterModelArrayList.clear();
                                mNewsLetterListView.setVisibility(View.GONE);
                                PaymentHistoryRecyclerAdapter newsLetterAdapter = new PaymentHistoryRecyclerAdapter(mContext, newsLetterModelArrayList);
                                mNewsLetterListView.setAdapter(newsLetterAdapter);
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No data available", R.drawable.exclamationicon, R.drawable.round);

                            }
                        }
                        else
                        {
                            mNewsLetterListView.setVisibility(View.GONE);
                            AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_GET_STAFF_WALLET_HISTORY,stud_id);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_GET_STAFF_WALLET_HISTORY,stud_id);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_GET_STAFF_WALLET_HISTORY,stud_id);

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


            }
        });

    }
    private PaymentWalletHistoryModel addEventsDetails(JSONObject obj) {
        PaymentWalletHistoryModel model = new PaymentWalletHistoryModel();
        try {
            if (obj.has("id"))
            {
                model.setId(obj.optString("id"));
            }
            else
            {
                model.setId("");
            }
            if (obj.has("created_on"))
            {
                model.setDate_time(obj.optString("created_on"));
            }
            else
            {
                model.setDate_time("");
            }
            if (obj.has("firstname"))
            {
                model.setName(obj.optString("firstname"));
            }
            else
            {
                model.setName("");
            }
            if (obj.has("amount"))
            {
                model.setAmount(obj.optString("amount"));
            }
            else
            {
                model.setAmount("");
            }
            if (obj.has("status"))
            {
                model.setStatus(obj.optString("status"));
            }
            else
            {
                model.setStatus("");
            }
            if (obj.has("keycode"))
            {
                model.setKeycode(obj.optString("keycode"));
            }
            else
            {
                model.setKeycode("");
            }
            if (obj.has("bill_no"))
            {
                model.setBill_no(obj.optString("bill_no"));
            }
            else
            {
                model.setBill_no("");
            }
            if (obj.has("invoice_note"))
            {
                 model.setInvoice(obj.optString("invoice_note"));
            }
            else
            {
                model.setInvoice("");
            }
            if (obj.has("trn_no"))
            {
                 model.setTrn_no(obj.optString("trn_no"));
            }
            else
            {
                model.setTrn_no("");
            }
             if (obj.has("payment_type"))
            {
                 model.setPayment_type(obj.optString("payment_type"));
            }
            else
            {
                model.setPayment_type("");
            }

        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }
}
