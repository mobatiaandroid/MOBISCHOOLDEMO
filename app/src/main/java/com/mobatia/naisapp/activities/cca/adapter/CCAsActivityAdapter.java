package com.mobatia.naisapp.activities.cca.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.CCASelectionActivity;
import com.mobatia.naisapp.activities.cca.model.CCADetailModel;
import com.mobatia.naisapp.activities.cca.model.CCAchoiceModel;
import com.mobatia.naisapp.activities.cca.model.WeekListModel;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAsActivityAdapter extends RecyclerView.Adapter<CCAsActivityAdapter.MyViewHolder> {
    Context mContext;
    GridLayoutManager recyclerViewLayoutManager;
    GridLayoutManager recyclerViewLayoutManager2;
    ArrayList<CCADetailModel> mCCAmodelArrayList;
    ArrayList<CCAchoiceModel> mCCAchoiceModel2;
    ArrayList<CCAchoiceModel> mCCAchoiceModel1;
    ArrayList<WeekListModel> weekList;
    RecyclerView recyclerWeek;
    int dayPosition = 0;
    int count = 2;
    CCAsChoiceListActivityAdapter mCCAsActivityAdapter2;
    CCAsChoiceListActivityAdapter mCCAsActivityAdapter1;
    String selectedChoice1 = "";
    String selectedChoice2 = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtView;
        RecyclerView recycler_review;
        RecyclerView recycler_review2;


        public MyViewHolder(View view) {
            super(view);

            listTxtView = (TextView) view.findViewById(R.id.textViewCCAaItem);
            recycler_review = (RecyclerView) view.findViewById(R.id.recycler_view_adapter_cca);
            recycler_review2 = (RecyclerView) view.findViewById(R.id.recycler_view_adapter_cca2);


        }
    }

    public CCAsActivityAdapter(Context mContext, ArrayList<CCADetailModel> mCcaArrayList) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCcaArrayList;
    }

    public CCAsActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAchoiceModel1, ArrayList<CCAchoiceModel> mCCAchoiceModel2, int mdayPosition) {
        this.mContext = mContext;
        this.mCCAchoiceModel1 = mCCAchoiceModel1;
        this.mCCAchoiceModel2 = mCCAchoiceModel2;
        this.dayPosition = mdayPosition;
    }

    public CCAsActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAchoiceModel1, ArrayList<CCAchoiceModel> mCCAchoiceModel2, int mdayPosition, ArrayList<WeekListModel> mWeekList, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mCCAchoiceModel1 = mCCAchoiceModel1;
        this.mCCAchoiceModel2 = mCCAchoiceModel2;
        this.dayPosition = mdayPosition;
        this.weekList = mWeekList;
        this.recyclerWeek = recyclerView;
        this.count = 2;

    }

    public CCAsActivityAdapter(Context mContext, int mcount) {
        this.mContext = mContext;
        this.mCCAchoiceModel1 = new ArrayList<>();
        this.mCCAchoiceModel2 = new ArrayList<>();
        this.count = mcount;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_activity, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (count != 0) {
            holder.recycler_review.setHasFixedSize(true);
            holder.recycler_review2.setHasFixedSize(true);
            recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
            recyclerViewLayoutManager2 = new GridLayoutManager(mContext, 1);
            holder.recycler_review.setLayoutManager(recyclerViewLayoutManager);
            holder.recycler_review2.setLayoutManager(recyclerViewLayoutManager2);
            if (position == 0) {
                if (mCCAchoiceModel1.size() > 0) {
                    if (mCCAchoiceModel2.size() <= 0) {
                        AppController.weekList.get(dayPosition).setChoiceStatus1("1");
                    }

                    holder.listTxtView.setText("First Choice : ");// + (position + 1)
                    if (CCASelectionActivity.ccaedit==1) {
                        for (int k = 0; k < CCASelectionActivity.CCADetailModelArrayList.size(); k++) {
                        for (int i = 0; i < mCCAchoiceModel1.size(); i++) {
                            if (mCCAchoiceModel1.get(i).getStatus().equalsIgnoreCase("1")) {
                                selectedChoice1 = mCCAchoiceModel1.get(i).getCca_item_name();

                                if (mCCAchoiceModel2 != null) {
                                    for (int j = 0; j < mCCAchoiceModel2.size(); j++) {
                                        if (mCCAchoiceModel2.get(j).getCca_item_name().equalsIgnoreCase(selectedChoice1)) {
                                            if (!(mCCAchoiceModel2.get(j).getCca_details_id().equalsIgnoreCase("-541"))) {
                                                mCCAchoiceModel2.get(j).setDisableCccaiem(true);
                                            } else {
                                                mCCAchoiceModel2.get(j).setDisableCccaiem(false);

                                            }

                                            break;
                                        } else {
                                            mCCAchoiceModel2.get(j).setDisableCccaiem(false);

                                        }
                                    }


                                }

                            }
                            }

                        }
                    }

                    Log.e("DESC VAL",mCCAchoiceModel1.get(0).getDescription());

                    mCCAsActivityAdapter1 = new CCAsChoiceListActivityAdapter(mContext, mCCAchoiceModel1, dayPosition, weekList, 0, recyclerWeek);
                    holder.recycler_review.setAdapter(mCCAsActivityAdapter1);

                }
            } else {
                if (mCCAchoiceModel2.size() > 0) {
                    if (mCCAchoiceModel1.size() <= 0) {
                        AppController.weekList.get(dayPosition).setChoiceStatus("1");
                    }
                    holder.listTxtView.setText("Second Choice : ");// + (position + 1)
                    if (CCASelectionActivity.ccaedit==1) {
                        for (int k = 0; k < CCASelectionActivity.CCADetailModelArrayList.size(); k++) {

                            for (int i = 0; i < mCCAchoiceModel2.size(); i++) {
                                System.out.println("Status2::" + mCCAchoiceModel2.get(position).getStatus());

                                if (mCCAchoiceModel2.get(i).getStatus().equalsIgnoreCase("1")) {
                                    selectedChoice2 = mCCAchoiceModel2.get(i).getCca_item_name();

                                    if (mCCAchoiceModel1 != null) {
                                        for (int j = 0; j < mCCAchoiceModel1.size(); j++) {
                                            if (mCCAchoiceModel1.get(j).getCca_item_name().equalsIgnoreCase(selectedChoice2)) {
                                                if (!(mCCAchoiceModel1.get(j).getCca_details_id().equalsIgnoreCase("-541"))) {
                                                    mCCAchoiceModel1.get(j).setDisableCccaiem(true);
                                                } else {
                                                    mCCAchoiceModel1.get(j).setDisableCccaiem(false);

                                                }

                                                break;
                                            } else {
                                                mCCAchoiceModel1.get(j).setDisableCccaiem(false);
                                            }
                                        }
                                    }


                                }
                            }
                        }
                    }
                    mCCAsActivityAdapter2 = new CCAsChoiceListActivityAdapter(mContext, mCCAchoiceModel2, dayPosition, weekList, 1, recyclerWeek);
                    holder.recycler_review2.setAdapter(mCCAsActivityAdapter2);
                    mCCAsActivityAdapter1.notifyDataSetChanged();
                    mCCAsActivityAdapter2.notifyDataSetChanged();
                }
            }
            holder.recycler_review.addOnItemTouchListener(new RecyclerItemListener(mContext, holder.recycler_review,
                    new RecyclerItemListener.RecyclerTouchListener() {
                        public void onClickItem(View v, int pos) {
                            if (!(mCCAchoiceModel1.get(pos).getDisableCccaiem())) {

                                for (int i = 0; i < mCCAchoiceModel1.size(); i++) {
                                    if (pos == i) {
                                        mCCAchoiceModel1.get(i).setStatus("1");
                                        selectedChoice1 = mCCAchoiceModel1.get(i).getCca_item_name();
                                        System.out.println("Choicere1:" + mCCAchoiceModel1.get(i).getCca_item_name());
                                        if (mCCAchoiceModel2 != null) {
                                            for (int j = 0; j < mCCAchoiceModel2.size(); j++) {
                                                if (mCCAchoiceModel2.get(j).getCca_item_name().equalsIgnoreCase(mCCAchoiceModel1.get(pos).getCca_item_name())) {
                                                    if (!(mCCAchoiceModel2.get(j).getCca_details_id().equalsIgnoreCase("-541"))) {
                                                        mCCAchoiceModel2.get(j).setDisableCccaiem(true);
                                                    } else {
                                                        mCCAchoiceModel2.get(j).setDisableCccaiem(false);

                                                    }
                                                    mCCAsActivityAdapter2.notifyDataSetChanged();

//                                                    break;
                                                } else {
                                                    mCCAchoiceModel2.get(j).setDisableCccaiem(false);
                                                    mCCAsActivityAdapter2.notifyDataSetChanged();

                                                }
                                            }


                                        }


                                    } else {
                                        mCCAchoiceModel1.get(i).setStatus("0");
                                        mCCAchoiceModel1.get(i).setDisableCccaiem(false);

                                        System.out.println("Choicere1 Else:" + mCCAchoiceModel1.get(i).getCca_item_name());
                                        if (mCCAchoiceModel1 != null) {

                                            for (int j = 0; j < mCCAchoiceModel1.size(); j++) {
                                                if (mCCAchoiceModel1.get(j).getCca_item_name().equalsIgnoreCase(selectedChoice2)) {
                                                    if (!(mCCAchoiceModel1.get(j).getCca_details_id().equalsIgnoreCase("-541"))) {
                                                        mCCAchoiceModel1.get(j).setDisableCccaiem(true);
                                                    } else {
                                                        mCCAchoiceModel1.get(j).setDisableCccaiem(false);

                                                    }
                                                    mCCAsActivityAdapter1.notifyDataSetChanged();

//                                                    break;
                                                } else {
                                                    mCCAchoiceModel1.get(j).setDisableCccaiem(false);
                                                    mCCAsActivityAdapter1.notifyDataSetChanged();

                                                }
                                            }
                                        }

                                    }

                                }
                                System.out.println("choicere1 text" + mCCAchoiceModel1.get(pos).getCca_item_name());
                                mCCAsActivityAdapter1 = new CCAsChoiceListActivityAdapter(mContext, mCCAchoiceModel1, dayPosition, weekList, 0, recyclerWeek);
                                mCCAsActivityAdapter1.notifyDataSetChanged();
                                holder.recycler_review.setAdapter(mCCAsActivityAdapter1);
                            }
                        }

                        public void onLongClickItem(View v, int position) {
                            System.out.println("On Long Click Item interface");
                        }
                    }));
            holder.recycler_review2.addOnItemTouchListener(new RecyclerItemListener(mContext, holder.recycler_review2,
                    new RecyclerItemListener.RecyclerTouchListener() {
                        public void onClickItem(View v, int pos) {

                            if (!(mCCAchoiceModel2.get(pos).getDisableCccaiem())) {
                                for (int i = 0; i < mCCAchoiceModel2.size(); i++) {
                                    if (pos == i) {
                                        mCCAchoiceModel2.get(i).setStatus("1");
                                        System.out.println("Choicere2:" + mCCAchoiceModel2.get(i).getCca_item_name());
                                        selectedChoice2 = mCCAchoiceModel2.get(i).getCca_item_name();

                                        if (mCCAchoiceModel1 != null) {
                                            for (int j = 0; j < mCCAchoiceModel1.size(); j++) {
                                                if (mCCAchoiceModel1.get(j).getCca_item_name().equalsIgnoreCase(mCCAchoiceModel2.get(pos).getCca_item_name())) {
                                                    if (!(mCCAchoiceModel1.get(j).getCca_details_id().equalsIgnoreCase("-541"))) {

                                                        mCCAchoiceModel1.get(j).setDisableCccaiem(true);
                                                    } else {
                                                        mCCAchoiceModel1.get(j).setDisableCccaiem(false);

                                                    }
                                                    mCCAsActivityAdapter1.notifyDataSetChanged();

//                                                    break;
                                                } else {
                                                    mCCAchoiceModel1.get(j).setDisableCccaiem(false);
                                                    mCCAsActivityAdapter1.notifyDataSetChanged();

                                                }
                                            }

                                        }

                                    } else {
                                        mCCAchoiceModel2.get(i).setStatus("0");
                                        mCCAchoiceModel2.get(i).setDisableCccaiem(false);

                                        System.out.println("Choicere2 else:" + mCCAchoiceModel2.get(i).getCca_item_name());

                                        if (mCCAchoiceModel2 != null) {
                                            for (int j = 0; j < mCCAchoiceModel2.size(); j++) {
                                                if (mCCAchoiceModel2.get(j).getCca_item_name().equalsIgnoreCase(selectedChoice1)) {
                                                    if (!(mCCAchoiceModel2.get(j).getCca_details_id().equalsIgnoreCase("-541"))) {

                                                        mCCAchoiceModel2.get(j).setDisableCccaiem(true);
                                                    } else {
                                                        mCCAchoiceModel2.get(j).setDisableCccaiem(false);

                                                    }
                                                    mCCAsActivityAdapter2.notifyDataSetChanged();

//                                                    break;
                                                } else {
                                                    mCCAchoiceModel2.get(j).setDisableCccaiem(false);
                                                    mCCAsActivityAdapter2.notifyDataSetChanged();

                                                }
                                            }

                                        }


                                    }

                                }
                                System.out.println("choicere2 text" + mCCAchoiceModel2.get(pos).getCca_item_name());
                                mCCAsActivityAdapter2 = new CCAsChoiceListActivityAdapter(mContext, mCCAchoiceModel2, dayPosition, weekList, 1, recyclerWeek);
                                mCCAsActivityAdapter2.notifyDataSetChanged();
                                holder.recycler_review2.setAdapter(mCCAsActivityAdapter2);
                            }
                        }

                        public void onLongClickItem(View v, int position) {
                            System.out.println("On Long Click Item interface");
                        }
                    }));
        }

    }


    @Override
    public int getItemCount() {

        return count;
    }
}
