package com.mobatia.naisapp.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mobatia.naisapp.R;


/**
 * Created by gayatri on 24/1/17.
 */

public class CustomFontSansProTextSplitColorBold extends TextView {
    public CustomFontSansProTextSplitColorBold(Context context) {
        super(context);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Bold.otf" );
        this.setTypeface(type);
        this.setTextColor(context.getResources().getColor(R.color.split_bg));
    }

    public CustomFontSansProTextSplitColorBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Bold.otf");
        this.setTypeface(type);
       this.setTextColor(context.getResources().getColor(R.color.split_bg));

    }

    public CustomFontSansProTextSplitColorBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/SourceSansPro-Bold.otf" );
        this.setTypeface(type);
        this.setTextColor(context.getResources().getColor(R.color.split_bg));

    }


}
