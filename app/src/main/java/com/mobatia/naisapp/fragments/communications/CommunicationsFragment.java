/**
 *
 */
package com.mobatia.naisapp.fragments.communications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.coming_up.wholeschool.WholeSchoolActivity;
import com.mobatia.naisapp.activities.newsletters.NewsLettersCategoryListActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.communications.adapter.CommunicationsAdapter;
import com.mobatia.naisapp.fragments.communications.model.CommunicationModel;
import com.mobatia.naisapp.fragments.social_media.SocialMediaActivity;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Rijo K Jose
 */

public class CommunicationsFragment extends Fragment implements OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {

    private View mRootView;
    private Context mContext;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    TextView descriptionTV;
    private ImageView mBannerImage;
    static public ListView mListView;
    TextView mTitleTextView;
    private ArrayList<CommunicationModel> mListViewArray;
    ImageView bannerImagePager;
    //	ViewPager bannerImagePager;
    ArrayList<String> bannerUrlImageArray;
    CommunicationsAdapter mCommunicationsAdapter;
    ArrayList<String> dataArrayStrings = new ArrayList<String>() {
        {
            //add("Notices");
            add("Coming Up - Whole School ");
            add("Information");
            add("Newsletters ");
            add("Social Media");
            //add("Events");
        }
    };
    //			add("Parents' Association");
    //add("Chatter Box Café");

    public CommunicationsFragment() {

    }

    public CommunicationsFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     * android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_communications_list, container,
                false);
//		setHasOptionsMenu(true);
        mContext = getActivity();
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI();

        return mRootView;
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        descriptionTV = (TextView) mRootView.findViewById(R.id.descriptionTV);
        mTitleTextView.setText(COMMUNICATIONS);
        mListView = (ListView) mRootView.findViewById(R.id.mListView);
        bannerImagePager = (ImageView) mRootView.findViewById(R.id.bannerImagePager);
//		bannerImagePager= (ViewPager) mRootView.findViewById(R.id.bannerImagePager);
        relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mListViewArray = new ArrayList<>();
        for (int i = 0; i < dataArrayStrings.size(); i++) {
            CommunicationModel mCommunicationModel = new CommunicationModel();
            mCommunicationModel.setmId(String.valueOf(i));
            mCommunicationModel.setmFileName("");
            mCommunicationModel.setmTitle(dataArrayStrings.get(i));
            if (!PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
                mListViewArray.add(mCommunicationModel);
            } else {
                if (i != 4) {
                    mListViewArray.add(mCommunicationModel);

                }
            }
        }
        mCommunicationsAdapter = new CommunicationsAdapter(getActivity(), mListViewArray);
        mListView.setAdapter(mCommunicationsAdapter);
        //mListView.setAdapter(new CommunicationsAdapter(getActivity(), mListViewArray));
        if (AppUtils.checkInternet(mContext)) {

            getBanner();
        } else {
            //Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
//		mBannerImage = (ImageView) mRootView.findViewById(R.id.bannerImage);
        mListView.setOnItemClickListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (mListViewArray.get(position).getmId().equalsIgnoreCase("0")) {
            Intent intent = new Intent(mContext, WholeSchoolActivity.class);
            intent.putExtra("tab_type", "Coming Up - Whole School");
            mContext.startActivity(intent);
        } else if (mListViewArray.get(position).getmId().equalsIgnoreCase("1")) {
            Intent intent = new Intent(mContext, CommunicationInformationActivity.class);
            intent.putExtra("tab_type", mListViewArray.get(position).getmTitle());//COMMUNICATIONS//rijo changed to news letters
            mContext.startActivity(intent);
        } else if (mListViewArray.get(position).getmId().equalsIgnoreCase("2")) {
            Intent intent = new Intent(mContext, NewsLettersCategoryListActivity.class);
            intent.putExtra("tab_type", mListViewArray.get(position).getmTitle());//COMMUNICATIONS//rijo changed to news letters
            mContext.startActivity(intent);
        } else if (mListViewArray.get(position).getmId().equalsIgnoreCase("3")) {
            Intent intent = new Intent(mContext, SocialMediaActivity.class);
            intent.putExtra("tab_type", mListViewArray.get(position).getmTitle());//COMMUNICATIONS//rijo changed to news letters
            mContext.startActivity(intent);
        }
	/*else if(mListViewArray.get(position).getmId().equalsIgnoreCase("2")){
		Intent intent = new Intent(mContext, EventsListActivity.class);
		intent.putExtra("tab_type", COMMUNICATIONS);
		mContext.startActivity(intent);
	}
	else if(mListViewArray.get(position).getmId().equalsIgnoreCase("3")){
			Intent intent = new Intent(mContext, ParentsAssociationMainActivity.class);
			intent.putExtra("tab_type", COMMUNICATIONS);
			mContext.startActivity(intent);
		}*/
//		else if(mListViewArray.get(position).getmId().equalsIgnoreCase("4")){
//			Intent intent = new Intent(mContext, ChatterBoxActivity.class);
//			intent.putExtra("tab_type", mListViewArray.get(position).getmTitle());
//			mContext.startActivity(intent);
//		}
    }

    public void getBanner() {

        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_COMMUNICATION_BANNER);
            String[] name = new String[]{JTAG_ACCESSTOKEN, "users_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};


            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("The response is communication banner" + successResponse);


                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        String bannerImage = respObject.optString(JTAG_BANNER_IMAGE);
                                        String description = respObject.optString("description");
                                        if (!bannerImage.equalsIgnoreCase("")) {
                                            Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, getActivity()));
                                        } else {
//											bannerImagePager.setBackgroundResource(R.drawable.communications_banner);
                                            bannerImagePager.setBackgroundResource(R.drawable.default_banner);

                                        }
                                        if (description.equalsIgnoreCase("")) {
                                            descriptionTV.setVisibility(View.GONE);
                                        } else {
                                            descriptionTV.setVisibility(View.VISIBLE);
                                            descriptionTV.setText(description);

                                        }
                                        String whole_school_coming_up_badge = respObject.optString("whole_school_coming_up_badge");
                                        PreferenceManager.setCommunicationWholeSchooldBadge(mContext, whole_school_coming_up_badge);
                                        String whole_school_coming_up_edited_badge = respObject.optString("whole_school_coming_up_edited_badge");
                                        PreferenceManager.setCommunicationWholeSchoolEditedBadge(mContext, whole_school_coming_up_edited_badge);
                                        System.out.println("badge edited" + PreferenceManager.getCommunicationWholeSchoolEditedBadge(mContext));
                                        System.out.println("badge" + PreferenceManager.getCommunicationWholeSchoolBadge(mContext));
                                        mCommunicationsAdapter = new CommunicationsAdapter(getActivity(), mListViewArray);
                                        mCommunicationsAdapter.notifyDataSetChanged();
                                        mListView.setAdapter(mCommunicationsAdapter);


                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getBanner();

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {

        super.onResume();


		/*getBanner();
		CommunicationsAdapter mCommunicationsAdapter=new CommunicationsAdapter(getActivity(),mListViewArray);
		mCommunicationsAdapter.notifyDataSetChanged();
		mListView.setAdapter(mCommunicationsAdapter);*/
        System.out.println("On Resume");

        //


        if (AppController.isCommunicationDetailVisited) {

            getBanner();
           // initialiseUI();

            AppController.isCommunicationDetailVisited=false;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("On Stop");

    }
}
