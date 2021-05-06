package com.faltenreich.diaguard.shared.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ViewSearchBinding;
import com.faltenreich.diaguard.shared.data.primitive.StringUtils;
import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import java.util.List;

public class SearchView extends FrameLayout implements ViewBindable<ViewSearchBinding>, Searchable, TextWatcher {

    private static final int INPUT_DELAY_IN_MILLIS = 1000;

    private ViewSearchBinding binding;

    private ImageView backButton;
    private AutoCompleteTextView inputField;
    private ImageView clearButton;
    private ImageView actionButton;

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

    @Override
    public ViewSearchBinding getBinding() {
        return binding;
    }

    private void init(@Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            getAttributes(attributeSet);
        }
        if (!isInEditMode()) {
            bindView();
            initLayout();
            invalidateLayout();
        }
    }

    private void getAttributes(@NonNull AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.SearchView);
        try {
            hint = typedArray.getString(R.styleable.SearchView_android_hint);
        } finally {
            typedArray.recycle();
        }
    }

    private void bindView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search, this);
        binding = ViewSearchBinding.bind(this);

        backButton = getBinding().backButton;
        inputField = getBinding().inputField;
        clearButton = getBinding().clearButton;
        actionButton = getBinding().actionButton;
    }

    private void initLayout() {
        setHint(hint);
        inputField.addTextChangedListener(this);
        backButton.setOnClickListener((view) -> onBackButtonClicked());
        clearButton.setOnClickListener((view) -> onClearButtonClicked());
        actionButton.setOnClickListener((view) -> onActionButtonClicked());
    }

    private void invalidateLayout() {
        clearButton.setVisibility(getQuery().isEmpty() ? GONE : VISIBLE);

        boolean hasAction = action != null;
        actionButton.setVisibility(hasAction ? VISIBLE : GONE);
        actionButton.setImageResource(hasAction ? action.getIconRes() : android.R.color.transparent);
        actionButton.setContentDescription(hasAction ? getContext().getString(action.getContentDescriptionRes()) : null);
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
        return inputField.getText().toString().trim();
    }

    @Override
    public void setQuery(String query, boolean submit) {
        if (submit) {
            inputField.setText(query);
        } else {
            inputField.removeTextChangedListener(this);
            inputField.setText(query);
            inputField.addTextChangedListener(this);
        }
        invalidateLayout();
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

    private void onBackButtonClicked() {
        if (listener != null) {
            listener.onQueryClosed();
        }
    }

    private void onClearButtonClicked() {
        // Workaround: TextWatcher breaks focus of EditText, so we trigger observers manually with slight delay
        inputField.removeTextChangedListener(this);
        inputField.setText(null);
        inputField.addTextChangedListener(this);
        new Handler().postDelayed(() -> {
            inputField.setText(null);
        }, 100);

    }

    private void onActionButtonClicked() {
        action.getCallback().onAction(actionButton);
    }

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        invalidateLayout();
    }
    @Override
    public void afterTextChanged(Editable editable) {
        onInputChanged(editable.toString());
    }
}
