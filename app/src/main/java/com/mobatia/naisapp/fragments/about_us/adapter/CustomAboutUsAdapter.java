/**
 * 
 */
package com.mobatia.naisapp.fragments.about_us.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.about_us.model.AboutusModel;

import java.util.ArrayList;


/**
 * @author Rijo K Jose
 * 
 */
public class CustomAboutUsAdapter extends RecyclerView.Adapter<CustomAboutUsAdapter.MyViewHolder> {

	private Context mContext;
	private ArrayList<AboutusModel> mStaffList;


	public class MyViewHolder extends RecyclerView.ViewHolder {
		TextView mTitleTxt;

		public MyViewHolder(View view) {
			super(view);

			mTitleTxt = (TextView) view.findViewById(R.id.listTxtTitle);


		}
	}


	public CustomAboutUsAdapter(Context mContext,ArrayList<AboutusModel> mStaffList) {
		this.mContext = mContext;
		this.mStaffList = mStaffList;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.custom_aboutus_list_adapter, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position) {

		holder.mTitleTxt.setText(mStaffList.get(position).getTabType());


	}


	@Override
	public int getItemCount() {
		return mStaffList.size();
	}
}
