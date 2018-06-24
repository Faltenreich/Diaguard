package com.faltenreich.diaguard.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import com.faltenreich.diaguard.R;

import java.text.DecimalFormatSymbols;

/**
 * EditText that supports both the default and localized decimal separator (e.g. '.' and ',' for de-DE).
 * This view is intended for usage with [InputType.TYPE_NUMBER_FLAG_DECIMAL] only,
 * so every default decimal separator is replaced with its localized counterpart (de-DE: "Hello." -> "Hello,")
 */

public class LocalizedNumberEditText extends AppCompatEditText implements TextWatcher {

    private static final Character DEFAULT_SEPARATOR = '.';
    private static final Character LOCALIZED_SEPARATOR = DecimalFormatSymbols.getInstance().getDecimalSeparator();
    private static final String ACCEPTED_CHARACTERS = String.format("0123456789%s%s", DEFAULT_SEPARATOR, LOCALIZED_SEPARATOR);

    private boolean restrictToLocalizedSeparator = false;

    public LocalizedNumberEditText(Context context) {
        super(context);
        initLayout();
    }

    public LocalizedNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
        initLayout();
    }

    public LocalizedNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
        initLayout();
    }

    public String getNonLocalizedText() {
        return getText().toString().replace(LOCALIZED_SEPARATOR, DEFAULT_SEPARATOR);
    }

    /**
     *
     * @return The input number if available, otherwise -1
     */
    public float getNumber() {
        return LocalizedNumberEditTextUtils.parseNumber(getNonLocalizedText());
    }

    /**
     * @param restrictToLocalizedSeparator Only localized separators are allowed, if set to true, otherwise default separators are allowed as well
     */
    public void restrictToLocalizedSeparator(boolean restrictToLocalizedSeparator) {
        this.restrictToLocalizedSeparator = restrictToLocalizedSeparator;
    }

    /**
     * @return True if only localized separators are allowed
     */
    public boolean isRestrictedToLocalizedSeparator() {
        return restrictToLocalizedSeparator;
    }

    private void initAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.LocalizedNumberEditText, 0, 0);
            restrictToLocalizedSeparator = typedArray.getBoolean(R.styleable.LocalizedNumberEditText_restrictToLocalizedSeparator, false);
            typedArray.recycle();
        }
    }

    private void initLayout() {
        removeTextChangedListener(this);
        if (needsInputValidation()) {
            setKeyListener(DigitsKeyListener.getInstance(ACCEPTED_CHARACTERS));
            addTextChangedListener(this);
        }
    }

    private boolean isInputTypeNumberDecimal() {
        return (getInputType() & InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }

    private boolean needsInputValidation() {
        return isInputTypeNumberDecimal() && LOCALIZED_SEPARATOR != DEFAULT_SEPARATOR;
    }

    private String invalidateSeparators(String text) {
        return restrictToLocalizedSeparator ? text.replace(DEFAULT_SEPARATOR, LOCALIZED_SEPARATOR) : text;
    }

    private String stripUnnecessarySeparators(String text) {
        if (LocalizedNumberEditTextUtils.countOccurrences(text, LOCALIZED_SEPARATOR, DEFAULT_SEPARATOR) > 1) {
            int firstIndexOfDefaultSeparator = LocalizedNumberEditTextUtils.firstIndexOfOrLastIndex(text, DEFAULT_SEPARATOR);
            int firstIndexOfLocalizedSeparator = LocalizedNumberEditTextUtils.firstIndexOfOrLastIndex(text, LOCALIZED_SEPARATOR);
            int firstIndexOfSeparator = Math.min(firstIndexOfDefaultSeparator, firstIndexOfLocalizedSeparator);
            String localized = text.substring(0, firstIndexOfSeparator + 1);
            String localizing = text.substring(firstIndexOfSeparator, text.length())
                    .replace(LOCALIZED_SEPARATOR.toString(), "")
                    .replace(DEFAULT_SEPARATOR.toString(), "");
            return localized + localizing;
        } else {
            return text;
        }
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        // TextView.setInputType() updates the key listener, so we force re-update it afterwards
        initLayout();
    }

    @Override
    public void beforeTextChanged(CharSequence text, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (needsInputValidation()) {
            String localized = stripUnnecessarySeparators(invalidateSeparators(text.toString()));
            if (!localized.equals(text.toString())) {
                setText(localized);
                setSelection(localized.length());
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}