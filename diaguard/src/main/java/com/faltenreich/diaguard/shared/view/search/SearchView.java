package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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

    private static final int SEARCH_INPUT_DELAY_IN_MILLIS = 1000;

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
            invalidateLayout();
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

        inputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                invalidateLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (listener != null) {
                    String query = editable.toString();
                    // Delay search in order to reduce obsolete searches
                    new Handler().postDelayed(() -> {
                        if (query.equals(getQuery())) {
                            listener.onQueryChanged(query);
                        }
                    }, SEARCH_INPUT_DELAY_IN_MILLIS);
                }
            }
        });
    }

    private void invalidateLayout() {
        if (getQuery().isEmpty()) {
            actionIcon.setVisibility(View.INVISIBLE);
        } else {
            actionIcon.setVisibility(View.VISIBLE);
        }
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, suggestions);
        inputField.setAdapter(adapter);
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
