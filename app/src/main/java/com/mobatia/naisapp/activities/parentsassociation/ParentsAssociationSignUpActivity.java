package com.mobatia.naisapp.activities.parentsassociation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 12/5/17.
 */
public class ParentsAssociationSignUpActivity extends Activity implements URLConstants,JSONConstants,NaisClassNameConstants,IntentPassValueConstants {
    Context mContext=this;
    RelativeLayout relativeHeader;
    RelativeLayout signRelative;
    HeaderManager headermanager;
    ImageView back,home,imageViewSlotInfo,sendEmail;
    RecyclerView mRecyclerView;
    Bundle extras;
    String  tab_type;
    TextView signUpModule;
    TextView descriptionTV;
    TextView descriptionTitle;
    ArrayList<String> bannerUrlImageArray;
    RelativeLayout mtitle;
    ImageView bannerImagePager;
//    ViewPager bannerImagePager;
    TextView text_content;
    TextView text_dialog;
    String description="";
    String contactEmail="";
    ArrayList<ParentAssociationEventsModel> parentAssociationEventsModelsArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_association_firstpage);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI(URL_PARENTS_ASSOCIATION);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            tab_type=extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        signRelative = (RelativeLayout) findViewById(R.id.signRelative);
        mtitle = (RelativeLayout) findViewById(R.id.title);
//        bannerImagePager= (ViewPager) findViewById(R.id.bannerImageViewPager);
        bannerImagePager= (ImageView) findViewById(R.id.bannerImageViewPager);
        mRecyclerView= (RecyclerView) findViewById(R.id.mStaffDirectoryListView);
        signUpModule= (TextView) findViewById(R.id.signUpModule);
        descriptionTV= (TextView) findViewById(R.id.descriptionTV);
        descriptionTitle= (TextView) findViewById(R.id.descriptionTitle);
        sendEmail= (ImageView) findViewById(R.id.sendEmail);
        headermanager = new HeaderManager(ParentsAssociationSignUpActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        //mRecyclerView.setHasFixedSize(true);
       /* GridLayoutManager recyclerViewLayout= new GridLayoutManager(mContext, 4);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(recyclerViewLayout);*/
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
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
 /*      signUpModule.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(PreferenceManager.getIsFirstTimeInPA(mContext)){
                   PreferenceManager.setIsFirstTimeInPA(mContext,false);
                   Intent mintent = new Intent(mContext, PTAinfoActivity.class);
                   mintent.putExtra(TYPE, 1);
                   startActivity(mintent);
               }else{
                   Intent intent = new Intent(mContext, ParentsAssociationListActivity.class);
                   intent.putExtra("tab_type",PARENTS_ASSOCIATION);
                   mContext.startActivity(intent);
               }
           }
       });*/
        signRelative.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

                   Intent intent = new Intent(mContext, ParentsAssociationListActivity.class);
                   intent.putExtra("tab_type","Volunteer Sign Up");
                   startActivity(intent);

           }
       });
        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (parentAssociationEventsModelsArrayList.size() > 0) {
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
                }));
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
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                    }
                });


                dialog.show();


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

                                ParentsAssociationAdapter adapter=new ParentsAssociationAdapter(mContext,parentAssociationEventsModelsArrayList);
                                mRecyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(ParentsAssociationSignUpActivity.this, "No data found", Toast.LENGTH_SHORT).show();
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
//			descriptionTitle.setVisibility(View.VISIBLE);
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
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffDirectoryListAPI(URL_PARENTS_ASSOCIATION);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                //callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_PARENTS_ASSOCIATION);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                               // callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_PARENTS_ASSOCIATION);

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
class ParentsAssociationAdapter extends RecyclerView.Adapter<ParentsAssociationAdapter.MyViewHolder> {

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


    public ParentsAssociationAdapter(Context mContext, ArrayList<ParentAssociationEventsModel> mnNewsLetterModelArrayList) {
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
     /*   if (mnNewsLetterModelArrayList.get(position).getPdfUrl().endsWith(".pdf")) {
            holder.imageIcon.setBackgroundResource(R.drawable.pdfdownloadbutton);
        }
        else
        {
            holder.imageIcon.setBackgroundResource(R.drawable.webcontentviewbutton);

        }*/

    }


    @Override
    public int getItemCount() {
        return mnNewsLetterModelArrayList.size();
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
                } catch (Exception ex) {
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


}
