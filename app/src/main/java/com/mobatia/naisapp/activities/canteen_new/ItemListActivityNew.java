package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.adapter.CanteenItemRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.CanteenItemRecyclerAdapterNEw;
import com.mobatia.naisapp.activities.canteen_new.adapter.CategoryRecyclerAdpter;
import com.mobatia.naisapp.activities.canteen_new.adapter.DateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenDetailShowModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenItemDataModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenItemListDataModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenItemListModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CategoryModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemListActivityNew extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    Context mContext;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, btn_history, home,noItemImg;
    Bundle extras;
     TextView singleDateViewTxt;
     ArrayList<DateModel> mDateArrayList;
     String ordered_user_type = "";
     String student_id = "";
     String parent_id = "";
     String staff_id = "";
     String jsonDate = "";
     String itemCountInCategory = "";
    static boolean isAvailable=false;

     RecyclerView categoryRecyclerView;
     RecyclerView dateRecyclerView;
     RecyclerView ItemRecyclerView;

     //Cart View
     LinearLayout cartLinear;
    TextView itemCount, totalAmount;

     ArrayList<CategoryModel>mCategoryArrayList;
     ArrayList<CanteenItemListModel>mCanteenItemArrayList;
     ArrayList<CanteenItemListDataModel>mCanteenItemDataArrayList;
    ArrayList<String>homeBannerUrlImageArray;
    ArrayList<CartItemDetailModel> mCartItemArrayList;
    ArrayList<CartItemDateModel> mCartDateArrayList;

    DateRecyclerAdapter dateRecyclerAdapter;
    CanteenItemRecyclerAdapterNEw mAdapter;

    String selectedCategory="";
    String selectedDate="";
    String selectedCategoryID="";
    String skip="";
    String limit="20";

    int cart_totoal=0;
    int total_items_in_cart=0;
    int skipInt=0;
    boolean isFromBottom=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity_new);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getCategoryList(URL_CANTEEN_CATEGORY_LIST);
            getCartDetail(URL_CANTEEN_CART_DETAIL);
        }
        else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            mDateArrayList = (ArrayList<DateModel>) getIntent().getSerializableExtra("dateArray");
            ordered_user_type = extras.getString("ordered_user_type");
            student_id = extras.getString("student_id");
            parent_id = extras.getString("parent_id");
            staff_id = extras.getString("staff_id");
        }
        isFromBottom=false;
        mCanteenItemDataArrayList=new ArrayList<>();
        System.out.println("ArrayList size date in itemList" + mDateArrayList.size());
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        singleDateViewTxt = (TextView) findViewById(R.id.singleDateViewTxt);
        itemCount = (TextView) findViewById(R.id.itemCount);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        headermanager = new HeaderManager(ItemListActivityNew.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        btn_history.setVisibility(View.INVISIBLE);
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
                Intent intent = new Intent(ItemListActivityNew.this, ConfirmedOrderActivity.class);
                intent.putExtra("ordered_user_type", ordered_user_type);
                intent.putExtra("tab_type", "History");
                intent.putExtra("student_id", student_id);
                intent.putExtra("parent_id", PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id", staff_id);

                startActivity(intent);
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

        //Category Recycler
        categoryRecyclerView=findViewById(R.id.categoryRecyclerView);
        noItemImg=findViewById(R.id.noItemImg);
        cartLinear=findViewById(R.id.cartLinear);
        categoryRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llc = new LinearLayoutManager(mContext);
        llc.setOrientation(LinearLayoutManager.HORIZONTAL);
        int spacingCategory = 5; // 50px
        ItemOffsetDecoration itemDecorationCategory = new ItemOffsetDecoration(mContext,spacingCategory);
        categoryRecyclerView.addItemDecoration(itemDecorationCategory);
        categoryRecyclerView.setLayoutManager(llc);
    //    mCanteenItemDataArrayList.clear();
        categoryRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), categoryRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position)
                    {
                        for (int i=0;i<mCategoryArrayList.size();i++)
                        {
                            if (mCategoryArrayList.get(i).isCategorySelected())
                            {
                                mCategoryArrayList.get(i).setCategorySelected(false);
                            }
                        }
                        mCategoryArrayList.get(position).setCategorySelected(true);
                        CategoryRecyclerAdpter adapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
                        categoryRecyclerView.setAdapter(adapter);
                        categoryRecyclerView.scrollToPosition(position);
                        selectedCategory=mCategoryArrayList.get(position).getCategoryName();
                        selectedCategoryID=mCategoryArrayList.get(position).getId();
                        limit="20";
                        skip="0";
                        mCanteenItemDataArrayList=new ArrayList<>();
                        skipInt=Integer.valueOf(skip);
                        isFromBottom=false;
                        if (AppUtils.isNetworkConnected(mContext)) {
                            getItemList(URL_CANTEEN_ITEM_LIST,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,limit,skip,ordered_user_type,student_id,staff_id);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                        }
                    }
                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));

        //Date Recycler

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
                        for (int i=0;i<mDateArrayList.size();i++)
                        {
                            if (mDateArrayList.get(i).isDateSelected())
                            {
                                mDateArrayList.get(i).setDateSelected(false);
                            }
                        }
                        mDateArrayList.get(position).setDateSelected(true);
                        for (int i=0;i<mCategoryArrayList.size();i++)
                        {
                            if (mCategoryArrayList.get(i).isCategorySelected())
                            {
                                mCategoryArrayList.get(i).setCategorySelected(false);
                            }
                        }
                       // mCanteenItemDataArrayList.clear();
                        mCategoryArrayList.get(0).setCategorySelected(true);
                        dateRecyclerAdapter=new DateRecyclerAdapter(mContext,mDateArrayList);
                        dateRecyclerView.setAdapter(dateRecyclerAdapter);
                        CategoryRecyclerAdpter adapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
                        categoryRecyclerView.setAdapter(adapter);
                        categoryRecyclerView.scrollToPosition(0);
                        selectedDate=mDateArrayList.get(position).getDate();
                        selectedCategory=mCategoryArrayList.get(0).getCategoryName();
                        selectedCategoryID=mCategoryArrayList.get(0).getId();
                        limit="20";
                        skip="0";
                        skipInt=Integer.valueOf(skip);
                        mCanteenItemDataArrayList=new ArrayList<>();
                        isFromBottom=false;
                        if (AppUtils.isNetworkConnected(mContext)) {
                            getItemList(URL_CANTEEN_ITEM_LIST,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,limit,skip,ordered_user_type,student_id,staff_id);
                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                        }
                    }
                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));

        //Item Recycler

        ItemRecyclerView=findViewById(R.id.ItemRecyclerView);
        ItemRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lli = new LinearLayoutManager(mContext);
        lli.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingItem = 5; // 50px
        ItemOffsetDecoration itemDecorationItem = new ItemOffsetDecoration(mContext,spacingItem);
        ItemRecyclerView.addItemDecoration(itemDecorationItem);
        ItemRecyclerView.setLayoutManager(lli);

        cartLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<mDateArrayList.size();i++)
                {
                    if (mDateArrayList.get(i).isDateSelected())
                    {
                        mDateArrayList.get(i).setDateSelected(false);
                    }
                }
                mDateArrayList.get(0).setDateSelected(true);
                selectedDate=mDateArrayList.get(0).getDate();
                selectedCategory=mCategoryArrayList.get(0).getCategoryName();
                selectedCategoryID=mCategoryArrayList.get(0).getId();
                Intent intent=new Intent(ItemListActivityNew.this,BasketListActivity.class);
                intent.putExtra("ordered_user_type",ordered_user_type);
                intent.putExtra("tab_type","Basket");
                intent.putExtra("student_id",student_id);
                intent.putExtra("isFrom","Items");
                intent.putExtra("dateArray",mDateArrayList);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
                finish();
                startActivity(intent);
            }
        });

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
            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

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
                                                if(mCategoryArrayList.size()==0)
                                                {
                                                    mCategoryArrayList.add(addEventsDetails(dataObject));
                                                }
                                                boolean isCatFound=false;
                                                for (int j=0;j<mCategoryArrayList.size();j++)
                                                {
                                                    if (dataObject.optString("id").equalsIgnoreCase(mCategoryArrayList.get(j).getId()))
                                                    {
                                                        isCatFound=true;
                                                    }
                                                }
                                                if(!isCatFound)
                                                {
                                                    mCategoryArrayList.add(addEventsDetails(dataObject));
                                                }
                                            }

                                            CategoryRecyclerAdpter adapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
                                            categoryRecyclerView.setAdapter(adapter);
                                           // System.out.println("date List"+dateList);

                                            selectedCategoryID=mCategoryArrayList.get(0).getId();
                                            selectedCategory=mCategoryArrayList.get(0).getCategoryName();
                                            selectedDate=mDateArrayList.get(0).getDate();
                                            skip="0";
                                            limit="20";
                                            skipInt=0;
                                            mDateArrayList.get(0).setDateSelected(true);
                                            if (mDateArrayList.size()==1)
                                            {
                                                dateRecyclerView.setVisibility(View.GONE);
                                                singleDateViewTxt.setVisibility(View.VISIBLE);
                                                singleDateViewTxt.setText(mDateArrayList.get(0).getDay()+","+AppUtils.dateConversionY(mDateArrayList.get(0).getDate()));
                                            }
                                            else
                                            {
                                                dateRecyclerView.setVisibility(View.VISIBLE);
                                                singleDateViewTxt.setVisibility(View.GONE);
                                                dateRecyclerAdapter=new DateRecyclerAdapter(mContext,mDateArrayList);
                                                dateRecyclerView.setAdapter(dateRecyclerAdapter);
                                            }

                                              mCategoryArrayList.get(0).setCategorySelected(true);
                                            isFromBottom=false;
                                            if (AppUtils.isNetworkConnected(mContext)) {
                                                getItemList(URL_CANTEEN_ITEM_LIST,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,limit,skip,ordered_user_type,student_id,staff_id);
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

    public void getItemList(final String URL,String selectedCategory,String selectedCategoryID,String selectedDate,ArrayList<DateModel>mDateArrayList,ArrayList<CategoryModel>mCategoryArrayList,String limit,String skip,String ordered_user_type,String student_id,String staff_id)
    {
        int type=11;
        if (isFromBottom)
        {
            type=14;
        }
        else
        {
            type=11;
        }

        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
            String[] name = {JTAG_ACCESSTOKEN,"date","category_id","limit","skip","ordered_user_type","parent_id","student_id","staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),selectedDate,selectedCategoryID,limit,skip,ordered_user_type,PreferenceManager.getUserId(mContext),student_id,staff_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) );
            System.out.println("Values date::::" + selectedDate);
            System.out.println("Values category_id::::" + selectedCategoryID);
            System.out.println("Values limit::::" + limit);
            System.out.println("Values skip::::" + skip);
            manager.getResponsePOST(mContext, type, name, value, new VolleyWrapper.ResponseListener()
            {

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
                                        JSONObject dataObject=respObject.getJSONObject("data");
                                        jsonDate =dataObject.optString("date");
                                        itemCountInCategory =dataObject.optString("items_count_in_category");
                                        JSONArray itemArray=dataObject.getJSONArray("items");
                                        if (itemArray.length()>0)
                                        {
                                            if (itemArray.length()==5)
                                            {
                                                isAvailable=true;
                                            }
                                            else
                                            {
                                                isAvailable=false;
                                            }
                                            for (int j=0;j<itemArray.length();j++)
                                            {
                                                        JSONObject itemObject = itemArray.optJSONObject(j);
                                                        CanteenItemListDataModel nModel=new CanteenItemListDataModel();
                                                        nModel.setId(itemObject.optString("id"));
                                                        nModel.setItem_name(itemObject.optString("item_name"));
                                                        nModel.setAvailable_date(itemObject.optString("available_date"));
                                                        nModel.setAvailable_quantity(itemObject.optString("available_quantity"));
                                                        nModel.setUnit(itemObject.optString("unit"));
                                                        nModel.setBar_code(itemObject.optString("bar_code"));
                                                        nModel.setProfit_margin(itemObject.optString("profit_margin"));
                                                        nModel.setPrice(itemObject.optString("price"));
                                                        nModel.setBarcode_qty(itemObject.optString("barcode_qty"));
                                                        nModel.setDescription(itemObject.optString("description"));
                                                        nModel.setItem_category_id(itemObject.optString("item_category_id"));
                                                        nModel.setStatus(itemObject.optString("status"));
                                                        nModel.setItem_already_ordered(itemObject.optString("item_already_ordered"));

                                                        homeBannerUrlImageArray = new ArrayList<>();
                                                        JSONArray imageArray = itemObject.getJSONArray("item_image");
                                                        if (imageArray.length() > 0) {
                                                            for (int m = 0; m < imageArray.length(); m++) {
                                                                homeBannerUrlImageArray.add(imageArray.optString(m));
                                                            }
                                                        }
                                                        nModel.setItem_image(homeBannerUrlImageArray);
                                                       boolean isFound=false;
                                                       int cartDatePos=0;
                                                     int cartItemPos=0;
                                                        if (mCartDateArrayList.size()>0)
                                                        {
                                                            for (int n=0;n<mCartDateArrayList.size();n++)
                                                            {
                                                                if (mCartDateArrayList.get(n).getDelivery_date().equalsIgnoreCase(selectedDate))
                                                                {

                                                                    for (int m=0;m<mCartDateArrayList.get(n).getmCartItemDetailArrayList().size();m++)
                                                                    {
                                                                        System.out.println(" JSON id"+itemObject.optString("id")+"  item id in cart "+mCartDateArrayList.get(n).getmCartItemDetailArrayList().get(m).getItem_id()+"  id in cart "+mCartDateArrayList.get(n).getmCartItemDetailArrayList().get(m).getId());
                                                                        if (itemObject.optString("id").equalsIgnoreCase(mCartDateArrayList.get(n).getmCartItemDetailArrayList().get(m).getItem_id()))
                                                                        {
                                                                            System.out.println("JSON id it found");
                                                                           isFound=true;
                                                                           cartDatePos=n;
                                                                           cartItemPos=m;
                                                                        }
                                                                    }

                                                                }

                                                            }
                                                        }
                                                    if (isFound)
                                                     {
                                                    nModel.setQuantityCart(mCartDateArrayList.get(cartDatePos).getmCartItemDetailArrayList().get(cartItemPos).getQuantity());
                                                    nModel.setCartItemId(mCartDateArrayList.get(cartDatePos).getmCartItemDetailArrayList().get(cartItemPos).getId());
                                                    nModel.setItemCart(true);

                                                    }
                                                    else
                                                     {
                                                    nModel.setQuantityCart("0");
                                                    nModel.setCartItemId("");
                                                    nModel.setItemCart(false);
                                                    }
                                                        if (mCanteenItemDataArrayList.size()==0)
                                                        {
                                                            mCanteenItemDataArrayList.add(nModel);
                                                        }
                                                        else
                                                        {
                                                            String insertID=itemObject.optString("id");
                                                            boolean isFoundItem=false;
                                                            for (int n=0;n<mCanteenItemDataArrayList.size();n++)
                                                            {
                                                                if (mCanteenItemDataArrayList.get(n).getId().equalsIgnoreCase(insertID))
                                                                {
                                                                    isFoundItem=true;
                                                                }
                                                            }
                                                            if (!isFoundItem)
                                                            {
                                                                mCanteenItemDataArrayList.add(nModel);
                                                            }
                                                        }


                                                    }




                                        }
                                        else
                                        {
                                            isAvailable=false;
                                          //  getCartDetail(URL_CANTEEN_CART_DETAIL);

                                        }



                                       if (mCanteenItemDataArrayList.size()>0)
                                       {
                                           ItemRecyclerView.setVisibility(View.VISIBLE);
                                           noItemImg.setVisibility(View.GONE);
                                           mAdapter=new CanteenItemRecyclerAdapterNEw(mContext,mCanteenItemDataArrayList,ordered_user_type,student_id,parent_id,staff_id,selectedCategory,selectedCategoryID,selectedDate,cartLinear,itemCount,totalAmount);
                                           ItemRecyclerView.setAdapter(mAdapter);
                                           if (isFromBottom)
                                           {
                                               int scrollPos=mCanteenItemDataArrayList.size()-20;
                                               ItemRecyclerView.scrollToPosition(scrollPos);
                                           }
                                           mAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                               @Override
                                               public void onBottomReached(int position) {
                                                   if (position==mCanteenItemDataArrayList.size()-1)
                                                   {
                                                       if (isAvailable)
                                                       {
                                                           String  limitValue="20";
                                                           skipInt=skipInt+20;
                                                           isFromBottom=true;
                                                           if (AppUtils.isNetworkConnected(mContext)) {
                                                               getItemList(URL_CANTEEN_ITEM_LIST,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,limitValue,String.valueOf(skipInt),ordered_user_type,student_id,staff_id);
                                                           } else {
                                                               AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                                           }



                                                       }
                                                       else
                                                       {
                                                       }

                                                   }



                                               }

                                           });
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
                                    getItemList(URL_CANTEEN_ITEM_LIST,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,"20",String.valueOf(skipInt),ordered_user_type,student_id,staff_id);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {

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

    public void getCartDetail(final String URL)

    {
        mCartDateArrayList=new ArrayList<>();
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"ordered_user_type","student_id","parent_id","staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),ordered_user_type,student_id,parent_id,staff_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

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

                                        cart_totoal=respObject.optInt("cart_totoal");
                                        total_items_in_cart=respObject.optInt("total_items_in_cart");
                                        if (total_items_in_cart==0)
                                        {
                                            cartLinear.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            cartLinear.setVisibility(View.VISIBLE);
                                            itemCount.setText(String.valueOf(total_items_in_cart)+" items");
                                            totalAmount.setText(String.valueOf(cart_totoal)+" AED");
                                        }
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                CartItemDateModel mModel=new CartItemDateModel();
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setTotal_amount(dataObject.optInt("total_amount"));
                                                mCartItemArrayList=new ArrayList<>();
                                                JSONArray itemsArray=dataObject.getJSONArray("items");
                                                if (itemsArray.length()>0)
                                                {
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
                                                }
                                                mModel.setmCartItemDetailArrayList(mCartItemArrayList);
                                                mCartDateArrayList.add(mModel);



                                            }

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
                                    getCartDetail(URL);

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

    @Override
    protected void onResume() {
        super.onResume();
        for (int i=0;i<mDateArrayList.size();i++)
        {
            if (mDateArrayList.get(i).isDateSelected())
            {
                mDateArrayList.get(i).setDateSelected(false);
            }
        }
        mDateArrayList.get(0).setDateSelected(true);
       if(PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
       {
           ordered_user_type="2";
           parent_id="";
           student_id="";
           staff_id=PreferenceManager.getStaffId(mContext);
       }
       else {
           ordered_user_type="1";
           parent_id=PreferenceManager.getUserId(mContext);
           student_id=PreferenceManager.getCanteenStudentId(mContext);
           staff_id="";
       }
        if (AppUtils.isNetworkConnected(mContext)) {
            mCanteenItemDataArrayList=new ArrayList<>();
            getCategoryList(URL_CANTEEN_CATEGORY_LIST);
            getCartDetail(URL_CANTEEN_CART_DETAIL);
        }
        else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
//        selectedCategoryID=mCategoryArrayList.get(0).getId();
//        selectedDate=mDateArrayList.get(0).getDate();
//        selectedCategory=mCategoryArrayList.get(0).getCategoryName();
//        skip="0";
//        limit="20";
//        if (AppUtils.isNetworkConnected(mContext)) {
//            getItemList(URL_CANTEEN_ITEM_LIST,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList,limit,skip);
//        } else {
//            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//        }
    }
}


