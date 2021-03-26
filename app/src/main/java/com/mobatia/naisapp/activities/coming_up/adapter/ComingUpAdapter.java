/**
 *
 */
package com.mobatia.naisapp.activities.coming_up.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.coming_up.model.ComingUpModel;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * @author Rijo K Jose
 */
public class ComingUpAdapter extends BaseAdapter implements
        CacheDIRConstants, IntentPassValueConstants {

    private Context mContext;
    private ArrayList<ComingUpModel> mStaffList;
    LayoutInflater inflater;

    TextView listTxtTitle;
    TextView listTxtDate;
    ImageView iconImg;
    TextView status;
    RelativeLayout statusLayout;
    ViewHolders mViewHolder;

    public ComingUpAdapter(Context context,
                           ArrayList<ComingUpModel> arrList) {
        this.mContext = context;
        this.mStaffList = arrList;

    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mStaffList.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mStaffList.get(position);
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
    public View getView(int position, View convertViews, ViewGroup parent) {


        if (convertViews == null) {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertViews = inflater.inflate(R.layout.custom_coming_up_list_adapter, null, false);

            mViewHolder = new ViewHolders(convertViews);

            convertViews.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolders) convertViews.getTag();

        }

        listTxtTitle =  mViewHolder.getListTxtTitle();
        iconImg = mViewHolder.geticonImg();
        listTxtDate = mViewHolder.getListTxtDate();
        status = mViewHolder.getStatus();
        statusLayout = mViewHolder.getStatusLayout();
        try {
            listTxtTitle.setText(mStaffList.get(position).getTitle().trim());
            listTxtDate.setText(AppUtils.dateParsingTodd_MMM_yyyy(mStaffList.get(position).getDate()));

            if (mStaffList.get(position).getStatus().equalsIgnoreCase("0")) {
                statusLayout.setVisibility(View.VISIBLE);
                status.setBackgroundResource(R.drawable.rectangle_red);
                status.setText("New");
            } else if (mStaffList.get(position).getStatus().equalsIgnoreCase("1") || mStaffList.get(position).getStatus().equalsIgnoreCase("")) {
                status.setVisibility(View.INVISIBLE);
            } else if (mStaffList.get(position).getStatus().equalsIgnoreCase("2")) {
                statusLayout.setVisibility(View.VISIBLE);
                status.setBackgroundResource(R.drawable.rectangle_orange);
                status.setText("Updated");
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertViews;
    }

    class ViewHolders {
        TextView listTxtTitle;
        TextView listTxtDate;
        ImageView iconImg;
        RelativeLayout statusLayout;
        TextView status;
        View baseView;

        ViewHolders(View base) {
            this.baseView = base;

        }

        public TextView getListTxtTitle() {
            if (listTxtTitle == null) {
                listTxtTitle = (TextView) baseView.findViewById(R.id.listTxtTitle);

            }
            return listTxtTitle;
        }
        public ImageView geticonImg() {
            if (iconImg == null) {
                iconImg = (ImageView) baseView.findViewById(R.id.iconImg);

            }
            return iconImg;
        }
        public TextView getListTxtDate() {
            if (listTxtDate == null) {
                listTxtDate = (TextView) baseView.findViewById(R.id.listTxtDate);

            }
            return listTxtDate;
        }
        public TextView getStatus() {
            if (status == null) {
                status = (TextView) baseView.findViewById(R.id.status);

            }
            return status;
        }
        public RelativeLayout getStatusLayout() {
            if (statusLayout == null) {
                statusLayout = (RelativeLayout) baseView.findViewById(R.id.statusLayout);

            }
            return statusLayout;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
