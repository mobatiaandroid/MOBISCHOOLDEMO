package com.mobatia.naisapp.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by gayatri on 28/3/17.
 */
public class CustomButtonFontSansButtonBold extends Button {

    private Typeface FONT_NAME = null;

    public CustomButtonFontSansButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Bold.otf");

        this.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        this.setTypeface(FONT_NAME);
    }
}
