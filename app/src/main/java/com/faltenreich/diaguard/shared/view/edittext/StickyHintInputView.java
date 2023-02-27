package com.faltenreich.diaguard.shared.view.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.ViewStickyHintInputBinding;
import com.faltenreich.diaguard.shared.view.ViewBindable;
import com.google.android.material.textfield.TextInputLayout;

public class StickyHintInputView extends TextInputLayout implements ViewBindable<ViewStickyHintInputBinding> {

    private static final int INPUT_TYPE_DEFAULT = InputType.TYPE_CLASS_NUMBER
        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        | InputType.TYPE_NUMBER_FLAG_SIGNED;

    private ViewStickyHintInputBinding binding;

    private LocalizedNumberEditText inputField;

    private CharSequence hint;
    private int inputType = INPUT_TYPE_DEFAULT;

    public StickyHintInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public StickyHintInputView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.textInputStyle);
    }

    public StickyHintInputView(Context context) {
        this(context, null);
    }

    @Override
    public ViewStickyHintInputBinding getBinding() {
        return binding;
    }

    private void init(@Nullable AttributeSet attributeSet) {
        if (!isInEditMode()) {
            if (attributeSet != null) {
                getAttributes(attributeSet);
            }
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
    }

    private void initLayout() {
        int paddingVertical = getResources().getDimensionPixelSize(R.dimen.padding);
        int paddingHorizontal = getResources().getDimensionPixelSize(R.dimen.margin_between);
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        setHint(hint);
        setBoxStrokeWidth(0);
        setBoxStrokeWidthFocused(0);
        setHelperTextEnabled(true);
        inputField.setInputType(inputType);
    }

    @NonNull
    public LocalizedNumberEditText getEditText() {
        return inputField;
    }

    @Nullable
    public String getText() {
        return inputField.getNonLocalizedText();
    }

    public void setText(@Nullable String text) {
        inputField.setText(text);
    }

    @Nullable
    public String getHint() {
        return super.getHint() != null ? super.getHint().toString() : null;
    }

    public void setHint(@Nullable String hint) {
        super.setHint(hint);
    }

    public void setError(@Nullable String error) {
        inputField.setError(error);
    }
}
