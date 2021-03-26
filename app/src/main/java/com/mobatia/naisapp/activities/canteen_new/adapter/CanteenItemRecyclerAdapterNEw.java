package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.BasketListActivity;
import com.mobatia.naisapp.activities.canteen_new.ItemListActivity;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenDetailShowModel;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenItemListDataModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CategoryModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.home.adapter.ImagePagerDrawableAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CanteenItemRecyclerAdapterNEw extends RecyclerView.Adapter<CanteenItemRecyclerAdapterNEw.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<CanteenItemListDataModel> mCartDetailArrayList;
    String ordered_user_type="";
    String student_id="";
    String parent_id="";
    String staff_id="";
    String selectedCategory="";
    String selectedCategoryID="";
    String selectedDate="";
    String canteen_cart_id="";
    String quantity="";
    LinearLayout cartLinear;
    TextView itemCount;
    TextView totalAmount;
    int cart_totoal=0;
    int total_items_in_cart=0;
    ArrayList<CartItemDetailModel> mCartItemArrayList;
    ArrayList<CartItemDateModel> mCartDateArrayList;
    ArrayList<String> homeBannerUrlImageArray;
    OnBottomReachedListener onBottomReachedListener;
    int currentPage = 0;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button cartStatusImg;
        TextView itemNameTxt,amountTxt,notAvailableTxt,itemDescription,confirmedTxt;
        ImageView itemImage;
        LinearLayout addLinear,multiLinear;
        ElegantNumberButton itemCount;
        ViewPager bannerImagePager;

        public MyViewHolder(View view) {
            super(view);

//            cartStatusImg = (Button) view.findViewById(R.id.cartStatusImg);
            itemNameTxt = (TextView) view.findViewById(R.id.itemNameTxt);
            amountTxt = (TextView) view.findViewById(R.id.amountTxt);
            notAvailableTxt = (TextView) view.findViewById(R.id.notAvailableTxt);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            addLinear = (LinearLayout) view.findViewById(R.id.addLinear);
            multiLinear = (LinearLayout) view.findViewById(R.id.multiLinear);
            itemCount=view.findViewById(R.id.itemCount);
            itemDescription=view.findViewById(R.id.itemDescription);
            confirmedTxt=view.findViewById(R.id.confirmedTxt);
            bannerImagePager = (ViewPager) view.findViewById(R.id.bannerImagePager);

        }
    }


    public CanteenItemRecyclerAdapterNEw(Context mContext, ArrayList<CanteenItemListDataModel> mCartDetailArrayList, String ordered_user_type, String student_id, String parent_id, String staff_id, String selectedCategory, String selectedCategoryID, String selectedDate,LinearLayout cartLinear, TextView itemCount,TextView totalAmount) {
        this.mContext = mContext;
        this.mCartDetailArrayList = mCartDetailArrayList;
        this.ordered_user_type = ordered_user_type;
        this.student_id = student_id;
        this.parent_id = parent_id;
        this.staff_id = staff_id;
        this.selectedCategory=selectedCategory;
        this.selectedCategoryID=selectedCategoryID;
        this.selectedDate=selectedDate;
        this.cartLinear=cartLinear;
        this.itemCount=itemCount;
        this.totalAmount=totalAmount;

    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
    @Override
    public CanteenItemRecyclerAdapterNEw.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_canteen_item_recycler_new, parent, false);

        return new CanteenItemRecyclerAdapterNEw.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CanteenItemRecyclerAdapterNEw.MyViewHolder holder, int position) {
        onBottomReachedListener.onBottomReached(position);
        holder.amountTxt.setText(mCartDetailArrayList.get(position).getPrice() + " AED");
        if (mCartDetailArrayList.get(position).getAvailable_quantity().equalsIgnoreCase("0")) {
            holder.notAvailableTxt.setVisibility(View.VISIBLE);
            holder.multiLinear.setVisibility(View.GONE);
            holder.addLinear.setVisibility(View.GONE);
            holder.itemNameTxt.setText(mCartDetailArrayList.get(position).getItem_name());

        } else {
            if (mCartDetailArrayList.get(position).getStatus().equalsIgnoreCase("1"))
            {
                holder.notAvailableTxt.setVisibility(View.GONE);
                holder.itemNameTxt.setText(mCartDetailArrayList.get(position).getItem_name());
                if (mCartDetailArrayList.get(position).isItemCart())
                {
                    holder.multiLinear.setVisibility(View.VISIBLE);
                    holder.addLinear.setVisibility(View.GONE);
                    holder.itemCount.setNumber(mCartDetailArrayList.get(position).getQuantityCart());
                    holder.itemCount.setRange(0,Integer.parseInt(mCartDetailArrayList.get(position).getAvailable_quantity()));
                }
                else
                {
                    holder.multiLinear.setVisibility(View.GONE);
                    holder.addLinear.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                holder.notAvailableTxt.setVisibility(View.VISIBLE);
                holder.multiLinear.setVisibility(View.GONE);
                holder.addLinear.setVisibility(View.GONE);
                holder.itemNameTxt.setText(mCartDetailArrayList.get(position).getItem_name());
            }


        }
        if (mCartDetailArrayList.get(position).getItem_already_ordered().equalsIgnoreCase("0"))
        {
            holder.confirmedTxt.setVisibility(View.GONE);
        }
        else
        {
            holder.confirmedTxt.setVisibility(View.VISIBLE);
        }
        holder.itemDescription.setText(mCartDetailArrayList.get(position).getDescription());

        homeBannerUrlImageArray=mCartDetailArrayList.get(position).getItem_image();
        if (homeBannerUrlImageArray != null)
        {

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == homeBannerUrlImageArray.size()) {
                        currentPage = 0;
                        holder.bannerImagePager.setCurrentItem(currentPage,
                                false);
                    } else {

                        holder.bannerImagePager
                                .setCurrentItem(currentPage++, true);
                    }

                }
            };
            final Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 100, 3000);
            holder.bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray, mContext));
        }
        else
        {
            holder.bannerImagePager.setBackgroundResource(R.drawable.noitem);
        }
        holder.addLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddToCart(URL_CANTEEN_ADD_TO_CART,position,"1");
            }
        });

        holder.itemCount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                canteen_cart_id=mCartDetailArrayList.get(position).getCartItemId();
                quantity=String.valueOf(newValue);
                System.out.println("Click works");
                if (AppUtils.isNetworkConnected(mContext))
                 {
                    if (newValue!=0)
                    {
                        if (newValue==Integer.valueOf(mCartDetailArrayList.get(position).getAvailable_quantity()))
                        {
                            Toast.makeText(mContext, "You have reached the Pre-Order limit for this item", Toast.LENGTH_SHORT).show();
                        }
                        //getCartUpdate(URL_CANTEEN_CONFIRMED_ORDER_EDIT,canteen_cart_id,quantity);
                      //  getAddToCart(URL_CANTEEN_ADD_TO_CART,position,String.valueOf(newValue));
                        getCartUpdate(URL_CANTEEN_CART_UPDATE,canteen_cart_id,mCartDetailArrayList.get(position).getAvailable_date(),ordered_user_type,mCartDetailArrayList.get(position).getId(),quantity,mCartDetailArrayList.get(position).getPrice(),position);
                    }
                    else
                    {
                        //getCartCancel(URL_CANTEEN_CONFIRMED_ORDER_ITEM_CELL_CANCEL,canteen_cart_id);

                        getCartCancel(URL_CANTEEN_REMOVE_CART_ITEM,mCartDetailArrayList.get(position).getCartItemId(),position);
                    }

                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        });

    }


    @Override
    public int getItemCount() {

        System.out.println("Item list size"+mCartDetailArrayList.size());
        return mCartDetailArrayList.size();
    }

    public  void getAddToCart(final String URL,int position,String quantity)
    {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"delivery_date","ordered_user_type","student_id","parent_id","staff_id","item_id","quantity","price","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),mCartDetailArrayList.get(position).getAvailable_date(),ordered_user_type,student_id,parent_id,staff_id,mCartDetailArrayList.get(position).getId(),quantity,mCartDetailArrayList.get(position).getPrice(),"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess uItem", successResponse);
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
                                      //  Toast.makeText(mContext,"Item Successfully added to cart",Toast.LENGTH_SHORT).show();
                                        mCartDetailArrayList.get(position).setQuantityCart(quantity);
                                        mCartDetailArrayList.get(position).setItemCart(true);
                                        getCartDetail(URL_CANTEEN_CART_DETAIL,position);
                                        notifyDataSetChanged();
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
                                    getAddToCart(URL,position,quantity);

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
    public  void getCartCancel(final String URL,String canteen_cart_id,int position)
    {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_cart_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess uItem", successResponse);
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
                                        mCartDetailArrayList.get(position).setItemCart(false);
                                        mCartDetailArrayList.get(position).setQuantityCart("0");
                                        mCartDetailArrayList.get(position).setCartItemId(canteen_cart_id);
                                        getCartDetail(URL_CANTEEN_CART_DETAIL,position);
                                        notifyDataSetChanged();
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
                                    getCartCancel(URL,canteen_cart_id,position);

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
    public void getCartDetail(final String URL,int position)

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
                                            itemCount.setText(String.valueOf(total_items_in_cart)+" Items");
                                            totalAmount.setText(String.valueOf(cart_totoal)+" AED");
                                        }
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        System.out.println("JSON id it found cart 1111");
                                        if (dataArray.length()>0)
                                        {
                                            System.out.println("JSON id it found cart 2222");
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                CartItemDateModel mModel=new CartItemDateModel();
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setTotal_amount(dataObject.optInt("total_amount"));
                                                System.out.println("JSON id it found cart 3333");
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
                                                        System.out.println("JSON id it found cart 4444");
                                                        mCartItemArrayList.add(model);
                                                    }
                                                }
                                                mModel.setmCartItemDetailArrayList(mCartItemArrayList);
                                                mCartDateArrayList.add(mModel);

                                            }



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
                                                            System.out.println(" JSON id"+mCartDetailArrayList.get(position).getId()+"  item id in cart "+mCartDateArrayList.get(n).getmCartItemDetailArrayList().get(m).getItem_id()+"  id in cart "+mCartDateArrayList.get(n).getmCartItemDetailArrayList().get(m).getId());
                                                            if (mCartDetailArrayList.get(position).getId().equalsIgnoreCase(mCartDateArrayList.get(n).getmCartItemDetailArrayList().get(m).getItem_id()))
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
                                                System.out.println("It eneter inside isCart Found");
                                                mCartDetailArrayList.get(position).setItemCart(true);
                                                mCartDetailArrayList.get(position).setCartItemId(mCartDateArrayList.get(cartDatePos).getmCartItemDetailArrayList().get(cartItemPos).getId());
                                                mCartDetailArrayList.get(position).setQuantityCart(mCartDateArrayList.get(cartDatePos).getmCartItemDetailArrayList().get(cartItemPos).getQuantity());
