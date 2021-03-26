
package com.mobatia.naisapp.volleywrappermanager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mobatia.naisapp.R;


public class CustomDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private String errorMessage;
    private String errortitle;
    private Button btn_ok;
    private PositiveBtnClick pvtBtnClick;

    public CustomDialog(Context context, String errortitle, String errormessage, PositiveBtnClick pvtBtnClck) {
        super(context);
        this.errorMessage = errormessage;
        this.errortitle = errortitle;
        this.context = context;
        this.pvtBtnClick = pvtBtnClck;
    }

    public CustomDialog(Context context, String errortitle, String errormessage) {
        super(context);
        this.errortitle = errortitle;
        this.errorMessage = errormessage;
        this.context = context;
        this.pvtBtnClick = null;
    }



    public CustomDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        setContentView(R.layout.confirmation_layout_dialog);
        initView();
    }


    private void initView() {
        TextView tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setText(errorMessage);
        TextView tvErrorTitle = (TextView) findViewById(R.id.txt_titlename);
        tvErrorTitle.setText(errortitle);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_ok) {
            if (pvtBtnClick != null) {
                pvtBtnClick.btnClicked();
            }
            dismiss();
        }
    }

    public interface PositiveBtnClick {
        public void btnClicked();
    }
}

