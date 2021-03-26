package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.adapter.CanteenItemRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.CartDateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.CategoryRecyclerAdpter;
import com.mobatia.naisapp.activities.canteen_new.adapter.DateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenDetailShowModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenItemDataModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CategoryModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.payment_history.PaymentWalletHistoryModel;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderNextActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemListActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    static  Context mContext;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,btn_history,home;
    Bundle extras;
    static   TextView singleDateViewTxt;
    static  ArrayList<DateModel>mDateArrayList;
    static   RecyclerView dateRecyclerView;
    static   RecyclerView categoryRecyclerView;
    static  RecyclerView ItemRecyclerView;
    DateRecyclerAdapter dateRecyclerAdapter;
    static   ArrayList<CategoryModel>mCategoryArrayList;
   static ArrayList<CanteenItemDataModel>mCanteenItemMainArrayList;
    static ArrayList<CanteenItemDetailModel>mCanteenDetailArrayList;
    static ArrayList<CanteenDetailModel>mCanteenItemDetailArrayList;
    static  String ordered_user_type ="";
    static  String student_id ="";
    static   String parent_id = "";
    static    String staff_id ="";
   static ArrayList<CartItemDateModel> mCartDateArrayList;
   static ArrayList<CartItemDetailModel> mCartItemArrayList;
  static   ArrayList<CanteenDetailShowModel> mCanteenItemShowArrayList;
    static   ImageView noItemImg;
    static  String selectedCategory="";
    static   String selectedCategoryID="";
    static  String selectedDate="";
    static  LinearLayout cartLinear,cartBtnLinear;
    static  TextView itemCount,totalAmount;
    static  String dateList="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity_new);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getCategoryList(URL_CANTEEN_CATEGORY_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

            int k = 0;

            for (int i = 0; i < mDateArrayList.size(); i++) {
                if (k == 0) {
                    dateList = mDateArrayList.get(i).getDate();
                } else {
                    dateList = dateList + "," + mDateArrayList.get(i).getDate();
                }
                k = k + 1;
            }


    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            mDateArrayList=(ArrayList<DateModel>) getIntent().getSerializableExtra("dateArray");
            ordered_user_type = extras.getString("ordered_user_type");
            student_id = extras.getString("student_id");
            parent_id = extras.getString("parent_id");
            staff_id = extras.getString("staff_id");
        }
        System.out.println("ArrayList size date in itemList"+mDateArrayList.size());
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        singleDateViewTxt = (TextView) findViewById(R.id.singleDateViewTxt);
        itemCount = (TextView) findViewById(R.id.itemCount);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        headermanager = new HeaderManager(ItemListActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemListActivity.this,ConfirmedOrderActivity.class);
                intent.putExtra("ordered_user_type",ordered_user_type);
                intent.putExtra("tab_type","History");
                intent.putExtra("student_id",student_id);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id",staff_id);
                startActivity(intent);
            }
        });
        home = headermanager.getLogoButton();

        cartLinear=findViewById(R.id.cartLinear);
        categoryRecyclerView=findViewById(R.id.categoryRecyclerView);

        noItemImg=findViewById(R.id.noItemImg);
        cartBtnLinear=findViewById(R.id.cartBtnLinear);
        dateRecyclerView=findViewById(R.id.dateRecyclerView);
        dateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        dateRecyclerView.addItemDecoration(itemDecoration);
        dateRecyclerView.setLayoutManager(llm);
        dateRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), dateRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position)
                    {
                        mCanteenItemShowArrayList.clear();
                      for(int i=0;i<mDateArrayList.size();i++)
                      {
                          if (mDateArrayList.get(i).isDateSelected())
                          {
                              mDateArrayList.get(i).setDateSelected(false);
                          }
                      }
                        mDateArrayList.get(position).setDateSelected(true);
                        dateRecyclerAdapter=new DateRecyclerAdapter(mContext,mDateArrayList);
                        dateRecyclerView.setAdapter(dateRecyclerAdapter);
                        for (int i=0;i<mCategoryArrayList.size();i++)
                        {
                            if (i==0)
                            {
                                mCategoryArrayList.get(0).setCategorySelected(true);
                            }
                            else
                            {
                                mCategoryArrayList.get(i).setCategorySelected(false);
                            }
                        }
                        CategoryRecyclerAdpter madapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
                        categoryRecyclerView.setAdapter(madapter);
                        selectedCategory=mCategoryArrayList.get(0).getCategoryName();
                        selectedCategoryID=mCategoryArrayList.get(0).getId();
                        selectedDate=mDateArrayList.get(position).getDate();
                        for (int i=0;i<mCanteenItemMainArrayList.size();i++)
                        {
                            if (selectedDate.equalsIgnoreCase(mCanteenItemMainArrayList.get(i).getDate()))
                            {
                                System.out.println("Date matches in"+i);
                                if (mCanteenItemMainArrayList.get(i).getItems_count_in_date()!=0)
                                {
                                    for (int j=0;j<mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().size();j++)
                                    {
                                        if (selectedCategory.equalsIgnoreCase(mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getCategory_name()))
                                        {
                                            System.out.println("Date matches in secondd hh"+j);
                                            System.out.println("List works"+mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getItems_count_in_category());

                                            if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getItems_count_in_category()!=0)
                                            {
                                                for (int k=0;k<mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().size();k++)
                                                {
                                                    if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getItem_category_id().equalsIgnoreCase(selectedCategoryID))

                                                    {
                                                        if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getAvailable_date().equalsIgnoreCase(selectedDate))
                                                        {
                                                            System.out.println("Date matches in secondd ddd"+k);
                                                            System.out.println("array size at end"+mCanteenItemDetailArrayList.size());
                                                            System.out.println("Item names"+mCanteenItemDetailArrayList.get(k).getItem_name());
                                                            System.out.println("Item names date"+mCanteenItemDetailArrayList.get(k).getAvailable_date());
                                                            CanteenDetailShowModel mModel=new CanteenDetailShowModel();
                                                            mModel.setId(mCanteenItemDetailArrayList.get(k).getId());
                                                            mModel.setItem_name(mCanteenItemDetailArrayList.get(k).getItem_name());
                                                            mModel.setAvailable_date(mCanteenItemDetailArrayList.get(k).getAvailable_date());
                                                            mModel.setAvailable_quantity(mCanteenItemDetailArrayList.get(k).getAvailable_quantity());
                                                            mModel.setUnit(mCanteenItemDetailArrayList.get(k).getUnit());
                                                            mModel.setBar_code(mCanteenItemDetailArrayList.get(k).getBar_code());
                                                            mModel.setBarcode_qty(mCanteenItemDetailArrayList.get(k).getBarcode_qty());
                                                            mModel.setProfit_margin(mCanteenItemDetailArrayList.get(k).getProfit_margin());
                                                            mModel.setPrice(mCanteenItemDetailArrayList.get(k).getPrice());
                                                            mModel.setItem_category_id(mCanteenItemDetailArrayList.get(k).getItem_category_id());
                                                            mModel.setStatus(mCanteenItemDetailArrayList.get(k).getStatus());
                                                            mModel.setItem_image(mCanteenItemDetailArrayList.get(k).getItem_image());
                                                            if (mCartDateArrayList.size()>0)
                                                            {
                                                                for (int m=0;m<mCartDateArrayList.size();m++)
                                                                {
                                                                    if (selectedDate.equalsIgnoreCase(mCartDateArrayList.get(m).getDelivery_date()))
                                                                    {
                                                                        for (int n=0;n<mCartDateArrayList.get(m).getmCartItemDetailArrayList().size();n++)
                                                                        {
                                                                            if (mCartDateArrayList.get(m).getmCartItemDetailArrayList().get(n).getItem_id().equalsIgnoreCase(mCanteenItemDetailArrayList.get(k).getId()))
                                                                            {
                                                                                mModel.setCartStatus(true);
                                                                                mModel.setItemQuantity(mCartDateArrayList.get(m).getmCartItemDetailArrayList().get(n).getQuantity());
                                                                            }
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        mModel.setCartStatus(false);
                                                                        mModel.setItemQuantity("0");

                                                                    }
                                                                }
                                                            }
                                                            else
                                                            {
                                                                mModel.setCartStatus(false);
                                                                mModel.setItemQuantity("0");
                                                            }
                                                            if (mCanteenItemShowArrayList.size()==0)
                                                            {
                                                                mCanteenItemShowArrayList.add(mModel);
                                                            }
                                                            else
                                                            {
                                                                boolean isIDFound=false;
                                                                for (int l=0;l<mCanteenItemShowArrayList.size();l++)
                                                                {
                                                                    String canteenListId=mCanteenItemShowArrayList.get(l).getId();
                                                                    String addingID=mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getId();
                                                                    if (canteenListId.equalsIgnoreCase(addingID))
                                                                    {
                                                                        isIDFound=true;
                                                                    }
                                                                }
                                                                if (!isIDFound)
                                                                {
                                                                    mCanteenItemShowArrayList.add(mModel);
                                                                }
                                                            }

                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }
                        if (mCanteenItemShowArrayList.size()>0)
                        {
                            ItemRecyclerView.setVisibility(View.VISIBLE);
                            noItemImg.setVisibility(View.GONE);
                            CanteenItemRecyclerAdapter adapter = new CanteenItemRecyclerAdapter(mContext,mCanteenItemShowArrayList,selectedDate,ordered_user_type,student_id,parent_id,staff_id,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,dateList);
                            ItemRecyclerView.setAdapter(adapter);
                        }
                        else
                        {
                            ItemRecyclerView.setVisibility(View.GONE);
                            noItemImg.setVisibility(View.VISIBLE);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
//        ItemRecyclerView.setHasFixedSize(true);
//        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        ItemRecyclerView.setLayoutManager(linearLayoutManager);
        ItemRecyclerView=findViewById(R.id.ItemRecyclerView);
        ItemRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lli = new LinearLayoutManager(mContext);
        lli.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingItem = 5; // 50px
        ItemOffsetDecoration itemDecorationItem = new ItemOffsetDecoration(mContext,spacingItem);
        ItemRecyclerView.addItemDecoration(itemDecorationItem);
        ItemRecyclerView.setLayoutManager(lli);

        categoryRecyclerView=findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llc = new LinearLayoutManager(mContext);
        llc.setOrientation(LinearLayoutManager.HORIZONTAL);
        int spacingCategory = 5; // 50px
        ItemOffsetDecoration itemDecorationCategory = new ItemOffsetDecoration(mContext,spacingCategory);
        categoryRecyclerView.addItemDecoration(itemDecorationCategory);
        categoryRecyclerView.setLayoutManager(llc);
        categoryRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), categoryRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        selectedCategory=mCategoryArrayList.get(position).getCategoryName();
                        selectedCategoryID=mCategoryArrayList.get(position).getId();
                        mCanteenItemShowArrayList.clear();
                      for (int i=0;i<mCategoryArrayList.size();i++)
                      {
                          if (mCategoryArrayList.get(i).isCategorySelected())
                          {
                              mCategoryArrayList.get(i).setCategorySelected(false);
                          }
                      }
                       mCategoryArrayList.get(position).setCategorySelected(true);
                        CategoryRecyclerAdpter madapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
                        categoryRecyclerView.setAdapter(madapter);
                        System.out.println(" Selected date in category click"+selectedDate);
                        for (int i=0;i<mCanteenItemMainArrayList.size();i++)
                        {
                            if (selectedDate.equalsIgnoreCase(mCanteenItemMainArrayList.get(i).getDate()))
                            {
                                System.out.println("Date matches in"+i);
                                if (mCanteenItemMainArrayList.get(i).getItems_count_in_date()!=0)
                                {
                                    for (int j=0;j<mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().size();j++)
                                    {
                                        if (selectedCategory.equalsIgnoreCase(mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getCategory_name()))
                                        {
                                            System.out.println("Date matches in secondd hh"+j);
                                            System.out.println("List works"+mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getItems_count_in_category());

                                            if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getItems_count_in_category()!=0)
                                            {
                                                for (int k=0;k<mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().size();k++)
                                                {
                                                    if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getItem_category_id().equalsIgnoreCase(selectedCategoryID))
                                                    {
                                                        if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getAvailable_date().equalsIgnoreCase(selectedDate))
                                                        {
                                                            System.out.println("Date matches in secondd ddd"+k);
                                                            System.out.println("array size at end"+mCanteenItemDetailArrayList.size());
                                                            System.out.println("Item names"+mCanteenItemDetailArrayList.get(k).getItem_name());
                                                            System.out.println("Item names date"+mCanteenItemDetailArrayList.get(k).getAvailable_date());
                                                            CanteenDetailShowModel mModel=new CanteenDetailShowModel();
                                                            mModel.setId(mCanteenItemDetailArrayList.get(k).getId());
                                                            mModel.setItem_name(mCanteenItemDetailArrayList.get(k).getItem_name());
                                                            mModel.setAvailable_date(mCanteenItemDetailArrayList.get(k).getAvailable_date());
                                                            mModel.setAvailable_quantity(mCanteenItemDetailArrayList.get(k).getAvailable_quantity());
                                                            mModel.setUnit(mCanteenItemDetailArrayList.get(k).getUnit());
                                                            mModel.setBar_code(mCanteenItemDetailArrayList.get(k).getBar_code());
                                                            mModel.setBarcode_qty(mCanteenItemDetailArrayList.get(k).getBarcode_qty());
                                                            mModel.setProfit_margin(mCanteenItemDetailArrayList.get(k).getProfit_margin());
                                                            mModel.setPrice(mCanteenItemDetailArrayList.get(k).getPrice());
                                                            mModel.setItem_category_id(mCanteenItemDetailArrayList.get(k).getItem_category_id());
                                                            mModel.setStatus(mCanteenItemDetailArrayList.get(k).getStatus());
                                                            mModel.setItem_image(mCanteenItemDetailArrayList.get(k).getItem_image());
                                                            if (mCartDateArrayList.size()>0)
                                                            {
                                                                for (int m=0;m<mCartDateArrayList.size();m++)
                                                                {
                                                                    if (selectedDate.equalsIgnoreCase(mCartDateArrayList.get(m).getDelivery_date()))
                                                                    {
                                                                        for (int n=0;n<mCartDateArrayList.get(m).getmCartItemDetailArrayList().size();n++)
                                                                        {
                                                                            if (mCartDateArrayList.get(m).getmCartItemDetailArrayList().get(n).getItem_id().equalsIgnoreCase(mCanteenItemDetailArrayList.get(k).getId()))
                                                                            {
                                                                                mModel.setCartStatus(true);
                                                                                mModel.setItemQuantity(mCartDateArrayList.get(m).getmCartItemDetailArrayList().get(n).getQuantity());
                                                                            }
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        mModel.setCartStatus(false);
                                                                        mModel.setItemQuantity("0");
                                                                    }
                                                                }
                                                            }
                                                            else
                                                            {
                                                                mModel.setCartStatus(false);
                                                                mModel.setItemQuantity("0");
                                                            }
                                                            if (mCanteenItemShowArrayList.size()==0)
                                                            {
                                                                mCanteenItemShowArrayList.add(mModel);
                                                            }
                                                            else
                                                            {
                                                                boolean isIDFound=false;
                                                                for (int l=0;l<mCanteenItemShowArrayList.size();l++)
                                                                {
                                                                    String canteenListId=mCanteenItemShowArrayList.get(l).getId();
                                                                    String addingID=mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getId();
                                                                    if (canteenListId.equalsIgnoreCase(addingID))
                                                                    {
                                                                        isIDFound=true;
                                                                    }
                                                                }
                                                                if (!isIDFound)
                                                                {
                                                                    mCanteenItemShowArrayList.add(mModel);
                                                                }
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }

                        }

                        if (mCanteenItemShowArrayList.size()>0)
                        {
                            ItemRecyclerView.setVisibility(View.VISIBLE);
                            noItemImg.setVisibility(View.GONE);
                            CanteenItemRecyclerAdapter adapter = new CanteenItemRecyclerAdapter(mContext,mCanteenItemShowArrayList,selectedDate,ordered_user_type,student_id,parent_id,staff_id,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,dateList);
                            ItemRecyclerView.setAdapter(adapter);
                        }
                        else
                        {
                            ItemRecyclerView.setVisibility(View.GONE);
                            noItemImg.setVisibility(View.VISIBLE);
                        }

                        }


                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        if (mDateArrayList.size()==1)
        {
            singleDateViewTxt.setVisibility(View.VISIBLE);
            dateRecyclerView.setVisibility(View.GONE);
            singleDateViewTxt.setText(AppUtils.dateConversionY(mDateArrayList.get(0).getDate()));
        }
        else
        {
            for (int i=0;i<mDateArrayList.size();i++)
            {
                if (i==0)
                {
                    mDateArrayList.get(0).setItemSelected(false);
                    mDateArrayList.get(0).setDateSelected(true);
                }
            }
            dateRecyclerView.setVisibility(View.VISIBLE);
            singleDateViewTxt.setVisibility(View.GONE);
            dateRecyclerAdapter=new DateRecyclerAdapter(mContext,mDateArrayList);
            dateRecyclerView.setAdapter(dateRecyclerAdapter);
        }
        cartBtnLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ItemListActivity.this,CartActivity.class);
                intent.putExtra("ordered_user_type","1");
                intent.putExtra("tab_type","Cart");
                intent.putExtra("student_id",student_id);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id","");
                startActivity(intent);
            }
        });

    }
    public static void getItemList(final String URL,String dateList,String selectedCategory,String selectedCategoryID,String selectedDate,ArrayList<DateModel>mDateArrayList,ArrayList<CategoryModel>mCategoryArrayList)
    {
        try {
            mCanteenItemMainArrayList = new ArrayList<>();

            mCanteenDetailArrayList = new ArrayList<>();
            mCanteenItemDetailArrayList = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"dates","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),dateList,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + dateList);
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess: ", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS))
                                    {
                                        JSONArray dataArray=respObject.optJSONArray("data");

                                        if (dataArray.length()>0)
                                        {
                                            for (int i = 0; i < dataArray.length(); i++)
                                            {

                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                CanteenItemDataModel model=new CanteenItemDataModel();
                                                model.setDate(dataObject.optString("date"));
                                                model.setStatus(dataObject.optString("status"));
                                                model.setItems_count_in_date(dataObject.optInt("items_count_in_date"));
                                                JSONArray detailsArray=dataObject.optJSONArray("details");
                                                if (detailsArray.length()>0)
                                                {

                                                    for (int j=0;j<detailsArray.length();j++)
                                                    {
                                                        JSONObject detailObjects = detailsArray.optJSONObject(j);
                                                        CanteenItemDetailModel mModel=new CanteenItemDetailModel();
                                                        mModel.setId(detailObjects.optString("id"));
                                                        mModel.setCategory_name(detailObjects.optString("category_name"));
                                                        mModel.setCategory_image(detailObjects.optString("category_image"));
                                                        mModel.setItems_count_in_category(detailObjects.optInt("items_count_in_category"));

                                                        JSONArray itemsArray =detailObjects.optJSONArray("items");
                                                        if (itemsArray.length()>0)
                                                        {
                                                            for (int k=0;k<itemsArray.length();k++)
                                                            {
                                                                JSONObject itemsObject = itemsArray.optJSONObject(k);
                                                                CanteenDetailModel nModel= new CanteenDetailModel();
                                                                nModel.setId(itemsObject.optString("id"));
                                                                nModel.setItem_name(itemsObject.optString("item_name"));
                                                                nModel.setAvailable_date(itemsObject.optString("available_date"));
                                                                nModel.setAvailable_date(itemsObject.optString("available_date"));
                                                                nModel.setAvailable_quantity(itemsObject.optString("available_quantity"));
                                                                nModel.setUnit(itemsObject.optString("unit"));
                                                                nModel.setBar_code(itemsObject.optString("bar_code"));
                                                                nModel.setProfit_margin(itemsObject.optString("profit_margin"));
                                                                nModel.setPrice(itemsObject.optString("price"));
                                                                nModel.setBarcode_qty(itemsObject.optString("barcode_qty"));
                                                                nModel.setBarcode_qty(itemsObject.optString("barcode_qty"));
                                                                nModel.setItem_category_id(itemsObject.optString("item_category_id"));
                                                                nModel.setStatus(itemsObject.optString("status"));
                                                                nModel.setItem_image(itemsObject.optString("item_image"));
                                                                mCanteenItemDetailArrayList.add(nModel);

                                                            }
                                                            mModel.setmCanteenDetailItemArrayList(mCanteenItemDetailArrayList);
                                                        }

                                                        mCanteenDetailArrayList.add(mModel);

                                                    }
                                                    model.setmCanteenItemdetailArrayList(mCanteenDetailArrayList);
                                                }


                                                mCanteenItemMainArrayList.add(model);

                                            }
                                            System.out.println("Array size mainArray"+mCanteenItemMainArrayList.size());

                                            if (AppUtils.isNetworkConnected(mContext)) {

                                                getCartDetail(URL_CANTEEN_CART_DETAIL,dateList,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList);
                                            } else {
                                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", "Network error occurred. Please check your internet connection and try again later", R.drawable.nonetworkicon, R.drawable.roundred);

                                            }
                                            for (int i=0;i<mCategoryArrayList.size();i++)
                                            {
                                                if (mCategoryArrayList.get(i).getId().equalsIgnoreCase(selectedCategoryID))
                                                {
                                                    mCategoryArrayList.get(i).setCategorySelected(true);
                                                }
                                                else
                                                {
                                                    mCategoryArrayList.get(i).setCategorySelected(false);
                                                }
                                            }
                                            CategoryRecyclerAdpter adapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
                                            categoryRecyclerView.setAdapter(adapter);
                                        }

                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                    }

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getItemList(URL,dateList,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void getCategoryList(final String URL)
    {
        try {
            mCategoryArrayList = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess: ", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS))
                                    {
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                mCategoryArrayList.add(addEventsDetails(dataObject));
                                            }

                                            CategoryRecyclerAdpter adapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
                                            categoryRecyclerView.setAdapter(adapter);
                                            System.out.println("date List"+dateList);
                                            if (AppUtils.isNetworkConnected(mContext)) {
                                                selectedCategory =mCategoryArrayList.get(0).getCategoryName();
                                                selectedCategoryID =mCategoryArrayList.get(0).getId();
                                                selectedDate= mDateArrayList.get(0).getDate();
                                                getItemList(URL_CANTEEN_ITEM_LIST,dateList,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList);
                                            } else {
                                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                            }
                                        }

                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                    }

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getCategoryList(URL);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private CategoryModel addEventsDetails(JSONObject obj) {
        CategoryModel model = new CategoryModel();
        try {
            if (obj.has("id"))
            {
                model.setId(obj.optString("id"));
            }
            else
            {
                model.setId("");
            }
            if (obj.has("category_name"))
            {
                model.setCategoryName(obj.optString("category_name"));
            }
            else
            {
                model.setCategoryName("");
            }
            if (obj.has("category_image"))
            {
                model.setCategoryImage(obj.optString("category_image"));
            }
            else
            {
                model.setCategoryImage("");
            }


        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }

    public static void getCartDetail(final String URL,String dateList,String selectedCategory,String selectedCategoryID,String selectedDate,ArrayList<DateModel>mDateArrayList,ArrayList<CategoryModel>mCategoryArrayList)

    {
        try {
            mCartDateArrayList = new ArrayList<>();
            mCartItemArrayList = new ArrayList<>();
            mCanteenItemShowArrayList = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"ordered_user_type","student_id","parent_id","staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),ordered_user_type,student_id,parent_id,staff_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("response cListItem", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS))
                                    {
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                CartItemDateModel mModel=new CartItemDateModel();
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setTotal_amount(dataObject.optInt("total_amount"));
                                                JSONArray itemsArray=dataObject.getJSONArray("items");
                                                for (int j=0;j<itemsArray.length();j++)
                                                {
                                                    JSONObject itemObject=itemsArray.optJSONObject(j);
                                                    CartItemDetailModel model=new CartItemDetailModel();
                                                    model.setId(itemObject.optString("id"));
                                                    model.setItem_id(itemObject.optString("item_id"));
                                                    model.setQuantity(itemObject.optString("quantity"));
                                                    model.setPrice(itemObject.optString("price"));
                                                    model.setItem_name(itemObject.optString("item_name"));
                                                    model.setItem_image(itemObject.optString("item_image"));
                                                    mCartItemArrayList.add(model);

                                                }
                                                mModel.setmCartItemDetailArrayList(mCartItemArrayList);
                                                mCartDateArrayList.add(mModel);
                                            }

                                        }
                                        else {

                                        }
                                        int cartItemCount=0;
                                        float cartAmount=0;
                                        if(mCartDateArrayList.size()>0)
                                        {

                                            for (int i=0;i<mCartDateArrayList.size();i++)
                                            {
                                                cartAmount=cartAmount+mCartDateArrayList.get(i).getTotal_amount();
                                                for (int j=0;j<mCartDateArrayList.get(i).getmCartItemDetailArrayList().size();j++)
                                                {
                                                    cartItemCount=cartItemCount+Integer.parseInt(mCartDateArrayList.get(i).getmCartItemDetailArrayList().get(j).getQuantity());

                                                }

                                            }
                                            cartLinear.setVisibility(View.VISIBLE);
                                            itemCount.setVisibility(View.VISIBLE);
                                            totalAmount.setVisibility(View.VISIBLE);
                                            totalAmount.setText(cartAmount+" AED");
                                            itemCount.setText(String.valueOf(cartItemCount)+" Item");
                                        }
                                        else
                                        {
                                            cartLinear.setVisibility(View.GONE);
                                        }

                                        System.out.println("Selected category name "+selectedCategory+"  date name "+selectedDate);

                                        for (int i=0;i<mCanteenItemMainArrayList.size();i++)
                                        {
                                            if (selectedDate.equalsIgnoreCase(mCanteenItemMainArrayList.get(i).getDate()))
                                            {
                                                System.out.println("Date matches in"+i);

                                                if (mCanteenItemMainArrayList.get(i).getItems_count_in_date()!=0)
                                                {
                                                    for (int j=0;j<mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().size();j++)
                                                    {
                                                        if (selectedCategory.equalsIgnoreCase(mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getCategory_name()))
                                                        {
                                                            System.out.println("Date matches in secondd hh"+j);
                                                            System.out.println("List works"+mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getItems_count_in_category());

                                                            if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getItems_count_in_category()!=0)
                                                            {

                                                                for (int k=0;k<mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().size();k++)
                                                                {

                                                                    if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getItem_category_id().equalsIgnoreCase(selectedCategoryID))
                                                                    {
                                                                        if (mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getAvailable_date().equalsIgnoreCase(selectedDate))
                                                                        {
                                                                            System.out.println("Date matches in secondd ddd"+k);
                                                                            System.out.println("array size at end"+mCanteenItemDetailArrayList.size());
                                                                            System.out.println("Item names"+mCanteenItemDetailArrayList.get(k).getItem_name());
                                                                            System.out.println("Item names date"+mCanteenItemDetailArrayList.get(k).getAvailable_date());
                                                                            CanteenDetailShowModel mModel=new CanteenDetailShowModel();
                                                                            mModel.setId(mCanteenItemDetailArrayList.get(k).getId());
                                                                            mModel.setItem_name(mCanteenItemDetailArrayList.get(k).getItem_name());
                                                                            mModel.setAvailable_date(mCanteenItemDetailArrayList.get(k).getAvailable_date());
                                                                            mModel.setAvailable_quantity(mCanteenItemDetailArrayList.get(k).getAvailable_quantity());
                                                                            mModel.setUnit(mCanteenItemDetailArrayList.get(k).getUnit());
                                                                            mModel.setBar_code(mCanteenItemDetailArrayList.get(k).getBar_code());
                                                                            mModel.setBarcode_qty(mCanteenItemDetailArrayList.get(k).getBarcode_qty());
                                                                            mModel.setProfit_margin(mCanteenItemDetailArrayList.get(k).getProfit_margin());
                                                                            mModel.setPrice(mCanteenItemDetailArrayList.get(k).getPrice());
                                                                            mModel.setItem_category_id(mCanteenItemDetailArrayList.get(k).getItem_category_id());
                                                                            mModel.setStatus(mCanteenItemDetailArrayList.get(k).getStatus());
                                                                            mModel.setItem_image(mCanteenItemDetailArrayList.get(k).getItem_image());
                                                                            if (mCartDateArrayList.size()>0)
                                                                            {
                                                                                for (int m=0;m<mCartDateArrayList.size();m++)
                                                                                {
                                                                                    if (selectedDate.equalsIgnoreCase(mCartDateArrayList.get(m).getDelivery_date()))
                                                                                    {
                                                                                        for (int n=0;n<mCartDateArrayList.get(m).getmCartItemDetailArrayList().size();n++)
                                                                                        {
                                                                                            if (mCartDateArrayList.get(m).getmCartItemDetailArrayList().get(n).getItem_id().equalsIgnoreCase(mCanteenItemDetailArrayList.get(k).getId()))
                                                                                            {
                                                                                                System.out.println("It works inside cart empty 123");
                                                                                                System.out.println("It works inside cart empty 123789"+mCartDateArrayList.get(m).getmCartItemDetailArrayList().get(n).getQuantity());
                                                                                                mModel.setCartStatus(true);
                                                                                                mModel.setItemQuantity(mCartDateArrayList.get(m).getmCartItemDetailArrayList().get(n).getQuantity());
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        System.out.println("It works inside cart empty145");
                                                                                        mModel.setCartStatus(false);
                                                                                        mModel.setItemQuantity("0");
                                                                                    }
                                                                                }
                                                                            }
                                                                            else
                                                                            {
                                                                                System.out.println("It works inside cart empty");
                                                                                mModel.setCartStatus(false);
                                                                                mModel.setItemQuantity("0");
                                                                            }
                                                                            if (mCanteenItemShowArrayList.size()==0)
                                                                            {
                                                                                mCanteenItemShowArrayList.add(mModel);
                                                                            }
                                                                            else
                                                                            {
                                                                                boolean isIDFound=false;
                                                                                for (int l=0;l<mCanteenItemShowArrayList.size();l++)
                                                                                {
                                                                                    String canteenListId=mCanteenItemShowArrayList.get(l).getId();
                                                                                    String addingID=mCanteenItemMainArrayList.get(i).getmCanteenItemdetailArrayList().get(j).getmCanteenDetailItemArrayList().get(k).getId();
                                                                                    if (canteenListId.equalsIgnoreCase(addingID))
                                                                                    {
                                                                                        isIDFound=true;
                                                                                    }
                                                                                }
                                                                                if (!isIDFound)
                                                                                {
                                                                                    mCanteenItemShowArrayList.add(mModel);
                                                                                }
                                                                            }


                                                                        }


                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                            }

                                        }

                                        if (mCanteenItemShowArrayList.size()>0)
                                        {
                                            ItemRecyclerView.setVisibility(View.VISIBLE);
                                            noItemImg.setVisibility(View.GONE);
                                            CanteenItemRecyclerAdapter adapter = new CanteenItemRecyclerAdapter(mContext,mCanteenItemShowArrayList,selectedDate,ordered_user_type,student_id,parent_id,staff_id,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,dateList);
                                            ItemRecyclerView.setAdapter(adapter);
                                        }
                                        else
                                        {
                                            ItemRecyclerView.setVisibility(View.GONE);
                                            noItemImg.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                    }

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getCartDetail(URL,dateList,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void fromAdapter(String selectedDate,String selectedCategory)
    {
        dateRecyclerAdapter=new DateRecyclerAdapter(mContext,mDateArrayList);
        dateRecyclerView.setAdapter(dateRecyclerAdapter);
        CategoryRecyclerAdpter madapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
        categoryRecyclerView.setAdapter(madapter);

    }
}

