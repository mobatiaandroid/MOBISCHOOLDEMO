package com.mobatia.naisapp.activities.staff_directory.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sona NR on 20/1/17.
 */
public class CustomStaffDeptRecyclerAdapter extends RecyclerView.Adapter<CustomStaffDeptRecyclerAdapter.MyViewHolder> implements CacheDIRConstants, IntentPassValueConstants,JSONConstants,URLConstants {

    private Context mContext;
    private ArrayList<StaffModel> mStaffList;
    String dept;
    EditText text_dialog,text_content;
    int pos;
     Dialog dialog;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView departmentName,staffName,staffRole;
        LinearLayout deptLayout;
        View separator;
        ImageView mail,staffImg;

        public MyViewHolder(View view) {
            super(view);

            departmentName = (TextView) view.findViewById(R.id.departmentName);
            staffName= (TextView) view.findViewById(R.id.staffName);
            deptLayout= (LinearLayout) view.findViewById(R.id.deptLayout);
            separator=(View)view.findViewById(R.id.separator);
            mail= (ImageView) view.findViewById(R.id.mailImage);
            staffImg= (ImageView) view.findViewById(R.id.staffImg);
            staffRole= (TextView) view.findViewById(R.id.staffRole);


        }
    }


    public CustomStaffDeptRecyclerAdapter(Context mContext,ArrayList<StaffModel> mStaffList,String dept) {
        this.mContext = mContext;
        this.mStaffList = mStaffList;
        this.dept=dept;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_stafflist_item, parent, false);
        dialog = new Dialog(mContext);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        pos=position;
System.out.println("Inside list item adapter---" + mStaffList.get(position).getStaffName());
        if(dept.equals("")){
            holder.deptLayout.setVisibility(View.GONE);
            holder.separator.setVisibility(View.GONE);

        }else if(dept.equals("list")){
           //holder.departmentName.setText(mStaffList.get(position).getStaffDepartment());
            holder.departmentName.setText(dept);
            holder.separator.setVisibility(View.GONE);
            holder.deptLayout.setVisibility(View.GONE);

        }else{
            //holder.departmentName.setText(dept);
            holder.deptLayout.setVisibility(View.GONE);
            holder.separator.setVisibility(View.GONE);

        }
//holder.staffImg.
if(!mStaffList.get(position).getStaffImage().equals("")){
    Picasso.with(mContext).load(AppUtils.replace(mStaffList.get(position).getStaffImage())).fit()
            .into(holder.staffImg, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
}
        holder.staffName.setText(mStaffList.get(position).getStaffName());
        holder.staffRole.setText(mStaffList.get(position).getRole());
        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click on mail--");
                if(!PreferenceManager.getUserId(mContext).equals("")) {
                     dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.alert_send_email_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                    Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                    Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
                    text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
                    text_content = (EditText) dialog.findViewById(R.id.text_content);
                    // text_dialog.setSelection(0);
                    //text_content.setSelection(0);
                    text_dialog.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                text_dialog.setHint("");
                                text_dialog.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                                text_dialog.setPadding(5, 5, 0, 0);
                            } else {
                                text_dialog.setHint("Enter your subject here...");

                                text_dialog.setGravity(Gravity.CENTER);

                            }
                        }
                    });
                    text_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                text_content.setGravity(Gravity.LEFT);
                            } else {
                                text_content.setGravity(Gravity.CENTER);

                            }
                        }
                    });
                    dialogCancelButton.setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            dialog.dismiss();

                        }

                    });

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("submit btn clicked");
                           /* if (AppUtils.isNetworkConnected(mContext)) {
                                if (text_content.equals("")) {
                                    AppUtils.setErrorForEditText(text_content, mContext.getString(R.string.mandatory_field));
                                } else if (text_dialog.equals("")) {
                                    AppUtils.setErrorForEditText(text_dialog, mContext.getString(R.string.mandatory_field));

                                } else {
                                    sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }*/
                            if (text_dialog.getText().equals("")) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Please enter your subject", R.drawable.exclamationicon, R.drawable.round);

                            } else if (text_content.getText().toString().equals("")) {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Please enter your content", R.drawable.exclamationicon, R.drawable.round);

                            } else {
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                            }

                        }
                    });

                    dialog.show();
                  ;

                }else{
                AppUtils.showDialogAlertDismiss((Activity)mContext,mContext.getString(R.string.alert_heading),"This feature is available for registered users.",R.drawable.exclamationicon,R.drawable.round);

            }

            }
        });


    }


    @Override
    public int getItemCount() {
        return mStaffList.size();
    }

    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};

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
                            /*Toast toast = Toast.makeText(mContext,"Successfully sent email to staff", Toast.LENGTH_SHORT);
                            toast.show();*/
                            dialog.dismiss();
                            AppUtils.showDialogAlertDismiss((Activity)mContext,"Success","Successfully sent email to staff",R.drawable.tick,R.drawable.round);

                        }else{
                            Toast toast = Toast.makeText(mContext,"Email not sent", Toast.LENGTH_SHORT);
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
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
                AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

            }
        });


    }

}
