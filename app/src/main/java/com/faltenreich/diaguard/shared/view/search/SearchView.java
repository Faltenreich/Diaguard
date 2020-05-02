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
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchView extends FrameLayout implements Searchable {

    private static final int INPUT_DELAY_IN_MILLIS = 1000;

    @BindView(R.id.backIcon)
    ImageView backIcon;

    @BindView(R.id.inputField)
    AutoCompleteTextView inputField;

    @BindView(R.id.actionIcon)
    ImageView actionIcon;

    private SearchViewListener listener;
    private SearchViewAction action;

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
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                invalidateLayout();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                onInputChanged(editable.toString());
            }
        });
    }

    private void invalidateLayout() {
        if (action != null) {
            boolean showAction = getQuery().isEmpty();
            int imageRes = showAction ? action.getIconRes() : R.drawable.ic_clear;
            int contentDescriptionRes = showAction ? action.getContentDescriptionRes() : R.string.query_clear;
            actionIcon.setImageResource(imageRes);
            actionIcon.setContentDescription(getContext().getString(contentDescriptionRes));
            actionIcon.setVisibility(View.VISIBLE);
        } else {
            actionIcon.setImageResource(R.drawable.ic_clear);
            actionIcon.setContentDescription(getContext().getString(R.string.query_clear));
            actionIcon.setVisibility(getQuery().isEmpty() ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private void onInputChanged(String input) {
        if (listener != null) {
            if (StringUtils.isBlank(input)) {
                listener.onQueryChanged(input);
            } else {
                // Delay search in order to prevent obsolete searches
                new Handler().postDelayed(() -> {
                    if (input.equals(getQuery())) {
                        listener.onQueryChanged(input);
                    }
                }, INPUT_DELAY_IN_MILLIS);
            }
        }
    }

    @Override
    public void setSearchListener(SearchViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void setAction(SearchViewAction action) {
        this.action = action;
        invalidateLayout();
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
        if (listener != null) {
            listener.onQueryClosed();
        }
    }

    @OnClick(R.id.actionIcon)
    void onActionIconClicked() {
        if (!StringUtils.isBlank(getQuery())) {
            inputField.setText(null);
        } else if (action != null) {
            action.getCallback().onAction(actionIcon);
        }
    }
}
