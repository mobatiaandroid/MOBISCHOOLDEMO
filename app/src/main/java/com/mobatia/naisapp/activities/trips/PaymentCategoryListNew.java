package com.mobatia.naisapp.activities.trips;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.report.adapter.StudentSpinnerAdapter;
import com.mobatia.naisapp.fragments.report.model.ReportModel;
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

public class PaymentCategoryListNew extends Activity implements JSONConstants, ResultConstants, StatusConstants, URLConstants {
    Context mContext = this;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type;
    String email;
    RelativeLayout relativeHeader;
    ArrayList<ReportModel> studentsModelArrayList;
    TextView studentName;
    TextView textViewYear;
    static String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    String stud_img = "";
    String section = "";
    LinearLayout mStudentSpinner;
    ImageView studImg;
    RecyclerView mNewsLetterListView;
    ArrayList<CategoryModel> newsLetterModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_payment_category);
        initUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI(URL_GET_STUDENT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }
    public void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            email = extras.getString("email");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(PaymentCategoryListNew.this, tab_type);
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
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        PreferenceManager.setStudIdForCCA(mContext, "");
        PreferenceManager.setCCAStudentIdPosition(mContext, "0");
        mStudentSpinner = findViewById(R.id.studentSpinner);
        studentName = findViewById(R.id.studentName);
        studImg = findViewById(R.id.imagicon);
        textViewYear =findViewById(R.id.textViewYear);
        mNewsLetterListView = (RecyclerView) findViewById(R.id.mListView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mNewsLetterListView.setLayoutManager(llm);
        mNewsLetterListView.setHasFixedSize(true);
        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialmediaList(studentsModelArrayList);
            }
        });
        mNewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mNewsLetterListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position)

                    {
                        if(newsLetterModelArrayList.get(position).getIs_fee_payment().equalsIgnoreCase("1"))
                        {
                            Intent intent = new Intent(PaymentCategoryListNew.this, PaymentDetailFeeOnlyActivityNew.class);
                            intent.putExtra("categoryId",newsLetterModelArrayList.get(position).getId());
                            intent.putExtra("category_name",newsLetterModelArrayList.get(position).getCategory_name());
                            intent.putExtra("invoice_description",newsLetterModelArrayList.get(position).getInvoice_description());
                            intent.putExtra("studentId",stud_id);
                            intent.putExtra("amount",newsLetterModelArrayList.get(position).getAmount());
                            intent.putExtra("is_paid",newsLetterModelArrayList.get(position).getIs_paid());
                            intent.putExtra("tab_type",newsLetterModelArrayList.get(position).getCategory_name());
                            intent.putExtra("paid_amount",newsLetterModelArrayList.get(position).getPaid_amount());
                            intent.putExtra("order_id",newsLetterModelArrayList.get(position).getOrder_id());
                            intent.putExtra("payment_date",newsLetterModelArrayList.get(position).getPayment_date());
                            intent.putExtra("invoice_note",newsLetterModelArrayList.get(position).getInvoice_note());
                            intent.putExtra("isams_no",newsLetterModelArrayList.get(position).getBilling_code());
                            intent.putExtra("parent_name",newsLetterModelArrayList.get(position).getParent_name());
                            intent.putExtra("formated_amount",newsLetterModelArrayList.get(position).getFormated_amount());
                            intent.putExtra("trn_no",newsLetterModelArrayList.get(position).getTrn_no());
                            intent.putExtra("invoice_no",newsLetterModelArrayList.get(position).getInvoice_no());
                            intent.putExtra("payment_type",newsLetterModelArrayList.get(position).getPayment_type());
                            intent.putExtra("studentName",newsLetterModelArrayList.get(position).getStudentName());
                            intent.putExtra("current",newsLetterModelArrayList.get(position).getCurrent());
                            intent.putExtra("vat_percentage",newsLetterModelArrayList.get(position).getVat_percentage());
                            intent.putExtra("vat_amount",newsLetterModelArrayList.get(position).getVat_amount());

                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(PaymentCategoryListNew.this, TripListActivity.class);
                            intent.putExtra("categoryId",newsLetterModelArrayList.get(position).getId());
                            intent.putExtra("studentId",stud_id);
                            intent.putExtra("email",email);
                            intent.putExtra("tab_type",newsLetterModelArrayList.get(position).getCategory_name());
                            startActivity(intent);
                        }


                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }
    private void getStudentsListAPI(final String URLHEAD) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list" + successResponse);
                try {
                    studentsModelArrayList = new ArrayList<>();
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    studentsModelArrayList.add(addStudentDetails(dataObject));
                                }
                                if (PreferenceManager.getStudIdForCCA(mContext).equalsIgnoreCase("")) {
                                    studentName.setText(studentsModelArrayList.get(0).getmName());
                                    stud_name = studentsModelArrayList.get(0).getmName();
                                    stud_class = studentsModelArrayList.get(0).getmClass();
                                    stud_img = studentsModelArrayList.get(0).getmPhoto();
                                    section = studentsModelArrayList.get(0).getmSection();
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    } else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }


                                    textViewYear.setText("Class : " + studentsModelArrayList.get(0).getmClass());

                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0");
                                    int studentSelectPosition = Integer.valueOf(PreferenceManager.getCCAStudentIdPosition(mContext));

                                    stud_id = studentsModelArrayList.get(studentSelectPosition).getmId();


                                } else {

                                    int studentSelectPosition = Integer.valueOf(PreferenceManager.getCCAStudentIdPosition(mContext));

                                    studentName.setText(studentsModelArrayList.get(studentSelectPosition).getmName());
                                    stud_id = studentsModelArrayList.get(studentSelectPosition).getmId();
                                    stud_name = studentsModelArrayList.get(studentSelectPosition).getmName();
                                    stud_class = studentsModelArrayList.get(studentSelectPosition).getmClass();
                                    stud_img = studentsModelArrayList.get(studentSelectPosition).getmPhoto();
                                    System.out.println("selected student image" + studentsModelArrayList.get(studentSelectPosition).getmPhoto());
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    } else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }
                                    textViewYear.setText("Class : " + studentsModelArrayList.get(studentSelectPosition).getmClass());


                                }
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    getEventsListApi(URL_PAYMENT_CATEGORY,stud_id);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            } else {
                                Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else {


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
    private ReportModel addStudentDetails(JSONObject dataObject) {
        ReportModel studentModel = new ReportModel();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));

        return studentModel;
    }
    private void getEventsListApi(String urlGetNewsletters, final String stud_id) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(urlGetNewsletters);
        String[] name={"access_token","student_id", "users_id"};
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
                                PaymentCategoryRecyclerAdapter newsLetterAdapter = new PaymentCategoryRecyclerAdapter(mContext, newsLetterModelArrayList);
                                mNewsLetterListView.setAdapter(newsLetterAdapter);
                            } else {
                                mNewsLetterListView.setVisibility(View.GONE);
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No data available", R.drawable.exclamationicon, R.drawable.round);

                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_PAYMENT_CATEGORY,stud_id);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_PAYMENT_CATEGORY,stud_id);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_PAYMENT_CATEGORY,stud_id);

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
    private CategoryModel addEventsDetails(JSONObject obj) {
        CategoryModel model = new CategoryModel();
        try {

            model.setId(obj.optString("id"));
            model.setCategory_name(obj.optString("category_name"));
            model.setStatus(obj.optString("status"));
            model.setIs_fee_payment(obj.optString("is_fee_payment"));
            model.setUpdateCount(obj.optString("updateCount"));
            model.setNewCount(obj.optString("newCount"));
            model.setInvoice_description(obj.optString("invoice_description"));
            model.setAmount(obj.optString("amount"));
            model.setIs_paid(obj.optString("is_paid"));
            model.setPayment_id(obj.optString("payment_id"));
            model.setPayment_type(obj.optString("payment_type"));
            model.setRemaining_amount(obj.optString("remaining_amount"));
            model.setPaid_amount(obj.optString("paid_amount"));
            model.setPayment_date(obj.optString("payment_date"));
            model.setOrder_id(obj.optString("order_id"));
            model.setStudentName(obj.optString("studentName"));
            model.setIsams_no(obj.optString("isams_no"));
            model.setInvoice_note(obj.optString("invoice_note"));
            model.setParent_name(obj.optString("parent_name"));
            model.setFormated_amount(obj.optString("formated_amount"));
            model.setTrn_no(obj.optString("trn_no"));
            PreferenceManager.setTrnNo(mContext,obj.optString("trn_no"));
            model.setInvoice_no(obj.optString("invoice_no"));
            model.setBilling_code(obj.optString("billing_code"));
            model.setCurrent(obj.optString("current"));
            model.setVat_amount(obj.optString("vat_amount"));
            model.setVat_percentage(obj.optString("vat_percentage"));
        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }

    public void showSocialmediaList(final ArrayList<ReportModel> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView socialMediaList = dialog.findViewById(R.id.recycler_view_social_media);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button));

        } else {
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button));

        }
        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider_teal)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        StudentSpinnerAdapter studentAdapter = new StudentSpinnerAdapter(mContext, mStudentArray);
        socialMediaList.setAdapter(studentAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener()
        {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        socialMediaList.addOnItemTouchListener(new RecyclerItemListener(mContext, socialMediaList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        dialog.dismiss();
                        studentName.setText(mStudentArray.get(position).getmName());
                        stud_id = mStudentArray.get(position).getmId();
                        stud_name = mStudentArray.get(position).getmName();
                        stud_class = mStudentArray.get(position).getmClass();
                        stud_img = mStudentArray.get(position).getmPhoto();
                        section = mStudentArray.get(position).getmSection();
                        textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        } else

                        {

                            studImg.setImageResource(R.drawable.boy);
                        }
                        if (AppUtils.isNetworkConnected(mContext)) {
                            getEventsListApi(URL_PAYMENT_CATEGORY,stud_id);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }


                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppUtils.isNetworkConnected(mContext)) {
            getEventsListApi(URL_PAYMENT_CATEGORY,stud_id);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }
}
