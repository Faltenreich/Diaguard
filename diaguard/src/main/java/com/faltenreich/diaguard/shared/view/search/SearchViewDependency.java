package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.lapism.searchview.SearchView;

public class SearchViewDependency extends SearchView {

    // Workaround: Duplicated property from library, since it is package-private
    private static final float STATE_ARROW = 0.0f;

    private SearchListener searchListener;

    public SearchViewDependency(Context context) {
        super(context);
    }

    public SearchViewDependency(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchViewDependency(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initLayout();
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public SearchListener getSearchListener() {
        return searchListener;
    }

    private void initLayout() {
        overrideRippleEffectForBackButton();
    }

    private void overrideRippleEffectForBackButton() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true);
        mBackImageView.setBackgroundResource(outValue.resourceId);
    }

    void focusSearchField() {
        ViewUtils.showKeyboard(findViewById(R.id.searchEditText_input));
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
