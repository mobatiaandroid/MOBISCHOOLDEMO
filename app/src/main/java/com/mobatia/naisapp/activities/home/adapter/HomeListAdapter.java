/**
 * 
 */
package com.mobatia.naisapp.activities.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.manager.PreferenceManager;


/**
 * @author RIJO K JOSE
 * 
 */
public class HomeListAdapter extends BaseAdapter{

	private Context mContext;
	private String[] mListItemArray;
	private TypedArray mListImgArray;
	private Button badge;
	private int customLayout;
	private boolean mDisplayListImage;

	public HomeListAdapter(Context context, String[] listItemArray,
						   TypedArray listImgArray, int customLayout, boolean displayListImage) {
		this.mContext = context;
		this.mListItemArray = listItemArray;
		this.mListImgArray = listImgArray;
		this.customLayout = customLayout;
		this.mDisplayListImage = displayListImage;
	}

	public HomeListAdapter(Context context, String[] listItemArray,
						   int customLayout, boolean displayListImage) {
		this.mContext = context;
		this.mListItemArray = listItemArray;
		this.customLayout = customLayout;
		this.mDisplayListImage = displayListImage;
	}
	static class ViewHolder {
		private TextView txtTitle;
		private TextView badge;
		private ImageView imgView;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListItemArray.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListItemArray[position];
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(customLayout, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView
					.findViewById(R.id.listTxtView);
			holder.imgView = (ImageView) convertView.findViewById(R.id.listImg);
			holder.badge = (TextView) convertView.findViewById(R.id.badge);
			convertView.setTag(holder);

		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		//badge=(Button)convertView.findViewById(R.id.badge);
	/*	TextView txtTitle = (TextView) convertView
				.findViewById(R.id.listTxtView);*/
		//ImageView imgView = (ImageView) convertView.findViewById(R.id.listImg);
		holder.txtTitle.setText(mListItemArray[position]);
		if (mDisplayListImage) {
			holder.imgView.setVisibility(View.VISIBLE);
			holder.imgView.setImageDrawable(mListImgArray.getDrawable(position));
		} else {
			holder.imgView.setVisibility(View.GONE);
		}
		if(PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
		{
			if (!PreferenceManager.getStaffId(mContext).equalsIgnoreCase(""))

			{
				if (position==1)
				{
					if (!(PreferenceManager.getStaffNotificationBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getStaffNotificationEditedBadge(mContext).equalsIgnoreCase("0")))) {
						holder.badge.setVisibility(View.VISIBLE);
						holder.badge.setText(PreferenceManager.getStaffNotificationBadge(mContext));
						holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
					} else if ((PreferenceManager.getStaffNotificationBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getStaffNotificationEditedBadge(mContext).equalsIgnoreCase("0")))) {
						holder.badge.setVisibility(View.VISIBLE);
						holder.badge.setText(PreferenceManager.getStaffNotificationEditedBadge(mContext));
						holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

					} else if (!(PreferenceManager.getStaffNotificationBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getStaffNotificationEditedBadge(mContext).equalsIgnoreCase("0")))) {
						holder.badge.setVisibility(View.VISIBLE);
						holder.badge.setText(PreferenceManager.getStaffNotificationBadge(mContext));
						holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

					} else {
						holder.badge.setVisibility(View.GONE);

					}
				}
				else
				{
					holder.badge.setVisibility(View.GONE);
				}
			}
			else
			{

			}

		}
		else {
			if (position == 1) {
				if (!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getCalendarBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else if ((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

				} else if (!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getCalendarBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

				} else {
					holder.badge.setVisibility(View.GONE);

				}
			} else if (position == 2) {
				if (!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getNotificationBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else if ((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

				} else if (!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getNotificationBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

				} else {
					holder.badge.setVisibility(View.GONE);

				}
			}
			else if (position == 3) {
				if (!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getNoticeBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else if ((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

				} else if (!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getNoticeBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

				} else {
					holder.badge.setVisibility(View.GONE);

				}
			}

			else if (position == 9) {
				if (!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getSportsBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else if ((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getSportsEditedBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

				} else if (!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getSportsBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

				} else {
					holder.badge.setVisibility(View.GONE);

				}
			} else if (position == 15) {
				System.out.println("report badge" + PreferenceManager.getReportsBadge(mContext) + "report edited badge" + PreferenceManager.getReportsEditedBadge(mContext));
				if (!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getReportsBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else if ((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getReportsEditedBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

				} else if (!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getReportsBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

				} else if ((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0")))) {

					holder.badge.setVisibility(View.GONE);

				} else {
					holder.badge.setVisibility(View.GONE);

				}
			} else if (position == 11) {
				if (!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getCcaBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else if ((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getCcaEditedBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

				} else if (!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getCcaBadge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

				} else {
					holder.badge.setVisibility(View.GONE);

				}
			}else if (position == 5) {
				if (!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getPaymentitem_badge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);
				} else if ((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_navy);

				} else if (!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0")))) {
					holder.badge.setVisibility(View.VISIBLE);
					holder.badge.setText(PreferenceManager.getPaymentitem_badge(mContext));
					holder.badge.setBackgroundResource(R.drawable.shape_circle_red);

				} else {
					holder.badge.setVisibility(View.GONE);

				}
			}
			else {
				holder.badge.setVisibility(View.GONE);

			}
		}
		return convertView;
	}


	
}
