package com.mobatia.naisapp.activities.parentsassociation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.chatter_box.ChatterBoxActivityNew;
import com.mobatia.naisapp.activities.class_representative.ClassRepresentativeActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

/**
 * Created by gayatri on 12/5/17.
 */
public class ParentsAssociationMainActivity extends Activity implements NaisClassNameConstants{
    ArrayList<String> dataArrayStrings=new ArrayList<String>(){
        {
            add("Parents' Association");
            add("Chatter Box Café");
            add("Class Representative");
            //add("Chatter Box Café");
        }};
    Context mContext;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,home,imageViewSlotInfo;
    RecyclerView mRecyclerView;
    Bundle extras;
    String  tab_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.parentsactivityrecyclerview);

        initUI();
    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        imageViewSlotInfo= (ImageView) findViewById(R.id.imageViewSlotInfo);
        imageViewSlotInfo.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) mRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(10, 0, 10, 0);
        mRecyclerView.setLayoutParams(marginLayoutParams);
        mRecyclerView.setHasFixedSize(true);
        headermanager = new HeaderManager(ParentsAssociationMainActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,5);
        mRecyclerView.addItemDecoration(itemDecoration);
        //mAboutUsList.setLayoutManager(recyclerViewLayoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));

        ParentsAssociationMainAdapter adapter =new ParentsAssociationMainAdapter(mContext,dataArrayStrings);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(mContext, mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                       if(dataArrayStrings.get(position).equals(PARENTS_ASSOCIATION)) {
                           Intent intent = new Intent(mContext, ParentsAssociationSignUpActivity.class);
                           intent.putExtra("tab_type", dataArrayStrings.get(position).toString());
                           mContext.startActivity(intent);
                       }
                       else if(dataArrayStrings.get(position).equals("Chatter Box Café")){
			Intent intent = new Intent(mContext, ChatterBoxActivityNew.class);//ChatterBoxActivity
			intent.putExtra("tab_type", dataArrayStrings.get(position).toString());
			mContext.startActivity(intent);
		}else if(dataArrayStrings.get(position).equals("Class Representative")){
                           Intent intent = new Intent(mContext, ClassRepresentativeActivity.class);//ClassRepresentativeActivity
                           intent.putExtra("tab_type", dataArrayStrings.get(position).toString());
                           mContext.startActivity(intent);
                       }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
    }


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


        public ParentsAssociationMainAdapter(Context mContext,ArrayList<String> mStaffList) {
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


