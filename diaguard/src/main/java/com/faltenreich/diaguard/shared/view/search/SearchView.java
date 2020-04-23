package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchView extends FrameLayout implements Searchable {

    @BindView(R.id.backIcon)
    ImageView backIcon;

    @BindView(R.id.inputField)
    AutoCompleteTextView inputField;

    @BindView(R.id.actionIcon)
    ImageView actionIcon;

    private SearchViewListener listener;

    private String hint;

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
            getAttributes(attributeSet);
            initLayout();
        }
    }

    private void getAttributes(@Nullable AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SearchView);
        try {
            hint = typedArray.getString(R.styleable.SearchView_android_hint);
        } finally {
            typedArray.recycle();
        }

    }

    private void initLayout() {
        setHint(hint);

    }

    @Override
    public String getQuery() {
        return inputField.getText().toString();
    }

    @Override
    public void setQuery(String query, boolean submit) {
        inputField.setText(query);
    }

    @Override
    public void setHint(String hint) {
        inputField.setHint(hint);
    }

    @Override
    public void setSearchListener(SearchViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void setSuggestions(List<String> suggestions) {

    }

    @Override
    public void focusSearchField() {
        ViewUtils.showKeyboard(inputField);
    }

    @OnClick(R.id.backIcon)
    void onBackIconClicked() {
        if (inputField.hasFocus()) {
            // FIXME: Does not work when focusSearchField() was called before
            ViewUtils.hideKeyboard(inputField);
        } else if (listener != null) {
            listener.onQueryClosed();
        }
    }

    @OnClick(R.id.actionIcon)
    void onActionIconClicked() {
        inputField.setText(null);
    }
}
