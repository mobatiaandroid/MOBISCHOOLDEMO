package com.mobatia.naisapp.activities.parentsassociation.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.parentsassociation.ParentsAssociationListActivity;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventItemsModel;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class ParentsAssociationMainRecyclerviewAdapter extends RecyclerView.Adapter<ParentsAssociationMainRecyclerviewAdapter.MyViewHolder> implements JSONConstants, URLConstants, ResultConstants, StatusConstants, NaisClassNameConstants {


    private Context mContext;
    private ArrayList<ParentAssociationEventsModel> mParentAssociationEventsModelArrayList;
    String photo_id = "-1";
    String startTime = "";
    boolean startTimeAm = true;
    boolean endTimeAm = true;
    String endTime = "";
    int mPosition = 0;
    int mainPosition = 0;
    int datePosition = 0;
    boolean alreadyslotBookedByUser=false;
    ArrayList<ParentAssociationEventItemsModel> mListViewArrayPost ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventDate;
        TextView notAvailableTV;
        LinearLayout gridClickRelative,layout;
        CardView card_view;
        RecyclerView mRecyclerViewItemName;
        RecyclerView mRecyclerViewItem;

        public MyViewHolder(View view) {
            super(view);

//            photoImageView = (ImageView) view.findViewById(R.id.imgView);
            eventName = (TextView) view.findViewById(R.id.eventName);
            eventDate = (TextView) view.findViewById(R.id.eventDate);
            notAvailableTV = (TextView) view.findViewById(R.id.notAvailableTV);
            mRecyclerViewItemName = (RecyclerView) view.findViewById(R.id.mRecyclerViewItemName);
            mRecyclerViewItem = (RecyclerView) view.findViewById(R.id.mRecyclerViewItem);
            gridClickRelative = (LinearLayout) view.findViewById(R.id.gridClickRelative);
            card_view = (CardView) view.findViewById(R.id.card_view);
            layout= (LinearLayout) view.findViewById(R.id.layout);
            mainPosition=getAdapterPosition();
            System.out.println("mainPosition="+getAdapterPosition());

        }
    }


    public ParentsAssociationMainRecyclerviewAdapter(Context mContext, ArrayList<ParentAssociationEventsModel> mParentAssociationEventsModelArrayList) {
        this.mContext = mContext;
        this.mParentAssociationEventsModelArrayList = mParentAssociationEventsModelArrayList;
    }

    public ParentsAssociationMainRecyclerviewAdapter(Context mContext, ArrayList<ParentAssociationEventsModel> mParentAssociationEventsModelArrayList, String photo_id) {
        this.mContext = mContext;
        this.mParentAssociationEventsModelArrayList = mParentAssociationEventsModelArrayList;
        this.photo_id = photo_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parents_association_main_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
//        mPosition = position;
//        holder.layout.getBackground().setAlpha(150);
        holder.eventName.setText(mParentAssociationEventsModelArrayList.get(position).getEventName());
        holder.eventDate.setText(mParentAssociationEventsModelArrayList.get(position).getDayOfTheWeek()+" "+mParentAssociationEventsModelArrayList.get(position).getDay() + " " + mParentAssociationEventsModelArrayList.get(position).getMonthString() + " " +
                mParentAssociationEventsModelArrayList.get(position).getYear());

        //holder.eventDate.setText(mParentAssociationEventsModelArrayList.get(position).getDay() + " " + mParentAssociationEventsModelArrayList.get(position).getMonthString() + " " + mParentAssociationEventsModelArrayList.get(position).getYear()+", "+mParentAssociationEventsModelArrayList.get(position).getDayOfTheWeek());
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        int spacings = 5; // 50px
        ItemOffsetDecoration itemDecorations = new ItemOffsetDecoration(mContext, spacings);
        holder.mRecyclerViewItemName.addItemDecoration(itemDecorations);
        holder.mRecyclerViewItemName.setLayoutManager(llm);
        if (mParentAssociationEventsModelArrayList.get(position).getEventItemList().get(mParentAssociationEventsModelArrayList.get(position).getPosition()).getEventItemStatusList().size()>0)
        {
            holder.mRecyclerViewItem.setVisibility(View.VISIBLE);
            holder.notAvailableTV.setVisibility(View.GONE);
        }
        else
        {
            holder.mRecyclerViewItem.setVisibility(View.GONE);
            holder.notAvailableTV.setVisibility(View.VISIBLE);
        }
//        holder.mRecyclerViewItemName.setAdapter(new ParentsAssociationItemNameRecyclerviewAdapter(mContext, mParentAssociationEventsModelArrayList.get(position).getEventItemList()));
        holder.mRecyclerViewItemName.setAdapter(new ParentsAssociationItemNameRecyclerviewAdapter(mContext, mParentAssociationEventsModelArrayList.get(position).getEventItemList(),mParentAssociationEventsModelArrayList.get(position).getPosition()));
        holder.mRecyclerViewItemName.addOnItemTouchListener(new RecyclerItemListener(mContext, holder.mRecyclerViewItemName,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int pos) {
                        mPosition=pos;
                        mainPosition=position;
                        mParentAssociationEventsModelArrayList.get(position).setPosition(pos);
                        Log.e("Pos","pos1"+""+position+"pos2"+mParentAssociationEventsModelArrayList.get(position).getPosition());
                        holder.mRecyclerViewItemName.setAdapter(new ParentsAssociationItemNameRecyclerviewAdapter(mContext, mParentAssociationEventsModelArrayList.get(position).getEventItemList(), pos));
                        if ( mParentAssociationEventsModelArrayList.get(position).getEventItemList().get(pos).getEventItemStatusList().size()>0) {
                            ParentsAssociationTimeSlotRecyclerviewAdapter mParentsAssociationTimeSlotRecyclerviewAdapter = new ParentsAssociationTimeSlotRecyclerviewAdapter(mContext, mParentAssociationEventsModelArrayList.get(position).getEventItemList().get(pos).getEventItemStatusList(),pos);
                            holder.mRecyclerViewItem.setAdapter(mParentsAssociationTimeSlotRecyclerviewAdapter);
                            mParentsAssociationTimeSlotRecyclerviewAdapter.notifyDataSetChanged();
                            holder.mRecyclerViewItem.setVisibility(View.VISIBLE);
                            holder.notAvailableTV.setVisibility(View.GONE);
                            mParentAssociationEventsModelArrayList.get(position).setPosition(pos);
                            Log.e("Pos", "pos1" + "" + position + "pos2" + mParentAssociationEventsModelArrayList.get(position).getPosition());

                        }
                        else
                        {
                            holder.mRecyclerViewItem.setVisibility(View.GONE);
                            holder.notAvailableTV.setVisibility(View.VISIBLE);
//                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "No timeslots available", R.drawable.questionmark_icon, R.drawable.round);

                        }

                    }

                    public void onLongClickItem(View v, int pos) {
//                        if ( mParentAssociationEventsModelArrayList.get(mPosition).getEventItemList().get(pos).getEventItemStatusList().size()>0) {
//                            ParentsAssociationTimeSlotRecyclerviewAdapter mParentsAssociationTimeSlotRecyclerviewAdapter = new ParentsAssociationTimeSlotRecyclerviewAdapter(mContext, mParentAssociationEventsModelArrayList.get(mPosition).getEventItemList().get(pos).getEventItemStatusList());
//                            holder.mRecyclerViewItem.setAdapter(mParentsAssociationTimeSlotRecyclerviewAdapter);
//                            mParentsAssociationTimeSlotRecyclerviewAdapter.notifyDataSetChanged();
//                            holder.mRecyclerViewItem.setVisibility(View.VISIBLE);
//
//                        }
//                        else
//                        {
//                            holder.mRecyclerViewItem.setVisibility(View.INVISIBLE);
//                        }

                    }
                }));
        holder.mRecyclerViewItem.setHasFixedSize(true);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        holder.mRecyclerViewItem.addItemDecoration(itemDecoration);
        holder.mRecyclerViewItem.setLayoutManager(recyclerViewLayoutManager);
        holder.mRecyclerViewItem.setAdapter(new ParentsAssociationTimeSlotRecyclerviewAdapter(mContext, mParentAssociationEventsModelArrayList.get(position).getEventItemList().get(mParentAssociationEventsModelArrayList.get(position).getPosition()).getEventItemStatusList(),mParentAssociationEventsModelArrayList.get(position).getPosition()));
