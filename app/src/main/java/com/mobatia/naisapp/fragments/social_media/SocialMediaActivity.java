package com.mobatia.naisapp.fragments.social_media;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.web_view.FullscreenWebViewActivityNoHeader;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.social_media.adapter.SocialMediaAdapter;
import com.mobatia.naisapp.fragments.social_media.model.SocialMediaModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SocialMediaActivity  extends Activity implements AdapterView.OnItemClickListener,
        NaisTabConstants,CacheDIRConstants, URLConstants,
        IntentPassValueConstants,NaisClassNameConstants,JSONConstants,View.OnClickListener {
    TextView mTitleTextView;
    Bundle extras;

    private Context mContext;
    private ListView mAboutUsList;
    private String mTitle;
    private String mTabId;
    private RelativeLayout mProgressDialog;
    private ImageView mBannerImage;
    private ImageView facebook, twitter, instagram;
    String type;
    //	private CustomAboutUsAdapter mAdapter;
    private ArrayList<SocialMediaModel> mSocialMediaArraylistFacebook = new ArrayList<>();
    private ArrayList<SocialMediaModel> mSocialMediaArraylistTwitter = new ArrayList<>();
    private ArrayList<SocialMediaModel> mSocialMediaArraylistInstagram = new ArrayList<>();
    private ArrayList<SocialMediaModel> mSocialMediaArray = new ArrayList<>();
    //ViewPager bannerImagePager;
    ImageView bannerImagePager;
    ArrayList<String> bannerUrlImageArray;
    String tab_type = "Social Media";
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView back;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_social_media);
        mContext=this;
        initialiseUI();

        if (AppUtils.isNetworkConnected(mContext)) {
            callSocialMediaListAPI(URL_GET_SOCIALMEDIA_LIS);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }
    private void initialiseUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            tab_type =extras.getString("tab_type");
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(SocialMediaActivity.this, tab_type);
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
        facebook= (ImageView)findViewById(R.id.facebookButton);
        twitter= (ImageView)findViewById(R.id.twitterButton);
        instagram= (ImageView)findViewById(R.id.instagramButton);
        bannerImagePager= (ImageView)findViewById(R.id.bannerImageViewPager);
        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram.setOnClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    public void showSocialmediaList(ArrayList<SocialMediaModel> mSocialMediaArray, final String type){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_social_media_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView= (ImageView) dialog.findViewById(R.id.iconImageView);
        RecyclerView socialMediaList= (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())
        if(type.equals("facebook")) {
            iconImageView.setImageResource(R.drawable.facebookiconmedia);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                iconImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundfb));
                dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.buttonfb));

            } else {
                iconImageView.setBackground(mContext.getResources().getDrawable(R.drawable.roundfb));
                dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.buttonfb));

            }
        }else if(type.equals("twitter")){
            iconImageView.setImageResource(R.drawable.twittericon);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                iconImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundtw));
                dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.buttontwi));

            } else {
                iconImageView.setBackground(mContext.getResources().getDrawable(R.drawable.roundtw));
                dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.buttontwi));

            }
        }else{
            iconImageView.setImageResource(R.drawable.instagramicon);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                iconImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.roundins));
                dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.buttonins));

            } else {
                iconImageView.setBackground(mContext.getResources().getDrawable(R.drawable.roundins));
                dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.buttonins));

            }
        }
        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider_teal)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        SocialMediaAdapter socialMediaAdapter=new SocialMediaAdapter(mContext,mSocialMediaArray);
        socialMediaList.setAdapter(socialMediaAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        socialMediaList.addOnItemTouchListener(new RecyclerItemListener(mContext, socialMediaList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if(type.equals("facebook")){
//						Intent i = new Intent(Intent.ACTION_VIEW);
//						i.setData(Uri.parse(mSocialMediaArraylistFacebook.get(position).getUrl()));
//						startActivity(i);
                            Intent mintent = new Intent(mContext, FullscreenWebViewActivityNoHeader.class);
                            mintent.putExtra("url",mSocialMediaArraylistFacebook.get(position).getUrl());
                            startActivity(mintent);
                        }else if(type.equals("twitter")){
//						Intent i = new Intent(Intent.ACTION_VIEW);
//						i.setData(Uri.parse(mSocialMediaArraylistTwitter.get(position).getUrl()));
//						startActivity(i);
                            Intent mintent = new Intent(mContext, FullscreenWebViewActivityNoHeader.class);
                            mintent.putExtra("url",mSocialMediaArraylistTwitter.get(position).getUrl());
                            startActivity(mintent);
                        }else if(type.equals("instagram")){
//						Intent i = new Intent(Intent.ACTION_VIEW);
//						i.setData(Uri.parse(mSocialMediaArraylistInstagram.get(position).getUrl()));
//						startActivity(i);
                            Intent mintent = new Intent(mContext, FullscreenWebViewActivityNoHeader.class);
                            mintent.putExtra("url",mSocialMediaArraylistInstagram.get(position).getUrl());
                            startActivity(mintent);					}
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

    private void callSocialMediaListAPI(String URL) {
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
                            String bannerImage=secobj.optString(JTAG_BANNER_IMAGE);
                            if (!bannerImage.equalsIgnoreCase("")) {
                                Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

//								bannerUrlImageArray = new ArrayList<>();
//								bannerUrlImageArray.add(bannerImage);
//								bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray,mContext));
                            }
                            else
                            {
                                bannerImagePager.setBackgroundResource(R.drawable.socialbanner);

                            }
                            JSONArray data = secobj.getJSONArray("data");
                            mSocialMediaArraylistInstagram.clear();
                            mSocialMediaArraylistFacebook.clear();
                            mSocialMediaArraylistTwitter.clear();
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    SocialMediaModel socialMediaModel=new SocialMediaModel();
                                    socialMediaModel.setId(dataObject.optString(JTAG_ID));
                                    socialMediaModel.setUrl(dataObject.optString(JTAG_URL));
                                    socialMediaModel.setTab_type(dataObject.optString(JTAG_TAB_TYPE));
                                    socialMediaModel.setImage(dataObject.optString(JTAG_IMAGE));
                                    if(dataObject.optString(JTAG_TAB_TYPE).contains("Facebook")) {
                                        mSocialMediaArraylistFacebook.add(socialMediaModel);
                                    }else if(dataObject.optString(JTAG_TAB_TYPE).contains("Twitter")){
                                        mSocialMediaArraylistTwitter.add(socialMediaModel);
                                        //mSocialMediaArray=mSocialMediaArraylistTwitter;
                                    }else if(dataObject.optString(JTAG_TAB_TYPE).contains("Instagram")){
                                        mSocialMediaArraylistInstagram.add(socialMediaModel);
                                        //mSocialMediaArray=mSocialMediaArraylistInstagram;
                                    }

                                }

                                //mSocialMediaArray=mSocialMediaArraylistFacebook;


                            } else {
                                Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
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
                        callSocialMediaListAPI(URL_GET_SOCIALMEDIA_LIS);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSocialMediaListAPI(URL_GET_SOCIALMEDIA_LIS);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSocialMediaListAPI(URL_GET_SOCIALMEDIA_LIS);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
                AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

            }
        });


    }

    @Override
    public void onClick(View v) {
        if((v==facebook)){
            type="facebook";
			/*if(AppUtils.isNetworkConnected(mContext)) {
				callSocialMediaListAPI(URL_GET_SOCIALMEDIA_LIS,type);
			}else{
				AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

			}*/if(mSocialMediaArraylistFacebook.size()==1){
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(mSocialMediaArraylistFacebook.get(0).getUrl()));
//				startActivity(i);
                Intent mintent = new Intent(mContext, FullscreenWebViewActivityNoHeader.class);
                mintent.putExtra("url", mSocialMediaArraylistFacebook.get(0).getUrl());
                startActivity(mintent);			}else {
                showSocialmediaList(mSocialMediaArraylistFacebook, type);

            }

        }else if(v==twitter){
            type="twitter";
            if(mSocialMediaArraylistTwitter.size()==1) {
                Intent mintent = new Intent(mContext, FullscreenWebViewActivityNoHeader.class);
                mintent.putExtra("url", mSocialMediaArraylistTwitter.get(0).getUrl());
                startActivity(mintent);
            }else{
                showSocialmediaList(mSocialMediaArraylistTwitter, type);

            }
			/*if(AppUtils.isNetworkConnected(mContext)) {
				callSocialMediaListAPI(URL_GET_SOCIALMEDIA_LIS,type);
			}else{
				AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

			}*/
        }else if(v==instagram){
            type="instagram";
            if(mSocialMediaArraylistInstagram.size()==1) {
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(mSocialMediaArraylistInstagram.get(0).getUrl()));
//				startActivity(i);
                Intent mintent = new Intent(mContext, FullscreenWebViewActivityNoHeader.class);
                mintent.putExtra("url", mSocialMediaArraylistInstagram.get(0).getUrl());
                startActivity(mintent);
            }else{
                showSocialmediaList(mSocialMediaArraylistInstagram, type);

            }
			/*if(AppUtils.isNetworkConnected(mContext)) {
				callSocialMediaListAPI(URL_GET_SOCIALMEDIA_LIS,type);
			}else{
				AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

			}*/
        }
    }
}
