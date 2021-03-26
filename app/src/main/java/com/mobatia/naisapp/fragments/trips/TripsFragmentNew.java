package com.mobatia.naisapp.fragments.trips;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.activities.trips.PaymentCategoryListNew;
import com.mobatia.naisapp.activities.trips.PaymentInformationActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TripsFragmentNew extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    private String mTitle;
    private String mTabId;
    Context mContext;
    private View mRootView;
    Bundle extras;
    TextView text_content;
    TextView text_dialog;
    String description="";
    String contactEmail="";
    RelativeLayout signRelative;
    ImageView back,home,imageViewSlotInfo,sendEmail;
    //RecyclerView mRecyclerView;
    String  tab_type;
    TextView signUpModule;
    TextView descriptionTV;
    TextView descriptionTitle;
    ArrayList<String> bannerUrlImageArray;
    RelativeLayout mtitle,informationRelative;
    ImageView bannerImagePager;
    ArrayList<ParentAssociationEventsModel> parentAssociationEventsModelsArrayList=new ArrayList<>();
    public TripsFragmentNew() {

    }

    public TripsFragmentNew(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_payment_new, container,
                false);
        mContext = getActivity();
        TextView mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        mTitleTextView.setText("Payments");


        signRelative = (RelativeLayout) mRootView.findViewById(R.id.paymentRelative);
        mtitle = (RelativeLayout) mRootView.findViewById(R.id.title);
        informationRelative = (RelativeLayout) mRootView.findViewById(R.id.informationRelative);
        bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImageViewPager);
       // mRecyclerView= (RecyclerView) mRootView.findViewById(R.id.mStaffDirectoryListView);
        signUpModule= (TextView) mRootView.findViewById(R.id.signUpModule);
        descriptionTV= (TextView) mRootView.findViewById(R.id.descriptionTV);
        descriptionTitle= (TextView) mRootView.findViewById(R.id.descriptionTitle);
        sendEmail= (ImageView) mRootView.findViewById(R.id.sendEmail);
       // mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
       // mRecyclerView.addItemDecoration(itemDecoration);
       // mRecyclerView.setLayoutManager(llm);
       // mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        if(AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI(URL_PAYMENT_BANNER);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
        signRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PaymentCategoryListNew.class);
                intent.putExtra("tab_type","Categories");
                intent.putExtra("email",contactEmail);
                startActivity(intent);

            }
        });
        informationRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PaymentInformationActivity.class);
                intent.putExtra("informationArrayList",parentAssociationEventsModelsArrayList);
                startActivity(intent);

            }
        });
        /*mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(mContext, mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (parentAssociationEventsModelsArrayList.size() > 0)
                        {
                            if (parentAssociationEventsModelsArrayList.get(position).getPdfUrl().endsWith(".pdf")) {
                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url", parentAssociationEventsModelsArrayList.get(position).getPdfUrl());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                                intent.putExtra("url", parentAssociationEventsModelsArrayList.get(position).getPdfUrl());
                                intent.putExtra("tab_type", tab_type);
                                startActivity(intent);
                            }

                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));*/
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_send_email_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
                text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
                text_content = (EditText) dialog.findViewById(R.id.text_content);


                dialogCancelButton.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                     //   AppUtils.hideKeyBoard(mContext);
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0);
                        dialog.dismiss();

                    }

                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                    }
                });


                dialog.show();


            }
        });
        return mRootView;
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private void initialiseUI() {
        if (extras != null) {
        }
    }

    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),contactEmail,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
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
                            Toast toast = Toast.makeText(mContext, "Successfully sent email to staff", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);


                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex)
                {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }
    private void callStaffDirectoryListAPI(String URL) {
        parentAssociationEventsModelsArrayList=new ArrayList<>();
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token"};
        String[] value={PreferenceManager.getAccessToken(mContext)};
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
                            String bannerImage = secobj.optString(JTAG_BANNER_IMAGE);
                            description = secobj.optString(JTAG_DESCRIPTION);
                            contactEmail = secobj.optString(JTAG_CONTACT_EMAIL);
                            System.out.println("banner img---" + bannerImage);
                            JSONObject canteenPayment=secobj.getJSONObject("payment_gateway_credentials");
                            PreferenceManager.setMerchantId(mContext,canteenPayment.optString("merchant_id"));
                            PreferenceManager.setAuthId(mContext,canteenPayment.optString("auth_id"));
                            PreferenceManager.setSessionUrl(mContext,canteenPayment.optString("session_url"));
                            PreferenceManager.setPaymentUrl(mContext,canteenPayment.optString("payment_url"));
                            PreferenceManager.setMerchantName(mContext,canteenPayment.optString("merchant_name"));
                            PreferenceManager.setMerchantPassword(mContext,canteenPayment.optString("password"));
                            System.out.println("values in api"+canteenPayment.optString("merchant_id"));
                            if (!bannerImage.equalsIgnoreCase("")) {
//                                bannerUrlImageArray = new ArrayList<>();
//                                bannerUrlImageArray.add(bannerImage);
//                                bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, mContext));
                                Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

                            } else {
                                bannerImagePager.setBackgroundResource(R.drawable.pabanner);

                            }
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    ParentAssociationEventsModel parentAssociationEventsModel=new ParentAssociationEventsModel();
                                    parentAssociationEventsModel.setPdfId(dataObject.optString("id"));
                                    parentAssociationEventsModel.setPdfTitle(dataObject.optString("title"));
                                    parentAssociationEventsModel.setPdfUrl(dataObject.optString("file"));
                                    parentAssociationEventsModelsArrayList.add(parentAssociationEventsModel);
                                }

                              //  PaymentRecyclerAdapter adapter=new PaymentRecyclerAdapter(mContext,parentAssociationEventsModelsArrayList);
                              //  mRecyclerView.setAdapter(adapter);

                            } else {
                                //Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
                            }
                            if (description.equalsIgnoreCase("")&&contactEmail.equalsIgnoreCase(""))
                            {
                                mtitle.setVisibility(View.GONE);
                            }
                            else
                            {
                                mtitle.setVisibility(View.VISIBLE);
                            }
                            if (description.equalsIgnoreCase(""))
                            {
                                descriptionTV.setVisibility(View.GONE);
                                descriptionTitle.setVisibility(View.GONE);
                            }else
                            {
                                descriptionTV.setText(description);
                                descriptionTitle.setVisibility(View.GONE);
                                descriptionTV.setVisibility(View.VISIBLE);
                                mtitle.setVisibility(View.VISIBLE);
                            }
                            System.out.println("contact email"+contactEmail);
                            if (contactEmail.equalsIgnoreCase(""))
                            {
                                sendEmail.setVisibility(View.GONE);
                            }else
                            {
                                mtitle.setVisibility(View.VISIBLE);

                                sendEmail.setVisibility(View.VISIBLE);
                            }


                        }
                    }
                    else if (response_code.equalsIgnoreCase("500"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                    else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffDirectoryListAPI(URL_PAYMENT_BANNER);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                //callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_PAYMENT_BANNER);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                // callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_PAYMENT_BANNER);

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
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }
    class PaymentRecyclerAdapter extends RecyclerView.Adapter<PaymentRecyclerAdapter.MyViewHolder> {

        private Context mContext;
        private ArrayList<ParentAssociationEventsModel> mnNewsLetterModelArrayList;
        String dept;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageIcon;
            TextView pdfTitle;
            public MyViewHolder(View view) {
                super(view);
                imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
                pdfTitle = (TextView) view.findViewById(R.id.pdfTitle);



            }
        }


        public PaymentRecyclerAdapter(Context mContext, ArrayList<ParentAssociationEventsModel> mnNewsLetterModelArrayList) {
            this.mContext = mContext;
            this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_pdf_adapter_row_new, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
            holder.pdfTitle.setText(mnNewsLetterModelArrayList.get(position).getPdfTitle());
            holder.imageIcon.setVisibility(View.GONE);


        }


        @Override
        public int getItemCount() {
            return mnNewsLetterModelArrayList.size();
        }

    }

}
