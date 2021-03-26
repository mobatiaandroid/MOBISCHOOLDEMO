package com.mobatia.naisapp.fragments.about_us;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.about_us.adapter.FacilityRecyclerAdapterNew;
import com.mobatia.naisapp.fragments.about_us.model.AboutusModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 10/5/17.
 */
public class FacilityActivity extends Activity implements NaisClassNameConstants,JSONConstants,URLConstants,ResultConstants,StatusConstants {
    Bundle extras;
    ArrayList<AboutusModel> aboutusModelArrayList=new ArrayList<>();
    private Context mContext=this;
//    private GridView mAboutUsList;
    private RecyclerView mAboutUsList;
    private ImageView mBannerImage,sendEmail;
    TextView mTitleTextView,weburl,description,descriptionTitle;
    //	private CustomAboutUsAdapter mAdapter;
    private ArrayList<AboutusModel> mAboutUsListArray;
    RelativeLayout relMain;
    String desc,title,bannerimg;
    ImageView bannerImagePager;
//    ViewPager bannerImagePager;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ArrayList<String> bannerUrlImageArray = new ArrayList<>();
    String contactEmail;
    EditText text_dialog;
    EditText text_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_grid);
        extras=getIntent().getExtras();
        initUI();
    }

    private void initUI() {
        if(extras!=null){
        mAboutUsListArray=(ArrayList<AboutusModel>) extras
        .getSerializable("array");
            desc=extras.getString("desc");
            title=extras.getString("title");
            bannerimg=extras.getString("banner_image");
            System.out.println("Image url--"+bannerimg);
            if(!bannerimg.equals("")) {
                bannerUrlImageArray.add(bannerimg);
            }
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        bannerImagePager= (ImageView) findViewById(R.id.bannerImagePager);
//        bannerImagePager= (ViewPager) findViewById(R.id.bannerImagePager);
        headermanager = new HeaderManager(FacilityActivity.this, "Facilities");
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
//        mAboutUsList = (GridView) findViewById(R.id.mAboutUsListView);
        mAboutUsList = (RecyclerView) findViewById(R.id.mAboutUsListView);
        description= (TextView) findViewById(R.id.descriptionTV);
        descriptionTitle= (TextView) findViewById(R.id.descriptionTitle);
        if (desc.equalsIgnoreCase(""))
        {
            description.setVisibility(View.GONE);
            descriptionTitle.setVisibility(View.GONE);
        }else
        {
            description.setText(desc);
            descriptionTitle.setVisibility(View.GONE);
//			descriptionTitle.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
        }
        weburl= (TextView) findViewById(R.id.weburl);
        sendEmail= (ImageView) findViewById(R.id.sendEmail);
        sendEmail.setVisibility(View.GONE);

//		mBannerImage = (ImageView) mRootView.findViewById(R.id.bannerImageView);
        /*relMain = (RelativeLayout) findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

/* mAbsenceListView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mAbsenceListView.setLayoutManager(llm);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, 2);
        mAbsenceListView.addItemDecoration(itemDecoration);*/

        mAboutUsList.setHasFixedSize(true);
//        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
    /*    GridLayoutManager recyclerViewLayout= new GridLayoutManager(mContext, 4);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        mAboutUsList.addItemDecoration(itemDecoration);
        mAboutUsList.setLayoutManager(recyclerViewLayout);*/
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mAboutUsList.setLayoutManager(llm);
        mAboutUsList.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, 2);
        mAboutUsList.addItemDecoration(itemDecoration);
        if(!bannerimg.equals("")) {
//            bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, mContext));
            Glide.with(mContext).load(AppUtils.replace(bannerimg)).centerCrop().into(bannerImagePager);

        }
        FacilityRecyclerAdapterNew mFacilityRecyclerAdapter=new FacilityRecyclerAdapterNew(mContext,mAboutUsListArray);
        mAboutUsList.setAdapter(mFacilityRecyclerAdapter);

        mAboutUsList.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mAboutUsList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (mAboutUsListArray.size()<=1) {
                            if (mAboutUsListArray.get(position).getItemPdfUrl().endsWith(".pdf")) {
                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url","Facilities");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                                intent.putExtra("url", mAboutUsListArray.get(position).getItemPdfUrl());
                                intent.putExtra("tab_type", "Facilities");
                                startActivity(intent);
                            }
                        }
                        else
                        {
                           if (mAboutUsListArray.get(position).getItemPdfUrl().endsWith(".pdf")) {
                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url", mAboutUsListArray.get(position).getItemPdfUrl());
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                                intent.putExtra("url", mAboutUsListArray.get(position).getItemPdfUrl());
                                intent.putExtra("tab_type", "Facilities");
                                startActivity(intent);
                            }
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
//        mAboutUsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               if(mAboutUsListArray.get(position).getItemPdfUrl().endsWith(".pdf")){
//                   Intent intent = new Intent(mContext, PdfReaderActivity.class);
//                   intent.putExtra("pdf_url",mAboutUsListArray.get(position).getItemPdfUrl());
//                   startActivity(intent);
//               }
//               else
//               {
//                   Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
//                   intent.putExtra("url",mAboutUsListArray.get(position).getItemPdfUrl());
//                   intent.putExtra("tab_type",mAboutUsListArray.get(position).getItemTitle());
//                   startActivity(intent);
//               }
//            }
//        });
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
