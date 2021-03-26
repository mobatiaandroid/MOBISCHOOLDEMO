/**
 * 
 */
package com.mobatia.naisapp.fragments.parentassociation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.chatter_box.ChatterBoxActivityNew;
import com.mobatia.naisapp.activities.class_representative.ClassRepresentativeActivity;
import com.mobatia.naisapp.activities.parentsassociation.ParentsAssociationSignUpActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

/**
 * @author Rijo K Jose
 * 
 */

public class ParentAssociationsFragment extends Fragment implements
		NaisTabConstants,CacheDIRConstants, URLConstants,
		IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {

	private View mRootView;
	private String mTitle;
	private String mTabId;
	private RelativeLayout relMain;
	TextView mTitleTextView;
	ArrayList<String> dataArrayStrings = new ArrayList<String>() {
		{
			add("Parents' Association");
			add("Chatter Box Café");
			add("Class Representatives");
			//add("Chatter Box Café");
		}
	};
	Context mContext;
	ImageView imageViewSlotInfo;
	RecyclerView mRecyclerView;

	public ParentAssociationsFragment() {

	}

	public ParentAssociationsFragment(String title, String tabId) {
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
		mRootView = inflater.inflate(R.layout.fragmentparentsactivityrecyclerview, container,
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
		mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.mRecyclerView);
		imageViewSlotInfo = (ImageView) mRootView.findViewById(R.id.imageViewSlotInfo);
		imageViewSlotInfo.setVisibility(View.GONE);
		ViewGroup.MarginLayoutParams marginLayoutParams =
				(ViewGroup.MarginLayoutParams) mRecyclerView.getLayoutParams();
		marginLayoutParams.setMargins(10, 0, 10, 0);
		mRecyclerView.setLayoutParams(marginLayoutParams);
		mRecyclerView.setHasFixedSize(true);
		mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
		mTitleTextView.setText(PARENTS_ASSOCIATION);
		relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
		relMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		LinearLayoutManager llm = new LinearLayoutManager(mContext);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(llm);
		ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, 5);
		mRecyclerView.addItemDecoration(itemDecoration);
		//mAboutUsList.setLayoutManager(recyclerViewLayoutManager);
		mRecyclerView.addItemDecoration(
				new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));

		ParentsAssociationMainAdapter adapter = new ParentsAssociationMainAdapter(mContext, dataArrayStrings);
		mRecyclerView.setAdapter(adapter);

		mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(mContext, mRecyclerView,
				new RecyclerItemListener.RecyclerTouchListener() {
					public void onClickItem(View v, int position) {
						if (dataArrayStrings.get(position).equals(PARENTS_ASSOCIATION)) {
							Intent intent = new Intent(mContext, ParentsAssociationSignUpActivity.class);
							intent.putExtra("tab_type", dataArrayStrings.get(position).toString());
							mContext.startActivity(intent);
						} else if (dataArrayStrings.get(position).equals("Chatter Box Café")) {
							Intent intent = new Intent(mContext, ChatterBoxActivityNew.class);//ChatterBoxActivity
							intent.putExtra("tab_type", dataArrayStrings.get(position).toString());
							mContext.startActivity(intent);
						} else if (dataArrayStrings.get(position).equals("Class Representatives")) {
							Intent intent = new Intent(mContext, ClassRepresentativeActivity.class);//ClassRepresentativeActivity
							intent.putExtra("tab_type", dataArrayStrings.get(position).toString());
							mContext.startActivity(intent);
						}
					}

					public void onLongClickItem(View v, int position) {
					}
				}));
	}


	class ParentsAssociationMainAdapter extends RecyclerView.Adapter<ParentsAssociationMainAdapter.MyViewHolder> {

		private Context mContext;
		private ArrayList<String> mStaffList;


		public class MyViewHolder extends RecyclerView.ViewHolder {
			TextView mTitleTxt;

			public MyViewHolder(View view) {
				super(view);

				mTitleTxt = (TextView) view.findViewById(R.id.listTxtTitle);


			}
		}


		public ParentsAssociationMainAdapter(Context mContext, ArrayList<String> mStaffList) {
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

			holder.mTitleTxt.setText(mStaffList.get(position));


		}


		@Override
		public int getItemCount() {
			return mStaffList.size();
		}
	}
}