/**
 *
 */
package com.mobatia.naisapp.fragments.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.photos.PhotosRecyclerViewActivity;
import com.mobatia.naisapp.activities.videos.VideosRecyclerViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.gallery.adapter.GalleryPhotosRecyclerviewAdapter;
import com.mobatia.naisapp.fragments.gallery.adapter.GalleryVideoRecyclerviewAdapter;
import com.mobatia.naisapp.fragments.gallery.model.PhotosListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author RIJO K JOSE
 *
 */

public class GalleryFragment extends Fragment implements
		NaisTabConstants,CacheDIRConstants,
		URLConstants, StatusConstants, JSONConstants, NameValueConstants,NaisClassNameConstants  {
	TextView mTitleTextView;
	TextView moreImage,moreVideo;
	ImageView photoImageIcon,videoImageIcon;
	RecyclerView viewGridPhotos;
	RecyclerView viewGridVideo;

	private View mRootView;
	private Context mContext;
	private ListView mAboutUsList;
	private String mTitle;
	private String mTabId;
	private RelativeLayout relMain;
	private ImageView mBannerImage;
	ArrayList<PhotosListModel> mPhotosModelArrayList;
	ArrayList<PhotosListModel> mVideoModelArrayList;
	GalleryPhotosRecyclerviewAdapter mPhotosRecyclerviewAdapter;
	GalleryVideoRecyclerviewAdapter mVideosRecyclerviewAdapter;
	RecyclerView.LayoutManager recyclerViewLayoutManager;
	RecyclerView.LayoutManager recyclerViewLayoutManagerVideo;

	public GalleryFragment() {

	}

	public GalleryFragment(String title, String tabId) {
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
		mRootView = inflater.inflate(R.layout.fragment_gallery, container,
				false);
//		setHasOptionsMenu(true);
		mContext = getActivity();
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
		initialiseUI();
//		GetAboutUsListAsyncTask aboutUsTask = new GetAboutUsListAsyncTask(
//				URL_ABOUTUS_LIST, ABOUT_US_DIR, 1, mTabId);
//		aboutUsTask.execute();
		return mRootView;
	}
	/*******************************************************
	 * Method name : initialiseUI Description : initialise UI elements
	 * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
	 * Surendranath
	 *****************************************************/
	private void initialiseUI() {
		mTitleTextView= (TextView) mRootView.findViewById(R.id.titleTextView);
		moreImage= (TextView) mRootView.findViewById(R.id.moreImage);
		moreVideo= (TextView) mRootView.findViewById(R.id.moreVideo);
		videoImageIcon= (ImageView) mRootView.findViewById(R.id.videoImageIcon);
		photoImageIcon= (ImageView) mRootView.findViewById(R.id.photoImageIcon);
		viewGridPhotos= (RecyclerView) mRootView.findViewById(R.id.viewGridPhotos);
		viewGridVideo= (RecyclerView) mRootView.findViewById(R.id.viewGridVideo);
		mTitleTextView.setText(GALLERY);
		relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
		relMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		viewGridPhotos.setHasFixedSize(true);
		recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
		recyclerViewLayoutManagerVideo = new GridLayoutManager(mContext, 3);
		int spacing = 4; // 50px
		ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
		viewGridPhotos.addItemDecoration(itemDecoration);
		viewGridVideo.addItemDecoration(itemDecoration);
		viewGridPhotos.setLayoutManager(recyclerViewLayoutManager);
		viewGridVideo.setHasFixedSize(true);
		viewGridVideo.setLayoutManager(recyclerViewLayoutManagerVideo);
		viewGridPhotos.addOnItemTouchListener(new RecyclerItemListener(mContext, viewGridPhotos,
				new RecyclerItemListener.RecyclerTouchListener() {
					public void onClickItem(View v, int position) {
						Intent intent = new Intent(mContext, PhotosRecyclerViewActivity.class);
						intent.putExtra("photo_id", mPhotosModelArrayList.get(position).getPhotoId());
						startActivity(intent);
					}

					public void onLongClickItem(View v, int position) {
					}
				}));
		viewGridVideo.addOnItemTouchListener(new RecyclerItemListener(mContext, viewGridVideo,
				new RecyclerItemListener.RecyclerTouchListener() {
					public void onClickItem(View v, int position) {
						Intent intent = new Intent(mContext, VideosRecyclerViewActivity.class);
						intent.putExtra("video_id", mVideoModelArrayList.get(position).getPhotoId());
						startActivity(intent);
					}

					public void onLongClickItem(View v, int position) {
					}
				}));

//		LinearLayoutManager llm = new LinearLayoutManager(mContext);
//		llm.setOrientation(LinearLayoutManager.VERTICAL);
//		viewGridPhotos.setLayoutManager(llm);
		if (AppUtils.isNetworkConnected(mContext)) {
			photosListApiCall();
		} else {
			AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
		}
		moreImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(mContext, PhotosRecyclerViewActivity.class);
				mContext.startActivity(mIntent);
			}
		});
		photoImageIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(mContext, PhotosRecyclerViewActivity.class);
				mContext.startActivity(mIntent);
			}
		});
		moreVideo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(mContext, VideosRecyclerViewActivity.class);
				mContext.startActivity(mIntent);
			}
		});
		videoImageIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(mContext, VideosRecyclerViewActivity.class);
				mContext.startActivity(mIntent);
			}
		});
	}

	private void photosListApiCall() {
		try {
			mPhotosModelArrayList = new ArrayList<PhotosListModel>();
			mVideoModelArrayList = new ArrayList<PhotosListModel>();
			String[] name = {NAME_ACCESS_TOKEN};
			String[] values = {PreferenceManager.getAccessToken(mContext)};
			final VolleyWrapper manager = new VolleyWrapper(URL_GET_THUM_NAIL_IMAGE_LIST);
			manager.getResponsePOST(mContext, 11, name, values,
					new VolleyWrapper.ResponseListener() {

						@Override
						public void responseSuccess(String successResponse) {
							if (successResponse != null) {
								try {
									System.out.println("successResponse::" + successResponse);
									JSONObject rootObject = new JSONObject(successResponse);
									String responseCode = rootObject.getString(JTAG_RESPONSECODE);
									if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
										JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
										String statusCode = responseObject.getString(JTAG_STATUSCODE);
										if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {

											JSONArray data = responseObject.optJSONArray(JTAG_IMAGE);
											for (int i = 0; i < data.length(); i++) {
												JSONObject imageDetail = data.optJSONObject(i);
												PhotosListModel mPhotosModel = new PhotosListModel();
												mPhotosModel.setPhotoId(imageDetail.optString(JTAG_ID));
												mPhotosModel.setPhotoUrl(imageDetail.optString(JTAG_THUMBNAIL_IMAGE));
												mPhotosModelArrayList.add(mPhotosModel);
											}
											JSONArray dataVideo = responseObject.optJSONArray(JTAG_VIDEO);
											for (int i = 0; i < dataVideo.length(); i++) {
												JSONObject imageDetail = dataVideo.optJSONObject(i);
												PhotosListModel mPhotosModel = new PhotosListModel();
												mPhotosModel.setPhotoId(imageDetail.optString(JTAG_ID));
												mPhotosModel.setPhotoUrl(imageDetail.optString(JTAG_THUMBNAIL_IMAGE));
												mVideoModelArrayList.add(mPhotosModel);
											}
											mPhotosRecyclerviewAdapter = new GalleryPhotosRecyclerviewAdapter(mContext, mPhotosModelArrayList);
											viewGridPhotos.setAdapter(mPhotosRecyclerviewAdapter);
											mVideosRecyclerviewAdapter = new GalleryVideoRecyclerviewAdapter(mContext, mVideoModelArrayList);
											viewGridVideo.setAdapter(mVideosRecyclerviewAdapter);
										} else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
												statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
											AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
												@Override
												public void getAccessToken() {
												}
											});
											photosListApiCall();

										} else {
											AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

										}

									}else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
											responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
											responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
										AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
											@Override
											public void getAccessToken() {
											}
										});
										photosListApiCall();

									} else {
										AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
									}

								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}

						@Override
						public void responseFailure(String failureResponse) {
							AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);
						}

					});
		} catch (Exception e) {
			// CustomStatusDialog(RESPONSE_FAILURE);
			e.printStackTrace();
		}
	}


}
