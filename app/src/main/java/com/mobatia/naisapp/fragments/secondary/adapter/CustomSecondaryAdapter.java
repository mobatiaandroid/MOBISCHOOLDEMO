/**
 * 
 */
package com.mobatia.naisapp.fragments.secondary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.fragments.primary.model.PrimaryModel;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;

import java.util.ArrayList;


/**
 * @author Rijo K Jose
 * 
 */
public class CustomSecondaryAdapter extends BaseAdapter implements
		CacheDIRConstants, IntentPassValueConstants {

	private Context mContext;
	private ArrayList<String> mAboutusLists;
	private ArrayList<SecondaryModel> mPrimaryArrayList;
	private View view;
	private TextView mTitleTxt;
	private ImageView mImageView;
	private String mTitle;
	private String mTabId;

//	public CustomAboutUsAdapter(Context context,
//								ArrayList<AboutUsModel> arrList, String title, String tabId) {
//		this.mContext = context;
//		this.mAboutusList = arrList;
//		this.mTitle = title;
//		this.mTabId = tabId;
//	}
public CustomSecondaryAdapter(Context context,
							  ArrayList<String> arrList, String title, String tabId) {
	this.mContext = context;
	this.mAboutusLists = arrList;
	this.mTitle = title;
	this.mTabId = tabId;
}
//	public CustomAboutUsAdapter(Context context,
//								ArrayList<String> arrList) {
//		this.mContext = context;
//		this.mAboutusList = arrList;
//
//	}
	public CustomSecondaryAdapter(Context context,
								  ArrayList<SecondaryModel> arrList) {
		this.mContext = context;
		this.mPrimaryArrayList = arrList;

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPrimaryArrayList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mPrimaryArrayList.get(position);
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
			view = inflate.inflate(R.layout.custom_aboutus_list_adapter, null);
		} else {
			view = convertView;
		}
		try {
			mTitleTxt = (TextView) view.findViewById(R.id.listTxtTitle);
			mTitleTxt.setText(mPrimaryArrayList.get(position).getmName());
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

}
