/**
 * 
 */
package com.mobatia.naisapp.fragments.communications.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.term_calendar.model.TermsCalendarModel;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.fragments.communications.model.CommunicationModel;
import com.mobatia.naisapp.manager.PreferenceManager;

import java.util.ArrayList;


/**
 * @author Rijo K Jose
 * 
 */
public class CommunicationsAdapter extends BaseAdapter implements
		CacheDIRConstants, IntentPassValueConstants {

	private Context mContext;
	private ArrayList<String> mAboutusLists;
	private ArrayList<CommunicationModel> mTermsCalendarModelArrayList;
	private View view;
	private TextView mTitleTxt;
	static public ImageView badge;
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
public CommunicationsAdapter(Context context,
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
	public CommunicationsAdapter(Context context,
								 ArrayList<CommunicationModel> arrList) {
		this.mContext = context;
		this.mTermsCalendarModelArrayList = arrList;

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTermsCalendarModelArrayList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mTermsCalendarModelArrayList.get(position);
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
			badge = (ImageView) view.findViewById(R.id.statusImg);
			mTitleTxt.setText(mTermsCalendarModelArrayList.get(position).getmTitle());

			if (!(PreferenceManager.getCommunicationWholeSchoolBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getCommunicationWholeSchoolEditedBadge(mContext).equalsIgnoreCase("0")))) {

				if (position == 0) {

					badge.setVisibility(View.VISIBLE);
					badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else {
					badge.setVisibility(View.INVISIBLE);

				}
			}
				else if ((PreferenceManager.getCommunicationWholeSchoolBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getCommunicationWholeSchoolEditedBadge(mContext).equalsIgnoreCase("0")))) {
				if (position == 0) {

					badge.setVisibility(View.VISIBLE);
					badge.setBackgroundResource(R.drawable.shape_circle_navy);
				}
				else {
					badge.setVisibility(View.INVISIBLE);

				}
				} else if (!(PreferenceManager.getCommunicationWholeSchoolBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getCommunicationWholeSchoolEditedBadge(mContext).equalsIgnoreCase("0")))) {
				if (position == 0) {

					badge.setVisibility(View.VISIBLE);
					badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else {
					badge.setVisibility(View.INVISIBLE);

				}
			}
				else if ((PreferenceManager.getCommunicationWholeSchoolBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getCommunicationWholeSchoolEditedBadge(mContext).equalsIgnoreCase("0")))) {

				    badge.setVisibility(View.GONE);

				}

			else {
				badge.setVisibility(View.GONE);
			}

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

}
