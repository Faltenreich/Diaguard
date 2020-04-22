package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchView extends FrameLayout implements Searchable {

    @BindView(R.id.search_view_dependency) SearchViewDependency dependency;

    public SearchView(Context context) {
        super(context);
        init(null);
    }

    public SearchView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public SearchView(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(attributeSet);
    }

    private void init(@Nullable AttributeSet attributeSet) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search, this);
        if (!isInEditMode()) {
            ButterKnife.bind(this);
            initLayout(attributeSet);
        }
    }

    private void initLayout(@Nullable AttributeSet attributeSet) {
        String hint = null;
        boolean showShadow = true;

        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SearchView);
            try {
                hint = typedArray.getString(R.styleable.SearchView_android_hint);
                showShadow = typedArray.getBoolean(R.styleable.SearchView_showShadow, true);
            } finally {
                typedArray.recycle();
            }
        }

        setHint(hint);
        setShadow(showShadow);
    }

    @Override
    public String getQuery() {
        return dependency.getQuery();
    }

    @Override
    public void setQuery(String query, boolean submit) {
        dependency.setQuery(query, submit);
    }

    @Override
    public void setHint(String hint) {
        dependency.setHint(hint);
    }

    @Override
    public void setShadow(boolean isEnabled) {
        dependency.setShadow(isEnabled);
    }

    @Override
    public void setSearchListener(SearchListener searchListener) {
        dependency.setSearchListener(searchListener);
    }

    @Override
    public void focusSearchField() {
        dependency.focusSearchField();
    }
}
