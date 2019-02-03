package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;

/**
 * Created by Faltenreich on 17.09.2015.
 */
public class TintImageView extends AppCompatImageView {

    public TintImageView(Context context) {
        super(context);
    }

    public TintImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TintImageView);
        try {
            setTintColor(typedArray.getColor(R.styleable.TintImageView_tintColor, 0));
        } finally {
            typedArray.recycle();
        }
    }

    public void setTintColor(@ColorInt int color) {
        setColorFilter(color);
    }
}
