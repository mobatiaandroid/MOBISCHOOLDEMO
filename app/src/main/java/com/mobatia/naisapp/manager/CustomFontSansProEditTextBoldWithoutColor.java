package com.mobatia.naisapp.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by gayatri on 24/1/17.
 */

public class CustomFontSansProEditTextBoldWithoutColor extends EditText {
    public CustomFontSansProEditTextBoldWithoutColor(Context context) {
        super(context);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Bold.otf" );
        this.setTypeface(type);
    }

    public CustomFontSansProEditTextBoldWithoutColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Bold.otf");
        this.setTypeface(type);

    }

    public CustomFontSansProEditTextBoldWithoutColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Bold.otf" );
        this.setTypeface(type);

    }


}
