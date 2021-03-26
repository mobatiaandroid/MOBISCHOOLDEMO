package com.mobatia.naisapp.activities.assessment_link;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.assessment_link.adapter.AssessmentSubRecyclerviewAdapter;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadhtmlContentViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

/**
 * Created by gayatri on 28/3/17.
 */
public class AssessmentSubListActivity extends Activity implements URLConstants, JSONConstants {
    Bundle extras;
    private Context mContext = this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ArrayList<AssementModel> mAssementModelArrayList = new ArrayList<>();
    AssessmentSubRecyclerviewAdapter mAssessmentRecyclerviewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecyclerView recycler_ListView;
    String tab_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assentmentsublistlink_activity);
        initUI();

    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            mAssementModelArrayList = (ArrayList<AssementModel>) extras.getSerializable("detail");
            tab_type = extras.getString("tab_type");

        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(AssessmentSubListActivity.this, tab_type);
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

        recycler_ListView = (RecyclerView) findViewById(R.id.mListView);
        recycler_ListView.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);

        //or
        recycler_ListView.addItemDecoration(
                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        recycler_ListView.addItemDecoration(itemDecoration);
        recycler_ListView.setLayoutManager(recyclerViewLayoutManager);
        mAssessmentRecyclerviewAdapter = new AssessmentSubRecyclerviewAdapter(mContext, mAssementModelArrayList);
        recycler_ListView.setAdapter(mAssessmentRecyclerviewAdapter);
        recycler_ListView.addOnItemTouchListener(new RecyclerItemListener(mContext, recycler_ListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (mAssementModelArrayList.get(position).getType().equalsIgnoreCase("1")) {
                            if (!mAssementModelArrayList.get(position).getDescription().equalsIgnoreCase("")) {
                                Intent intent = new Intent(mContext, LoadhtmlContentViewActivity.class);
                                intent.putExtra("url", mAssementModelArrayList.get(position).getDescription());
                                intent.putExtra("tab_type", tab_type);
                                startActivity(intent);
                            }
                        } else {
                            if (!mAssementModelArrayList.get(position).getDescription().equalsIgnoreCase("")) {

                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url", mAssementModelArrayList.get(position).getDescription());
                                startActivity(intent);
                            }
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));

    }


}
