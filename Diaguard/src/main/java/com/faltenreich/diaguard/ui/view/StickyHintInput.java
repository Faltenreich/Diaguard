package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.util.ViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Faltenreich on 13.11.2016.
 */

public class StickyHintInput extends LinearLayout implements TextWatcher {

    @BindView(R.id.input) EditText input;
    @BindView(R.id.label) TextView label;

    private CharSequence hint;

    public StickyHintInput(Context context) {
        super(context);
        init();
    }

    public StickyHintInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StickyHintInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, new int[]{android.R.attr.hint});
        try {
            hint = typedArray.getText(0);
        } finally {
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_sticky_hint_input, this);
        if (!isInEditMode()) {
            ButterKnife.bind(this);
            input.addTextChangedListener(this);
            input.setHint(hint);
            label.setText(hint);
            label.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewHelper.requestFocusShowKeyboard(input);
                }
            });
        }
    }

    private void update() {
        boolean isVisible = input.getText().toString().length() > 0;
        label.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public EditText getEditText() {
        return input;
    }

    public TextView getTextView() {
        return label;
    }

    public String getText() {
        return input.getText().toString();
    }

    public void setText(String text) {
        input.setText(text);
    }

    public String getHint() {
        return input.getHint().toString();
    }

    public void setHint(String hint) {
        input.setHint(hint);
        label.setText(hint);
    }

    public void setError(String error) {
        input.setError(error);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        update();
    }
}