//System.out.println("sizes2:"+mParentAssociationEventsModelArrayList.get(position).getEventItemStatusList().size());
//System.out.println("sizes3:" + mParentAssociationEventsModelArrayList.get(position).getEventItemList().get(0).getEventItemStatusList().size());
        holder.mRecyclerViewItem.addOnItemTouchListener(new RecyclerItemListener(mContext, holder.mRecyclerViewItem,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int p) {
                        Log.e("positoin clikc", ""+v.getId());
                        datePosition= p;
                        mainPosition=position;
                        Log.e("positoin mainPosition", ""+mainPosition);
                        mPosition=v.getId();
                        System.out.println("datePosition=" + datePosition);
                        if (mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(p).getStatus().equalsIgnoreCase("0"))
                        {
                            String dateOnly=mParentAssociationEventsModelArrayList.get(mainPosition).getDay()+" "+mParentAssociationEventsModelArrayList.get(mainPosition).getMonthString()+" "+mParentAssociationEventsModelArrayList.get(mainPosition).getYear();
                            String itemName=mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getItemName();
                            String dateTime=mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(p).getFrom_time()+" - "+mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(p).getTo_time();
                            showDialogAlertDoubeBtn((Activity) mContext, "Alert", "Do you want to volunteer for "+itemName+" on "+dateOnly+" at "+dateTime+"?", R.drawable.questionmark_icon, R.drawable.round);

                        }
                        else  if (mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(p).getStatus().equalsIgnoreCase("1"))
                        {

                            showDialogAlertDoubeBtn((Activity) mContext, "Alert", "Do you want to cancel the request?", R.drawable.questionmark_icon, R.drawable.round);

                        }
                        else  if (mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(p).getStatus().equalsIgnoreCase("2"))
                        {
                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "This slot is not available", R.drawable.exclamationicon, R.drawable.round);

                        }

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return mParentAssociationEventsModelArrayList.size();
    }
