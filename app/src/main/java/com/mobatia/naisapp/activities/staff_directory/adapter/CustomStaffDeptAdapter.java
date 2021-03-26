/**
 *
 */
package com.mobatia.naisapp.activities.staff_directory.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.home.adapter.ImagePagerDrawableAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.CustomDialog;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author Rijo K Jose
 *
 */
public class CustomStaffDeptAdapter extends BaseAdapter implements
        CacheDIRConstants, IntentPassValueConstants ,JSONConstants,URLConstants{


    private Context mContext;
    //	private ArrayList<AboutUsModel> mAboutusList;
    private ArrayList<StaffModel> mStaffList;
    private View view;
    private TextView departmentName,staffName;
    private ImageView mImageView,mail;
    private String mTitle;
    private String mTabId;
    LinearLayout deptLayout;
    int pos;
    EditText text_dialog,text_content;
    View separator;
    //private PopupWindow mPopupWindow;
     Dialog dialog;


    //	public CustomAboutUsAdapter(Context context,
//								ArrayList<AboutUsModel> arrList, String title, String tabId) {
//		this.mContext = context;
//		this.mAboutusList = arrList;
//		this.mTitle = title;
//		this.mTabId = tabId;
//	}
    public CustomStaffDeptAdapter(Context context,
                                       ArrayList<StaffModel> mStaffList, String title, String tabId) {
        this.mContext = context;
        this.mStaffList = mStaffList;
        this.mTitle = title;
        this.mTabId = tabId;
    }
    public CustomStaffDeptAdapter(Context context,
                                       ArrayList<StaffModel> arrList) {
        this.mContext = context;
        this.mStaffList = arrList;

    }
    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mStaffList.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mStaffList.get(position);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflate = LayoutInflater.from(mContext);
            view = inflate.inflate(R.layout.custom_adapter_stafflist_item, null);
        } else {
            view = convertView;
        }
        try {
            pos=position;
            departmentName = (TextView) view.findViewById(R.id.departmentName);
            staffName= (TextView) view.findViewById(R.id.staffName);
            deptLayout= (LinearLayout) view.findViewById(R.id.deptLayout);
            separator=(View)view.findViewById(R.id.separator);
            mail= (ImageView) view.findViewById(R.id.mailImage);
            if(mStaffList.get(position).getStaffDepartment().equals("")){
                deptLayout.setVisibility(View.GONE);

            }else {
                departmentName.setText(mStaffList.get(position).getStaffDepartment());
                separator.setVisibility(View.GONE);
            }
            System.out.println("name---"+mStaffList.get(position).getStaffName());
            staffName.setText(mStaffList.get(position).getStaffName());
            mail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert_send_email_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                    Button submitButton= (Button) dialog.findViewById(R.id.submitButton);
                     text_dialog= (EditText) dialog.findViewById(R.id.text_dialog);
                     text_content= (EditText) dialog.findViewById(R.id.text_content);
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                  /*  DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
                    int width = displayMetrics.widthPixels;
                    int height = displayMetrics.heightPixels;
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);
                    Double width = metrics.widthPixels*.9;
                    Double height = metrics.heightPixels*.999;
                    Window win = dialog.getWindow();
                    win.setLayout(width, height);*/

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
                            sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                        }
                    });


                    dialog.show();




            }
            });
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),"gayathri.v@mobatia.com",PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};

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
                            Toast toast = Toast.makeText(mContext,"Successfully sent email to staff", Toast.LENGTH_SHORT);
toast.show();
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
