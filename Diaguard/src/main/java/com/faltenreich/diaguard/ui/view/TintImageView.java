package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.faltenreich.diaguard.R;

import java.util.jar.Attributes;

/**
 * Created by Faltenreich on 17.09.2015.
 */
public class TintImageView extends ImageView {

    public TintImageView(Context context) {
        super(context);
    }

    public TintImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TintImageView);
        try {
            setTintColor(typedArray.getColor(R.styleable.TintImageView_tintColor, 0));
        } finally {
            typedArray.recycle();
        }
    }

    public void setTintColor(@ColorInt int colorResourceId) {
        setColorFilter(colorResourceId);
    }

}