//    public void didTapButton(View view) {
////        ImageView button = (ImageView)view.findViewById(R.id.imgView);
////        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
////        button.startAnimation(myAnim);
//        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//
//        // Use bounce interpolator with amplitude 0.2 and frequency 20
//        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.6, 20);
//        myAnim.setInterpolator(interpolator);
//
//        view.startAnimation(myAnim);
//    }
//    @Override
//    public void onViewDetachedFromWindow(MyViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//        holder.photoImageView.clearAnimation();
//    }
public void postSelectedSlot() {

    try {
        final VolleyWrapper manager = new VolleyWrapper(URL_BOOK_PTA_TIME_SLOT_NEW);//URL_BOOK_PTA_TIME_SLOT
//        String[] name = new String[]{JTAG_ACCESSTOKEN, JTAG_USERS_ID,"item","event_id","time"};
//        String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getItemName(),mParentAssociationEventsModelArrayList.get(mainPosition).getEvenId(),mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(datePosition).getStart_time()+" - "+mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(datePosition).getEnd_time()};

        String[] name = new String[]{JTAG_ACCESSTOKEN, JTAG_USERS_ID,"id"};
        String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),mParentAssociationEventsModelArrayList.get(mainPosition).getEventItemList().get(mPosition).getEventItemStatusList().get(datePosition).getEventId()};
        manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

            @Override
            public void responseSuccess(String successResponse) {
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
                                    ParentsAssociationListActivity.callListApis(mContext, mParentAssociationEventsModelArrayList ,new ParentsAssociationListActivity.GetPtaItemList() {
                                        @Override
                                        public void getPtaItemData() {

                                        }
                                    });
                                    showDialogAlertSingleBtn((Activity) mContext, "Alert", "Your time slot has been booked successfully.", R.drawable.tick, R.drawable.round);

//                                    getPtaAllotedDateList();
                                }
                                else  if (statusCode.equals(STATUS_CANCEL)) {
                                    ParentsAssociationListActivity.callListApis(mContext, mParentAssociationEventsModelArrayList ,new ParentsAssociationListActivity.GetPtaItemList() {
                                        @Override
                                        public void getPtaItemData() {

                                        }
                                    });
                                    showDialogAlertSingleBtn((Activity) mContext, "Alert", "Request cancelled successfully.", R.drawable.tick, R.drawable.round);

//                                    getPtaAllotedDateList();
                                }
                                else  if (statusCode.equals(STATUS_BOOKED_BY_USER)) {
//                                    ParentsAssociationListActivity.callListApis(mContext, mParentAssociationEventsModelArrayList ,new ParentsAssociationListActivity.GetPtaItemList() {
//                                        @Override
//                                        public void getPtaItemData() {
//
//                                        }
//                                    });
                                    showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot is already booked by an another user.", R.drawable.exclamationicon, R.drawable.round);

//                                    getPtaAllotedDateList();
                                }
                                else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                    Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                                }
                            } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                    responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                postSelectedSlot();

                            }
                        } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                            Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void responseFailure(String failureResponse) {
                // CustomStatusDialog(RESPONSE_FAILURE);
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }


}
    public  void showDialogAlertDoubeBtn(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postSelectedSlot();
                dialog.dismiss();
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public  void showDialogAlertSingleBtn(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

    }

}
