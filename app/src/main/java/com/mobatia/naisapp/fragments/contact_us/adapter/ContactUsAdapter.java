package com.mobatia.naisapp.fragments.contact_us.adapter;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.contact_us.model.ContactUsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gayatri on 11/5/17.
 */
public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ContactUsModel> mStaffList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;
        TextView subTitle,cotactEmail;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt = (TextView) view.findViewById(R.id.contactName);
            subTitle= (TextView) view.findViewById(R.id.cotactNumber);
            cotactEmail= (TextView) view.findViewById(R.id.cotactEmail);


        }
    }


    public ContactUsAdapter(Context mContext,ArrayList<ContactUsModel> mStaffList) {
        this.mContext = mContext;
        this.mStaffList = mStaffList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_contact_us, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mTitleTxt.setText(mStaffList.get(position).getContact_name());
        if(!mStaffList.get(position).getContact_phone().equals("")) {
            holder.subTitle.setText(mStaffList.get(position).getContact_phone());
        }else{
            holder.subTitle.setVisibility(View.GONE);
        }
        //forGotpasswordText.setText(getString(R.string.forgot_password));
        holder.cotactEmail.setText(mStaffList.get(position).getContact_email());
if(!mStaffList.get(position).getContact_email().equals("")){
    holder.cotactEmail.setPaintFlags(holder.cotactEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
}else{
    holder.cotactEmail.setVisibility(View.GONE);
}
        holder.subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + holder.subTitle.getText().toString()));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mContext.startActivity(intent);
            }
        });

        holder.cotactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(
                        Intent.ACTION_SEND_MULTIPLE);
                String[] deliveryAddress = {holder.cotactEmail.getText().toString()};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
                emailIntent.setType("text/plain");
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                PackageManager pm = v.getContext().getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(
                        emailIntent, 0);
                for (final ResolveInfo app : activityList) {
                    System.out.println("packge name" + app.activityInfo.name);
                    if ((app.activityInfo.name).contains("com.google.android.gm")) {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(
                                activity.applicationInfo.packageName, activity.name);
                        emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        emailIntent.setComponent(name);
                        v.getContext().startActivity(emailIntent);
                        break;
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mStaffList.size();
    }
}
