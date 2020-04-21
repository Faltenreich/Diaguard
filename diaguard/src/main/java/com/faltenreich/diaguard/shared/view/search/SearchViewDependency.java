package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.lapism.searchview.SearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchViewDependency extends SearchView {

    // Workaround: Duplicated property from library, since it is package-private
    private static final float STATE_ARROW = 0.0f;

    @BindView(R.id.searchEditText_input) EditText inputField;

    public SearchViewDependency(Context context) {
        super(context);
        init();
    }

    public SearchViewDependency(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchViewDependency(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            ButterKnife.bind(this);
            initLayout();
        }
    }

    private void initLayout() {
        overrideRippleEffectForBackButton();
    }

    private void overrideRippleEffectForBackButton() {
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