//                                                nModel.setQuantityCart(mCartDateArrayList.get(cartDatePos).getmCartItemDetailArrayList().get(cartItemPos).getQuantity());
//                                                nModel.setCartItemId(mCartDateArrayList.get(cartDatePos).getmCartItemDetailArrayList().get(cartItemPos).getId());
//                                                nModel.setItemCart(true);

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
                                    getCartDetail(URL,position);

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

    public  void getCartUpdate(final String URL,String canteen_cart_id,String delivery_date,String ordered_user_type,String item_id,String quantity,String price,int position)
    {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_cart_id","delivery_date","ordered_user_type","student_id","parent_id","staff_id","item_id","quantity","price","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,delivery_date,ordered_user_type,student_id,parent_id,staff_id,item_id,quantity,price,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            System.out.println("Values :::: canteen_cart_id" + canteen_cart_id+ "delivery_date " + delivery_date+" item_id "+item_id+" quantity "+quantity+" price "+price);
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess uItem", successResponse);
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

                                       //Toast.makeText(mContext,"Item Successfully added to basket",Toast.LENGTH_SHORT).show();
                                        mCartDetailArrayList.get(position).setQuantityCart(quantity);
                                        mCartDetailArrayList.get(position).setItemCart(true);
                                        mCartDetailArrayList.get(position).setCartItemId(canteen_cart_id);
                                        getCartDetail(URL_CANTEEN_CART_DETAIL,position);
                                        notifyDataSetChanged();
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
                                    getCartUpdate(URL,canteen_cart_id,delivery_date,ordered_user_type,item_id,quantity,price,position);

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
}
