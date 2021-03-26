/**
 * 
 */
package com.mobatia.naisapp.fragments.nas_today.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.fragments.nas_today.model.NasTodayModel;
import com.mobatia.naisapp.activities.nas_today.nas_today_detail.NasTodayDetailWebViewActivity;

import java.util.ArrayList;


/**
 * @author Rijo K Jose
 * 
 */
public class NasTodayAdapter extends BaseAdapter implements
		CacheDIRConstants, IntentPassValueConstants {

	private Context mContext;
//	private ArrayList<AboutUsModel> mAboutusList;
	private ArrayList<NasTodayModel> mNasTodayList;
	private View view;
	private TextView mTitleTxt;
	private ImageView mImageView;
	private String mTitle;
	private String mTabId;
	ViewHolder mViewHolder;
//	public CustomAboutUsAdapter(Context context,
//								ArrayList<AboutUsModel> arrList, String title, String tabId) {
//		this.mContext = context;
//		this.mAboutusList = arrList;
//		this.mTitle = title;
//		this.mTabId = tabId;
//	}
public NasTodayAdapter(Context context,
					   ArrayList<NasTodayModel> mNasTodayList, String title, String tabId) {
	this.mContext = context;
	this.mNasTodayList = mNasTodayList;
	this.mTitle = title;
	this.mTabId = tabId;
}
	public NasTodayAdapter(Context context,
						   ArrayList<NasTodayModel> arrList) {
		this.mContext = context;
		this.mNasTodayList = arrList;

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNasTodayList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mNasTodayList.get(position);
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
			 mViewHolder=new ViewHolder();
			convertView = inflate.inflate(R.layout.custom_nastoday_list_adapter, null);
			mViewHolder.listTxtTitle = (TextView) convertView.findViewById(R.id.listTxtTitle);
			mViewHolder.listTxtDate = (TextView) convertView.findViewById(R.id.listTxtDate);
			mViewHolder.listTxtDescription = (TextView) convertView.findViewById(R.id.listTxtDescription);
			mViewHolder.readMoreTextView = (TextView) convertView.findViewById(R.id.readMoreTextView);

			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			mViewHolder.listTxtTitle.setText(mNasTodayList.get(position).getTitle().trim());
			mViewHolder.listTxtDescription.setText(mNasTodayList.get(position).getDescription().trim());
			mViewHolder.listTxtDate.setText(mNasTodayList.get(position).getDay()+"-"+mNasTodayList.get(position).getMonth()+"-"+mNasTodayList.get(position).getYear()+" "+mNasTodayList.get(position).getTime());
			mViewHolder.readMoreTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String webViewComingUpDetail= "<!DOCTYPE html>\n" +
							"<html>\n" +
							"<head>\n" +
							"<style>\n" +
							"\n" +
							"@font-face {\n" +
							"font-family: SourceSansPro-Semibold;"+
							"src: url(SourceSansPro-Semibold.ttf);"+

							"font-family: SourceSansPro-Regular;"+
							"src: url(SourceSansPro-Regular.ttf);"+
							"}"+
							".title {"+
							"font-family: SourceSansPro-Regular;"+
							"font-size:16px;"+
							"text-align:left;"+
							"color:	#46C1D0;"+
							"text-align: ####TEXT_ALIGN####;"+
							"}"+
							".date {"+
					"font-family: SourceSansPro-Regular;"+
					"font-size:14px;"+
					"text-align:left;"+
					"color:	#46C1D0;"+
					"text-align: ####TEXT_ALIGN####;"+
							"}"+
							".description {"+
							"font-family: SourceSansPro-Light;"+
							"text-align:justify;"+
							"font-size:14px;"+
							"color: #000000;"+
							"text-align: ####TEXT_ALIGN####;"+
							"}"+
							"</style>\n"+"</head>"+
							"<body>"+
							"<p class='title'>"+mNasTodayList.get(position).getTitle()+"</p>";
					if (!mNasTodayList.get(position).getImage().equalsIgnoreCase("")) {
						webViewComingUpDetail = webViewComingUpDetail+"<p class='date'>"+mNasTodayList.get(position).getDay()+"-"+mNasTodayList.get(position).getMonth()+"-"+mNasTodayList.get(position).getYear()+
								" "+mNasTodayList.get(position).getTime() + "<center><img src='" + mNasTodayList.get(position).getImage() + "'width='100%', height='auto'>";
					}
					webViewComingUpDetail=webViewComingUpDetail+"</p><p class='description'>"+mNasTodayList.get(position).getDescription()+"</p>"+
							"</body>\n</html>";
					Intent mIntent = new Intent(mContext, NasTodayDetailWebViewActivity.class);
					mIntent.putExtra("webViewComingDetail", webViewComingUpDetail);
					mIntent.putExtra("pdf",  mNasTodayList.get(position).getPdf());
					mContext.startActivity(mIntent);
				}
			});
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	class ViewHolder
	{
		TextView listTxtTitle;
		TextView listTxtDate;
		TextView listTxtDescription;
		TextView readMoreTextView;

	}
}
