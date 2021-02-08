package com.faltenreich.diaguard.shared.view.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ViewStickyHintInputBinding;
import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.faltenreich.diaguard.shared.view.ViewUtils;

public class StickyHintInputView extends LinearLayout implements ViewBindable<ViewStickyHintInputBinding> {

    private static final int INPUT_TYPE_DEFAULT = InputType.TYPE_CLASS_NUMBER
        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        | InputType.TYPE_NUMBER_FLAG_SIGNED;

    private ViewStickyHintInputBinding binding;

    private LocalizedNumberEditText inputField;
    private TextView hintLabel;

    private CharSequence hint;
    private int inputType = INPUT_TYPE_DEFAULT;

    public StickyHintInputView(Context context) {
        super(context);
        init(null);
    }

    public StickyHintInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StickyHintInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public ViewStickyHintInputBinding getBinding() {
        return binding;
    }

    private void init(@Nullable AttributeSet attributeSet) {
        if (attributeSet != null) {
            getAttributes(attributeSet);
        }
        if (!isInEditMode()) {
            bindView();
            initLayout();
        }
    }

    private void getAttributes(@NonNull AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(
            attributeSet,
            new int[]{android.R.attr.hint, android.R.attr.inputType}
        );
        try {
            hint = typedArray.getText(0);
            inputType = typedArray.getInt(1, INPUT_TYPE_DEFAULT);
        } finally {
            typedArray.recycle();
        }
    }

    private void bindView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_sticky_hint_input, this);
        binding = ViewStickyHintInputBinding.bind(this);

        inputField = getBinding().editText;
        hintLabel = getBinding().hintLabel;
    }

    private void initLayout() {
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                update();
            }
        });
        inputField.setHint(hint);
        inputField.setInputType(inputType);
        hintLabel.setText(hint);
        hintLabel.setOnClickListener(view -> ViewUtils.showKeyboard(inputField));
    }

    private void update() {
        boolean isVisible = inputField.getText() != null && inputField.getText().toString().length() > 0;
        hintLabel.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public LocalizedNumberEditText getEditText() {
        return inputField;
    }

    public String getText() {
        return inputField.getNonLocalizedText();
    }

    public void setText(String text) {
        inputField.setText(text);
    }

    public String getHint() {
        return inputField.getHint().toString();
    }

    public void setHint(String hint) {
        inputField.setHint(hint);
        hintLabel.setText(hint);
    }

    public void setError(String error) {
        inputField.setError(error);
    }
}
