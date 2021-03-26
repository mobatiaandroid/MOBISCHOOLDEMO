package com.mobatia.naisapp.fragments.canteen_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen.CanteenFirstActivity;
import com.mobatia.naisapp.activities.canteen.CanteenFirstStaffActivity;
import com.mobatia.naisapp.activities.canteen_new.CanteenInfoActivity;
import com.mobatia.naisapp.activities.canteen_new.PreOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.PreOrderStaffActivity;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.activities.sports.calendar.CalendarActivity;
import com.mobatia.naisapp.activities.sports.information.InformationActivity;
import com.mobatia.naisapp.activities.sports.teams.TeamActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.fragments.sports.SportsActivity;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CanteenFragmentNew  extends Fragment implements AdapterView.OnItemClickListener,
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
    ImageView sendEmail;
    String  tab_type;
    TextView signUpModule;
    TextView descriptionTitle;
    ArrayList<String> bannerUrlImageArray;
    RelativeLayout mtitle;
    ImageView bannerImagePager;
    ArrayList<ParentAssociationEventsModel> parentAssociationEventsModelsArrayList=new ArrayList<>();
    LinearLayout paymentLinear,preOrderLinear,informationLinear,StaffinformationLinear,staffLinear,studeLinear,StaffpreOrderLinear;
    public CanteenFragmentNew() {

    }

    public CanteenFragmentNew(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_canteen_new, container,
                false);
        mContext = getActivity();
        TextView mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        mTitleTextView.setText("Lunch Box");


        preOrderLinear = (LinearLayout) mRootView.findViewById(R.id.preOrderLinear);
        informationLinear = (LinearLayout) mRootView.findViewById(R.id.informationLinear);
        paymentLinear = (LinearLayout) mRootView.findViewById(R.id.paymentLinear);
        //StaffinformationLinear,staffLinear,,StaffpreOrderLinear
        StaffinformationLinear = (LinearLayout) mRootView.findViewById(R.id.StaffinformationLinear);
        staffLinear = (LinearLayout) mRootView.findViewById(R.id.staffLinear);
        studeLinear = (LinearLayout) mRootView.findViewById(R.id.studeLinear);
        StaffpreOrderLinear = (LinearLayout) mRootView.findViewById(R.id.StaffpreOrderLinear);
        mtitle = (RelativeLayout) mRootView.findViewById(R.id.title);
        bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImagePager);
        signUpModule= (TextView) mRootView.findViewById(R.id.signUpModule);

        descriptionTitle= (TextView) mRootView.findViewById(R.id.descriptionTitle);
        sendEmail= (ImageView) mRootView.findViewById(R.id.sendEmail);
        if(AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI(URL_CANTEEN_BANNER);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
        if(PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            studeLinear.setVisibility(View.VISIBLE);
            staffLinear.setVisibility(View.GONE);
        }
        else
        {
            studeLinear.setVisibility(View.VISIBLE);
            staffLinear.setVisibility(View.GONE);
        }
        preOrderLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferenceManager.getUserId(mContext).equals(""))
                {
                    Intent intent = new Intent(mContext, PreOrderStaffActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }
                else
                {
                    Intent intent = new Intent(mContext, PreOrderActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }

            }
        });
        StaffpreOrderLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferenceManager.getUserId(mContext).equals(""))
                {
                    Intent intent = new Intent(mContext, PreOrderStaffActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }
                else
                {
                    Intent intent = new Intent(mContext, PreOrderActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }

            }
        });

        informationLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CanteenInfoActivity.class);
                intent.putExtra("tab_type", "Information");
                startActivity(intent);

            }
        });
        StaffinformationLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CanteenInfoActivity.class);
                intent.putExtra("tab_type", "Information");
                startActivity(intent);

            }
        });
        paymentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
                {
              //      AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Module under construction", R.drawable.exclamationicon, R.drawable.round);
                    Intent intent = new Intent(mContext, CanteenFirstStaffActivity.class);
                    intent.putExtra("tab_type","Payment");
                    intent.putExtra("email",contactEmail);
                    intent.putExtra("isFrom","payment");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(mContext, CanteenFirstActivity.class);
                    intent.putExtra("tab_type","Payment");
                    intent.putExtra("email",contactEmail);
                    intent.putExtra("isFrom","payment");
                    startActivity(intent);
                }



            }
        });
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
    private void callStaffDirectoryListAPI(String URL)
    {
        parentAssociationEventsModelsArrayList=new ArrayList<>();
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token"};
        String[] value={PreferenceManager.getAccessToken(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response of canteen" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            String bannerImage = secobj.optString(JTAG_BANNER_IMAGE);
                            description = secobj.optString("description");
                            contactEmail = secobj.optString(JTAG_CONTACT_EMAIL);
                            System.out.println("banner img---" + bannerImage);
                            if (!bannerImage.equalsIgnoreCase("")) {
                                System.out.println("banner img--- inside if" + bannerImage +"Description"+description+"   "+"Conatct Email"+contactEmail);
                                Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

                            } else {
                                System.out.println("banner img--- inside else" + bannerImage);
                                bannerImagePager.setBackgroundResource(R.drawable.default_banner);

                            }

                            if (description.equalsIgnoreCase("")&&contactEmail.equalsIgnoreCase(""))
                            {
                                System.out.println("banner img---1111");
                                mtitle.setVisibility(View.GONE);
                            }
                            else
                            {
                                System.out.println("banner img---22222");
                                mtitle.setVisibility(View.VISIBLE);
                            }
                            if (description.equalsIgnoreCase(""))
                            {
                                System.out.println("banner img--- 3333");

                                descriptionTitle.setVisibility(View.GONE);
                            }else
                            {
                                System.out.println("banner img--- 4444");
                                descriptionTitle.setText(description);
                                descriptionTitle.setVisibility(View.VISIBLE);

                                mtitle.setVisibility(View.VISIBLE);
                            }
                            System.out.println("contact email"+contactEmail);
                            if (contactEmail.equalsIgnoreCase(""))
                            {
                                System.out.println("banner img--- 5555");
                                sendEmail.setVisibility(View.GONE);
                            }else
                            {
                                System.out.println("banner img--- 66666");
                                mtitle.setVisibility(View.VISIBLE);

                                sendEmail.setVisibility(View.VISIBLE);
                            }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                //callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                // callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER);

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

}
