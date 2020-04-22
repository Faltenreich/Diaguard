package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.lapism.searchview.SearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchViewDelegate extends SearchView
    implements Searchable, SearchView.OnQueryTextListener, SearchView.OnMenuClickListener {

    // Workaround: Duplicated property from library, since it is package-private
    private static final float STATE_ARROW = 0.0f;

    @BindView(R.id.searchEditText_input) EditText inputField;

    private SearchListener searchListener;

    public SearchViewDelegate(Context context) {
        super(context);
        init();
    }

    public SearchViewDelegate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchViewDelegate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        setBackgroundColor(ColorUtils.getBackgroundPrimary(getContext()));
        setTextColor(ColorUtils.getTextColorPrimary(getContext()));
        setIconColor(ColorUtils.getIconColorPrimary(getContext()));
        setHintColor(ColorUtils.getTextColorTertiary(getContext()));
        setArrowOnly(true);
        overrideRippleEffectForBackButton();

        setOnQueryTextListener(this);
        setOnMenuClickListener(this);
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

    @Override
    public String getQuery() {
        return super.getQuery().toString();
    }

    @Override
    public void setQuery(String query, boolean submit) {
        // Workaround: onQueryTextChange() is called either way, so we disable and re-enable it
        setOnQueryTextListener(null);
        super.setQuery(query, submit);
        setOnQueryTextListener(this);
    }

    @Override
    public void setHint(String hint) {
        super.setHint(hint);
    }

    @Override
    public void setShadow(boolean shadow) {
        super.setShadow(shadow);
    }

    @Override
    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    public void focusSearchField() {
        ViewUtils.showKeyboard(inputField);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchListener.onQueryChanged(newText);
        return false;
    }

    @Override
    public void onMenuClick() {
        searchListener.onQueryClosed();
    }
}
