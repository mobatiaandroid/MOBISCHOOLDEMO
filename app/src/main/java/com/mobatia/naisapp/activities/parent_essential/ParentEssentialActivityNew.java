package com.mobatia.naisapp.activities.parent_essential;

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
import com.mobatia.naisapp.activities.parent_essential.adapter.ParentEssentialActivityListAdapterNew;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderNextActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parent_essentials.model.ParentEssentialsModel;
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
 * Created by gayatri on 23/3/17.
 */
public class ParentEssentialActivityNew extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants,NaisClassNameConstants {
    Bundle extras;
    ArrayList<ParentEssentialsModel> list;
    String tab_type;
    String tab_typeName;
    Context mContext=this;
    RecyclerView mNewsLetterListView;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ImageView mailImageView;
    TextView descriptionTV;
    TextView descriptionTitle;
    ImageView bannerImagePager;
//    ViewPager bannerImagePager;
    ArrayList<String> bannerUrlImageArray;
    String bannerImage="";
    String contactEmail="";
    String description="";
    EditText text_content;
    EditText text_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_parent_essential_sublist_new);
        initUI();
        setListAdapter();

    }

    private void setListAdapter() {
        ParentEssentialActivityListAdapterNew newsDetailListAdapter=new ParentEssentialActivityListAdapterNew(mContext,list);
        mNewsLetterListView.setAdapter(newsDetailListAdapter);
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            list = (ArrayList<ParentEssentialsModel>) extras.getSerializable("submenuArray");
            tab_type=extras.getString("tab_type");
            bannerImage=extras.getString("bannerImage");
            contactEmail=extras.getString("contactEmail");
            description=extras.getString("description");
            tab_typeName=extras.getString("tab_typeName");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        descriptionTV = (TextView) findViewById(R.id.descriptionTV);
        descriptionTitle = (TextView) findViewById(R.id.descriptionTitle);
        mailImageView = (ImageView) findViewById(R.id.mailImageView);
        bannerImagePager= (ImageView) findViewById(R.id.bannerImagePager);
//        bannerImagePager= (ViewPager) findViewById(R.id.bannerImagePager);
        mNewsLetterListView = (RecyclerView) findViewById(R.id.mListView);
        headermanager = new HeaderManager(ParentEssentialActivityNew.this, tab_type);
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
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        mNewsLetterListView.setLayoutManager(llm);

        if (!bannerImage.equalsIgnoreCase("")) {
//            bannerUrlImageArray = new ArrayList<>();
//            bannerUrlImageArray.add(bannerImage);
//            bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray,mContext));
            Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

        }
        else
        {
            bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//            bannerImagePager.setBackgroundResource(R.drawable.demo);

        }
        if (!description.equalsIgnoreCase(""))
        {
            descriptionTV.setVisibility(View.VISIBLE);
            descriptionTV.setText(description);
            descriptionTitle.setVisibility(View.GONE);
//			descriptionTitle.setVisibility(View.VISIBLE);
        }
        else
        {
            descriptionTV.setVisibility(View.GONE);
            descriptionTitle.setVisibility(View.GONE);

        }
        if (!contactEmail.equalsIgnoreCase("") && !PreferenceManager.getUserId(mContext).equals(""))
        {
            mailImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            mailImageView.setVisibility(View.GONE);

        }
        mailImageView.setOnClickListener(new View.OnClickListener() {
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

        mNewsLetterListView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        mNewsLetterListView.addItemDecoration(itemDecoration);
        mNewsLetterListView.setLayoutManager(llm);
        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        mNewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mNewsLetterListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (list.size()<=1) {
                            if (list.get(position).getFilename().endsWith(".pdf")) {
                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url", list.get(position).getFilename());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                                intent.putExtra("url", list.get(position).getFilename());
                                intent.putExtra("tab_type", tab_type);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            if (list.get(position).getFilename().endsWith(".pdf") && tab_typeName.equalsIgnoreCase("NAS lunch box menu")) {
                                Intent intent = new Intent(mContext, PdfReaderNextActivity.class);
                                intent.putExtra("position", position);
                                intent.putExtra("submenuArray", list);
                                startActivity(intent);
                            }
                            else  if (list.get(position).getFilename().endsWith(".pdf")) {
                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url", list.get(position).getFilename());
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                                intent.putExtra("url", list.get(position).getFilename());
                                intent.putExtra("tab_type", tab_type);
                                startActivity(intent);
                            }
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
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