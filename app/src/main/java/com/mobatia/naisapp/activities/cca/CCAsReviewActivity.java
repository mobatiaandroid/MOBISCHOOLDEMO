package com.mobatia.naisapp.activities.cca;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.adapter.CCAfinalReviewAdapter;
import com.mobatia.naisapp.activities.cca.model.CCADetailModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

public class CCAsReviewActivity extends Activity implements URLConstants, JSONConstants {
    GridLayoutManager recyclerViewLayoutManager;
    RecyclerView recycler_review;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView back;
    Button submitBtn;
    ImageView home;
    String tab_type = "CCAs";
    Bundle extras;
    Context mContext;
    public ArrayList<CCADetailModel> mCCADetailModelArrayList;
    public ArrayList<String> mCCAItemIdArray;
    TextView textViewCCAaItem;
    String cca_details = "";
    String cca_detailsId = "[";
    CCADetailModel mCCADetailModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cca_review);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        recycler_review = (RecyclerView) findViewById(R.id.recycler_view_cca);
        textViewCCAaItem = (TextView) findViewById(R.id.textViewCCAaItem);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        headermanager = new HeaderManager(CCAsReviewActivity.this, "CCA Summary");//tab_type
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
        recycler_review.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        recycler_review.setLayoutManager(recyclerViewLayoutManager);
        mCCADetailModelArrayList = new ArrayList<>();
        mCCAItemIdArray = new ArrayList<>();
        if (PreferenceManager.getStudClassForCCA(mContext).equalsIgnoreCase("")) {
            textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)));
        } else {
            textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext) + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(mContext)));
        }
        for (int i = 0; i < AppController.weekList.size(); i++) {
            for (int j = 0; j < CCASelectionActivity.CCADetailModelArrayList.size(); j++) {
                if (AppController.weekList.get(i).getWeekDay().equalsIgnoreCase(CCASelectionActivity.CCADetailModelArrayList.get(j).getDay())) {
                    CCADetailModel mCCADetailModel = new CCADetailModel();
                    mCCADetailModel.setDay(CCASelectionActivity.CCADetailModelArrayList.get(j).getDay());
                    mCCADetailModel.setChoice1(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice1());
                    mCCADetailModel.setChoice2(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice2());
                    mCCADetailModel.setChoice1Id(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice1Id());
                    mCCADetailModel.setChoice2Id(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice2Id());
                    for (int k = 0; k < CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().size(); k++)
                        if (CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice1().equalsIgnoreCase(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_name())) {
                            if (CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_start_time() != null && CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_end_time() != null) {
                                mCCADetailModel.setCca_item_start_timechoice1(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_start_time());
                                mCCADetailModel.setCca_item_end_timechoice1(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_end_time());
                                break;
                            }
                        }
                    for (int k = 0; k < CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().size(); k++)
                        if (CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice2().equalsIgnoreCase(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_name())) {
                            if (CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_start_time() != null && CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_end_time() != null) {
                                mCCADetailModel.setCca_item_start_timechoice2(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_start_time());
                                mCCADetailModel.setCca_item_end_timechoice2(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_end_time());
                                break;
                            }
                        }
                    mCCADetailModelArrayList.add(mCCADetailModel);
                    break;
                }
            }
        }
        CCAfinalReviewAdapter mCCAsActivityAdapter = new CCAfinalReviewAdapter(mContext, mCCADetailModelArrayList);
        recycler_review.setAdapter(mCCAsActivityAdapter);
        for (int j = 0; j < mCCADetailModelArrayList.size(); j++) {

            if (mCCADetailModelArrayList.get(j).getChoice1() != null && mCCADetailModelArrayList.get(j).getChoice2() != null) {
                if (!(mCCADetailModelArrayList.get(j).getChoice1Id().equalsIgnoreCase("-541")) && !(mCCADetailModelArrayList.get(j).getChoice2Id().equalsIgnoreCase("-541"))) {

                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice1Id());
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice2Id());
                } else if (!mCCADetailModelArrayList.get(j).getChoice1Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice1Id());

                } else if (!mCCADetailModelArrayList.get(j).getChoice2Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice2Id());

                }
            } else if (mCCADetailModelArrayList.get(j).getChoice1() != null) {
                if (!mCCADetailModelArrayList.get(j).getChoice1Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice1Id());

                }
            } else if (mCCADetailModelArrayList.get(j).getChoice2() != null) {
                if (!mCCADetailModelArrayList.get(j).getChoice2Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice2Id());

                }
            }


        }

        if (mCCAItemIdArray.size() == 0) {
            cca_detailsId += "]}";

        }
        for (int i = 0; i < mCCAItemIdArray.size(); i++) {

            if ((mCCAItemIdArray.size() - 1) == 0) {
                cca_detailsId += "\"" + mCCAItemIdArray.get(i).toString() + "\"]}";

            } else if (i == mCCAItemIdArray.size() - 1) {
                cca_detailsId += mCCAItemIdArray.get(i).toString() + "\"]}";
            } else if (i == 0) {
                cca_detailsId += "\"" + mCCAItemIdArray.get(i).toString() + "\",\"";

            } else {
                cca_detailsId += mCCAItemIdArray.get(i).toString() + "\",\"";

            }
        }
        cca_details = "{\"cca_days_id\":\"" + PreferenceManager.getCCAItemId(mContext) + "\",\"student_id\":\"" + PreferenceManager.getStudIdForCCA(mContext) + "\",\"users_id\":\"" + PreferenceManager.getUserId(mContext) + "\",\"cca_days_details_id\":" + cca_detailsId;

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogReviewSubmit((Activity) mContext, "Confirm", "", R.drawable.exclamationicon, R.drawable.round);

                }
        });
    }

    private void ccaSubmitAPI() {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_CCA_SUBMIT);
        String[] name = {"access_token", "cca_details"};
        String[] value = {PreferenceManager.getAccessToken(mContext), cca_details};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            showDialogAlert((Activity) mContext, "Success", "You are able to make changes until the closing date. After the closing date selections are final", R.drawable.tickicon, R.drawable.round);


                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaSubmitAPI();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaSubmitAPI();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaSubmitAPI();

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }

    public void showDialogAlert(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {

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
                Intent intent = new Intent(mContext, CCA_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        dialog.show();

    }

    public void showDialogReviewSubmit(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
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
                dialog.dismiss();
                if (AppUtils.isNetworkConnected(mContext)) {
                    ccaSubmitAPI();
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }


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
}
