package com.faltenreich.diaguard.shared.view.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.view.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickyHintInput extends LinearLayout implements TextWatcher {

    private static final int INPUT_TYPE_DEFAULT = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED;

    @BindView(R.id.input) LocalizedNumberEditText input;
    @BindView(R.id.label) TextView label;

    private CharSequence hint;
    private int inputType = INPUT_TYPE_DEFAULT;

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
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, new int[]{ android.R.attr.hint, android.R.attr.inputType });
        try {
            hint = typedArray.getText(0);
            inputType = typedArray.getInt(1, INPUT_TYPE_DEFAULT);
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
            input.setInputType(inputType);
            label.setText(hint);
            label.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewUtils.showKeyboard(input);
                }
            });
        }
    }

    private void update() {
        boolean isVisible = input.getText().toString().length() > 0;
        label.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public LocalizedNumberEditText getInputView() {
        return input;
    }

    public TextView getTextView() {
        return label;
    }

    public String getText() {
        return input.getNonLocalizedText();
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
