package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchView extends FrameLayout implements Searchable {

    @BindView(R.id.search_view_dependency) SearchViewDependency dependency;

    private SearchListenerAdapter listenerAdapter;

    private String hint;
    private boolean showShadow;

    public SearchView(Context context) {
        super(context);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public SearchView(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(attributeSet);
    }

    public void setListener(SearchListener listener) {
        listenerAdapter = new SearchListenerAdapter(listener);
        dependency.setOnQueryTextListener(listenerAdapter);
        dependency.setOnMenuClickListener(listenerAdapter);
    }

    private void init(AttributeSet attributeSet) {
        getAttributes(attributeSet);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search, this);
        if (!isInEditMode()) {
            ButterKnife.bind(this);
            initLayout();
        }
    }

    private void getAttributes(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SearchView);
        try {
            hint = typedArray.getString(R.styleable.SearchView_android_hint);
            showShadow = typedArray.getBoolean(R.styleable.SearchView_showShadow, true);
        } finally {
            typedArray.recycle();
        }
    }

    private void initLayout() {
        dependency.setHint(hint);
        dependency.setBackgroundColor(ColorUtils.getBackgroundPrimary(getContext()));
        dependency.setTextColor(ColorUtils.getTextColorPrimary(getContext()));
        dependency.setIconColor(ColorUtils.getIconColorPrimary(getContext()));
        dependency.setHintColor(ColorUtils.getTextColorTertiary(getContext()));
        dependency.setArrowOnly(true);
        dependency.setShadow(showShadow);
    }

    @Override
    public String getQuery() {
        return dependency.getQuery().toString();
    }

    @Override
    public void setQuery(String query, boolean submit) {
        // Workaround: onQueryTextChange() is called either way, so we disable and re-enable it
        dependency.setOnQueryTextListener(null);
        dependency.setQuery(query, submit);
        dependency.setOnQueryTextListener(listenerAdapter);
    }

    @Override
    public void focusSearchField() {
        ViewUtils.showKeyboard(dependency.inputField);
    }
}
