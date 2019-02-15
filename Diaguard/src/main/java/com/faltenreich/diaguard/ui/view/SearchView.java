package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.faltenreich.diaguard.R;

import androidx.annotation.Nullable;

public class SearchView extends com.lapism.searchview.SearchView {
    // Workaround: Copy package-private property from library
    private static final float STATE_ARROW = 0.0f;

    public SearchView(Context context) {
        super(context);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true);
        mBackImageView.setBackgroundResource(outValue.resourceId);
    }

    @Override
    public void onClick(View view) {
        if (view == mBackImageView) {
            // Perform onClick listener in any case
            if (mSearchArrow != null && mIsSearchArrowHamburgerState == STATE_ARROW) {
                close(true);
            }
            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick();
            }
        } else {
            super.onClick(view);
        }
    }
}
